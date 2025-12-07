package com.kenhorizon.beyondhorizon.server.item.util;

import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;

public class ItemStackUtil {


    public static int getInsertOffset(boolean advanced, int tooltipSize, ItemStack stack) {
        int offset = 0;
        if (advanced) {
            // item id
            offset++;
            // tag count
            if (stack.hasTag()) {
                offset++;
            }
            // durability
            if (stack.isDamaged()) {
                offset++;
            }
        }
        return Math.max(0, tooltipSize - offset);
    }

    public static float getDestroySpeed(ItemStack stack) {
        float destroySpeed = ItemStackUtil.getDestroySpeed(stack, stack.getItem());
        if (destroySpeed > 1.0F) {
            final int efficiencyLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, stack);
            if (efficiencyLevel > 0) {
                destroySpeed += (float) (efficiencyLevel * efficiencyLevel + 1);
            }
        }

        return destroySpeed;
    }
    private static float getDestroySpeed(ItemStack stack, Item item) {
        if (item instanceof PickaxeItem pickaxe) {
            return pickaxe.getDestroySpeed(stack, Blocks.COBBLESTONE.defaultBlockState());
        }
        if (item instanceof AxeItem axe) {
            return axe.getDestroySpeed(stack, Blocks.OAK_PLANKS.defaultBlockState());
        }
        if (item instanceof ShovelItem shovel) {
            return shovel.getDestroySpeed(stack, Blocks.DIRT.defaultBlockState());
        }
        if (item instanceof HoeItem hoe) {
            return hoe.getDestroySpeed(stack, Blocks.DRIED_KELP_BLOCK.defaultBlockState());
        }
        if (item instanceof SwordItem sword) {
            return sword.getDestroySpeed(stack, Blocks.SUNFLOWER.defaultBlockState());
        }
        if (item instanceof ShearsItem shears) {
            return shears.getDestroySpeed(stack, Blocks.OAK_LEAVES.defaultBlockState());
        }
        if (item instanceof TieredItem tieredItem) {
            return tieredItem.getTier().getSpeed();
        }
        return 0f;
    }
}
