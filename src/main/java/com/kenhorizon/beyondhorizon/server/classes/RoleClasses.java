package com.kenhorizon.beyondhorizon.server.classes;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.accessory.Accessory;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class RoleClasses {
    public static final ResourceKey<Registry<RoleClass>> REGISTRY_KEY = ResourceKey.createRegistryKey(BeyondHorizon.resource("role_class"));
    public static final DeferredRegister<RoleClass> REGISTRY = DeferredRegister.create(REGISTRY_KEY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<RoleClass>> SUPPLIER_KEY = REGISTRY.makeRegistry(() -> new RegistryBuilder<RoleClass>().disableSaving().disableOverrides());

}
