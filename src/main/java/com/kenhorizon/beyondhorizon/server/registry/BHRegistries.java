package com.kenhorizon.beyondhorizon.server.registry;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.armor_ability.ArmorAbility;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.SpawnerConfig;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class BHRegistries {
    public static final DeferredRegister<Skill> DEF_SKILL = DeferredRegister.create(Keys.SKILL, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<Skill>> SKILL_KEY = DEF_SKILL.makeRegistry(() -> new RegistryBuilder<Skill>().disableSaving());

    public static final DeferredRegister<Accessory> DEF_ACCESSORY = DeferredRegister.create(Keys.ACCESSORY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<Accessory>> ACCESSORY_KEY = DEF_ACCESSORY.makeRegistry(() -> new RegistryBuilder<Accessory>().disableSaving());

    public static final DeferredRegister<ArmorAbility> DEF_ARMOR_ABILITY = DeferredRegister.create(Keys.ARMOR_ABILITY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<ArmorAbility>> ARMOR_ABILITY_KEY = DEF_ARMOR_ABILITY.makeRegistry(() -> new RegistryBuilder<ArmorAbility>().disableSaving());


    public static class Keys {
        public static final ResourceKey<Registry<SpawnerConfig>> SPAWNER_BUILDER = ResourceKey.createRegistryKey(ResourceLocation.parse("base_spawner"));
        public static final ResourceKey<Registry<Skill>> SKILL = ResourceKey.createRegistryKey(BeyondHorizon.resource("skills"));
        public static final ResourceKey<Registry<Accessory>> ACCESSORY = ResourceKey.createRegistryKey(BeyondHorizon.resource("accessorry_items"));
        public static final ResourceKey<Registry<ArmorAbility>> ARMOR_ABILITY = ResourceKey.createRegistryKey(BeyondHorizon.resource("armor_ability"));

    }
}
