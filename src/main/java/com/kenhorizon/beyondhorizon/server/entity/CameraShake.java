package com.kenhorizon.beyondhorizon.server.entity;

import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CameraShake extends Entity {
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(CameraShake.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MAGNITUDE = SynchedEntityData.defineId(CameraShake.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(CameraShake.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FADE_DURATION = SynchedEntityData.defineId(CameraShake.class, EntityDataSerializers.INT);
    public static final String NBT_RADIUS = "radius";
    public static final String NBT_MAGNITUDE = "magnitude";
    public static final String NBT_DURATION = "duration";
    public static final String NBT_FADE_DURATION = "fade_duration";
    public static final String NBT_TICK_EXISTED = "ticks_existed";

    public CameraShake(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    public CameraShake(Level level, Vec3 position, float radius, float magnitude, int duration, int fadeDuration) {
        this(BHEntity.CAMERA_SHAKE.get(), level);
        this.setRadius(radius);
        this.setMagnitude(magnitude);
        this.setDuration(duration);
        this.setFadeDuration(fadeDuration);
        this.setPos(position.x(), position.y(), position.z());
    }


    @Override
    protected void defineSynchedData() {
        this.entityData.define(RADIUS, 10.0f);
        this.entityData.define(MAGNITUDE, 1.0f);
        this.entityData.define(DURATION, 0);
        this.entityData.define(FADE_DURATION, 5);
    }

    public float getRadius() {
        return this.entityData.get(RADIUS);
    }

    public void setRadius(float radius) {
        this.entityData.set(RADIUS, radius);
    }

    public float getMagnitude() {
        return this.entityData.get(MAGNITUDE);
    }

    public void setMagnitude(float magnitude) {
        this.entityData.set(MAGNITUDE, magnitude);
    }

    public int getDuration() {
        return this.entityData.get(DURATION);
    }

    public void setDuration(int duration) {
        this.entityData.set(DURATION, duration);
    }

    public int getFadeDuration() {
        return this.entityData.get(FADE_DURATION);
    }

    public void setFadeDuration(int fadeDuration) {
        this.entityData.set(FADE_DURATION, fadeDuration);
    }



    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        setRadius(nbt.getFloat(NBT_RADIUS));
        setMagnitude(nbt.getFloat(NBT_MAGNITUDE));
        setDuration(nbt.getInt(NBT_DURATION));
        setFadeDuration(nbt.getInt(NBT_FADE_DURATION));
        tickCount = nbt.getInt(NBT_TICK_EXISTED);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putFloat(NBT_RADIUS, this.getRadius());
        nbt.putFloat(NBT_MAGNITUDE, this.getMagnitude());
        nbt.putInt(NBT_DURATION, this.getDuration());
        nbt.putInt(NBT_FADE_DURATION, this.getFadeDuration());
        nbt.putInt(NBT_TICK_EXISTED, tickCount);
    }

    @Override
    public void tick() {
        if (tickCount > this.getDuration() + this.getFadeDuration()) this.discard();
    }

    public static void spawn(Level level, Vec3 position, float radius, float magnitude, int duration, int fadeDuration) {
        if (!level.isClientSide()) {
            CameraShake cameraShake = new CameraShake(level, position, radius, magnitude, duration, fadeDuration);
            level.addFreshEntity(cameraShake);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getShakeAmount(Player player, float delta) {
        float ticksDelta = this.tickCount + delta;
        float timeFrac = 1.0f - (ticksDelta - this.getDuration()) / (this.getFadeDuration() + 1.0f);
        float baseAmount = ticksDelta < getDuration() ? getMagnitude() : timeFrac * timeFrac * getMagnitude();
        Vec3 playerPos = player.getEyePosition(delta);
        float distFrac = (float) (1.0f - Mth.clamp(position().distanceTo(playerPos) / this.getRadius(), 0, 1));
        return baseAmount * distFrac * distFrac;
    }
}
