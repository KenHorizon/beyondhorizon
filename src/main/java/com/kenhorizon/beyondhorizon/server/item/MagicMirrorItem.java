package com.kenhorizon.beyondhorizon.server.item;

import com.kenhorizon.beyondhorizon.server.item.util.TeleportHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MagicMirrorItem extends BasicItem {

    public MagicMirrorItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemStack) {
        return 30;
    }
    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity) {
        Minecraft mc = Minecraft.getInstance();
        if (level.isClientSide()) {
            mc.gameRenderer.displayItemActivation(itemStack);
        } else if (entity instanceof ServerPlayer serverPlayer) {
            BlockPos respawnPosition = serverPlayer.getRespawnPosition();
            if (respawnPosition != null) {
                serverPlayer.teleportTo(respawnPosition.getX(), respawnPosition.getY(), respawnPosition.getZ());
            } else {
                TeleportHandler.teleportHome(serverPlayer, level);
            }
            serverPlayer.getCooldowns().addCooldown(this, 10);

        }
        return itemStack;
    }
}
