package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


/**
 * {@link com.kenhorizon.beyondhorizon.server.datagen.BHLangProvider}
 * */
public class Accessories {
    public static final RegistryObject<Accessory> NONE = registerSkill("none", AccessorySkill::new);

    public static final RegistryObject<Accessory> SPECTRAL_CLOAK = registerSkill("spectral_cloak", () -> new BootsAccessory()
            .addAttributes(Attributes.MAX_HEALTH, "56c926ec-6dae-4cc1-887a-bca94ad097f2", Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.MAGIC_PENETRATION.get(), "29ebac4c-4845-44cf-aa07-2b9ef950c932", Constant.MAGIC_RESISTANCE_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.HEALTH_REGENERATION.get(), "81a1ce5c-e820-47f3-9165-3b467d712e56", Constant.REGEN, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> UNSTABLE_RUNIC_TOME = registerSkill("unstable_runic_tome", () -> new BootsAccessory()
            .addAttributes(BHAttributes.ABILITY_POWER.get(), "dc4893ce-e329-4cb7-afbb-639413a5a225", Constant.ABILITY_POWER_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.MAX_MANA.get(), "173973bc-2ef2-41ef-8a36-dc69c04a0090", Constant.MAX_MANA_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(Attributes.MOVEMENT_SPEED, "f9ed79b5-cdc4-4b54-b023-4b9e7fe0ec5f", Constant.BOOTS_TIER_1, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> CRYSTALLIZED_PLATE = registerSkill("crystallized_plate", () -> new BootsAccessory()
            .addAttributes(Attributes.MAX_HEALTH, "065099dd-42ed-4ba3-874e-c8923a2a0502", Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> CINDER_STONE = registerSkill("cinder_stone", () -> new BootsAccessory()
            .addAttributes(Attributes.MAX_HEALTH, "a65f31b4-c3f2-4f5a-bf60-9bae56e32c70", Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> AGILE_DAGGER = registerSkill("agile_dagger", () -> new BootsAccessory()
            .addAttributes(BHAttributes.CRITICAL_STRIKE.get(), "c65092cd-69fd-44b0-97d7-ffabe9c2db53", Constant.CRITICAL_STRIKE_0, AttributeModifier.Operation.ADDITION)
            .addAttributes(Attributes.MOVEMENT_SPEED, "dafafcc2-2d26-49b1-af3a-386d69c03878", Constant.BOOTS_TIER_1, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> CHAIN_VEST = registerSkill("chain_vest", () -> new BootsAccessory()
            .addAttributes(Attributes.ARMOR, "7aa1a339-925d-4c55-bad7-d4b6efc3571d", Constant.ARMOR_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> LEATHER_AGILITY = registerSkill("leather_agility", () -> new BootsAccessory()
            .addAttributes(BHAttributes.CRITICAL_STRIKE.get(), "3493efb1-52e6-48d7-a64f-4bbb77fe364d", Constant.CRITICAL_STRIKE_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> NULL_MAGIC_RUNE = registerSkill("null_magic_run", () -> new BootsAccessory()
            .addAttributes(BHAttributes.MAGIC_RESISTANCE.get(), "45683092-0b26-4572-b7bd-09393a010c2a", Constant.MAGIC_RESISTANCE_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> FIREFLY_FAYE = registerSkill("firefly_faye", () -> new BootsAccessory()
            .addAttributes(BHAttributes.COOLDOWN.get(), "5743e43b-eea8-4554-aa6e-310e51c2f7da", Constant.COOLDOWN_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> SAPPHIRE_CRYSTAL = registerSkill("sapphire_crystal", () -> new BootsAccessory()
            .addAttributes(BHAttributes.MAX_MANA.get(), "423554e1-36cf-4d1a-b983-878a7e1c197c", Constant.MAX_MANA_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> RUMINATIVE_BEADS = registerSkill("ruminative_beads", () -> new BootsAccessory()
            .addAttributes(BHAttributes.HEALTH_REGENERATION.get(), "073bd83a-4b0f-4668-a73d-39675598eb4a", Constant.REGEN, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> TOUGH_CLOTH = registerSkill("tough_cloth", () -> new BootsAccessory()
            .addAttributes(BHAttributes.HEALTH_REGENERATION.get(), "eec2663f-3b32-457d-b54b-dda7897db1a6", Constant.ARMOR_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> VITALITY_STONE = registerSkill("vitality_stone", () -> new BootsAccessory()
            .addAttributes(Attributes.MAX_HEALTH, "93068588-1f7a-4e61-bd3d-4758fbe80e01", Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> BOOTS = registerSkill("boots", () -> new BootsAccessory()
            .addAttributes(Attributes.MOVEMENT_SPEED, "221521ae-847b-4607-bef8-95de7e2f227b", Constant.BOOTS_TIER_1, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> BERSERKER_BOOTS = registerSkill("berserker_boots", () -> new BootsAccessory()
            .addAttributes(Attributes.MOVEMENT_SPEED, "221521ae-847b-4607-bef8-95de7e2f227b", Constant.BOOTS_TIER_2, AttributeModifier.Operation.ADDITION)
            .addAttributes(Attributes.ATTACK_SPEED, "f9f8e054-4d00-4361-bae5-a92224ab6481", Constant.BERSERKER_BOOTS, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> IRON_PLATED_BOOTS = registerSkill("iron_plated_boots", () -> new BootsAccessory()
            .addAttributes(Attributes.MOVEMENT_SPEED, "fd01b56f-0adc-4112-8ed9-f09fa24010fc", Constant.BOOTS_TIER_2, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.DAMAGE_TAKEN.get(), "fa44c7ee-0a5e-43a7-baad-92bc23c2e5b8", -Constant.IRON_PLATED_BOOTS, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> MINING_BOOTS = registerSkill("mining_boots", () -> new BootsAccessory()
            .addAttributes(Attributes.MOVEMENT_SPEED, "3ff3046e-dcaf-47a4-b3f7-f57eb9fa141c", Constant.BOOTS_TIER_2, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.MINING_SPEED.get(), "16f527b1-3dc4-4db0-9c22-8e8e0aea6b07", Constant.MINING_SPEED_BASIC, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> BRAVERY = registerSkill("bravery", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.DAMAGE_DEALT.get(), "ec9a9f57-2a3d-4165-93d2-ce335791aaf6", Constant.BRAVERY_DAMAGE, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> ANCIENT_PICKAXE = registerSkill("ancient_pickaxe", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.MINING_EFFICIENCY.get(), "4e9adf0a-14ec-4115-82f8-9ef0c9d0cb90", Constant.ANCIENT_PICK_MINING_EFFECIENCY, AttributeModifier.Operation.ADDITION)
            .addAttributes(BHAttributes.MINING_SPEED.get(), "a132412a-f5ae-4eed-a4e9-d143b5229aba", Constant.ANCIENT_PICK_MINING_SPEED, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> ANCIENT_CHISEL = registerSkill("ancient_chisel", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.MINING_SPEED.get(), "9921072b-3bab-418c-961a-6d34074a457d", Constant.ANCIENT_CHISEL_MINING_SPEED, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> RECTRIX = registerSkill("rectrix", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_SPEED, "02c0a38d-4292-464d-b1e5-353e7a739793", Constant.RECTRIX_ATTACK_SPEED, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> FORTUNE_SHIKIGAMI = registerSkill("fortune_shikigami", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.LUCK, "be27e0c1-b96f-467a-8026-83c372ec590d", Constant.FORTUNE_SHIKIGAMI, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> AETHER_WISP = registerSkill("aether_wisp", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.ABILITY_POWER.get(), "8da8b801-b5fe-4b2a-9cc9-9d5746181d61", Constant.AETHER_WISP_ABILITY_POWER, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> POWER_GLOVES = registerSkill("power_gloves", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_DAMAGE, "847a9dc2-56ee-4ebc-a892-0e5095012667", Constant.POWER_GLOVES_ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION)
            .addAttributes(Attributes.ATTACK_KNOCKBACK, "c71ca564-8178-4ca2-bc63-29a4f90361e3", Constant.POWER_GLOVES_KNOCBACK, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> SWIFT_DAGGER = registerSkill("swift_dagger", () -> new AttributeOnlyAccessory()
            .addAttributes(Attributes.ATTACK_SPEED, "847a9dc2-56ee-4ebc-a892-0e5095012667", Constant.SWIFT_DAGGER_ATTACK_SPEED, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> MAGICAL_OPS = registerSkill("magical_ops", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.ABILITY_POWER.get(), "c54e0eef-b659-42fa-b9bb-41f1eede8019", Constant.MAGICAL_OPS, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> POWER_CLAW = registerSkill("power_claw", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.DAMAGE_DEALT.get(), "dc5d5e4a-378e-49cc-90ee-daae66bb2609", Constant.POWER_CLAW, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> MINER_GLOVES = registerSkill("miner_gloves", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.MINING_SPEED.get(), "031ded39-1d76-4e88-a277-f2adf5c289e4", Constant.MINING_SPEED_BASIC, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> DWARF_MINER_RING = registerSkill("dwarf_miner_ring", () -> new AttributeOnlyAccessory()
            .addAttributes(BHAttributes.MINING_SPEED.get(), "a58ef541-8de1-4ec3-b647-17fcf9466ff3", Constant.MINING_SPEED_BASIC, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Accessory> KNOWLEDGE = registerSkill("knowledge", () -> new ExperienceAccessory(Constant.KNOWLEDGE_XP_MODIFIER));
    public static final RegistryObject<Accessory> VENOM = registerSkill("venom", () -> new ApplyEffectAccessory((int) Constant.VENOM_DURATION, (int) Constant.VENOM_POISON_LEVEL, MobEffects.POISON, BHEffects.LETHAL_POISON.get())
            .chances(Constant.VENOM_INFLICT_CHANCE));

    public static final RegistryObject<Accessory> JUMP_BOOST = registerSkill("jump_boost", () -> new SinglePassiveAccessory(Constant.JUMP_BOOST));
    public static final RegistryObject<Accessory> FIRE_IMMUNITY = registerSkill("fire_immunity", SinglePassiveAccessory::new);
    public static final RegistryObject<Accessory> BURN_EFFECT = registerSkill("burn_effect", SinglePassiveAccessory::new);
    public static final RegistryObject<Accessory> DESPAIR_AND_DEFY = registerSkill("despair_and_defy", () -> new BleedingEffectAccessory(Constant.DESPAIR_AND_DEFY));
    public static final RegistryObject<Accessory> OVERGROWTH = registerSkill("overgrowth", () -> new GainBonusHealthAccessory((float) Constant.OVERGROWTH_BONUS_HEALTH, GainBonusHealthAccessory.Type.TOTAL).disableAttributeTooltip());
    public static final RegistryObject<Accessory> FEATHER_FEET = registerSkill("feather_feet", SinglePassiveAccessory::new);
    public static final RegistryObject<Accessory> RAGE = registerSkill("rage", () -> new ExtraDamageAccessory(Constant.RAGE, ExtraDamageAccessory.USER_MISSING_HEALTH));
    public static final RegistryObject<Accessory> THORNS = registerSkill("thorns", () -> new ThornsAccessory(Constant.THORN_BASE_DAMAGE, Constant.THORN_DAMAGE_MODIFIER));

    public static RegistryObject<Accessory> registerSkill(String name, Supplier<Accessory> properties) {
        return BHRegistries.DEFERRED_ACCESSORY.register(name, properties);
    }

    public static void register(IEventBus eventBus) {
        BHRegistries.DEFERRED_ACCESSORY.register(eventBus);
    }
}
