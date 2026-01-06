package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class CleaveAbility extends AbilityEntity {
    public enum Type {
        CIRCLE,
        CONE
    }
    protected CleaveAbility.Type cleaveType = Type.CIRCLE;
    public CleaveAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setDuration(1);
        this.setRadius(2.5F);
    }

    public static void spawn(Level level, LivingEntity target, LivingEntity owner, float damage, float range, Type type) {
        CleaveAbility ability = new CleaveAbility(BHEntity.CLEAVE_ABILITY.get(), level);
        ability.setBaseDamage(damage);
        ability.setRadius(range);
        ability.setPos(target.position().add(0, target.getBbHeight() * 0.05D, 0));
        ability.setSourceDamage(owner);
        ability.setCleaveType(type);
        ability.setCaster(owner);
        ability.setCasterID(owner.getUUID());
        level.addFreshEntity(ability);
    }

    public void setCleaveType(CleaveAbility.Type type) {
        this.cleaveType = type;
    }

    public Type getCleaveType() {
        return cleaveType;
    }

    @Override
    protected void onStart() {
        float yaw = (float) Math.toRadians(-this.getYRot());
        float pitch = (float) Math.toRadians(-this.getXRot());
        float spread = 0.25f;
        float speed = 1.56f;
        float xComp = (float) (Math.sin(yaw) * Math.cos(pitch));
        float yComp = (float) (Math.sin(pitch));
        float zComp = (float) (Math.cos(yaw) * Math.cos(pitch));
        if (this.level().isClientSide()) {
            if (this.getCleaveType() == Type.CONE) {
                float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                this.level().addParticle(new RingParticleOptions(yaw, -pitch, 15, r, g, b, 1.0F, 110.0F * spread, false, RingParticles.Behavior.GROW), this.getX(), this.getY() + 0.1D, this.getZ(), speed * xComp, speed * yComp, speed * zComp);
            }
            if (this.getCleaveType() == Type.CIRCLE) {
                float r = ColorUtil.getFARGB(0xFF6500)[0];
                float g = ColorUtil.getFARGB(0xFF6500)[1];
                float b = ColorUtil.getFARGB(0xFF6500)[2];
                this.level().addParticle(new RingParticleOptions(0, (float) Math.PI / 2, 15, r, g, b, 1.0F, 64.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
        }
    }

    @Override
    protected void onEnd() {
        LivingEntity user = this.getCaster();
        if (this.getCleaveType() == Type.CIRCLE) {
            this.cleaveAttack();
        }
        if (this.getCleaveType() == Type.CONE) {
            if (user != null) {
                this.coneAttack();
            }
        }
        if (!this.sentSpikeEvent) {
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.sentSpikeEvent = true;
        }
    }
    public void cleaveAttack() {
        LivingEntity attacker = this.getCaster();
        List<Entity> entityOnRangeInCleave = this.level().getEntities(this, this.getBoundingBox().inflate(this.getRadius()));
        for (Entity entityOnRange : entityOnRangeInCleave) {
            if (entityOnRange instanceof LivingEntity targetOnRange) {
                if (targetOnRange == attacker || targetOnRange == this.getTarget()) continue;
                if (targetOnRange.isAlive() && !targetOnRange.isInvulnerable()) {
                    targetOnRange.hurt(BHDamageTypes.physicalDamage(attacker, entityOnRange), this.getBaseDamage());
                }
            }
        }
    }


    public void coneAttack() {
        LivingEntity attacker = this.getCaster();
        List<Entity> entitiesHit = this.getEntityLivingBaseNearby(3, 3, 3, 3);
        float damage = this.getBaseDamage();
        for (Entity entityHit : entitiesHit) {
            if (entityHit == getCaster()) continue;
            if (entityHit instanceof ItemEntity) continue;
            float entityHitYaw = (float) ((Math.atan2(entityHit.getZ() - getZ(), entityHit.getX() - getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingYaw = getYRot() % 360;
            if (entityHitYaw < 0) {
                entityHitYaw += 360;
            }
            if (entityAttackingYaw < 0) {
                entityAttackingYaw += 360;
            }
            float entityRelativeYaw = entityHitYaw - entityAttackingYaw;
            float xzDistance = (float) Math.sqrt((entityHit.getZ() - getZ()) * (entityHit.getZ() - getZ()) + (entityHit.getX() - getX()) * (entityHit.getX() - getX()));
            double hitY = entityHit.getY() + entityHit.getBbHeight() / 2.0;
            float entityHitPitch = (float) ((Math.atan2((hitY - getY()), xzDistance) * (180 / Math.PI)) % 360);
            float entityAttackingPitch = -getXRot() % 360;
            if (entityHitPitch < 0) {
                entityHitPitch += 360;
            }
            if (entityAttackingPitch < 0) {
                entityAttackingPitch += 360;
            }
            float entityRelativePitch = entityHitPitch - entityAttackingPitch;

            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - getZ()) * (entityHit.getZ() - getZ()) + (entityHit.getX() - getX()) * (entityHit.getX() - getX()) + (hitY - getY()) * (hitY - getY()));

            boolean inRange = entityHitDistance <= 3;
            boolean yawCheck = (entityRelativeYaw <= 45.0F / 2f && entityRelativeYaw >= -45.0F / 2f) || (entityRelativeYaw >= 360 - 45.0F / 2f || entityRelativeYaw <= -360 + 45.0F / 2f);
            boolean pitchCheck = (entityRelativePitch <= 45.0F / 2f && entityRelativePitch >= -45.0F / 2f) || (entityRelativePitch >= 360 - 45.0F / 2f || entityRelativePitch <= -360 + 45.0F / 2f);
            if (inRange && yawCheck && pitchCheck) {
                if (!checkEntity(entityHit)) continue;
                if (entityHit.hurt(BHDamageTypes.physicalDamage(this, attacker), damage) && entityHit instanceof LivingEntity) {
                    entityHit.setDeltaMovement(entityHit.getDeltaMovement().multiply(0.25, 1, 0.25));
                }
            }
        }
    }
}
