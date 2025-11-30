package com.kenhorizon.beyondhorizon.server.skills.skill;

import com.kenhorizon.beyondhorizon.server.item.materials.IWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.skills.WeaponSkills;
import com.kenhorizon.beyondhorizon.server.util.Tooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ExtraDamageSkill extends WeaponSkills {
    @FunctionalInterface
    public interface DamageTypeFunction {
        public float calculate(float magnitude, float level, float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target);
    }
    public static final DamageTypeFunction CURRENT_HEALTH = ((magnitude, level, damageDealt, source, attacker, target) -> {
        return damageDealt + (target.getHealth() * (magnitude * level));
    });
    public static final DamageTypeFunction MAX_HEALTH = ((magnitude, level, damageDealt, source, attacker, target) -> {
        return damageDealt + (target.getMaxHealth() * (magnitude * level));
    });
    public static final DamageTypeFunction MISSING_HEALTH = ((magnitude, level, damageDealt, source, attacker, target) -> {
        float damageMultiplier = (target.getMaxHealth() - target.getHealth() / target.getMaxHealth());
        float amplifier = 0.0F;
        if ((magnitude * level) != 0) {
            for (int i = 0; i < (100 * magnitude); ++i) {
                if (i % magnitude == 0) {
                    amplifier++;
                    float perDamage = (amplifier / 100.0F);
                    return (1.0F + perDamage) * damageDealt;
                }
            }
        }
        return (float) (damageDealt * (1.0F + (Mth.clamp(damageMultiplier, 0.0F, 1.0F))));
    });
    public static final DamageTypeFunction BONUS_DAMAGE = ((magnitude, level, damageDealt, source, attacker, target) -> {
        return damageDealt + (magnitude * level);
    });
    private final DamageTypeFunction damageFunction;
    private final float magnitude;
    private final float level;

    public ExtraDamageSkill(float magnitude, float level, DamageTypeFunction damageFunction) {
        this.damageFunction = damageFunction;
        this.magnitude = magnitude;
        this.level = level;
    }

    public ExtraDamageSkill(float magnitude, DamageTypeFunction damageTypeFunction) {
        this(magnitude, 1.0F, damageTypeFunction);
    }

    @Override
    protected void addTooltipDescription(ItemStack itemStack, List<Component> tooltip) {
        if (this.magnitude > 0.0F && this.level > 0.0F) {
            tooltip.add(Component.translatable(this.createId(), this.magnitude, this.level).withStyle(Tooltips.TOOLTIP[1]));
        } else {
            tooltip.add(Component.translatable(this.createId(), this.magnitude).withStyle(Tooltips.TOOLTIP[1]));
        }
    }
    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        return this.damageFunction.calculate(this.magnitude, this.level, damageDealt, source, attacker, target);
    }
}

