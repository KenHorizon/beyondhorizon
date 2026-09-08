package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.item.QuiverItem;
import com.kenhorizon.beyondhorizon.server.item.RecoveryPotionItem;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ManaReplenishAccessory extends AccessoryPassiveSkill {

    public enum ManaRestoredType {
        ALL,
        AUTO,
        DAMAGE_TAKEN
    }

    private final ManaRestoredType manaRestoredType;

    public ManaReplenishAccessory(ManaRestoredType manaRestoredType) {
        this.manaRestoredType = manaRestoredType;
    }

    @Override
    public float damageTaken(DamageContext context, DamageSource source, LivingEntity entity) {
        if (this.manaRestoredType == ManaRestoredType.ALL || this.manaRestoredType == ManaRestoredType.DAMAGE_TAKEN) {
            if (entity instanceof Player player) {
                PlayerData playerData = Capabilities.data(player);
                if (playerData != null && player.isAlive()) {
                    float manaRestored = context.damage() / 2.0F;
                    playerData.addMana(manaRestored);
                }
            }
        }
        return context.damage();
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (this.manaRestoredType == ManaRestoredType.ALL || this.manaRestoredType == ManaRestoredType.AUTO) {

            if (entity instanceof Player player) {
                PlayerData playerData = Capabilities.data(player);
                if (playerData != null && player.isAlive()) {
                    if (playerData.getMana() <= 0 && player.isUsingItem()) {
                        for (ItemStack potionStacks : ManaReplenishAccessory.findValidManaPotion(player)) {
                            if (potionStacks.getItem() instanceof RecoveryPotionItem potionItem) {
                                potionItem.usePotionItem(entity, entity.level(), potionStacks);
                            }
                        }
                    }
                }
            }
        }
    }
    public static List<ItemStack> findValidManaPotion(Player player) {
        List<ItemStack> result = new ArrayList<ItemStack>();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && (stack.getItem() instanceof RecoveryPotionItem potionItem)) {
                if (potionItem.mana > 0) {
                    result.add(stack);
                }
            }
        }
        return result;
    }
}
