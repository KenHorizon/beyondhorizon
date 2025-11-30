package com.kenhorizon.beyondhorizon.server.item.materials;

import com.kenhorizon.beyondhorizon.server.item.base.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.skills.Skill;
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
    BAMBOO("bamboo", 0, 39, 2.0F, 0.0F, 12, () -> {
        return Ingredient.of(ItemTags.PLANKS);
    }),
    WOOD("wooden", 0, 59, 2.0F, 0.0F, 15, () -> {
        return Ingredient.of(ItemTags.PLANKS);
    }),
    STONE("stone", 1, 131, 4.0F, 1.0F, 5, () -> {
        return Ingredient.of(ItemTags.STONE_TOOL_MATERIALS);
    }),
    IRON("iron", 2, 250, 6.0F, 2.0F, 14, () -> {
        return Ingredient.of(Items.IRON_INGOT);
    }),
    //    EMERALD("emerald", 2, 765, 4.0F, 1.5F, 16, () -> {
//        return Ingredient.of(BHItems.EMERALD_INGOT.get());
//    }),
//    SILVER("silver", 3, 235, 5.5F, 2.0F, 22, () -> {
//        return Ingredient.of(Items.AIR);
//    }, AbilityTraitBuilder.DOUBLE_DAMAGE_TO_UNDEAD),
//    COBALT("cobalt", 3, 434, 10.0F, 2.5F, 30, () -> {
//        return Ingredient.of(Items.AIR);
//    }, AbilityTraitBuilder.COBALT),
//    HELLSTONE("hellstone", 3, 734, 7.5F, 2.0F, 15, true, () -> {
//        return Ingredient.of(Items.AIR);
//    }, AbilityTraitBuilder.HELLSTONE_AND_HELLFIRE),
//    HELLFIRE("hellfire", 3, 1334, 7.5F, 3.5F, 25, true, () -> {
//        return Ingredient.of(Items.AIR);
//    }, AbilityTraitBuilder.HELLSTONE_AND_HELLFIRE),
//    GOLD("golden", 0, 32, 12.0F, 0.0F, 22, () -> {
//        return Ingredient.of(Items.GOLD_INGOT);
//    }),
//    DIAMOND("diamond", 3, 1561, 8.0F, 3.0F, 10, () -> {
//        return Ingredient.of(Items.DIAMOND);
//    }),
//    PALLADIUM("palladium", 3, 1034, 7.5F, 3.5F, 14, true, () -> {
//        return Ingredient.of(BHItems.PALLADIUM_INGOT.get());
//    }, AbilityTraitBuilder.PALLADIUM),
//    NETHERITE("netherite", 3, 2031, 9.0F, 4.0F, 15, true, () -> {
//        return Ingredient.of(Items.NETHERITE_INGOT);
//    }),
//    ADAMANTITE("adamantite", 3, 1234, 9.5F, 4.5F, 16, true, () -> {
//        return Ingredient.of(BHItems.ADAMANTITE_INGOT.get());
//    }, AbilityTraitBuilder.ADAMANTITE),
//    TITANIUM("titanium", 3, 834, 9.5F, 4.5F, 15, true, () -> {
//        return Ingredient.of(BHItems.TITANIUM_INGOT.get());
//    }, AbilityTraitBuilder.TITANIUM),
    BLADE_OF_THE_ENDERLORD("blade_of_the_enderlord",
            0,
            3251,
            2.0F,
            0.0F,
            22,
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

    // Main
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