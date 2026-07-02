package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.datagen.BHLangProvider;
import com.kenhorizon.beyondhorizon.server.api.accessory.ability.*;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import com.kenhorizon.beyondhorizon.server.tags.BHEffectTags;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


/**
 * {@link BHLangProvider}
 * */
public class Accessories {
    public static final RegistryObject<Accessory> NONE = registerSkill("none", AccessoryPassiveSkill::new);

    public static final RegistryObject<Accessory> SHEEN = registerSkill("sheen", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_DAMAGE, Constant.SHEEN_AD, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> DORAN_BLADE_STATS = registerSkill("doran_blade_stats", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_DAMAGE, Constant.ATTACKDAMAGE_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(Attributes.MAX_HEALTH, Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> DORAN_BOW_STATS = registerSkill("doran_bow_stats", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_DAMAGE, Constant.ATTACKDAMAGE_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(Attributes.ATTACK_SPEED, 0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributes(BHAttributes.OMNIVAMP.get(), 1.5D, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> DORAN_HELM_STATS = registerSkill("doran_helm_stats", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.MAX_HEALTH, Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(Attributes.ARMOR, Constant.ARMOR_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.MAGIC_RESISTANCE.get(), Constant.MAGIC_RESISTANCE_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> DORAN_SHIELD_STATS = registerSkill("doran_shield_stats", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.MAX_HEALTH, Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.HEALTH_REGENERATION.get(), Constant.REGEN, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> DORAN_RING_STATS = registerSkill("doran_ring_stats", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.ABILITY_POWER.get(), Constant.ABILITY_POWER_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(Attributes.MAX_HEALTH, Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> CURSED_SKULL = registerSkill("cursed_skull", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.MAX_HEALTH, Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> TWILIGHT_SWORD = registerSkill("twilight_sword", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_DAMAGE,  Constant.TS_AD, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.ABILITY_POWER.get(), Constant.TS_AP, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> SPECTRAL_CLOAK = registerSkill("spectral_cloak", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.MAX_HEALTH, Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.MAGIC_RESISTANCE.get(), Constant.MAGIC_RESISTANCE_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.HEALTH_REGENERATION.get(),  Constant.REGEN, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> UNSTABLE_RUNIC_TOME = registerSkill("unstable_runic_tome", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.ABILITY_POWER.get(),  Constant.ABILITY_POWER_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.MAX_MANA.get(),  Constant.MAX_MANA_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(Attributes.MOVEMENT_SPEED,  Constant.BOOTS_TIER_1, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> CRYSTALLIZED_PLATE = registerSkill("crystallized_plate", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.MAX_HEALTH,  Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> VITALITY_STONE = registerSkill("vitality_stone", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.MAX_HEALTH,  Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> CINDER_STONE = registerSkill("cinder_stone", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.MAX_HEALTH,  Constant.MAX_HEALTH_1, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.COOLDOWN.get(),  Constant.COOLDOWN_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> INFERNO_HEART_STONE = registerSkill("inferno_heart_stone", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.MAX_HEALTH,  Constant.MAX_HEALTH_1, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.COOLDOWN.get(),  Constant.COOLDOWN_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> AGILE_DAGGER = registerSkill("agile_dagger", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.CRITICAL_CHANCE.get(),  Constant.CRITICAL_STRIKE_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(Attributes.MOVEMENT_SPEED,  Constant.BOOTS_TIER_1, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> CHAIN_VEST = registerSkill("chain_vest", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ARMOR, Constant.ARMOR_1, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> TOUGH_CLOTH = registerSkill("tough_cloth", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ARMOR, Constant.ARMOR_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> LEATHER_AGILITY = registerSkill("leather_agility", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.CRITICAL_CHANCE.get(),  Constant.CRITICAL_STRIKE_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> NULL_MAGIC_RUNE = registerSkill("null_magic_run", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.MAGIC_RESISTANCE.get(),  Constant.MAGIC_RESISTANCE_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> FIREFLY_FAYE = registerSkill("firefly_faye", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.COOLDOWN.get(),  Constant.COOLDOWN_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> SAPPHIRE_CRYSTAL = registerSkill("sapphire_crystal", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.MAX_MANA.get(),  Constant.MAX_MANA_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> RUMINATIVE_BEADS = registerSkill("ruminative_beads", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.HEALTH_REGENERATION.get(),  Constant.REGEN, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> BOOTS_1 = registerSkill("boots_0", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.MOVEMENT_SPEED,  Constant.BOOTS_TIER_1, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> BOOTS_2 = registerSkill("boots_1", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.MOVEMENT_SPEED,  Constant.BOOTS_TIER_2, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> BOOTS_3 = registerSkill("boots_2", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.MOVEMENT_SPEED,  Constant.BOOTS_TIER_3, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> BERSERKER_BOOTS = registerSkill("berserker_boots", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_SPEED,  Constant.BERSERKER_BOOTS, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> IRON_PLATED_BOOTS = registerSkill("iron_plated_boots", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.DAMAGE_TAKEN.get(),  -Constant.IRON_PLATED_BOOTS, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> MINING_BOOTS = registerSkill("mining_boots", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.MINING_SPEED.get(),  Constant.MINING_SPEED_BASIC, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> BRAVERY = registerSkill("bravery", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.DAMAGE_DEALT.get(),  Constant.BRAVERY_DAMAGE, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> ANCIENT_PICKAXE = registerSkill("ancient_pickaxe", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.MINING_EFFICIENCY.get(),  Constant.ANCIENT_PICK_MINING_EFFECIENCY, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.MINING_SPEED.get(),  Constant.ANCIENT_PICK_MINING_SPEED, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> ANCIENT_CHISEL = registerSkill("ancient_chisel", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.MINING_SPEED.get(),  Constant.ANCIENT_CHISEL_MINING_SPEED, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> RECTRIX = registerSkill("rectrix", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_SPEED,  Constant.RECTRIX_ATTACK_SPEED, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> FORTUNE_SHIKIGAMI = registerSkill("fortune_shikigami", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.LUCK,  Constant.FORTUNE_SHIKIGAMI, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> AETHER_WISP = registerSkill("aether_wisp", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.ABILITY_POWER.get(), Constant.AETHER_WISP_ABILITY_POWER, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> HARPOON_HEAD = registerSkill("harpoon_head", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.ARMOR_PENETRATION.get(),  Constant.STANDARD_ARMOR_PEN_0, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> POWER_GLOVES = registerSkill("power_gloves", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_DAMAGE,  Constant.POWER_GLOVES_ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION)
            .addAttributes(Attributes.ATTACK_KNOCKBACK, Constant.POWER_GLOVES_KNOCBACK, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> SWIFT_DAGGER = registerSkill("swift_dagger", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_SPEED, Constant.SWIFT_DAGGER_ATTACK_SPEED, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> MAGICAL_OPS = registerSkill("magical_ops", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.ABILITY_POWER.get(), Constant.MAGICAL_OPS, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> POWER_CLAW = registerSkill("power_claw", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.DAMAGE_DEALT.get(), Constant.POWER_CLAW, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> MINER_GLOVES = registerSkill("miner_gloves", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.MINING_SPEED.get(),  Constant.MINING_SPEED_BASIC, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> DWARF_MINER_RING = registerSkill("dwarf_miner_ring", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.MINING_SPEED.get(),  Constant.MINING_SPEED_BASIC, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> LETHAL_BURST = registerSkill("lethal_burst", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.CRITICAL_DAMAGE.get(),  Constant.LETHAL_BURST, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> VOID_STAFF = registerSkill("void_staff", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.PERCENTAGE_MAGIC_PENETRATION.get(),  Constant.STANDARD_MAGIC_PEN_PERCENT_2, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> SPEAR_OF_CHAOS = registerSkill("spear_of_chaos", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.ARMOR_PENETRATION.get(),  Constant.STANDARD_ARMOR_PEN_2, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> ABYSSAL_TOOTH = registerSkill("abyssal_tooth", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.ABILITY_POWER.get(), Constant.ABILITY_POWER_1, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> TRUE_HERO_SWORD = registerSkill("true_lethal_sword", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.DAMAGE_DEALT.get(),  Constant.TRUE_HERO_SWORD_DMG, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributes(Attributes.ATTACK_SPEED,  Constant.TRUE_HERO_SWORD_ATK_SPD, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributes(Attributes.ATTACK_KNOCKBACK,  Constant.TRUE_HERO_SWORD_ATK_KNOCKBACK, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> ASCENSION = registerSkill("ascension", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_DAMAGE,  Constant.ASCENSION_ATTRITUBE_ADD, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributes(BHAttributes.ABILITY_POWER.get(),  Constant.ASCENSION_ATTRITUBE_ADD, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributes(Attributes.MAX_HEALTH, Constant.ASCENSION_ATTRITUBE_ADD, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributes(Attributes.ARMOR, Constant.ASCENSION_ATTRITUBE_ADD, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributes(BHAttributes.MAGIC_RESISTANCE.get(), Constant.ASCENSION_ATTRITUBE_ADD, AttributeModifier.Operation.MULTIPLY_TOTAL));


    public static final RegistryObject<Accessory> DUSKBLADE = registerSkill("duskblade_stats", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.DAMAGE_TAKEN.get(), Constant.NIGHTSTALKER_DAMGE_TAKEN, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> ULTIMA = registerSkill("ultima_stats", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.DAMAGE_DEALT.get(), Constant.UlTIMA_DAMAGE_AMP, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributes(BHAttributes.DAMAGE_TAKEN.get(), Constant.UlTIMA_DAMAGE_TAKEN, AttributeModifier.Operation.MULTIPLY_TOTAL));


    public static final RegistryObject<Accessory> KNOWLEDGE_1 = registerSkill("knowledge", () -> new ExperienceAccessory(Constant.KNOWLEDGE_XP_MODIFIER));

    public static final RegistryObject<Accessory> WEIGHTS = registerSkill("weights", () -> new ImmunityEffectAccessory(BHEffectTags.WEIGHT_IMMUNE_TO).removeOnTick());
    public static final RegistryObject<Accessory> VITAMINS = registerSkill("vitamins", () -> new ImmunityEffectAccessory(BHEffectTags.VITAMINS_IMMUNE_TO).removeOnTick());
    public static final RegistryObject<Accessory> INVULNERABLE = registerSkill("invulnerable", () -> new ImmunityEffectAccessory(BHEffectTags.INVULNERABLE_IMMUNE_TO).removeOnTick());
    public static final RegistryObject<Accessory> HEMORRHAGE_CONTROL = registerSkill("hemorrhage_control", () -> new ImmunityEffectAccessory(BHEffectTags.HEMORRHAGE_CONTROL_IMMUNE_TO).removeOnTick());
    public static final RegistryObject<Accessory> BAD_APPLE = registerSkill("bad_apple", () -> new ImmunityEffectAccessory(BHEffectTags.BODY_POISON_IMMUNE_TO).removeOnTick());
    public static final RegistryObject<Accessory> SEEK_ONE_SEEK_TWICE = registerSkill("seek_one_seek_twice", () -> new ImmunityEffectAccessory(BHEffectTags.SEEK_ONE_SEEK_TWICE_IMMUNE_TO).removeOnTick());
    public static final RegistryObject<Accessory> UNBOTHERED = registerSkill("unbothered", () -> new ImmunityEffectAccessory(BHEffectTags.UNBOTHERED_IMMUNE_TO).removeOnTick());
    public static final RegistryObject<Accessory> PRESERVED = registerSkill("preserved", () -> new ImmunityEffectAccessory(BHEffectTags.PRESERVED_IMMUNE_TO).removeOnTick());

    public static final RegistryObject<Accessory> VENOM = registerSkill("venom", () -> new ApplyEffectAccessory((int) Constant.VENOM_DURATION, (int) Constant.VENOM_POISON_LEVEL, MobEffects.POISON, BHEffects.LETHAL_POISON.get())
            .chances(Constant.VENOM_INFLICT_CHANCE));

    public static final RegistryObject<Accessory> ETERNAL_LIFE = registerSkill("eternal_life", ImmuneDeathAccessory::new);
    public static final RegistryObject<Accessory> NULLIFY = registerSkill("nullify", () -> new SinglePassiveAccessory(Constant.JUMP_BOOST));
    public static final RegistryObject<Accessory> JUMP_BOOST = registerSkill("jump_boost", () -> new SinglePassiveAccessory(Constant.JUMP_BOOST));
    public static final RegistryObject<Accessory> FIRE_IMMUNITY = registerSkill("fire_immunity", SinglePassiveAccessory::new);
    public static final RegistryObject<Accessory> BURN_EFFECT = registerSkill("burn_effect", () -> new SinglePassiveAccessory(Constant.FIRE_EFFECT));
    public static final RegistryObject<Accessory> DESPAIR_AND_DEFY = registerSkill("despair_and_defy", () -> new BleedingEffectAccessory(Constant.DESPAIR_AND_DEFY));
    public static final RegistryObject<Accessory> OVERGROWTH = registerSkill("overgrowth", () -> new GainBonusHealthAccessory((float) Constant.OVERGROWTH_BONUS_HEALTH, GainBonusHealthAccessory.Type.TOTAL).disableAttributeTooltip());
    public static final RegistryObject<Accessory> FEATHER_FEET = registerSkill("feather_feet", SinglePassiveAccessory::new);
    public static final RegistryObject<Accessory> RAGE = registerSkill("rage", () -> new ExtraDamageAccessory(Constant.RAGE, ExtraDamageAccessory.USER_MISSING_HEALTH));
    public static final RegistryObject<Accessory> THORNS = registerSkill("thorns", () -> new ThornsAccessory(Constant.THORN_BASE_DAMAGE, Constant.THORN_DAMAGE_MODIFIER));
    public static final RegistryObject<Accessory> DEATH = registerSkill("death", () -> new ExecuteAbilityAccessory(Constant.DEATH_HEALTH_THRESOHOLD));
    public static final RegistryObject<Accessory> TAXS = registerSkill("taxs", TaxsAbilityAccessory::new);

    public static final RegistryObject<Accessory> FLUOROCARBON = registerSkill("fluorocarbon", () -> new StringBowAccessory(StringBowAccessory.StringBowType.LIGHT));
    public static final RegistryObject<Accessory> POLYETHYLENE = registerSkill("polyethylene", () -> new StringBowAccessory(StringBowAccessory.StringBowType.HEAVY));
    public static final RegistryObject<Accessory> LIFE_SIPHON = registerSkill("life_siphon", () -> new SinglePassiveAccessory(Constant.SOUL_SIPHON_CURRENT_HEALTH_DAMAGE));
    public static final RegistryObject<Accessory> CORRUPTED_BITE = registerSkill("corrupted_bite", () -> new SinglePassiveAccessory(Constant.CORRUPTED_BITE_DAMAGE_SCALE));

    public static final RegistryObject<Accessory> SPELL_BLADE_0 = registerSkill("spell_blade_0", () -> new SpellBladeAccessory(Constant.SPELLBLADE_INTERVAL, Constant.SPELLBLADE_BASE));
    public static final RegistryObject<Accessory> SPELL_BLADE_1 = registerSkill("spell_blade_1", () -> new TwilightSpellBladeAccessory(Constant.SPELLBLADE_INTERVAL, Constant.TWILIGHT_SPELLBLADE));
    public static final RegistryObject<Accessory> SPELL_BLADE_2 = registerSkill("spell_blade_2", () -> new ForceImpactSpellBladeAccessory(Constant.SPELLBLADE_INTERVAL, Constant.FORCE_IMPACT_SPELLBLADE));
    public static final RegistryObject<Accessory> CLEANSE = registerSkill("cleanse", SinglePassiveAccessory::new);
    public static final RegistryObject<Accessory> ROCK_SOLID = registerSkill("rock_solid", () -> new DamageReductionAccessory(Constant.ROCK_SOLID_REDUCE, DamageReductionAccessory.DamageReductionType.BASIC_ATTACK));
    public static final RegistryObject<Accessory> INFLAME = registerSkill("inflame", () -> new ApplyEffectAccessory(3, 0, BHEffects.INFLAME.get()).showIcon(true).ambient(true));
    public static final RegistryObject<Accessory> STING = registerSkill("sting", () -> new SinglePassiveAccessory(Constant.STING_DAMAGE));
    public static final RegistryObject<Accessory> TORMENT = registerSkill("torment", () -> new ApplyEffectAccessory(3, 0, BHEffects.TORMENT.get()).showIcon(true).ambient(true));
    public static final RegistryObject<Accessory> BRING_IT_DOWN = registerSkill("bring_it_down", () -> new SinglePassiveAccessory(Constant.BRING_IT_DOWN_BASE_DAMAGE));
    public static final RegistryObject<Accessory> GHOUL = registerSkill("ghoul", () -> new SinglePassiveAccessory(Constant.BRING_IT_DOWN_BASE_DAMAGE));
    public static final RegistryObject<Accessory> PENALTY_0 = registerSkill("penalty", () -> new DamageEffectivenessAccessory(Constant.EXCORIATE_DAMAGE_PENALTY));
    public static final RegistryObject<Accessory> EXCORIATE = registerSkill("excoriate", () -> new SinglePassiveAccessory(Constant.EXCORIATE_DAMAGE));
    public static final RegistryObject<Accessory> NIGHTSTALKER = registerSkill("nightstalker", () -> new ExtraDamageAccessory(Constant.EXCORIATE_DAMAGE, Constant.EXCORIATE_PER_MISSING_HP, ExtraDamageAccessory.TARGET_MISSING_HEALTH));
    public static final RegistryObject<Accessory> TWO_WORLD = registerSkill("two_world", () -> new SinglePassiveAccessory(Constant.BRING_IT_DOWN_BASE_DAMAGE)
            .innate(Accessories.DARK_SUN).innate(Accessories.FADED_MOON));
    public static final RegistryObject<Accessory> DARK_SUN = registerSkill("dark_sun", () -> new SinglePassiveAccessory(Constant.DARK_SUN_CONVERT));
    public static final RegistryObject<Accessory> FADED_MOON = registerSkill("faded_moon", () -> new SinglePassiveAccessory(Constant.FADED_MOON_CONVERT));

    public static final RegistryObject<Accessory> IMMOLATE_0 = registerSkill("immolate_0", () -> new BaseImmolateAccessory(Constant.BASE_IMMOLATE_DAMAGE));
    public static final RegistryObject<Accessory> IMMOLATE_1 = registerSkill("immolate_1", () -> new BaseImmolateAccessory.ImmolateInfernoHeart(Constant.INFENRO_HEART_STONE_IMMOLATE_DAMAGE));
    public static final RegistryObject<Accessory> IMMOLATE_2 = registerSkill("immolate_2", () -> new BaseImmolateAccessory.ImmolateVoid(Constant.VOID_IMMOLATE_DAMAGE));
    public static final RegistryObject<Accessory> SUPREMACY = registerSkill("supremacy", () -> new SupremacyAccessory(Constant.SUPREMACY_STACKS, Constant.SUPREMACY_DAMAGE, Constant.SUPREMACY_ONDEATH));
    public static final RegistryObject<Accessory> SWIFTNESS = registerSkill("swiftness", SwiftnessAccessory::new);
    public static final RegistryObject<Accessory> STALKER = registerSkill("stalker", StalkerAccessory::new);
    public static final RegistryObject<Accessory> TITANIC_CRESCENT = registerSkill("titanic_crescent", TitanicCrescentAccessory::new);
    public static final RegistryObject<Accessory> ELECTROSHOCK = registerSkill("electroshock", SinglePassiveAccessory::new);
    public static final RegistryObject<Accessory> ENERGIZED = registerSkill("energized", EnergizedAccessory::new);
    public static final RegistryObject<Accessory> KNOWLEDGE_2 = registerSkill("ultima_knowledge", () -> new ExperienceAccessory(Constant.ULTIMA_KNOWLEDGE_XP_MODIFIER));

    public static RegistryObject<Accessory> registerSkill(String name, Supplier<Accessory> properties) {
        return BHRegistries.DEFERRED_ACCESSORY.register(name, properties);
    }

    public static void register(IEventBus eventBus) {
        BHRegistries.DEFERRED_ACCESSORY.register(eventBus);
    }
}
