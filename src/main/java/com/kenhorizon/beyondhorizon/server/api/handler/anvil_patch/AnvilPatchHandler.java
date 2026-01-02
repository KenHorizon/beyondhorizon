package com.kenhorizon.beyondhorizon.server.api.handler.anvil_patch;

import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.configs.common.ModCommonConfig;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
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
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack output = left.copy();
        int addedRepairCost = 0;
        Map<Enchantment, Integer> outputEnchantmentItem = EnchantmentHelper.getEnchantments(output);
        boolean isRightEnchantmentItem = right.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(right).isEmpty();
        boolean shouldIncreaseCost = BHConfigs.ANVIL_COSTING == AnvilCostSettings.KEEP;
        boolean shouldApplyIncreaseCost = BHConfigs.ANVIL_COSTING != AnvilCostSettings.REMOVE;
        int cost = 1;
        if (output.isDamageableItem() && output.getItem().isValidRepairItem(left, right)) {
            int amountRepairedByMat = Math.min(output.getDamageValue(), output.getMaxDamage() / 4);
            if (amountRepairedByMat <= 0) {
                return;
            }
            for (cost = 0; amountRepairedByMat > 0 && cost < right.getCount(); ++cost) {
                int newDamageValue = output.getDamageValue() - amountRepairedByMat;
                output.setDamageValue(newDamageValue);
                ++addedRepairCost;
                amountRepairedByMat = Math.min(output.getDamageValue(), output.getMaxDamage() / 4);
            }
        } else {
            if (!isRightEnchantmentItem && (output.getItem() != right.getItem() || !output.isDamageableItem())) {
                return;
            }
            if (output.isDamageableItem() && !isRightEnchantmentItem) {
                int leftDurability = left.getMaxDamage() - left.getDamageValue();
                int rightDurability = right.getMaxDamage() - right.getDamageValue();
                int newDurability = leftDurability + rightDurability + output.getMaxDamage() * 12 / 100;
                int newDamage = output.getMaxDamage() - newDurability;
                if (newDamage < 0) {
                    newDamage = 0;
                }
                if (newDamage < output.getDamageValue()) {
                    output.setDamageValue(newDamage);
                    addedRepairCost += 2;
                }
            }
            Map<Enchantment, Integer> enchantmentApply = EnchantmentHelper.getEnchantments(right);
            boolean rightItemHasCompatibleEnchantments = false;
            boolean rightItemHasIncompatibleEnchantments = false;

            for (Enchantment addEnchantments : enchantmentApply.keySet()) {
                if (addEnchantments != null) {
                    int currentEnchantmentLevel = outputEnchantmentItem.getOrDefault(addEnchantments, 0);
                    int newEnchantmentLevel = enchantmentApply.get(addEnchantments);
                    if (newEnchantmentLevel == currentEnchantmentLevel && newEnchantmentLevel < addEnchantments.getMaxLevel())
                        newEnchantmentLevel++;
                    newEnchantmentLevel = Math.max(newEnchantmentLevel, currentEnchantmentLevel);
                    boolean canEnchantmentBeAppliedToLeftItem = addEnchantments.canEnchant(left);
                    if (player.getAbilities().instabuild || left.is(Items.ENCHANTED_BOOK)) {
                        canEnchantmentBeAppliedToLeftItem = true;
                    }
                    for (Enchantment enchantment : outputEnchantmentItem.keySet()) {
                        newEnchantmentLevel++;
                        ++addedRepairCost;
                        canEnchantmentBeAppliedToLeftItem = true;
                        if (enchantment != addEnchantments && !addEnchantments.isCompatibleWith(enchantment)) {
                            canEnchantmentBeAppliedToLeftItem = false;
                            ++addedRepairCost;
                        }
                    }
                    if (!canEnchantmentBeAppliedToLeftItem) {
                        rightItemHasIncompatibleEnchantments = true;
                    } else {
                        rightItemHasCompatibleEnchantments = true;
                        if (newEnchantmentLevel > addEnchantments.getMaxLevel() && BHConfigs.ENCHANTMENT_BREAK_LEVEL) {
                            newEnchantmentLevel = addEnchantments.getMaxLevel();
                        }
                        outputEnchantmentItem.put(addEnchantments, newEnchantmentLevel);
                        int repairCostAddedByEnchantmentRarity = getRepairCostAddedByEnchantmentRarity(addEnchantments, isRightEnchantmentItem);
                        addedRepairCost += repairCostAddedByEnchantmentRarity * newEnchantmentLevel;
                        if (left.getCount() > 1) {
                            return;
                        }
                    }
                }
            }
            if (rightItemHasIncompatibleEnchantments && !rightItemHasCompatibleEnchantments) {
                return;
            }
            shouldIncreaseCost = shouldIncreaseCost || rightItemHasCompatibleEnchantments && BHConfigs.ANVIL_COSTING != AnvilCostSettings.REMOVE;
            shouldApplyIncreaseCost = shouldApplyIncreaseCost && (rightItemHasCompatibleEnchantments || BHConfigs.ANVIL_COSTING != AnvilCostSettings.ENCHANTMENT_ONLY);
        }
        int renameAddedCost = 0;
        String repairedItemName = event.getName();
        if (Util.isBlank(repairedItemName)) {
            if (left.hasCustomHoverName()) {
                renameAddedCost = 1;
                addedRepairCost += renameAddedCost;
                output.resetHoverName();
            }
        } else if (!repairedItemName.equals(left.getDisplayName())) {
            renameAddedCost = 1;
            addedRepairCost += renameAddedCost;
            output.setHoverName(Component.literal(repairedItemName));
        }
        if (isRightEnchantmentItem && !output.getItem().isBookEnchantable(output, right)) {
            output = ItemStack.EMPTY;
        }
        int totalRepairCost = (shouldApplyIncreaseCost ? event.getCost() : 0) + addedRepairCost;
        if (totalRepairCost <= 0) {
            output = ItemStack.EMPTY;
        }
        if (addedRepairCost == renameAddedCost && BHConfigs.ANVIL_COST_CAP >= 0 && totalRepairCost >= BHConfigs.ANVIL_COST_CAP) {
            totalRepairCost = ModCommonConfig.ANVIL_COST_CAP.get() - 1;
        }
        if (BHConfigs.ANVIL_COST_CAP >= 0 && totalRepairCost >= BHConfigs.ANVIL_COST_CAP) {
            if (event.getOutput().isEmpty()) {
                event.setCanceled(true);
            }
            return;
        }
        if (!output.isEmpty()) {
            if (shouldIncreaseCost) {
                int newCost = output.getBaseRepairCost();
                if (!right.isEmpty() && newCost < right.getBaseRepairCost()) {
                    newCost = right.getBaseRepairCost();
                }
                if (renameAddedCost != addedRepairCost || renameAddedCost == 0) {
                    newCost = newCost * 2 + 1;
                }
                output.setRepairCost(newCost);
            }
            EnchantmentHelper.setEnchantments(outputEnchantmentItem, output);
            if (output.isDamageableItem() && output.getItem().isValidRepairItem(left, right)) {
                event.setMaterialCost(cost);
            }
            event.setCost(totalRepairCost);
            event.setOutput(output);
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
        event.setBreakChance((float) BHConfigs.ANVIL_BREAK_CHANCES);
    }
}
