package com.kenhorizon.beyondhorizon.server.item.base.tools;

import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.Optional;

public class AxeBaseItem extends DiggerBaseItem {


    public AxeBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, float attackRange, Properties properties, SkillBuilder skillBuilder) {
        super(materials, attackDamage, attackSpeed, attackRange, BlockTags.MINEABLE_WITH_AXE, properties, skillBuilder);
    }

    public AxeBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, Properties properties, SkillBuilder skillBuilder) {
        super(materials, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_AXE, properties, skillBuilder);
    }

    public AxeBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, Properties properties) {
        super(materials, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_AXE, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState blockstate = level.getBlockState(blockpos);
        Optional<BlockState> strippableActions = Optional.ofNullable(blockstate.getToolModifiedState(context, ToolActions.AXE_STRIP, false));
        Optional<BlockState> axeScrapeActions = strippableActions.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(context, ToolActions.AXE_SCRAPE, false));
        Optional<BlockState> waxOffActions = strippableActions.isPresent() || axeScrapeActions.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(context, ToolActions.AXE_WAX_OFF, false));
        ItemStack itemstack = context.getItemInHand();
        Optional<BlockState> isEmpty = Optional.empty();
        if (strippableActions.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            isEmpty = strippableActions;
        } else if (axeScrapeActions.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3005, blockpos, 0);
            isEmpty = axeScrapeActions;
        } else if (waxOffActions.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3004, blockpos, 0);
            isEmpty = waxOffActions;
        }

        if (isEmpty.isPresent()) {
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
            }

            level.setBlock(blockpos, isEmpty.get(), 11);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, isEmpty.get()));
            if (player != null) {
                itemstack.hurtAndBreak(1, player, (use) -> {
                    use.broadcastBreakEvent(context.getHand());
                });
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            return InteractionResult.PASS;
        }
    }
    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction);
    }
}
