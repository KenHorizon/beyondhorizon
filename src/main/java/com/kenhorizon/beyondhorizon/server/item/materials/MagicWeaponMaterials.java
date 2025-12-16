package com.kenhorizon.beyondhorizon.server.item.materials;

import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public enum MagicWeaponMaterials implements IWeaponMaterials, Comparable<MagicWeaponMaterials> {
    TIER_ONE(0, 500, 16),
    TIER_TWO(1, 1500, 20),
    TIER_THREE(2, 2500, 24);

    private final int tier;
    private final int durability;
    private final int enchantmentValue;
    private final boolean haveFireResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;
    private final SkillBuilder skillBuilder;

    MagicWeaponMaterials(int tier, int durability, int enchantmentValue,
                         boolean haveFireResistance, Supplier<Ingredient> repairIngredient, SkillBuilder skillBuilder) {
        this.tier = tier;
        this.durability = durability;
        this.enchantmentValue = enchantmentValue;
        this.haveFireResistance = haveFireResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
        this.skillBuilder = skillBuilder;
    }

    MagicWeaponMaterials(int tier, int durability, int enchantmentValue, Supplier<Ingredient> repairIngredient, SkillBuilder skillBuilder) {
        this(tier, durability, enchantmentValue, false, repairIngredient, skillBuilder);
    }
    MagicWeaponMaterials(int tier, int durability,  boolean haveFireResistance, int enchantmentValue, SkillBuilder skillBuilder) {
        this(tier, durability, enchantmentValue, haveFireResistance, () -> { return Ingredient.of(Items.AIR); }, skillBuilder);
    }
    MagicWeaponMaterials(int tier, int durability, int enchantmentValue, SkillBuilder skillBuilder) {
        this(tier, durability, enchantmentValue, false, () -> { return Ingredient.of(Items.AIR); }, skillBuilder);
    }
    MagicWeaponMaterials(int tier, int durability, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this(tier, durability, enchantmentValue, false, repairIngredient, null);
    }
    MagicWeaponMaterials(int tier, int durability, boolean haveFireResistance, int enchantmentValue) {
        this(tier, durability, enchantmentValue, haveFireResistance, () -> { return Ingredient.of(Items.AIR); }, null);
    }
    MagicWeaponMaterials(int tier, int durability, int enchantmentValue) {
        this(tier, durability, enchantmentValue, false, () -> { return Ingredient.of(Items.AIR); }, null);
    }
    MagicWeaponMaterials(int tier, int durability, int enchantmentValue, boolean haveFireResistance, Supplier<Ingredient> repairIngredient) {
        this(tier, durability, enchantmentValue, haveFireResistance, repairIngredient, null);
    }


    @Override
    public String getName() {
        return "magic_weapon";
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
        return 0.0F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 0.0F;
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
