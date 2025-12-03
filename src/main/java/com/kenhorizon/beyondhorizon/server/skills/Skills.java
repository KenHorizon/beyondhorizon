package com.kenhorizon.beyondhorizon.server.skills;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Skills {
    public static final ResourceKey<Registry<Skill>> REGISTRY_KEY = ResourceKey.createRegistryKey(BeyondHorizon.resource("skills"));
    public static final DeferredRegister<Skill> REGISTRY = DeferredRegister.create(REGISTRY_KEY, BeyondHorizon.ID);
    public static final Supplier<IForgeRegistry<Skill>> SUPPLIER_KEY = REGISTRY.makeRegistry(() -> new RegistryBuilder<Skill>().disableSaving().disableOverrides());

    public static final RegistryObject<Skill> NONE = registerSkill("none", EmptySkills::new);

    public static final RegistryObject<Skill> RUINED_BLADE = registerSkill("ruined_blade", () -> new ExtraDamageSkill(Constant.RUINED_BLADE, ExtraDamageSkill.CURRENT_HEALTH)
            .melee()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));
    public static final RegistryObject<Skill> BLADE_EDGE = registerSkill("blade_edge", () -> new ExtraDamageSkill(Constant.BLADE_EDGE, ExtraDamageSkill.MAX_HEALTH)
            .melee()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));
    public static final RegistryObject<Skill> RADIANT = registerSkill("radiant", () -> new ExtraDamageSkill(Constant.RADIANT, MobType.UNDEAD, ExtraDamageSkill.BONUS_DAMAGE)
            .melee()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));
    public static final RegistryObject<Skill> FIRE_EFFECT = registerSkill("fire_effect", () -> new InflictFireAttackOnHitSkill(Constant.FIRE_EFFECT)
            .universal()
            .format(Skill.Format.NORMAL)
            .type(Skill.Type.PASSIVE));


    public static RegistryObject<Skill> registerSkill(String name, Supplier<Skill> properties) {
        return REGISTRY.register(name, properties);
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
