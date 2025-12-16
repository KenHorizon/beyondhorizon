package com.kenhorizon.beyondhorizon.server.api.classes;

import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class MasterySkills {
    public static final RegistryObject<MasterySkill> NONE = registerSkill("none", () -> new MasterySkill(MasterySkill.Slot.ONE, 0, 0));

    public static final RegistryObject<MasterySkill> FURY = registerSkill("fury", () -> new MasterySkill(MasterySkillCategories.PRECISION, MasterySkill.Slot.ONE, 0, 5));
    public static final RegistryObject<MasterySkill> SORCERY = registerSkill("sorcery", () -> new MasterySkill(MasterySkillCategories.PRECISION,MasterySkill.Slot.ONE, 0, 5));
    public static final RegistryObject<MasterySkill> FEAST = registerSkill("feast", () -> new MasterySkill(MasterySkillCategories.PRECISION,MasterySkill.Slot.ONE, 0, 5));

    public static final RegistryObject<MasterySkill> VAMPIRISM = registerSkill("vampirism", () -> new MasterySkill(MasterySkillCategories.PRECISION,MasterySkill.Slot.TWO, 0, 5));
    public static final RegistryObject<MasterySkill> NATURAL_TALENT = registerSkill("natural_talent", () -> new MasterySkill(MasterySkillCategories.PRECISION,MasterySkill.Slot.TWO, 0, 5));
    public static final RegistryObject<MasterySkill> GRANDFATHER_GIFT = registerSkill("grandfather_gift", () -> new MasterySkill(MasterySkillCategories.PRECISION,MasterySkill.Slot.TWO, 0, 5));

    public static final RegistryObject<MasterySkill> LAST_STAND = registerSkill("last_stand", () -> new MasterySkill(MasterySkillCategories.PRECISION,MasterySkill.Slot.THREE, 0, 5));
    public static final RegistryObject<MasterySkill> TITAN_SLAYER = registerSkill("titan_slayer", () -> new MasterySkill(MasterySkillCategories.PRECISION,MasterySkill.Slot.THREE, 0, 5));
    public static final RegistryObject<MasterySkill> MERCILESS = registerSkill("merciless", () -> new MasterySkill(MasterySkillCategories.PRECISION,MasterySkill.Slot.THREE, 0, 5));

    public static RegistryObject<MasterySkill> registerSkill(String name, Supplier<MasterySkill> properties) {
        return BHRegistries.DEFERRED_MASTERY.register(name, properties);
    }

    public static void register(IEventBus eventBus) {
        BHRegistries.DEFERRED_MASTERY.register(eventBus);
    }
}
