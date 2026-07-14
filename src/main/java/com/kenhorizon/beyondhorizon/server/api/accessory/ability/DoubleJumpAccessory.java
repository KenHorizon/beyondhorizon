package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DoubleJumpAccessory extends AccessoryPassiveSkill {
    protected boolean entityHasJump;

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (entity instanceof Player player && !player.getAbilities().flying && !entity.onGround() && Utils.isSpacePressed() && !isEntityHasJump() && entity.getDeltaMovement().y <= 0.07D) {
            entity.addDeltaMovement(entity.getLookAngle().multiply(0.15D, 0.0D, 0.15D).normalize().scale((double) (2.222555F * 0.222235F) * 0.65F).add(0.0D, (double) (1.4285F * 0.45F) * 0.65F, 0.0D));
            if (!entity.level().isClientSide() && entity.level() instanceof ServerLevel slevel) {
                slevel.sendParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), 50, 0.0D, 0.0D, 0.0D, 0.015D);
            }
            this.entityHasJump = true;
        }
        if (entity.onGround()) {
            this.entityHasJump = false;
        }
    }

    private boolean isEntityHasJump() {
        return entityHasJump;
    }
}
