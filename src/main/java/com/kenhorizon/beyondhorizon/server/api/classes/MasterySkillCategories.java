package com.kenhorizon.beyondhorizon.server.api.classes;

import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class MasterySkillCategories {

    public static final RegistryObject<MasterySkillCategory> NONE = registerSkill("none", MasterySkillCategory::new);
    public static final RegistryObject<MasterySkillCategory> PRECISION = registerSkill("precision", MasterySkillCategory::new);
    public static final RegistryObject<MasterySkillCategory> DOMINATION = registerSkill("domination", MasterySkillCategory::new);
    public static final RegistryObject<MasterySkillCategory> SORCECY = registerSkill("sorcecy", MasterySkillCategory::new);
    public static final RegistryObject<MasterySkillCategory> RESOLVE = registerSkill("resolve", MasterySkillCategory::new);

    public static RegistryObject<MasterySkillCategory> registerSkill(String name, Supplier<MasterySkillCategory> properties) {
        return BHRegistries.DEFERRED_MASTERY_CATEGORY.register(name, properties);
    }

    public static void register(IEventBus eventBus) {
        BHRegistries.DEFERRED_MASTERY_CATEGORY.register(eventBus);
    }
}
