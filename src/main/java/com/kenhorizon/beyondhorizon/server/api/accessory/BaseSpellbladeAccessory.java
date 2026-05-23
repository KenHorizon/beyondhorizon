package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityData;
import com.kenhorizon.beyondhorizon.server.level.ICombatCore;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class BaseSpellbladeAccessory extends AccessorySkill {
    protected float attackScale;
    protected int timer;
    protected int attackInterval;
    protected boolean isActive;

    public BaseSpellbladeAccessory(int attackInterval, float attackScale) {
        this.attackScale = attackScale;
        this.attackInterval = MathUtils.sec(attackInterval);
    }

    protected abstract float spellBladeDamage(LivingEntity attacker, float damage, float damageScale);

    protected abstract String spellBladeTag();

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        return Component.translatable(this.createId(), MathUtils.format((this.attackInterval / 20.0F)), MathUtils.format(this.attackScale * 100.0F));
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        RandomSource random = entity.getRandom();
        CompoundTag tag = EntityData.getOrCreateTag(entity);
        int cooldown = 0;
        boolean flag = false;
        ICombatCore combat = CapabilityCaller.combat(entity);
        if (!entity.level().isClientSide()) {
            cooldown = tag.getInt(this.spellBladeTag());
            this.timer = cooldown;
            if (cooldown < this.attackInterval && !combat.OnCombat()) {
                cooldown++;
                tag.putInt(this.spellBladeTag(), cooldown);
            }
            this.isActive = tag.getInt(this.spellBladeTag()) >= this.attackInterval;
            BeyondHorizon.LOGGER.debug("[Accessory] Spell blade {} {} || {}", this.spellBladeTag(), cooldown, this.isActive);
        }
        if (this.isActive) {
            Level level = entity.level();
            if (level instanceof ServerLevel slevel) {
                slevel.sendParticles(ParticleTypes.FLAME, entity.getX(), entity.getY(0.5D), entity.getZ(), 10, 0,0,0,0);
            }
        }
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        CompoundTag tag = EntityData.getOrCreateTag(attacker);
        if (this.isActive) {
            tag.putInt(this.spellBladeTag(), 0);
            this.isActive = false;
            return (float) (damageDealt + this.spellBladeDamage(attacker, damageDealt, this.attackScale));
        }
        return damageDealt;
    }
}
