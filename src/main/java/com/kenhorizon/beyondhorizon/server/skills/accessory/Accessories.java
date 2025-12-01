package com.kenhorizon.beyondhorizon.server.skills.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class Accessories {
    public static final ResourceKey<Registry<Accessory>> REGISTRY_KEY = ResourceKey.createRegistryKey(BeyondHorizon.resource("accessorry_items"));
    public static final DeferredRegister<Accessory> ACCESSORY_ITEMS = DeferredRegister.create(REGISTRY_KEY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<Accessory>> SUPPLIER_KEY = ACCESSORY_ITEMS.makeRegistry(() -> new RegistryBuilder<Accessory>().disableSaving().disableOverrides());

    public static void register(IEventBus eventBus) {
        ACCESSORY_ITEMS.register(eventBus);
    }
}
