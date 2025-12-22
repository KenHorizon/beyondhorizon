package com.kenhorizon.beyondhorizon.server.level.damagesource;

import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class DamageInfo implements IDamageInfo {
    private float preDamage = 0.0F;
    private float postDamage = 0.0F;
    private float preStoredDamage = 0.0F;
    private float postStoredDamage = 0.0F;
    private LivingEntity attacker;

    public static final String NBT_PRE_DAMAGE = "PreDamage";
    public static final String NBT_POST_DAMAGE = "PostDamage";
    public static final String NBT_STORED_PRE_DAMAGE = "StoredPreDamage";
    public static final String NBT_STORED_POST_DAMAGE = "StoredPostDamage";

    public static IDamageInfo getInstance(LivingEntity entity) {
        return CapabilityCaller.damageInfo(entity);
    }

    @Override
    public void setPreDamage(float damage) {
        this.preDamage = damage;
    }
    @Override
    public void setPostDamage(float damage) {
        this.postDamage = damage;
    }

    @Override
    public void setPreStoredDamage(float storedDamage) {
        this.preStoredDamage = storedDamage;
    }
    @Override
    public void setPostStoredDamage(float storedDamage) {
        this.postStoredDamage = storedDamage;
    }

    @Override
    public float getPostStoredDamage() {
        return this.preStoredDamage;
    }
    @Override
    public float getPreStoredDamage() {
        return this.postStoredDamage;
    }

    @Override
    public float preDamage() {
        return this.preDamage;
    }

    @Override
    public float postDamage() {
        return this.postDamage;
    }

    @Override
    public void setLastAttacker(LivingEntity attacker) {
        this.attacker = attacker;
    }

    @Override
    public LivingEntity getAttacker() {
        return this.attacker;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat(NBT_PRE_DAMAGE, this.preDamage);
        nbt.putFloat(NBT_POST_DAMAGE, this.postDamage);
        nbt.putFloat(NBT_STORED_PRE_DAMAGE, this.preStoredDamage);
        nbt.putFloat(NBT_STORED_POST_DAMAGE, this.postStoredDamage);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.preDamage = nbt.getFloat(NBT_PRE_DAMAGE);
        this.postDamage = nbt.getFloat(NBT_POST_DAMAGE);
        this.preStoredDamage = nbt.getFloat(NBT_STORED_PRE_DAMAGE);
        this.postStoredDamage = nbt.getFloat(NBT_STORED_POST_DAMAGE);
    }
}
