package com.kenhorizon.beyondhorizon.server.world.entity.boss.pyrolliger;

import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.server.api.skills.ability.active.InfernalRaySkill;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.beyondhorizon.server.world.entity.BHLibEntity;
import com.kenhorizon.beyondhorizon.server.world.entity.ILinkedEntity;
import com.kenhorizon.beyondhorizon.server.world.entity.ability.beam.BeamDamageTags;
import com.kenhorizon.beyondhorizon.server.world.entity.ability.beam.BeamTypeFunction;
import com.kenhorizon.beyondhorizon.server.world.entity.ability.beam.InfernalRayAbility;
import com.kenhorizon.beyondhorizon.server.world.entity.ai.MobAttackGoal;
import com.kenhorizon.beyondhorizon.server.world.entity.boss.blazing_inferno.BlazingInferno;
import com.kenhorizon.beyondhorizon.server.world.entity.projectiles.BlazingRod;
import com.kenhorizon.beyondhorizon.server.world.entity.util.AnimationTickers;
import com.kenhorizon.beyondhorizon.server.world.entity.util.EntityUtils;
import com.kenhorizon.beyondhorizon.server.world.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.world.network.packet.server.ServerboundAbilityEffectPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class PyroGem extends BHLibEntity implements ILinkedEntity {
    public static final int ATTACK_INTERVAL = Maths.sec(6);
    public static final int SPECIAL_ATTACK = Maths.sec(30);
    public AnimationState animationAttack = new AnimationState();
    public AnimationState animationPrepDeathRay = new AnimationState();
    public AnimationState animationDeathRay = new AnimationState();
    public final AnimationTickers attackCooldown = AnimationTickers.create(ATTACK_INTERVAL);
    public final AnimationTickers specialAttackCooldown = AnimationTickers.create(SPECIAL_ATTACK);
    private LivingEntity cachedCaster;
    public static final String NBT_OWNER = "Owner";
    public static int animationId = 1;

    public static final int ID_ATTACK = createAnimationID();
    public static final int ID_PREPARE_DEATH_RAY = createAnimationID();
    public static final int ID_DEATH_RAY = createAnimationID();

    private static final EntityDataAccessor<Optional<UUID>> ENTITY_UUID = SynchedEntityData.defineId(PyroGem.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> ENTITY_ID = SynchedEntityData.defineId(PyroGem.class, EntityDataSerializers.INT);

    private static int createAnimationID() {
        return animationId++;
    }

    public PyroGem(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public PyroGem(Level level, LivingEntity caster) {
        super(BHEntity.PYRO_GEM.get(), level);
        this.setOwner(caster);
        this.setPos(caster.getX(), caster.getY(), caster.getZ());
    }

    public static AttributeSupplier createAttributes() {
        return createEntityAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));

        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_ATTACK, ID_ANIMATION_EMPTY, 30, Maths.sec(3)) {
            @Override
            public boolean canUse() {
                return super.canUse() && this.entity.attackCooldown.isReadyToUse();
            }

            @Override
            public void stop() {
                super.stop();
                this.entity.attackCooldown.setCooldown();
            }
        });
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_ANIMATION_EMPTY, ID_PREPARE_DEATH_RAY, ID_DEATH_RAY, 60, Maths.sec(5)) {
            @Override
            public boolean canUse() {
                return super.canUse() && this.entity.specialAttackCooldown.isReadyToUse();
            }
        });
        this.goalSelector.addGoal(1, new MobAttackGoal<>(this, ID_DEATH_RAY, ID_DEATH_RAY, ID_ANIMATION_EMPTY, Maths.sec(5), Maths.sec(10)) {

            @Override
            public void stop() {
                super.stop();
                this.entity.specialAttackCooldown.setCooldown();
            }

            @Override
            public void tick() {
                super.tick();
                LivingEntity target = this.entity.getTarget();
                super.tick();
                if (this.entity.getAnimationTick() == 2) {
                    float radius = 0.80F;
                    int duration = Maths.sec(5);
                    InfernalRayAbility ability = new InfernalRayAbility(this.entity.level(), this.entity,
                            this.entity.getX() + radius * Math.sin(-this.entity.getYRot() * Math.PI / 180),
                            this.entity.getY() + 1.4, this.entity.getZ() + radius * Math.cos(-this.entity.getYRot() * Math.PI / 180),
                            (float) ((this.entity.yHeadRot + 90) * Math.PI / 180), (float) (-this.entity.getXRot() * Math.PI / 180), duration);
                    ability.setBaseDamage(1.0F);
                    ability.damageConfig(new BeamTypeFunction(BeamDamageTags.DEFAULT, 0));
                    ability.setCanBurnTarget(true);
                    ability.setImmunityFrameIgnore(true);
                    this.entity.level().addFreshEntity(ability);
                }
                if (this.entity.getAnimationTick() >= 2) {
                    if (target != null) {
                        this.entity.getLookControl().setLookAt(target.getX(),target.getY() + target.getBbHeight() / 2, target.getZ(), 1.0F, 5.0F);
                    }
                }
            }
        });
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ENTITY_UUID, Optional.empty());
        this.entityData.define(ENTITY_ID, -1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.hasUUID(NBT_OWNER)) {
            this.setEntityUUID(nbt.getUUID(NBT_OWNER));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (this.getEntityUUID().isPresent()) {
            nbt.putUUID(NBT_OWNER, this.getEntityUUID().get());
        }
    }

    public void setEntityUUID(UUID uuid) {
        this.entityData.set(ENTITY_UUID, Optional.ofNullable(uuid));
    }

    public Optional<UUID> getEntityUUID() {
        return this.entityData.get(ENTITY_UUID);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    private void shoot(LivingEntity entity, float velocity, float inaccuracy) {
        double d0 = this.getX();
        double d1 = this.getY() + (this.getBbHeight() / 2) + 0.5D;
        double d2 = this.getZ();
        BlazingRod projectile = new BlazingRod(this.level(), d0, d1, d2, this);
        projectile.setBaseDamage((float) EntityUtils.getAttackDamage(entity, 0.25F));
        Vec3 vector3d = this.getViewVector(1.0F);
        projectile.shoot(vector3d.x(), vector3d.y(), vector3d.z(), velocity, inaccuracy);
        this.level().addFreshEntity(projectile);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        if (this.isCantDespawn()) {
            return false;
        } else {
            return this.getUsingEntity() != null;
        }
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return false;
    }

    @Override
    public void move(MoverType type, Vec3 vec3) {
        super.move(type, vec3);
        this.checkInsideBlocks();
    }

    @Override
    public void tick() {
        super.tick();
        attackCooldown.cooldownTick();
        specialAttackCooldown.cooldownTick();
        super.tick();
        this.reapplyPosition();
        LivingEntity owner;
        if (this.getUsingEntity() == null) {
            owner = this.getOwner();
        } else {
            owner = this.getUsingEntity();
        }
        if (owner != null && !owner.isAlive()) this.discard();
        if (owner != null) {
            if (this.level().isClientSide()) {
                for (int i = 0; i < 2; i++) {
                    this.level().addParticle(new TrailParticleOptions(20, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, TrailParticles.Behavior.SHRINK, owner.position().add(0, owner.getBbHeight() * 0.5D, 0)), this.getRandomX(0.25D), this.getY() + (this.getBbHeight() / 2), this.getRandomZ(0.25D), 0, 0, 0);
                }
            }
            Entity entity = this.getEntityId() == -1 ? null : level().getEntity(this.getEntityId());
            if (owner instanceof PathfinderMob mob) {
                if (mob.getTarget() != null) {
                    this.setTarget(mob.getTarget());
                }
            }
            if (owner.deathTime > 0) {
                this.kill();
            }
            this.pyroGemTicks(entity != null ? entity : owner);
        }
    }

    @Override
    public void onStartAnimation() {
        LivingEntity owner;
        if (this.getUsingEntity() == null) {
            owner = this.getOwner();
        } else {
            owner = this.getUsingEntity();
        }
        if (owner != null) {

            Entity entity = this.getEntityId() == -1 ? null : level().getEntity(this.getEntityId());
            if (this.getAnimationState(ID_ATTACK)) {
                for (int i = 0; i < 3 ;i++) {
                    this.shoot(owner, 1.0F, 1.54F);
                }
            }
        }
    }

    public void setOwner(LivingEntity entity) {
        this.setEntityUUID(entity.getUUID());
        this.setEntityId(entity.getId());
        this.cachedCaster = entity;
    }

    private void setEntityId(int id) {
        this.entityData.set(ENTITY_ID, id);
    }

    private int getEntityId() {
        return this.entityData.get(ENTITY_ID);
    }

    private void pyroGemTicks(Entity entity) {
        Vec3 orbitBy = new Vec3(0.0D, 2.0D, 0.0D);
        Vec3 orbitTarget = entity.position().add(0, entity.getBbHeight() * 0.25D, 0).add(orbitBy).subtract(this.position());
        this.setDeltaMovement(orbitTarget.scale(0.30F));
        this.noPhysics = true;
    }

    protected static float lerpRotation(float currentRotation, float targetRotation) {
        while(targetRotation - currentRotation < -180.0F) {
            currentRotation -= 360.0F;
        }

        while(targetRotation - currentRotation >= 180.0F) {
            currentRotation += 360.0F;
        }

        return Mth.lerp(0.2F, currentRotation, targetRotation);
    }
    @Override
    public void link(Entity entity) {
        if (entity instanceof LivingEntity) {
            this.cachedCaster = (LivingEntity) entity;
        }
    }

    public LivingEntity getUsingEntity() {
        if (this.getEntityUUID().isPresent() && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.getEntityUUID().get());
            if (entity instanceof LivingEntity) {
                this.cachedCaster = (LivingEntity) entity;
                NetworkHandler.sendAll(new ServerboundAbilityEffectPacket(this, this.cachedCaster), this);
            }
        }
        return this.cachedCaster;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public LivingEntity getOwner() {
        return this.cachedCaster;
    }
}
