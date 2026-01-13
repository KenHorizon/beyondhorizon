package com.kenhorizon.beyondhorizon.server.tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

public class BHEffectTags {
    public static final TagKey<MobEffect> DIVINE_ANKH_IMMUNE_TO = create("divine_ankh_immune_to");
    public static final TagKey<MobEffect> LIGHTNESS_IMMUNE_TO = create("lightness_immune_to");
    public static final TagKey<MobEffect> OWL_SIGHT_IMMUNE_TO = create("owl_sight_immune_to");
    public static final TagKey<MobEffect> HAUNTING_GUISE_IMMUNE_TO = create("haunting_guise_immune_to");
    public static final TagKey<MobEffect> BODY_POISON_IMMUNE_TO = create("body_poison_immune_to");
    public static final TagKey<MobEffect> WEIGHT_IMMUNE_TO = create("weight_immune_to");
    public static final TagKey<MobEffect> TOUGH_SKIN_IMMUNE_TO = create("tough_skin_immune_to");
    public static final TagKey<MobEffect> VITAMINS_IMMUNE_TO = create("vitamins_immune_to");
    public static final TagKey<MobEffect> HEMORRHAGE_CONTROL_IMMUNE_TO = create("hemorrhage_control_immune_to");
    public static final TagKey<MobEffect> INVULNERABLE_IMMUNE_TO = create("invulnerable_immune_to");
    public static final TagKey<MobEffect> UNBOTHERED_IMMUNE_TO = create("unbothered_immune_to");
    public static final TagKey<MobEffect> PRESERVED_IMMUNE_TO = create("preserved_immune_to");
    public static final TagKey<MobEffect> BRUTE_RESISTANCE_IMMUNE_TO = create("brute_resistance_immune_to");
    public static final TagKey<MobEffect> SEEK_ONE_SEEK_TWICE_IMMUNE_TO = create("seek_one_seek_twice_immune_to");

    public static TagKey<MobEffect> create(String name) {
        return TagKey.create(ForgeRegistries.MOB_EFFECTS.getRegistryKey(), BeyondHorizon.resource(name));
    }
}
