package com.kenhorizon.beyondhorizon.server.entity.ai.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class DodgeAbility {
    public static final String COOLDOWN = "cd";
    public static final String DODGE_CHANCES = "chances";
    private LivingEntity entity;
    private int cooldown = 0;
    private float dodgeChances = 0.0F;

    public DodgeAbility(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setDodgeChances(float dodgeChances) {
        this.dodgeChances = dodgeChances;
    }

    public float getDodgeChances() {
        return dodgeChances;
    }

    public void tick() {
        if (this.getCooldown() > 0) this.setCooldown(this.getCooldown() - 1);
    }

    public boolean doDodge(int cooldown) {
        boolean flag = this.getEntity().getRandom().nextFloat() <= this.dodgeChances;
        if (flag) {
            this.setCooldown(cooldown);
            return true;
        } else {
            return false;
        }
    }


    public CompoundTag saveNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(COOLDOWN, this.getCooldown());
        nbt.putFloat(DODGE_CHANCES, this.getDodgeChances());
        return nbt;
    }

    public void loadNbt(CompoundTag nbt) {
        this.setCooldown(nbt.getInt(COOLDOWN));
        this.setDodgeChances(nbt.getFloat(DODGE_CHANCES));
    }

}
