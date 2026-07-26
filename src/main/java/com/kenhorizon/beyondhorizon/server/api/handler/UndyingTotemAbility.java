package com.kenhorizon.beyondhorizon.server.api.handler;

import com.kenhorizon.beyondhorizon.server.api.accessory.Accessories;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class UndyingTotemAbility {
    public static boolean onUse(ServerLevel world, ServerPlayer player) {

        Minecraft mc = Minecraft.getInstance();
        if (player.getMainHandItem().getItem().equals(Items.TOTEM_OF_UNDYING) || player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING)) return true;
        ItemStack totemstack = getItemStack(player);

        if (totemstack == null) {
            return true;
        }
        mc.gameRenderer.displayItemActivation(totemstack);
        player.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
        CriteriaTriggers.USED_TOTEM.trigger(player, totemstack);
        player.setHealth(1.0F);
        player.removeAllEffects();
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        world.broadcastEntityEvent(player, (byte) 35);
        totemstack.shrink(1);
        return false;
    }

    @Nullable
    private static ItemStack getItemStack(ServerPlayer player) {
        Inventory inv = player.getInventory();
        ItemStack totemstack = null;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem().equals(Items.TOTEM_OF_UNDYING)) {
                totemstack = stack;

            }
        }

        return totemstack;
    }
}
