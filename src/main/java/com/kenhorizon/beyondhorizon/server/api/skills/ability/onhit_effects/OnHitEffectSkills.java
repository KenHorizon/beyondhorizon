package com.kenhorizon.beyondhorizon.server.api.skills.ability.onhit_effects;

import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageInfo;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;

public abstract class OnHitEffectSkills extends WeaponPassiveSkills {
    private final MobType mobType;

    public OnHitEffectSkills(float magnitude, float level, MobType mobType) {
        super(magnitude, level);
        this.mobType = mobType;
    }

    public OnHitEffectSkills(float magnitude, float level) {
        this(magnitude, level, null);
    }

    public MobType getMobType() {
        return mobType;
    }

    public static class RuinedBlade extends OnHitEffectSkills {

        public RuinedBlade(float magnitude) {
            super(magnitude, 1);
        }

        @Override
        public float preMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
            if (attacker == null || target == null) return context.damage();
            return DamageInfo.getCurrentHealth(target, context, this.getMagnitude());
        }
    }
}

