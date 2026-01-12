package com.kenhorizon.beyondhorizon.server.item.base.tools;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;

public class ShovelBaseItem extends DiggerBaseItem {
    protected static final Map<Block, BlockState> FLATTENABLES = Maps.newHashMap((new ImmutableMap.Builder()).put(Blocks.GRASS_BLOCK, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.DIRT, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.PODZOL, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.COARSE_DIRT, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.MYCELIUM, Blocks.DIRT_PATH.defaultBlockState()).put(Blocks.ROOTED_DIRT, Blocks.DIRT_PATH.defaultBlockState()).build());

    public ShovelBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, float attackRange, Properties properties, SkillBuilder skillBuilder) {
        super(materials, attackDamage, attackSpeed, attackRange, BlockTags.MINEABLE_WITH_SHOVEL, properties, skillBuilder);
    }

    public ShovelBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, Properties properties, SkillBuilder skillBuilder) {
        super(materials, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_SHOVEL, properties, skillBuilder);
    }

    public ShovelBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, Properties properties) {
        super(materials, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_SHOVEL, properties);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        } else {
            Player player = context.getPlayer();
            BlockState canBeFlattenDown = blockstate.getToolModifiedState(context, net.minecraftforge.common.ToolActions.SHOVEL_FLATTEN, false);
            BlockState flattenDown = null;
            if (canBeFlattenDown != null && level.isEmptyBlock(blockpos.above())) {
                level.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                flattenDown = canBeFlattenDown;
            } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                if (!level.isClientSide()) {
                    level.levelEvent((Player) null, 1009, blockpos, 0);
                }

                CampfireBlock.dowse(context.getPlayer(), level, blockpos, blockstate);
                flattenDown = blockstate.setValue(CampfireBlock.LIT, Boolean.valueOf(false));
            }

            if (flattenDown != null) {
                if (!level.isClientSide) {
                    level.setBlock(blockpos, flattenDown, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, flattenDown));
                    if (player != null) {
                        context.getItemInHand().hurtAndBreak(1, player, (use) -> {
                            use.broadcastBreakEvent(context.getHand());
                        });
                    }
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @org.jetbrains.annotations.Nullable
    public static BlockState getShovelPathingState(BlockState originalState) {
        return FLATTENABLES.get(originalState.getBlock());
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
    }
}
