package com.kenhorizon.beyondhorizon.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class BHBaseEntity extends PathfinderMob {
    private float damageCap;
    private boolean active;
    private boolean dodge;
    protected int dodgeAttempt = 0;
    protected int tickFarAway;
    protected int tickDodge;
    public float targetDistance = -1;
    public float targetAngle = -1;
    private static final byte MUSIC_PLAY_ID = 67;
    private static final byte MUSIC_STOP_ID = 68;
    public BHBaseEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public float getHealthRatio() {
        return Mth.clamp(this.getHealth() / this.getMaxHealth(), 0.0F, 1.0F);
    }

    public static AttributeSupplier.Builder createEntityAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
    }
    public static boolean checkMonsterSpawnRules(EntityType<? extends BHBaseEntity> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos blockPos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && BHBaseEntity.isDarkEnoughToSpawn(level, blockPos, random) && checkMobSpawnRules(type, level, spawnType, blockPos, random);
    }

    private static boolean isDarkEnoughToSpawn(ServerLevelAccessor level, BlockPos blockPos, RandomSource random) {
        if (level.getBrightness(LightLayer.SKY, blockPos) > random.nextInt(32)) {
            return false;
        } else {
            DimensionType dimensionType = level.dimensionType();
            int lightLimit = dimensionType.monsterSpawnBlockLightLimit();
            if (lightLimit < 15 && level.getBrightness(LightLayer.BLOCK, blockPos) > lightLimit) {
                return false;
            } else {
                int lightLevel = level.getLevel().isThundering() ? level.getMaxLocalRawBrightness(blockPos, 10) : level.getMaxLocalRawBrightness(blockPos);
                return lightLevel <= dimensionType.monsterSpawnLightTest().sample(random);
            }
        }
    }

    public void setConfigattribute(LivingEntity entity, double hpConfig, double dmgConfig) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr != null) {
            double difference = maxHealthAttr.getBaseValue() * hpConfig - maxHealthAttr.getBaseValue();
            maxHealthAttr.addTransientModifier(new AttributeModifier(UUID.fromString("9513569b-57b6-41f5-814e-bdc49b81799f"), "Health config multiplier", difference, AttributeModifier.Operation.ADDITION));
            entity.setHealth(entity.getMaxHealth());
        }
        AttributeInstance attackDamageAttr = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamageAttr != null) {
            double difference = attackDamageAttr.getBaseValue() * dmgConfig - attackDamageAttr.getBaseValue();
            attackDamageAttr.addTransientModifier(new AttributeModifier(UUID.fromString("5b17d7cb-294e-4379-88ab-136c372bec9b"), "Attack config multiplier", difference, AttributeModifier.Operation.ADDITION));

        }
    }

    protected BHBossInfo bossInfo() {
        return new BHBossInfo(this);
    }

    public boolean hasBossBar() {
        return false;
    }

    public void setDamageCap(float damageCap) {
        this.damageCap = damageCap;
    }

    public float getDamageCap() {
        return this.damageCap;
    }

    public boolean allowDamageCap() {
        return this.getDamageCap() > 0;
    }

    public boolean hasBossMusic() {
        return false;
    }
    @Override
    public void setCustomName(@Nullable Component nbt) {
        super.setCustomName(nbt);
        this.bossInfo().setName(this.getDisplayName());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 4 == 0) this.bossInfo().update();
        if (!level().isClientSide() && this.hasBossMusic()) {
            if (this.canPlayMusic()) {
                this.level().broadcastEntityEvent(this, MUSIC_PLAY_ID);
            } else {
                this.level().broadcastEntityEvent(this, MUSIC_STOP_ID);
            }
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo().addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo().removePlayer(player);
    }
    protected boolean canPlayMusic() {
        return this.isAlive() && !this.isSilent() && this.getTarget() instanceof Player && getTarget() != null;
    }

    public boolean canPlayerHearMusic(Player player) {
        return player != null && this.canAttack(player) && distanceTo(player) < 2500;
    }

