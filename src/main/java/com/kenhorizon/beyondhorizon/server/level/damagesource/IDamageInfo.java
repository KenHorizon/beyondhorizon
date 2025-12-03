package com.kenhorizon.beyondhorizon.server.level.damagesource;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

public interface IDamageInfo extends INBTSerializable<CompoundTag> {

    void setPreDamage(float damage);

    void setPostDamage(float damage);

    void setPreStoredDamage(float storedDamage);

    void setPostStoredDamage(float storedDamage);

    float getPostStoredDamage();

    float getPreStoredDamage();

    float preDamage();

    float postDamage();

    void setLastAttacker(LivingEntity attacker);

    LivingEntity getAttacker();

}
