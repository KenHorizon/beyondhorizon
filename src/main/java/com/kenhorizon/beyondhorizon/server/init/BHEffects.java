package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.effect.*;
import com.kenhorizon.libs.registry.RegistryEntries;
import com.kenhorizon.libs.registry.RegistryHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;


public class BHEffects {

    public static final RegistryObject<MobEffect> BLEED =
            RegistryHelper.registerEffects("bleed", () -> new BHMobEffect(MobEffectCategory.HARMFUL, 0x4F0000));
    public static final RegistryObject<MobEffect> LETHAL_POISON =
            RegistryHelper.registerEffects("lethal_poison", () -> new BHMobEffect(MobEffectCategory.HARMFUL, 0x1A3000));
    public static final RegistryObject<MobEffect> LIGHTNING =
            RegistryHelper.registerEffects("lightning", () -> new BHMobEffect(MobEffectCategory.NEUTRAL, 0x0094FF));
    public static final RegistryObject<MobEffect> THORNS =
            RegistryHelper.registerEffects("thorns", () -> new BHMobEffect(MobEffectCategory.BENEFICIAL, 0x007F0E));
    public static final RegistryObject<MobEffect> CURSED =
            RegistryHelper.registerEffects("cursed", () -> new BHMobEffect(MobEffectCategory.BENEFICIAL, 0x00FF21));
    public static final RegistryObject<MobEffect> IRON_SKIN =
            RegistryHelper.registerEffects("iron_skin", () -> new ModifierEffect(MobEffectCategory.NEUTRAL, 0x808080, 0.10F).addAttributeModifier(Attributes.ARMOR, "923ae74a-a3a1-490f-b183-2734f16d00b8", (double) 0.05F, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> ARMOR_BREAK =
            RegistryHelper.registerEffects("armor_break", () -> new ModifierEffect(MobEffectCategory.NEUTRAL, 0x808080, -0.05F).addAttributeModifier(Attributes.ARMOR, "afd508a1-981f-4cad-aaa5-4fda014e8c53", (double) -0.15F, AttributeModifier.Operation.MULTIPLY_BASE).addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "d042bf31-085c-454e-94ef-31cee0c83ba8", (double) -0.15F, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> NATURE_HEAL =
            RegistryHelper.registerEffects("rapid_healing", () -> new ModifierEffect(MobEffectCategory.NEUTRAL, 0x808080, 0.05F).addAttributeModifier(BHAttributes.HEALTH_REGENERATION.get(), "2c23c41e-069d-48b1-88a6-6cc5bfeaedb2", (double) 0.15F, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> IMMUNITY =
            RegistryHelper.registerEffects("immunity", () -> new BHMobEffect(MobEffectCategory.BENEFICIAL, 0xFFEA82));
    public static final RegistryObject<MobEffect> RECOVERY =
            RegistryHelper.registerEffects("recovery", () -> new BHMobEffect(MobEffectCategory.NEUTRAL, 0x4CFF00));
    public static final RegistryObject<MobEffect> PARALYZE =
            RegistryHelper.registerEffects("paralyze", () -> new BHMobEffect(MobEffectCategory.NEUTRAL, 0xFFD800));
    public static final RegistryObject<MobEffect> LEECH =
            RegistryHelper.registerEffects("leech", () -> new ModifierEffect(MobEffectCategory.BENEFICIAL, 0xFF9E9E, 0.10F).addAttributeModifier(BHAttributes.PHYSICALVAMP.get(), "3dee7ca1-5b28-48c2-811f-083979e21d01", (double) 0.05F, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> FEAR =
            RegistryHelper.registerEffects("fear", () -> new FearEffect(MobEffectCategory.NEUTRAL, 0x303030));
    public static final RegistryObject<MobEffect> RAGE =
            RegistryHelper.registerEffects("rage", () -> new ModifierEffect(MobEffectCategory.BENEFICIAL, 0xFF0000, 0.05D).addAttributeModifier(BHAttributes.DAMAGE_DEALT.get(), "0b962cab-73cc-4159-970d-f4354c51bd18", (double) 0.05D, AttributeModifier.Operation.ADDITION).addAttributeModifier(Attributes.MOVEMENT_SPEED, "2547b4b4-4ac2-4085-b503-564c00835e39", (double) 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> VULNERABLE =
            RegistryHelper.registerEffects("vulnerable", () -> new ModifierEffect(MobEffectCategory.HARMFUL, 0x4040400, 0.05F).addAttributeModifier(BHAttributes.DAMAGE_TAKEN.get(), "9a5dcc14-4692-42ef-8304-3985a91f5220", (double)-0.05F, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> HEALING_SICKNESS =
            RegistryHelper.registerEffects("healing_sickness", () -> new BHMobEffect(MobEffectCategory.NEUTRAL, 0x7F0000));
    public static final RegistryObject<MobEffect> STUN =
            RegistryHelper.registerEffects("stun", () -> new StunEffect(MobEffectCategory.NEUTRAL, 0xFFE16B).addAttributeModifier(Attributes.MOVEMENT_SPEED, "0e530966-8625-4300-890c-3992a8198d6e", -Integer.MAX_VALUE, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> DRAGON_FLAME =
            RegistryHelper.registerEffects("dragon_flame", () -> new BHMobEffect(MobEffectCategory.HARMFUL, 0xFF25FF));
    public static final RegistryObject<MobEffect> ROOTED =
            RegistryHelper.registerEffects("rooted", () -> new RootedEffect(MobEffectCategory.NEUTRAL, 0x9B4D00).addAttributeModifier(Attributes.MOVEMENT_SPEED, "0e530966-8625-4300-890c-3992a8198d6e", -Integer.MAX_VALUE, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> TORMENT =
            RegistryHelper.registerEffects("torment", () -> new TormentEffect(MobEffectCategory.HARMFUL, 0xFF6A00));
    public static final RegistryObject<MobEffect> WOUNDED =
            RegistryHelper.registerEffects("wounded", () -> new BHMobEffect(MobEffectCategory.NEUTRAL, 0xFF8206)
                    .addAttributeModifier(BHAttributes.HEALING.get(), "4ca80eb7-871b-4751-af4e-c7ec47642425", 0.40D, AttributeModifier.Operation.MULTIPLY_BASE)
                    .addAttributeModifier(BHAttributes.HEALTH_REGENERATION.get(), "1331d9ae-32f1-4a33-87d9-cf0621a72aa3", 0.40D, AttributeModifier.Operation.MULTIPLY_BASE));

    public static void register(IEventBus eventBus) {
        RegistryEntries.MOB_EFFECTS.register(eventBus);
    }
}
