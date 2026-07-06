package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.server.api.skills.ability.*;
import com.kenhorizon.beyondhorizon.server.api.skills.item_properties.GuardianSwordProperties;
import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Skills {
    public static final RegistryObject<Skill> NONE = registerSkill("none", EmptySkills::new);

    public static final RegistryObject<Skill> ENERGIZED = registerSkill("energized", () -> new EnergizedSkill()
            .universal());

    public static final RegistryObject<Skill> INFERNO_STRIKE = registerSkill("inferno_strike", () -> new InfernoStrikeSkill(Constant.INFERNO_STRIKE_SLOW, Constant.INFERNO_STRIKE_SCALE)
            .melee());

    public static final RegistryObject<Skill> INFERNAL_RAY = registerSkill("infernal_ray", () -> new InfernalRaySkill(Constant.INFERNAL_AD, Constant.INFERNAL_AP, Constant.INFERNAL_BASE_DAMAGE, true, DamageType.PHYSICAL_DAMAGE, AbstractDeathRayAbility.BeamDamageTags.DEFAULT)
            .universal());

    public static final RegistryObject<Skill> HEAVY_HITTER = registerSkill("heavy_hitter", () -> new HeavyHitterSkill(Constant.HEAVY_HITTER_DAMAGE_PER_SCALE, Constant.HEAVY_HITTER_DAMAGE)
            .melee());

    public static final RegistryObject<Skill> PERFECT_STRIKE = registerSkill("perfect_strike", () -> new AlwaysCrtiAttackSkill(Constant.ALWAYS_CRIT_MODIFIER)
            .melee());

    public static final RegistryObject<Skill> CELESTIAL_STRIKE = registerSkill("celestial_strike", () -> new CelestialStrikeSkill(Constant.STELLAR_AXE_SLASH_DAMAGE)
            .melee().addAttributes(BHAttributes.CRITICAL_DAMAGE.get(), Constant.STELLAR_AXE_CRIT_DAMAGE, AttributeModifier.Operation.ADDITION).innate(Skills.PERFECT_STRIKE));

    public static final RegistryObject<Skill> RUINED_BLADE = registerSkill("ruined_blade", () -> new ExtraDamageSkill(Constant.RUINED_BLADE, ExtraDamageSkill.CURRENT_HEALTH)
            .melee());

    public static final RegistryObject<Skill> BLADE_EDGE = registerSkill("blade_edge", () -> new ExtraDamageSkill(Constant.BLADE_EDGE, ExtraDamageSkill.MAX_HEALTH)
            .melee());

    public static final RegistryObject<Skill> RADIANT = registerSkill("radiant", () -> new ExtraDamageSkill(Constant.RADIANT, MobType.UNDEAD, ExtraDamageSkill.BONUS_DAMAGE)
            .melee());

    public static final RegistryObject<Skill> TRANNY = registerSkill("tranny", () -> new HealthToDamageSkill(Constant.TRANNY_HEALTH_SCALE)
            .universal()
            .innate(Skills.RETRIBUTION));

    public static final RegistryObject<Skill> RETRIBUTION = registerSkill("retribution", () -> new ExtraDamageSkill(Constant.TRANNY_MISSING_HEALTH_SCALE, ExtraDamageSkill.USER_MISSING_HEALTH)
            .universal()
            .isInnate());

    public static final RegistryObject<Skill> KINETIC_STRIKE = registerSkill("kinetic_strike", () -> new ExtraDamageSkill(Constant.KINETIC_STRIKE_DAMAGE_MODIFIER, ExtraDamageSkill.KINETIC_WEAPON)
            .universal());

    public static final RegistryObject<Skill> DEATH = registerSkill("death", () -> new ExecuteDamageSkill(Constant.DEATH_HEALTH_THRESOHOLD)
            .universal());

    public static final RegistryObject<Skill> LETHALITY = registerSkill("lethality", () -> new ExtraDamageSkill(Constant.LETHALITY, ExtraDamageSkill.BONUS_DAMAGE)
            .universal());

    public static final RegistryObject<Skill> BURN_EFFECT = registerSkill("fire_effect", () -> new InflictFireAttackOnHitSkill(Constant.FIRE_EFFECT)
            .universal());

    public static final RegistryObject<Skill> DARK_BLADE = registerSkill("dark_blade", () -> new WeaponPassiveSkills()
            .addAttributes(BHAttributes.ARMOR_PENETRATION.get(), Constant.DARK_BLADE_PEN, AttributeModifier.Operation.ADDITION)
            .universal()
            .disableTooltipName()
            .innate(Skills.PERFECTION));

    public static final RegistryObject<Skill> PERFECTION = registerSkill("perfection", () -> new ExtraDamageSkill(Constant.PERFECTION, ExtraDamageSkill.PERFECTION)
            .universal());

    public static final RegistryObject<Skill> PIERCING_EDEGE = registerSkill("piercing_edge", () -> new ExtraDamageSkill(Constant.PIERCING_EDGE_DAMAGE, Constant.PIERCING_EDGE_SCALE_DAMAGE, ExtraDamageSkill.ARMORED_DAMAGE)
            .universal());

    public static final RegistryObject<Skill> BLAZING_CLEAVE = registerSkill("blazing_cleave", () -> new BlazingCleaveSkill(Constant.BLAZING_CLEAVE_DAMAGE, Constant.BLAZING_CLEAVE_RANGE)
            .universal());

    public static final RegistryObject<Skill> FEAST = registerSkill("feast", () -> new BlazingCleaveSkill(Constant.BLAZING_CLEAVE_DAMAGE, Constant.BLAZING_CLEAVE_RANGE)
            .universal());

    public static final RegistryObject<Skill> GUARDIAN_SWORD_TRAIT = registerSkill("guardian_sword_trait", GuardianSwordProperties::new);

    public static RegistryObject<Skill> registerSkill(String name, Supplier<Skill> properties) {
        return BHRegistries.DEFERRED_SKILL.register(name, properties);
    }

    public static void register(IEventBus eventBus) {
        BHRegistries.DEFERRED_SKILL.register(eventBus);
    }
}
