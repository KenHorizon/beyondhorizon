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
        nbt.putFloat("pre_damage", this.preDamage);
        nbt.putFloat("post_damage", this.postDamage);
        nbt.putFloat("stored_pre_damage", this.preStoredDamage);
        nbt.putFloat("stored_post_damage", this.postStoredDamage);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.preDamage = nbt.getFloat("pre_damage");
        this.postDamage = nbt.getFloat("post_damage");
        this.preStoredDamage = nbt.getFloat("stored_pre_damage");
        this.postStoredDamage = nbt.getFloat("stored_post_damage");
    }
}