//    @Override
//    public void handleEntityEvent(byte id) {
//        if (id == MUSIC_PLAY_ID) {
//            BossMusicPlayer.requestBossMusic(this);
//        } else if (id == MUSIC_STOP_ID) {
//            BossMusicPlayer.stopBossMusic(this);
//        } else {
//            super.handleEntityEvent(id);
//        }
//    }

    protected void afterBossIsDefeated(@Nullable LivingEntity entity) {}

    public boolean canBePushedByEntity(Entity entity) {
        return true;
    }

//    @OnlyIn(Dist.CLIENT)
//    public BossMusic getBossMusic() {
//        return null;
//    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!source.is(DamageTypeTags.BYPASSES_ARMOR) && this.allowDamageCap()) {
            amount = Math.min(this.getDamageCap(), amount);
        }
        return super.hurt(source, amount);
    }
    @Override
    public void push(Entity entityIn) {
        if (!this.isSleeping()) {
            if (!this.isPassengerOfSameVehicle(entityIn)) {
                if (!entityIn.noPhysics && !this.noPhysics) {
                    double d0 = entityIn.getX() - this.getX();
                    double d1 = entityIn.getZ() - this.getZ();
                    double d2 = Mth.absMax(d0, d1);
                    if (d2 >= (double)0.01F) {
                        d2 = Math.sqrt(d2);
                        d0 = d0 / d2;
                        d1 = d1 / d2;
                        double d3 = 1.0D / d2;
                        if (d3 > 1.0D) {
                            d3 = 1.0D;
                        }

                        d0 = d0 * d3;
                        d1 = d1 * d3;
                        d0 = d0 * (double)0.05F;
                        d1 = d1 * (double)0.05F;
                        if (!this.isVehicle()) {
                            if (this.canBePushedByEntity(entityIn)) {
                                this.push(-d0, 0.0D, -d1);
                            }
                        }

                        if (!entityIn.isVehicle()) {
                            entityIn.push(d0, 0.0D, d1);
                        }
                    }

                }
            }
        }
    }
    public List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double r) {
        return level().getEntitiesOfClass(entityClass, getBoundingBox().inflate(r, r, r), e -> e != this && distanceTo(e) <= r + e.getBbWidth() / 2f);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return level().getEntitiesOfClass(entityClass, getBoundingBox().inflate(dX, dY, dZ), e -> e != this && distanceTo(e) <= r + e.getBbWidth() / 2f && e.getY() <= getY() + dY);
    }

    protected void repelEntities(float x, float y, float z, float radius, LivingEntity owner) {
        List<LivingEntity> nearbyEntities = this.getEntityLivingBaseNearby(x, y, z, radius);
        for (LivingEntity entity : nearbyEntities) {
            if (entity == owner) continue;
            if (entity.isPickable() && !entity.noPhysics) {
                double angle = (this.getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
                entity.setDeltaMovement(-0.1 * Math.cos(angle), entity.getDeltaMovement().y, -0.1 * Math.sin(angle));
            }
        }
    }

    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.getZ() - first.getZ(), second.getX() - first.getX()) * (180 / Math.PI) + 90;
    }

    public boolean hurtEntitiesAround(Vec3 center, float radius, float damageAmount, float knockbackAmount, boolean setsOnFire, boolean disablesShields){
        AABB aabb = this.getBoundingBox().inflate(radius);
        boolean flag = false;
        DamageSource damageSource = this.damageSources().mobAttack(this);
        for(LivingEntity living : level().getEntitiesOfClass(LivingEntity.class, aabb, EntitySelector.NO_CREATIVE_OR_SPECTATOR)){
            if (!living.is(this) && !living.isAlliedTo(this) && living.getType() != this.getType()) {
                if (living.isDamageSourceBlocked(damageSource) && disablesShields && living instanceof Player player){
                    player.disableShield(true);
                }
                if (living.hurt(damageSource, damageAmount)){
                    flag = true;
                    if (setsOnFire){
                        living.setSecondsOnFire(10);
                    }
                    living.knockback(knockbackAmount, center.x - living.getX(), center.z - living.getZ());
                }
            }
        }
        return flag;
    }
    public void disableShield(Player player, int disalbeDuration) {
        if (player.isBlocking()) {
            player.getCooldowns().addCooldown(this.getUseItem().getItem(), disalbeDuration);
            player.stopUsingItem();
            this.level().broadcastEntityEvent(this, (byte) 30);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
