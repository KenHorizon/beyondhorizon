package com.kenhorizon.beyondhorizon.mixins.common;

import com.kenhorizon.beyondhorizon.server.api.event.HarvestBlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixins {
    @Inject(at = @At("HEAD"), method = "playerDestroy", cancellable = true)
    private void modifiedPlayerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack, CallbackInfo ci) {
        LootParams.Builder builder = (new LootParams.Builder((ServerLevel) player.level())).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPos)).withParameter(LootContextParams.BLOCK_STATE, blockState).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity).withOptionalParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.TOOL, itemStack);
        HarvestBlockEvent event = new HarvestBlockEvent(level, blockPos, blockState, player, itemStack, blockState.getDrops(builder));
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            ci.cancel();
        }
        if (!event.isCanDropLoot()) {
            ci.cancel();
        }
    }
}
