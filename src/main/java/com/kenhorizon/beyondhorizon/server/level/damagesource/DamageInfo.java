package com.kenhorizon.beyondhorizon.server.level.damagesource;

import com.kenhorizon.beyondhorizon.server.api.level.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class DamageInfo implements IDamageInfo {
    private float preDamage = 0.0F;
    private float postDamage = 0.0F;
    private float preStoredDamage = 0.0F;
    private float postStoredDamage = 0.0F;
    private boolean isDamageCrit = false;
    private LivingEntity attacker;
    private DamageSource damageSource;

    public static final String NBT_RECEIVED_CRIT_DAMAGE = "ReceivedCritDamage";
    public static final String NBT_PRE_DAMAGE = "PreDamage";
    public static final String NBT_POST_DAMAGE = "PostDamage";
    public static final String NBT_STORED_PRE_DAMAGE = "StoredPreDamage";
    public static final String NBT_STORED_POST_DAMAGE = "StoredPostDamage";

    @Override
    public void setDamageSource(DamageSource damageSource) {
        this.damageSource = damageSource;
    }

    @Override
    public DamageSource getDamageSource() {
        return damageSource;
    }

    @Override
    public void setReceivedCritDamage(boolean receivedCritDamage) {
        this.isDamageCrit = receivedCritDamage;
    }

    @Override
    public boolean isReceivedCritDamage() {
        return this.isDamageCrit;
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
    public void reset() {
        this.setPreDamage(0);
        this.setPostDamage(0);
        this.setPreStoredDamage(0);
        this.setPostStoredDamage(0);
        this.setLastAttacker(null);
        this.setDamageSource(null);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean(NBT_RECEIVED_CRIT_DAMAGE, this.isDamageCrit);
        nbt.putFloat(NBT_PRE_DAMAGE, this.preDamage);
        nbt.putFloat(NBT_POST_DAMAGE, this.postDamage);
        nbt.putFloat(NBT_STORED_PRE_DAMAGE, this.preStoredDamage);
        nbt.putFloat(NBT_STORED_POST_DAMAGE, this.postStoredDamage);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.isDamageCrit = nbt.getBoolean(NBT_RECEIVED_CRIT_DAMAGE);
        this.preDamage = nbt.getFloat(NBT_PRE_DAMAGE);
        this.postDamage = nbt.getFloat(NBT_POST_DAMAGE);
        this.preStoredDamage = nbt.getFloat(NBT_STORED_PRE_DAMAGE);
        this.postStoredDamage = nbt.getFloat(NBT_STORED_POST_DAMAGE);
    }
}
