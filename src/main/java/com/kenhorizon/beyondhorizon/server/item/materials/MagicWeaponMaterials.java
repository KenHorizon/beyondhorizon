package com.kenhorizon.beyondhorizon.server.item.materials;

import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public enum MagicWeaponMaterials implements IWeaponMaterials,  Comparable<MagicWeaponMaterials> {
    WOODEN(0, 500, 1.0F, 8),
    REFINED_WOODEN(0, 500, 1.5F, 8),
    IRON(0, 500, 2.0F, 16),
    GOLDEN(0, 500, 3.0F, 24),
    DIAMOND(0, 500, 4.0F, 18),
    NETHERITE(0, 500, 4.0F, 20, true),
    TIER_ONE(0, 500, 16),
    TIER_TWO(1, 1500, 20),
    TIER_THREE(2, 2500, 24);

    private final int tier;
    private final int durability;
    private final int enchantmentValue;
    private final float abilityPowerBonus;
    private final boolean haveFireResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    MagicWeaponMaterials(int tier, int durability, float abilityPowerBonus, int enchantmentValue,
                         boolean haveFireResistance, Supplier<Ingredient> repairIngredient) {
        this.tier = tier;
        this.durability = durability;
        this.abilityPowerBonus = abilityPowerBonus;
        this.enchantmentValue = enchantmentValue;
        this.haveFireResistance = haveFireResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    MagicWeaponMaterials(int tier, int durability, int enchantmentValue) {
        this(tier, durability, 0, enchantmentValue, false, () -> { return Ingredient.of(Items.AIR); });
    }
    MagicWeaponMaterials(int tier, int durability, float abilityPowerBonus, int enchantmentValue) {
        this(tier, durability, abilityPowerBonus, enchantmentValue, false, () -> { return Ingredient.of(Items.AIR); });
    }

    MagicWeaponMaterials(int tier, int durability, float abilityPowerBonus, int enchantmentValue, boolean haveFireResistance) {
        this(tier, durability, abilityPowerBonus, enchantmentValue, haveFireResistance, () -> { return Ingredient.of(Items.AIR); });
    }

    MagicWeaponMaterials(int tier, int durability, int enchantmentValue, boolean haveFireResistance, Supplier<Ingredient> repairIngredient) {
        this(tier, durability, 0, enchantmentValue, haveFireResistance, repairIngredient);
    }
    MagicWeaponMaterials(int tier, int durability, float abilityPowerBonus, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this(tier, durability, abilityPowerBonus, enchantmentValue, false, repairIngredient);
    }


    @Override
    public String getName() {
        return "magic_weapon";
    }

    @Override
    public boolean fireImmune() {
        return this.haveFireResistance;
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
        return this.abilityPowerBonus;
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
