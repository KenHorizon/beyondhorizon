package com.kenhorizon.beyondhorizon.server.api.handler.anvil_patch;

import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.configs.common.ModCommonConfig;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

/**
 * <a href="https://www.curseforge.com/minecraft/mc-mods/anvil-patch-lawful">Anvil Patch v1.1.1</a> <br>
 * Features: <br>
 * Remove Level Cap (Can Edit on Configs) <br>
 * Choice-able options on anvil cost <br>
 * Also fix visual bug on anvil patch <br>
 * Creator:lumberwizards <br>
 *
 * @author KenHorizon (Ported in 1.20.1) <br>
 * @version 1.0 <br>
 */
public class AnvilPatchHandler {
    @SubscribeEvent
    public void anvilUpdateEvent(AnvilUpdateEvent event) {
        Player player = event.getPlayer();
        if (!event.getOutput().isEmpty()) return;
        ItemStack leftItem = event.getLeft();
        ItemStack rightItem = event.getRight();
        ItemStack outputItem = leftItem.copy();
        int addedRepairCost = 0;
        Map<Enchantment, Integer> outputEnchantmentItem = EnchantmentHelper.getEnchantments(outputItem);

        boolean isRightEnchantmentItem = rightItem.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(rightItem).isEmpty();

        boolean shouldIncreaseCost = BHConfigs.ANVIL_COSTING == AnvilCostSettings.KEEP;
        boolean shouldApplyIncreasedCost = BHConfigs.ANVIL_COSTING != AnvilCostSettings.REMOVE;

        int materialCost = 1;

        if (outputItem.isDamageableItem() && outputItem.getItem().isValidRepairItem(leftItem, rightItem)) {
            int amountRepairedByMat = Math.min(outputItem.getDamageValue(), outputItem.getMaxDamage() / 4);

            if (amountRepairedByMat <= 0) {
                return;
            }
            shouldApplyIncreasedCost = shouldApplyIncreasedCost && BHConfigs.ANVIL_COSTING != AnvilCostSettings.ENCHANTMENT_ONLY;

            for (materialCost = 0; amountRepairedByMat > 0 && materialCost < rightItem.getCount(); ++materialCost) {
                int newDamageValue = outputItem.getDamageValue() - amountRepairedByMat;
                outputItem.setDamageValue(newDamageValue);
                ++addedRepairCost;
                amountRepairedByMat = Math.min(outputItem.getDamageValue(), outputItem.getMaxDamage() / 4);
            }

        } else {
            if (!isRightEnchantmentItem && (outputItem.getItem() != rightItem.getItem() || !outputItem.isDamageableItem())) {
                return;
            }

            if (outputItem.isDamageableItem() && !isRightEnchantmentItem) {
                int leftDurability = leftItem.getMaxDamage() - leftItem.getDamageValue();
                int rightDurability = rightItem.getMaxDamage() - rightItem.getDamageValue();
                int newDurability = leftDurability + rightDurability + outputItem.getMaxDamage() * 12 / 100;
                int newDamage = outputItem.getMaxDamage() - newDurability;

                if (newDamage < 0) {
                    newDamage = 0;
                }

                if (newDamage < outputItem.getDamageValue()) {  // vanilla uses metadata here instead of damage.
                    outputItem.setDamageValue(newDamage);
                    addedRepairCost += 2;
                }
            }
            Map<Enchantment, Integer> enchantmentApply = EnchantmentHelper.getEnchantments(rightItem);
            boolean rightItemHasCompatibleEnchantments = false;
            boolean rightItemHasIncompatibleEnchantments = false;

            for (Enchantment enchantmentToAdd : enchantmentApply.keySet()) {
                if (enchantmentToAdd != null) {
                    int enchLevel = outputEnchantmentItem.getOrDefault(enchantmentToAdd, 0);
                    int enchNewLevel = enchantmentApply.get(enchantmentToAdd);
                    if (enchNewLevel == enchLevel && enchNewLevel < enchantmentToAdd.getMaxLevel()) enchNewLevel++;
                    enchNewLevel = Math.max(enchNewLevel, enchLevel);
                    boolean canEnchantmentBeAppliedToLeftItem = enchantmentToAdd.canEnchant(leftItem);

                    if (leftItem.is(Items.ENCHANTED_BOOK)) {
                        canEnchantmentBeAppliedToLeftItem = true;
                    }

                    for (Enchantment enchantment : outputEnchantmentItem.keySet()) {
                        enchNewLevel++;
                        ++addedRepairCost;
                        canEnchantmentBeAppliedToLeftItem = true;
                        if (enchantment != enchantmentToAdd && !enchantmentToAdd.isCompatibleWith(enchantment)) {
                            canEnchantmentBeAppliedToLeftItem = false;
                            ++addedRepairCost;
                        }
                    }

                    if (!canEnchantmentBeAppliedToLeftItem) {
                        rightItemHasIncompatibleEnchantments = true;
                    } else {
                        rightItemHasCompatibleEnchantments = true;

                        if (enchNewLevel > enchantmentToAdd.getMaxLevel() && BHConfigs.ENCHANTMENT_BREAK_LEVEL) {
                            enchNewLevel = enchantmentToAdd.getMaxLevel();
                        }

                        outputEnchantmentItem.put(enchantmentToAdd, enchNewLevel);
                        int repairCostAddedByEnchantmentRarity = getRepairCostAddedByEnchantmentRarity(enchantmentToAdd, isRightEnchantmentItem);

                        addedRepairCost += repairCostAddedByEnchantmentRarity * enchNewLevel;

                        if (leftItem.getCount() > 1) {
                            return;
                        }
                    }
                }
            }
            if (rightItemHasIncompatibleEnchantments && !rightItemHasCompatibleEnchantments) {
                return;
            }

            shouldIncreaseCost = shouldIncreaseCost || rightItemHasCompatibleEnchantments && BHConfigs.ANVIL_COSTING != AnvilCostSettings.REMOVE;
            shouldApplyIncreasedCost = shouldApplyIncreasedCost && (rightItemHasCompatibleEnchantments || BHConfigs.ANVIL_COSTING != AnvilCostSettings.ENCHANTMENT_ONLY);
        }

        int renameAddedCost = 0;

        String repairedItemName = event.getName();

        if (Util.isBlank(repairedItemName)) {
            if (leftItem.hasCustomHoverName()) {
                renameAddedCost = 1;
                addedRepairCost += renameAddedCost;
                outputItem.resetHoverName();
            }
        } else if (!repairedItemName.equals(leftItem.getDisplayName())) {
            renameAddedCost = 1;
            addedRepairCost += renameAddedCost;
            outputItem.setHoverName(Component.literal(repairedItemName));
        }
        if (isRightEnchantmentItem && !outputItem.getItem().isBookEnchantable(outputItem, rightItem)) {
            outputItem = ItemStack.EMPTY;
        }

        int totalRepairCost = (shouldApplyIncreasedCost ? event.getCost() : 0) + addedRepairCost;

        if (totalRepairCost <= 0) {
            outputItem = ItemStack.EMPTY;
        }

        if (addedRepairCost == renameAddedCost && BHConfigs.ANVIL_COST_CAP >= 0 && totalRepairCost >= BHConfigs.ANVIL_COST_CAP) {
            totalRepairCost = BHConfigs.ANVIL_COST_CAP - 1;
        }

        if (BHConfigs.ANVIL_COST_CAP >= 0 && totalRepairCost >= BHConfigs.ANVIL_COST_CAP) {
            if (event.getOutput().isEmpty()) {
                event.setCanceled(true);
            }
            return;
        }

        if (!outputItem.isEmpty()) {
            if (shouldIncreaseCost) {
                int newCost = outputItem.getBaseRepairCost();
                if (!rightItem.isEmpty() && newCost < rightItem.getBaseRepairCost()) {
                    newCost = rightItem.getBaseRepairCost();
                }
                if (renameAddedCost != addedRepairCost || renameAddedCost == 0) {
                    newCost = newCost * 2 + 1;
                }
                outputItem.setRepairCost(newCost);
            }
            EnchantmentHelper.setEnchantments(outputEnchantmentItem, outputItem);
            if (outputItem.isDamageableItem() && outputItem.getItem().isValidRepairItem(leftItem, rightItem)) {
                event.setMaterialCost(materialCost);
            }
            event.setCost(totalRepairCost);
            event.setOutput(outputItem);
        }
    }

    private int getRepairCostAddedByEnchantmentRarity(Enchantment addEnchantments, boolean isRightEnchantmentItem) {
        int repairCostAddedByEnchantmentRarity = switch (addEnchantments.getRarity()) {
            case COMMON -> 1;
            case UNCOMMON -> 2;
            case RARE -> 4;
            case VERY_RARE -> 8;
        };

        if (isRightEnchantmentItem) {
            repairCostAddedByEnchantmentRarity = Math.max(1, repairCostAddedByEnchantmentRarity / 2);
        }
        return repairCostAddedByEnchantmentRarity;
    }

    @SubscribeEvent
    public static void onAnvilRepair(AnvilRepairEvent event) {
        event.setBreakChance((float) Mth.clamp((BHConfigs.ANVIL_BREAK_CHANCES / 100.0D), 0.0D, 1.0D));
    }
}
