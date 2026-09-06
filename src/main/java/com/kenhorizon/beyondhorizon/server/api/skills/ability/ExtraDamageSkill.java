package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.DamageTypeFunction;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageInfo;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ExtraDamageSkill extends WeaponPassiveSkills {
    private final DamageTypeFunction damageFunction;
    private MobType mobType;

    public ExtraDamageSkill(float magnitude, float level, MobType mobType, DamageTypeFunction damageFunction) {
        this.damageFunction = damageFunction;
        this.setMagnitude(magnitude);
        this.setLevel(level);
        this.mobType = mobType;
    }
    public ExtraDamageSkill(float magnitude, float level, DamageTypeFunction damageTypeFunction) {
        this(magnitude, level, null, damageTypeFunction);
    }
    public ExtraDamageSkill(float magnitude, DamageTypeFunction damageTypeFunction) {
        this(magnitude, 1.0F, null, damageTypeFunction);
    }

    public ExtraDamageSkill(float magnitude, MobType mobType, DamageTypeFunction damageTypeFunction) {
        this(magnitude, 1.0F, mobType, damageTypeFunction);
    }



    @Override
    public float preMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return context.damage();
        return this.damageFunction.calculate(this.getMagnitude(), this.getLevel(), this.mobType, context, source, attacker, target);
    }
}

