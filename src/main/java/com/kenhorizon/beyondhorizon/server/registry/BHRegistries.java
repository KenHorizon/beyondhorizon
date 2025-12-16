package com.kenhorizon.beyondhorizon.server.registry;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.classes.MasterySkillCategory;
import com.kenhorizon.beyondhorizon.server.api.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.api.classes.MasterySkill;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class BHRegistries {
    public static final DeferredRegister<Skill> DEFERRED_SKILL = DeferredRegister.create(Keys.SKILL, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<Skill>> SKILL_KEY = DEFERRED_SKILL.makeRegistry(() -> new RegistryBuilder<Skill>().disableSaving());

    public static final DeferredRegister<RoleClass> DEFERRED_ROLE_CLASS = DeferredRegister.create(Keys.ROLE_CLASS, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<RoleClass>> ROLE_CLASS_KEY = DEFERRED_ROLE_CLASS.makeRegistry(() -> new RegistryBuilder<RoleClass>().disableSaving());

    public static final DeferredRegister<Accessory> DEFERRED_ACCESSORY = DeferredRegister.create(Keys.ACCESSORY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<Accessory>> ACCESSORY_KEY = DEFERRED_ACCESSORY.makeRegistry(() -> new RegistryBuilder<Accessory>().disableSaving());

    public static final DeferredRegister<MasterySkill> DEFERRED_MASTERY = DeferredRegister.create(Keys.MASTERY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<MasterySkill>> MASTERY_KEY = DEFERRED_MASTERY.makeRegistry(() -> new RegistryBuilder<MasterySkill>().setDefaultKey(BeyondHorizon.resource("none")).disableSaving().disableOverrides());

    public static final DeferredRegister<MasterySkillCategory> DEFERRED_MASTERY_CATEGORY = DeferredRegister.create(Keys.MASTERY_CATEGORY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<MasterySkillCategory>> MASTERY_CATEGORY_KEY = DEFERRED_MASTERY_CATEGORY.makeRegistry(() -> new RegistryBuilder<MasterySkillCategory>().setDefaultKey(BeyondHorizon.resource("none")).disableSaving().disableOverrides());



    public static class Keys {
        public static final ResourceKey<Registry<Skill>> SKILL = ResourceKey.createRegistryKey(BeyondHorizon.resource("skills"));
        public static final ResourceKey<Registry<RoleClass>> ROLE_CLASS = ResourceKey.createRegistryKey(BeyondHorizon.resource("role_classes"));
        public static final ResourceKey<Registry<Accessory>> ACCESSORY = ResourceKey.createRegistryKey(BeyondHorizon.resource("accessorry_items"));
        public static final ResourceKey<Registry<MasterySkill>> MASTERY = ResourceKey.createRegistryKey(BeyondHorizon.resource("role_mastery"));
        public static final ResourceKey<Registry<MasterySkillCategory>> MASTERY_CATEGORY = ResourceKey.createRegistryKey(BeyondHorizon.resource("role_mastery_category"));

    }
}
