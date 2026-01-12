package com.kenhorizon.beyondhorizon.server.item.materials;

import com.google.common.collect.ImmutableMultimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ArmorBaseMaterials implements StringRepresentable, IArmorBaseMaterial, ArmorMaterial {
    WILDFIRE("wildfire", 40, Util.make(new EnumMap<>(ArmorItem.Type.class), (tier) -> {
        tier.put(ArmorItem.Type.BOOTS, ImmutableMultimap.of(
                () -> Attributes.ARMOR, 3.0D,
                () -> Attributes.ARMOR_TOUGHNESS, 3.0D,
                () -> Attributes.KNOCKBACK_RESISTANCE, 0.1D,
                BHAttributes.MAGIC_RESISTANCE, 5.0D
        ));
        tier.put(ArmorItem.Type.LEGGINGS, ImmutableMultimap.of(
                () -> Attributes.ARMOR, 3.0D,
                () -> Attributes.ARMOR_TOUGHNESS, 3.0D,
                () -> Attributes.KNOCKBACK_RESISTANCE, 0.1D,
                BHAttributes.MAGIC_RESISTANCE, 7.0D
        ));
        tier.put(ArmorItem.Type.CHESTPLATE, ImmutableMultimap.of(
                () -> Attributes.MAX_HEALTH, 20.0D,
                () -> Attributes.ARMOR, 7.0D,
                () -> Attributes.ARMOR_TOUGHNESS, 3.0D,
                () -> Attributes.KNOCKBACK_RESISTANCE, 0.1D,
                BHAttributes.MAGIC_RESISTANCE, 8.0D
        ));
        tier.put(ArmorItem.Type.HELMET, ImmutableMultimap.of(
                () -> Attributes.ARMOR, 2.0D,
                () -> Attributes.ARMOR_TOUGHNESS, 3.0D,
                () -> Attributes.KNOCKBACK_RESISTANCE, 0.1D,
                BHAttributes.MAGIC_RESISTANCE, 6.0D
        ));

    }), 15, SoundEvents.ARMOR_EQUIP_IRON, () -> {
        return Ingredient.of(BHItems.WILDFIRE_FRAGMENT.get());
    });

    public static final StringRepresentable.EnumCodec<ArmorBaseMaterials> CODEC = net.minecraft.util.StringRepresentable.fromEnum(ArmorBaseMaterials::values);
    private static final EnumMap<ArmorItem.Type, Integer> BASE_DURABILITY = Util.make(new EnumMap<>(ArmorItem.Type.class), (enumMap) -> {
        enumMap.put(ArmorItem.Type.BOOTS, 13);
        enumMap.put(ArmorItem.Type.LEGGINGS, 15);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 16);
        enumMap.put(ArmorItem.Type.HELMET, 11);
    });
    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, ImmutableMultimap<Supplier<Attribute>, Double>> attributes;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ArmorBaseMaterials(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, ImmutableMultimap<Supplier<Attribute>, Double>> attributes, int enchantmentValue, SoundEvent sound, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.attributes = attributes;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }
    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return BASE_DURABILITY.get(type) * this.durabilityMultiplier;
    }
    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return 0;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public ImmutableMultimap<Supplier<Attribute>, Double> getAttributes(ArmorItem.Type type) {
        return this.attributes.get(type);
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return BeyondHorizon.ID + ":" + this.name;
    }

    public String get() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
