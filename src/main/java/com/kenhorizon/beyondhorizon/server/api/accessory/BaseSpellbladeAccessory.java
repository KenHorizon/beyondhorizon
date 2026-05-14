package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityData;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class BaseSpellbladeAccessory extends AccessorySkill {
    private float attackScale;
    private int timer;
    private int attackInterval;
    private boolean isActive;

    public BaseSpellbladeAccessory(int attackInterval, float attackScale) {
        this.attackScale = attackScale;
        this.attackInterval = MathUtils.sec(attackInterval);
    }

    protected abstract float spellBladeDamage(LivingEntity attacker, float damage, float damageScale);
    protected abstract String spellBladeTag();

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        RandomSource random = entity.getRandom();
        CompoundTag tag = EntityData.getOrCreateTag(entity);
        int cooldown = 0;
        boolean flag = false;
        if (!entity.level().isClientSide()) {
            cooldown = tag.getInt(this.spellBladeTag());
            this.timer = cooldown;
            if (cooldown < this.attackInterval) {
                cooldown += 1;
                tag.putInt(this.spellBladeTag(), cooldown);
            }
            tag.putBoolean(this.spellBladeTag() + "_active", cooldown >= this.attackInterval);
            flag = tag.getBoolean(this.spellBladeTag() + "_active");
            this.isActive = flag;
            BeyondHorizon.LOGGER.debug("[Accessory] Spell blade {} {} || {}", this.spellBladeTag(), cooldown, flag);

        }
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        CompoundTag tag = EntityData.getOrCreateTag(attacker);
        if (this.isActive) {
            tag.putInt(this.spellBladeTag(), 0);
            return (float) (damageDealt + this.spellBladeDamage(attacker, damageDealt, this.attackScale));
        }
        return damageDealt;
    }
}
