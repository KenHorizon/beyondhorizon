package com.kenhorizon.beyondhorizon.server.classes;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RoleClasses {
    public static final ResourceKey<Registry<RoleClass>> REGISTRY_KEY = ResourceKey.createRegistryKey(BeyondHorizon.resource("role_class"));
    public static final DeferredRegister<RoleClass> REGISTRY = DeferredRegister.create(REGISTRY_KEY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<RoleClass>> SUPPLIER_KEY = REGISTRY.makeRegistry(() -> new RegistryBuilder<RoleClass>().disableSaving().disableOverrides());

    public static RegistryObject<RoleClass> ASSASSIN = REGISTRY.register("assassin", () -> new RoleClass(RoleClassTypes.ASSSASSIN));
    public static RegistryObject<RoleClass> MARKSMAN = REGISTRY.register("marksman", () -> new RoleClass(RoleClassTypes.MARKSMAN));
    public static RegistryObject<RoleClass> CASTER = REGISTRY.register("caster", () -> new RoleClass(RoleClassTypes.CASTER));
    public static RegistryObject<RoleClass> STRIKER = REGISTRY.register("striker", () -> new RoleClass(RoleClassTypes.STRIKER));
    public static RegistryObject<RoleClass> VANGUARD = REGISTRY.register("vanguard", () -> new RoleClass(RoleClassTypes.VANGAURD));
    public static RegistryObject<RoleClass> DEFENDER = REGISTRY.register("defender", () -> new RoleClass(RoleClassTypes.DEFENDER));
    public static RegistryObject<RoleClass> SUPPORT = REGISTRY.register("support", () -> new RoleClass(RoleClassTypes.SUPPORT));
}
