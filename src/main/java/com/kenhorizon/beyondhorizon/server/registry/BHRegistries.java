package com.kenhorizon.beyondhorizon.server.registry;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
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
    public static final DeferredRegister<Skill> DEFERRED_SKILL = DeferredRegister.create(Keys.SKILL, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<Skill>> SKILL_KEY = DEFERRED_SKILL.makeRegistry(() -> new RegistryBuilder<Skill>().disableSaving());

    public static final DeferredRegister<Accessory> DEFERRED_ACCESSORY = DeferredRegister.create(Keys.ACCESSORY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<Accessory>> ACCESSORY_KEY = DEFERRED_ACCESSORY.makeRegistry(() -> new RegistryBuilder<Accessory>().disableSaving());


    public static class Keys {
        public static final ResourceKey<Registry<SpawnerConfig>> SPAWNER_BUILDER = ResourceKey.createRegistryKey(ResourceLocation.parse("base_spawner"));
        public static final ResourceKey<Registry<Skill>> SKILL = ResourceKey.createRegistryKey(BeyondHorizon.resource("skills"));
        public static final ResourceKey<Registry<Accessory>> ACCESSORY = ResourceKey.createRegistryKey(BeyondHorizon.resource("accessorry_items"));

    }
}
