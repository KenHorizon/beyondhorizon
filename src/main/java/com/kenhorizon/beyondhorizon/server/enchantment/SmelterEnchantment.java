package com.kenhorizon.beyondhorizon.server.enchantment;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class SmelterEnchantment extends AdvancedEnchantment {

    public SmelterEnchantment(Builder builder) {
        super(builder);
    }

    @Override
    public boolean onHarverstDrop(int level, Player player, LevelAccessor levelAccessor, ItemStack itemStack, BlockPos blockPos, BlockState blockState, List<ItemStack> drops) {
        RandomSource random = player.getRandom();
        boolean flag = random.nextDouble() <= (0.20D * (level + 1));
        if (itemStack.isCorrectToolForDrops(blockState) && flag) {
            List<ItemStack> blockDrops = new ArrayList<>();
//            BeyondHorizon.LOGGER.debug("Block Drops {} | {}", drops.size(), drops.toArray());
            for (ItemStack itemDrop : drops) {
                if (itemDrop.isEmpty()) continue;
                int amount = itemDrop.getCount();
                if (this.canSmelt(itemDrop, player.level())) {
                    RecipeManager recipeManager = player.level().getRecipeManager();
                    List<SmeltingRecipe> list = recipeManager.getRecipesFor(RecipeType.SMELTING, new SimpleContainer(itemDrop), player.level());
                    if (!list.isEmpty()) {
                        SmeltingRecipe recipe = list.get(0);
                        ItemStack smeltItem = recipe.assemble(new SimpleContainer(itemDrop), levelAccessor.registryAccess());
                        ItemStack dropStack = smeltItem.copy();
                        blockDrops.add(new ItemStack(dropStack.getItem(), amount));
                    }
                }
            }
            for (ItemStack drop : blockDrops) {
                if (!player.level().isClientSide()) {
                    if (player.level() instanceof ServerLevel sLevel) {
                        sLevel.sendParticles(ParticleTypes.FLAME, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, 10, 0.0D, 0.0D, 0.0D, 0.025D);
                    }
                }
                Block.popResource(player.level(), blockPos, drop);
            }
            return false;
        }
        return true;
    }

    protected boolean canSmelt(ItemStack itemStack, Level level) {
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemStack), level).isPresent();
    }

    @Override
    public int getMinCost(int level) {
        return 3 * level;
    }

    @Override
    public int getMaxCost(int level) {
        return super.getMinCost(level) + 8;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment != Enchantments.SILK_TOUCH;
    }
}
