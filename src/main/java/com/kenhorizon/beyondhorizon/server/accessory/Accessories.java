package com.kenhorizon.beyondhorizon.server.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.skills.Skill;
import com.kenhorizon.beyondhorizon.server.skills.skill.ExtraDamageSkill;
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

public class Accessories {
    public static final ResourceKey<Registry<Accessory>> REGISTRY_KEY = ResourceKey.createRegistryKey(BeyondHorizon.resource("accessorry_items"));
    public static final DeferredRegister<Accessory> REGISTRY = DeferredRegister.create(REGISTRY_KEY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<Accessory>> SUPPLIER_KEY = REGISTRY.makeRegistry(() -> new RegistryBuilder<Accessory>().disableSaving().disableOverrides());

    public static final RegistryObject<Accessory> NONE = registerSkill("none", Accessory::new);
    public static final RegistryObject<Accessory> BOOTS_0 = registerSkill("boots_0", () -> new Accessory()
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "c0003115-4354-4e84-b4ab-a364f530bf3b", 0.025D, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> BOOTS_1 = registerSkill("boots_1", () -> new Accessory()
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, "0d9b4d17-2d64-4891-a0b2-8a5e998110ce", 0.040D, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Accessory> BRAVERY = registerSkill("bravery", () -> new Accessory()
            .addAttributeModifier(BHAttributes.DAMAGE_DEALT.get(), "ec9a9f57-2a3d-4165-93d2-ce335791aaf6", 0.1D, AttributeModifier.Operation.MULTIPLY_BASE));

    public static RegistryObject<Accessory> registerSkill(String name, Supplier<Accessory> properties) {
        return REGISTRY.register(name, properties);
    }
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
