package com.kenhorizon.beyondhorizon.server.api.classes;

import com.kenhorizon.beyondhorizon.server.api.classes.classes.*;
import com.kenhorizon.beyondhorizon.server.classes.classes.*;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RoleClasses {

    public static final RegistryObject<RoleClass> NONE = registerSkill("none", RoleClass::new);
    public static final RegistryObject<RoleClass> ASSASSIN = registerSkill("assassin", AssassinRole::new);
    public static final RegistryObject<RoleClass> MARKSMAN = registerSkill("marksman", MarksmanRole::new);
    public static final RegistryObject<RoleClass> VANGUARD = registerSkill("vanguard", VanguardRole::new);
    public static final RegistryObject<RoleClass> STRIKER = registerSkill("striker", StrikerRole::new);
    public static final RegistryObject<RoleClass> SUPPORT = registerSkill("support", SupportRole::new);
    public static final RegistryObject<RoleClass> CASTER = registerSkill("caster", CasterRole::new);

    public static RegistryObject<RoleClass> registerSkill(String name, Supplier<RoleClass> properties) {
        return BHRegistries.DEFERRED_ROLE_CLASS.register(name, properties);
    }
    public static void register(IEventBus eventBus) {
        BHRegistries.DEFERRED_ROLE_CLASS.register(eventBus);
    }
}
