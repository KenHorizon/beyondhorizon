package com.kenhorizon.libs.registry;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RegistryHelper {

    public static RegistryObject<Enchantment> registerEnchantments(String name, Supplier<Enchantment> supplier) {
        RegistryObject<Enchantment> register = RegistryEntries.ENCHANTMENTS.register(name, supplier);
        registerEnchantmentLang(name, register);
        return register;
    }

    public static RegistryObject<MobEffect> registerEffects(String name, Supplier<MobEffect> supplier) {
        RegistryObject<MobEffect> register = RegistryEntries.MOB_EFFECTS.register(name, supplier);
        reigsterEffectLang(name, register);
        return register;
    }
    public static RegistryObject<SoundEvent> registerSounds(String name) {
        ResourceLocation id = BeyondHorizon.resource(name);
        return RegistryEntries.SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
    public static RegistryObject<SoundEvent> registerSounds(String name, String subtitleName) {
        ResourceLocation id = BeyondHorizon.resource(name);
        RegistryObject<SoundEvent> object = RegistryEntries.SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
        if (!subtitleName.isBlank()) {
            String localization = String.format("subtitle.%s.%s", BeyondHorizon.ID, name);
            RegistryLanguage.ADD_SOUNDS_TRANSLATION.put(object, subtitleName);
        }
        return object;
    }

    private static void reigsterEffectLang(String name, RegistryObject<MobEffect> object) {
        String[] array = name.split("_");
        StringBuilder splitName = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            splitName.append(i == 0 ? array[i] : " " + array[i]);
        }
        RegistryLanguage.ADD_MOB_EFFECT_TRANSLATION.put(object, Utils.capitalize(splitName.toString()));
    }

    public static void registerAttribiuteLang(String name, RegistryObject<Attribute> attributes) {
        String[] array = name.split("[_.]");
        StringBuilder splitName = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            splitName.append(i == 0 ? array[i] : " " + array[i]);
        }
        RegistryLanguage.ADD_ATTRIBUTE_TRANSLATION.put(attributes, Utils.capitalize(splitName.toString()));
    }

    private static void registerEnchantmentLang(String name, RegistryObject<Enchantment> object) {
        String[] array = name.split("_");
        StringBuilder splitName = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            splitName.append(i == 0 ? array[i] : " " + array[i]);
        }
        RegistryLanguage.ADD_ENCHANTMENT_TRANSLATION.put(object, Utils.capitalize(splitName.toString()));
    }

//    public static <T extends Block> NonNullConsumer<? super T> casingConnectivity(BiConsumer<T, CasingConnectivity> consumer) {
//        return entry -> onClient(() -> () -> registerCasingConnectivity(entry, consumer));
//    }
//
//    public static <T extends Block> NonNullConsumer<? super T> connectedTextures(
//            Supplier<ConnectedTextureBehaviour> behavior) {
//        return entry -> onClient(() -> () -> registerCTBehviour(entry, behavior));
//    }
//
//    protected static void onClient(Supplier<Runnable> toRun) {
//        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, toRun);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    private static <T extends Block> void registerCasingConnectivity(T entry, BiConsumer<T, CasingConnectivity> consumer) {
//        consumer.accept(entry, ClientProxy.CASING_CONNECTIVITY);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    private static void registerCTBehviour(Block entry, Supplier<ConnectedTextureBehaviour> behaviorSupplier) {
//        ConnectedTextureBehaviour behavior = behaviorSupplier.get();
//        ClientProxy.MODEL_SWAPPER.getCustomBlockModels().register(getKeyOrThrow(entry), model -> new CTModel(model, behavior));
//    }
    @NotNull
    public static <V> ResourceLocation getKeyOrThrow(IForgeRegistry<V> registry, V value) {
        ResourceLocation key = registry.getKey(value);
        if (key == null) {
            throw new IllegalArgumentException("Could not get key for value " + value + "!");
        }
        return key;
    }

    @NotNull
    public static ResourceLocation getKeyOrThrow(Block value) {
        return getKeyOrThrow(ForgeRegistries.BLOCKS, value);
    }

    @NotNull
    public static ResourceLocation getKeyOrThrow(Item value) {
        return getKeyOrThrow(ForgeRegistries.ITEMS, value);
    }

    @NotNull
    public static ResourceLocation getKeyOrThrow(Fluid value) {
        return getKeyOrThrow(ForgeRegistries.FLUIDS, value);
    }

    @NotNull
    public static ResourceLocation getKeyOrThrow(EntityType<?> value) {
        return getKeyOrThrow(ForgeRegistries.ENTITY_TYPES, value);
    }

    @NotNull
    public static ResourceLocation getKeyOrThrow(BlockEntityType<?> value) {
        return getKeyOrThrow(ForgeRegistries.BLOCK_ENTITY_TYPES, value);
    }

    @NotNull
    public static ResourceLocation getKeyOrThrow(Potion value) {
        return getKeyOrThrow(ForgeRegistries.POTIONS, value);
    }

    @NotNull
    public static ResourceLocation getKeyOrThrow(ParticleType<?> value) {
        return getKeyOrThrow(ForgeRegistries.PARTICLE_TYPES, value);
    }

    @NotNull
    public static ResourceLocation getKeyOrThrow(RecipeSerializer<?> value) {
        return getKeyOrThrow(ForgeRegistries.RECIPE_SERIALIZERS, value);
    }

    @NotNull
    public static <V> V getValueOrThrow(IForgeRegistry<V> registry, ResourceLocation value) {
        V key = registry.getValue(value);
        if (key == null) {
            throw new IllegalArgumentException("Could not get value for value " + value + "!");
        }
        return key;
    }
}
