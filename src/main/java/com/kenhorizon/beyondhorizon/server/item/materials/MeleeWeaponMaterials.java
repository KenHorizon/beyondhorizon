package com.kenhorizon.beyondhorizon.server.item.materials;

import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public enum MeleeWeaponMaterials implements IWeaponMaterials, Comparable<MeleeWeaponMaterials> {
    HELLSTONE("hellstone",
            2,
            650,
            6.0F,
            2.0F,
            14, () -> {
        return Ingredient.of(BHItems.RAW_EMBED_HELLSTONE.get());
    }),
    STARITE("adamantite",
            3,
            2034,
            9.0F,
            3.0F,
            27, () -> {
        return Ingredient.of(BHItems.STARITE_INGOT.get());
    }),
    TIER_ONE("tier_one",
            0,
            0,
            2.0F,
            0.0F,
            16,
            true, () -> {
        return Ingredient.of(Items.AIR);
    }),
    TIER_TWO("tier_one",
            1,
            0,
            2.0F,
            0.0F,
            20,
            true, () -> {
        return Ingredient.of(Items.AIR);
    }),
    TIER_THREE("tier_three",
            2,
            0,
            2.0F,
            0.0F,
            24,
            true, () -> {
        return Ingredient.of(Items.AIR);
    });

    private final String name;
    private final int tier;
    private final int durability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantmentValue;
    private final boolean haveFireResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;
    private final SkillBuilder skillBuilder;

    MeleeWeaponMaterials(String name, int tier, int durability, float miningSpeed, float attackDamage, int enchantmentValue,
                         boolean haveFireResistance, Supplier<Ingredient> repairIngredient, SkillBuilder skillBuilder) {
        this.name = name;
        this.tier = tier;
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantmentValue = enchantmentValue;
        this.haveFireResistance = haveFireResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
        this.skillBuilder = skillBuilder;
    }

    MeleeWeaponMaterials(String name, int tier, int durability, float miningSpeed, float attackDamage, int enchantmentValue, Supplier<Ingredient> repairIngredient, SkillBuilder skillBuilder) {
        this(name, tier, durability, miningSpeed, attackDamage, enchantmentValue, false, repairIngredient, skillBuilder);
    }
    MeleeWeaponMaterials(String name, int tier, int durability, float miningSpeed, float attackDamage, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this(name, tier, durability, miningSpeed, attackDamage, enchantmentValue, false, repairIngredient, null);
    }
    MeleeWeaponMaterials(String name, int tier, int durability, float miningSpeed, float attackDamage, int enchantmentValue, boolean haveFireResistance, Supplier<Ingredient> repairIngredient) {
        this(name, tier, durability, miningSpeed, attackDamage, enchantmentValue, haveFireResistance, repairIngredient, null);
    }
    MeleeWeaponMaterials(int tier, int durability, float miningSpeed, float attackDamage, int enchantmentValue, boolean haveFireResistance, Supplier<Ingredient> repairIngredient, SkillBuilder skillBuilder) {
        this(null, tier, durability, miningSpeed, attackDamage, enchantmentValue, haveFireResistance, repairIngredient, skillBuilder);
    }

    MeleeWeaponMaterials(int tier, int durability, float miningSpeed, float attackDamage, int enchantmentValue, Supplier<Ingredient> repairIngredient, SkillBuilder skillBuilder) {
        this(null, tier, durability, miningSpeed, attackDamage, enchantmentValue, false, repairIngredient, skillBuilder);
    }

    MeleeWeaponMaterials(int tier, int durability, float miningSpeed, float attackDamage, int enchantmentValue, boolean haveFireResistance, Supplier<Ingredient> repairIngredient) {
        this(null, tier, durability, miningSpeed, attackDamage, enchantmentValue, haveFireResistance, repairIngredient, null);
    }

    MeleeWeaponMaterials(int tier, int durability, float miningSpeed, float attackDamage, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this(null, tier, durability, miningSpeed, attackDamage, enchantmentValue, false, repairIngredient, null);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasSkills() {
        return !this.skillBuilder.getSkills().isEmpty();
    }

    @Override
    public boolean hasSkills(Skill abilityTrait) {
        return this.skillBuilder != null && this.skillBuilder.getSkills().contains(abilityTrait);
    }

    @Override
    public boolean fireImmune() {
        return this.haveFireResistance;
    }

    @Override
    public List<Skill> getSkills() {
        return this.skillBuilder == null ? List.of() : new ArrayList<>(new HashSet<>(this.skillBuilder.getSkills()));
    }

    @Override
    public int getUses() {
        return this.durability;
    }

    @Override
    public float getSpeed() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getLevel() {
        return this.tier;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}