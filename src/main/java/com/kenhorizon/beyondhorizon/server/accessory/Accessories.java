package com.kenhorizon.beyondhorizon.server.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


/**
 * {@link com.kenhorizon.beyondhorizon.server.datagen.BHLangProvider}
 * */
public class Accessories {
    public static final ResourceKey<Registry<Accessory>> REGISTRY_KEY = ResourceKey.createRegistryKey(BeyondHorizon.resource("accessorry_items"));
    public static final DeferredRegister<Accessory> REGISTRY = DeferredRegister.create(REGISTRY_KEY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<Accessory>> SUPPLIER_KEY = REGISTRY.makeRegistry(() -> new RegistryBuilder<Accessory>().disableSaving().disableOverrides());

    public static final RegistryObject<Accessory> NONE = registerSkill("none", AccessorySkill::new);

    public static final RegistryObject<Accessory> BOOTS_0 = registerSkill("boots_0", () -> new BootsAccessory()
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "c0003115-4354-4e84-b4ab-a364f530bf3b", Constant.BOOTS_TIER_1, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> BOOTS_1 = registerSkill("boots_1", () -> new BootsAccessory()
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "0d9b4d17-2d64-4891-a0b2-8a5e998110ce", Constant.BOOTS_TIER_2, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> BOOTS_2 = registerSkill("boots_2", () -> new BootsAccessory()
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "d0e2e6f1-58ed-4312-8b89-c97f2b124fd7", Constant.BOOTS_TIER_3, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> BERSERKER_BOOTS = registerSkill("berserker_boots", () -> new BootsAccessory()
            .addAttributeModifier(Attributes.ATTACK_SPEED, "f9f8e054-4d00-4361-bae5-a92224ab6481", Constant.BERSERKER_BOOTS, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Accessory> IRON_PLATED_BOOTS = registerSkill("iron_plated_boots", () -> new BootsAccessory()
            .addAttributeModifier(BHAttributes.DAMAGE_TAKEN.get(), "fa44c7ee-0a5e-43a7-baad-92bc23c2e5b8", -Constant.IRON_PLATED_BOOTS, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> MINING_BOOTS = registerSkill("mining_boots", () -> new BootsAccessory()
            .addAttributeModifier(BHAttributes.MINING_SPEED.get(), "16f527b1-3dc4-4db0-9c22-8e8e0aea6b07", Constant.MINING_BOOTS, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Accessory> BRAVERY = registerSkill("bravery", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(BHAttributes.DAMAGE_DEALT.get(), "ec9a9f57-2a3d-4165-93d2-ce335791aaf6", Constant.BRAVERY_DAMAGE, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Accessory> ANCIENT_PICKAXE = registerSkill("ancient_pickaxe", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(BHAttributes.MINING_EFFICIENCY.get(), "4e9adf0a-14ec-4115-82f8-9ef0c9d0cb90", Constant.ANCIENT_PICK_MINING_EFFECIENCY, AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(BHAttributes.MINING_SPEED.get(), "a132412a-f5ae-4eed-a4e9-d143b5229aba", Constant.ANCIENT_PICK_MINING_SPEED, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Accessory> ANCIENT_CHISEL = registerSkill("ancient_chisel", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(BHAttributes.MINING_SPEED.get(), "9921072b-3bab-418c-961a-6d34074a457d", Constant.ANCIENT_CHISEL_MINING_SPEED, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Accessory> RECTRIX = registerSkill("rectrix", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(Attributes.ATTACK_SPEED, "02c0a38d-4292-464d-b1e5-353e7a739793", Constant.RECTRIX_ATTACK_SPEED, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Accessory> FORTUNE_SHIKIGAMI = registerSkill("fortune_shikigami", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(Attributes.LUCK, "be27e0c1-b96f-467a-8026-83c372ec590d", Constant.FORTUNE_SHIKIGAMI, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> AETHER_WISP = registerSkill("aether_wisp", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(BHAttributes.ABILITY_POWER.get(), "8da8b801-b5fe-4b2a-9cc9-9d5746181d61", Constant.AETHER_WISP_ABILITY_POWER, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> POWER_GLOVES = registerSkill("power_gloves", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, "847a9dc2-56ee-4ebc-a892-0e5095012667", Constant.POWER_GLOVES_ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION)
            .addAttributeModifier(Attributes.ATTACK_KNOCKBACK, "c71ca564-8178-4ca2-bc63-29a4f90361e3", Constant.POWER_GLOVES_KNOCBACK, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Accessory> SWIFT_DAGGER = registerSkill("swift_dagger", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(Attributes.ATTACK_SPEED, "847a9dc2-56ee-4ebc-a892-0e5095012667", Constant.SWIFT_DAGGER_ATTACK_SPEED, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Accessory> BASIC_CRIT = registerSkill("basic_crit", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(BHAttributes.CRITICAL_STRIKE.get(), "9b64880a-586f-4ba2-9861-b30dbe823f56", Constant.CRITICAL_STRIKE_0, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> UPGRADED_CRIT = registerSkill("upgraded_crit", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(BHAttributes.CRITICAL_STRIKE.get(), "20989fcb-6d4a-4c95-b838-d06f8c5cdf7d", Constant.CRITICAL_STRIKE_1, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> BASIC_MOVEMENT_SPEED = registerSkill("basic_movement_speed", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(BHAttributes.CRITICAL_STRIKE.get(), "f5af9c27-d8cc-4ccf-8117-de088434a105", Constant.MOVEMENT_SPEED_0, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> UPGRADED_MOVEMENT_SPEED = registerSkill("upgraded_movement_speed", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(BHAttributes.CRITICAL_STRIKE.get(), "c36cdacd-3ce2-41f4-9d39-8327a810bba9", Constant.MOVEMENT_SPEED_1, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> MAX_HEALTH_0 = registerSkill("max_health_0", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(Attributes.MAX_HEALTH, "f8cd02af-5eb4-47f6-a689-305716a26d5e", Constant.MAX_HEALTH_0, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> MAX_HEALTH_1 = registerSkill("max_health_1", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(Attributes.MAX_HEALTH, "580a8f14-e8d2-42dc-990b-98016ebe9ecb", Constant.MAX_HEALTH_1, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> MAX_HEALTH_2 = registerSkill("max_health_2", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(Attributes.MAX_HEALTH, "b35f9bdd-6178-481f-a0a9-9aa3eafa195e", Constant.MAX_HEALTH_2, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> MAX_HEALTH_3 = registerSkill("max_health_3", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(Attributes.MAX_HEALTH, "4d4315e2-641e-4838-91c8-dbe64ff1a0d7", Constant.MAX_HEALTH_3, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> MAX_HEALTH_4 = registerSkill("max_health_4", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(Attributes.MAX_HEALTH, "801a6faf-f1e0-4340-bbc0-ff6b7b8b4cae", Constant.MAX_HEALTH_4, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> MAGICAL_OPS = registerSkill("magical_ops", () -> new AttributeOnlyAccessory()
            .addAttributeModifier(BHAttributes.ABILITY_POWER.get(), "c54e0eef-b659-42fa-b9bb-41f1eede8019", Constant.MAGICAL_OPS, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<Accessory> BURN_EFFECT = registerSkill("burn_effect", AccessorySkill::new);
    public static final RegistryObject<Accessory> DESPAIR_AND_DEFY = registerSkill("despair_and_defy", () -> new BleedingEffectAccessory(Constant.DESPAIR_AND_DEFY));

    public static final RegistryObject<Accessory> OVERGROWTH = registerSkill("overgrowth", () -> new GainBonusHealthAccessory((float) Constant.OVERGROWTH_BONUS_HEALTH, GainBonusHealthAccessory.Type.TOTAL).disableAttributeTooltip());
    public static final RegistryObject<Accessory> FEATHER_FEET = registerSkill("feather_feet", AccessorySkill::new);

    public static RegistryObject<Accessory> registerSkill(String name, Supplier<Accessory> properties) {
        return REGISTRY.register(name, properties);
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
