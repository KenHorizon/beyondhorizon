package com.kenhorizon.libs.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class RegistryLanguage {
    public static Map<RegistryObject<Enchantment>, String> ADD_ENCHANTMENT_TRANSLATION = new HashMap<>();
    public static Map<String, String> ADD_PAINTING_TRANSLATION_AUTHOR = new HashMap<>();
    public static Map<String, String> ADD_PAINTING_TRANSLATION_TITLE = new HashMap<>();
    public static Map<RegistryObject<Attribute>, String> ADD_ATTRIBUTE_TRANSLATION = new HashMap<>();
    public static Map<RegistryObject<? extends Item>, String> ADD_ITEM_TRANSLATION = new HashMap<>();
    public static Map<RegistryObject<? extends Block>, String> ADD_BLOCK_TRANSLATION = new HashMap<>();
    public static Map<RegistryObject<MobEffect>, String> ADD_MOB_EFFECT_TRANSLATION = new HashMap<>();
    public static Map<String, String> ADD_SOUNDS_TRANSLATION = new HashMap<>();
    public static Map<String, String> ADD_ITEM_LORE = new HashMap<>();
    public static Map<RegistryObject<EntityType<?>>, String> ADD_ENTITY_TRANSLATION = new HashMap<>();

}
