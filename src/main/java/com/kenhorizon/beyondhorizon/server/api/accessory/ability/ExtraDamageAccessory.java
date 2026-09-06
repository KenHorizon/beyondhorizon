package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.DamageTypeFunction;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.skills.ability.ExtraDamageSkill;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageInfo;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ExtraDamageAccessory extends AccessoryPassiveSkill {
    private final DamageTypeFunction damageFunction;
    private MobType mobType;

    public ExtraDamageAccessory(float magnitude, int level, MobType mobType, DamageTypeFunction damageFunction) {
        super(magnitude, level);
        this.damageFunction = damageFunction;
        this.setMagnitude(magnitude);
        this.setLevel(level);
        this.mobType = mobType;
    }
    public ExtraDamageAccessory(float magnitude, int level, DamageTypeFunction damageTypeFunction) {
        this(magnitude, level, null, damageTypeFunction);
    }
    public ExtraDamageAccessory(float magnitude, DamageTypeFunction damageTypeFunction) {
        this(magnitude, 1, null, damageTypeFunction);
    }
    public ExtraDamageAccessory(float magnitude, MobType mobType, DamageTypeFunction damageTypeFunction) {
        this(magnitude, 1, mobType, damageTypeFunction);
    }

    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        if (this.getMagnitude() > 0.0F && this.getLevel() > 0.0F) {
            return Component.translatable(this.createId(), Maths.format(100.0F * this.getMagnitude()), this.getLevel());
        } else {
            return Component.translatable(this.createId(), Maths.format(100.0F * this.getMagnitude()));
        }
    }

    @Override
    public float preMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return context.damage();
        return this.damageFunction.calculate(this.getMagnitude(), this.getLevel(), this.mobType, context, source, attacker, target);
    }
}
