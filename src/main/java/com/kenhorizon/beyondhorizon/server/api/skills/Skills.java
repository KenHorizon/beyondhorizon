package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveAbility;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Skills {
    public static final RegistryObject<Skill> NONE = registerSkill("none", EmptySkills::new);

    public static final RegistryObject<Skill> RUINED_BLADE = registerSkill("ruined_blade", () -> new ExtraDamageSkill(Constant.RUINED_BLADE, ExtraDamageSkill.CURRENT_HEALTH)
            .melee()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> BLADE_EDGE = registerSkill("blade_edge", () -> new ExtraDamageSkill(Constant.BLADE_EDGE, ExtraDamageSkill.MAX_HEALTH)
            .melee()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> RADIANT = registerSkill("radiant", () -> new ExtraDamageSkill(Constant.RADIANT, MobType.UNDEAD, ExtraDamageSkill.BONUS_DAMAGE)
            .melee()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> TRANNY = registerSkill("tranny", () -> new HealthToDamageSkill(Constant.TRANNY_HEALTH_SCALE)
            .universal()
            .innate(Skills.RETRIBUTION)
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> RETRIBUTION = registerSkill("retribution", () -> new ExtraDamageSkill(Constant.TRANNY_MISSING_HEALTH_SCALE, ExtraDamageSkill.USER_MISSING_HEALTH)
            .universal()
            .isInnate()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> KINETIC_STRIKE = registerSkill("kinetic_strike", () -> new ExtraDamageSkill(Constant.KINETIC_STRIKE_DAMAGE_MODIFIER, ExtraDamageSkill.KINETIC_WEAPON)
            .universal()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> DEATH = registerSkill("death", () -> new ExecuteDamageSkill(Constant.DEATH_HEALTH_THRESOHOLD)
            .universal()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> LETHALITY = registerSkill("lethality", () -> new ExtraDamageSkill(Constant.LETHALITY, ExtraDamageSkill.BONUS_DAMAGE)
            .universal()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> BURN_EFFECT = registerSkill("fire_effect", () -> new InflictFireAttackOnHitSkill(Constant.FIRE_EFFECT)
            .universal()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> DARK_BLADE = registerSkill("dark_blade", () -> new WeaponSkills()
            .addAttributes(BHAttributes.ARMOR_PENETRATION.get(), "f8d72f64-418b-4b18-977a-e136661e1439", Constant.DARK_BLADE_PEN, AttributeModifier.Operation.ADDITION)
            .universal()
            .disableTooltipName()
            .innate(Skills.PERFECTION)
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> PERFECTION = registerSkill("perfection", () -> new ExtraDamageSkill(Constant.PERFECTION, ExtraDamageSkill.PERFECTION)
            .universal()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> PIERCING_EDEGE = registerSkill("piercing_edge", () -> new ExtraDamageSkill(Constant.PIERCING_EDGE_DAMAGE, Constant.PIERCING_EDGE_SCALE_DAMAGE, ExtraDamageSkill.ARMORED_DAMAGE)
            .universal()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static final RegistryObject<Skill> BLAZING_CLEAVE = registerSkill("blazing_cleave", () -> new CleaveEffectSkill(Constant.BLAZING_CLEAVE_DAMAGE, Constant.BLAZING_CLEAVE_RANGE, CleaveAbility.Type.CIRCLE)
            .universal()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));

    public static RegistryObject<Skill> registerSkill(String name, Supplier<Skill> properties) {
        return BHRegistries.DEFERRED_SKILL.register(name, properties);
    }

    public static void register(IEventBus eventBus) {
        BHRegistries.DEFERRED_SKILL.register(eventBus);
    }
}
