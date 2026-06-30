package com.kenhorizon.beyondhorizon.server.item.backport;

import com.kenhorizon.beyondhorizon.server.entity.projectiles.ExtendedProjectileUtil;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import javax.swing.*;
import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

public class KineticWeaponItem {
    public int delayTicks;
    public int contactCooldownTicks;
    public float forwardMovement;
    public float damageMultiplier;
    public final Optional<Condition> dismountConditions;
    public final Optional<Condition> knockbackConditions;
    public final Optional<Condition> damageConditions;
    public final Optional<Holder<SoundEvent>> sound;
    public final Optional<Holder<SoundEvent>> hitSound;

    public KineticWeaponItem(int delayTicks, int contactCooldownTicks, Optional<Condition> dismountConditions, Optional<Condition> knockbackConditions
            , Optional<Condition> damageConditions, float forwardMovement, float damageMultiplier, Optional<Holder<SoundEvent>> sound, Optional<Holder<SoundEvent>> hitSound) {
        this.delayTicks = delayTicks;
        this.contactCooldownTicks = contactCooldownTicks;
        this.dismountConditions = dismountConditions;
        this.knockbackConditions = knockbackConditions;
        this.damageConditions = damageConditions;
        this.sound = sound;
        this.hitSound = hitSound;
        this.forwardMovement = forwardMovement;
        this.damageMultiplier = damageMultiplier;

    }

    public static Vec3 getMotion(Entity holder) {
        if (!(holder instanceof Player) && holder.isPassenger()) {
            holder = holder.getRootVehicle();
        }
        return holder.getDeltaMovement().scale(20.0F);
    }

    public void makeSound(Entity causer) {
        this.sound.ifPresent((s) -> causer.level().playSound((Player) null, causer.getX(), causer.getY(), causer.getZ(), s.get(), causer.getSoundSource(), 1.0F, 1.0F));
    }

    public void makeLocalHitSound(Entity causer) {
        this.hitSound.ifPresent((s) -> causer.level().playSound((Player) null, causer.getX(), causer.getY(), causer.getZ(), s.get(), causer.getSoundSource(), 1.0F, 1.0F));
    }

    public int computeDamageUseDuration() {
        return this.delayTicks + this.damageConditions.map(Condition::getMaxDurationTicks).orElse(0);
    }

    public void damageEntity(ItemStack stack, int tickRemaining, LivingEntity holder, EquipmentSlot slot) {
        int tickUsed = stack.getUseDuration() - tickRemaining;
        if (tickUsed >= this.delayTicks) {
            tickUsed -= this.delayTicks;
            Vec3 holderLookVector = holder.getLookAngle();
            double holderSpeedProjection = holderLookVector.dot(getMotion(holder));
            float actionFactor = holder instanceof Player ? 1.0F : 0.2F;
            float attackRange = (float) holder.getAttributeValue(ForgeMod.ENTITY_REACH.get());
            double baseDamage = holder.getAttributeValue(Attributes.ATTACK_DAMAGE);
            boolean affected = false;
            ExtendedProjectileUtil.getHitEntitiesAlong(holder, attackRange, this::canHitEntity, ClipContext.Block.COLLIDER).map((a) -> List.of(), (e) -> {
                for (EntityHitResult hitResult : e) {

                }
                return e;
            });
        }
    }

    protected boolean canHitEntity(Entity entity) {
        return entity.isAlive() && !entity.noPhysics;
    }

    protected void checkEntityHit(LivingEntity holder, double range) {
        if (!holder.level().isClientSide()) {
            for (Entity entity : this.getEntityCollisions(holder, range)) {
                this.onHitEntity(new EntityHitResult(entity));
            }
        }
    }

    private void onHitEntity(EntityHitResult entityHitResult) {

    }


    protected Set<Entity> getEntityCollisions(LivingEntity holder, double range) {
        List<Entity> collisions = new ArrayList<>(holder.level().getEntities(holder, holder.getBoundingBox().inflate(range)));
        return collisions.stream().filter(target ->
                target instanceof LivingEntity && target != holder
        ).collect(Collectors.toSet());
    }

    public static class Condition {
        public int maxDurationTicks;
        public float minSpeed;
        public float minRelativeSpeed;

        public Condition(int maxDurationTicks, float minSpeed, float minRelativeSpeed) {
            this.maxDurationTicks = maxDurationTicks;
            this.minSpeed = minSpeed;
            this.minRelativeSpeed = minRelativeSpeed;
        }

        public int getMaxDurationTicks() {
            return this.maxDurationTicks;
        }

        public float getMinSpeed() {
            return minSpeed;
        }

        public float getMinRelativeSpeed() {
            return minRelativeSpeed;
        }

        public boolean test(int tickUsed, double attackSpeed, double relativeSpeed, double entityFactor) {
            return tickUsed <= this.maxDurationTicks && attackSpeed >= this.minSpeed * entityFactor
                    && relativeSpeed >= this.minRelativeSpeed * entityFactor;
        }
        public static Optional<Condition> ofHolderSpeed(int untilTicks, float minHolderSpeed) {
            return Optional.of(new Condition(untilTicks, minHolderSpeed, 0.0F));
        }

        public static Optional<Condition> ofRelativeSpeed(int untilTicks, float minRelativeSped) {
            return Optional.of(new Condition(untilTicks, 0.0F, minRelativeSped));
        }
    }
}
