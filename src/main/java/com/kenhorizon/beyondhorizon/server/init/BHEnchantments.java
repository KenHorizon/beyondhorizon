package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.enchantment.AdvancedEnchantment;
import com.kenhorizon.beyondhorizon.server.enchantment.ExtendedDamageEnchantment;
import com.kenhorizon.beyondhorizon.server.enchantment.SmelterEnchantment;
import com.kenhorizon.libs.registry.RegistryEntries;
import com.kenhorizon.libs.registry.RegistryHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHEnchantments {
    public static RegistryObject<Enchantment> SPELL_POWER = RegistryHelper.registerEnchantments("spell_power",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(10)
                    .minCost(10)
                    .rarity(Enchantment.Rarity.COMMON)
                    .category(AdvancedEnchantment.MAGIC_WEAPON)
                    .addAttributeModifier(BHAttributes.ABILITY_POWER.get(), 0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL)
            ));
    public static RegistryObject<Enchantment> MANA_RECOVERY = RegistryHelper.registerEnchantments("mana_recovery",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(4)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(10)
                    .minCost(10)
                    .rarity(Enchantment.Rarity.UNCOMMON)
                    .category(AdvancedEnchantment.MAGIC_WEAPON)
                    .addAttributeModifier(BHAttributes.MANA_REGENERATION.get(), 0.05D, AttributeModifier.Operation.ADDITION)
            ));
    public static RegistryObject<Enchantment> MANA_COST = RegistryHelper.registerEnchantments("mana_cost",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(3)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(15)
                    .minCost(5)
                    .rarity(Enchantment.Rarity.COMMON)
                    .category(AdvancedEnchantment.MAGIC_WEAPON)
                    .addAttributeModifier(BHAttributes.MANA_COST.get(), 0.05D, AttributeModifier.Operation.ADDITION)
            ));

    public static RegistryObject<Enchantment> DRAW_SPEED = RegistryHelper.registerEnchantments("draw_speed",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(20)
                    .minCost(6)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.BOW)
            ));
    public static RegistryObject<Enchantment> STUNNING = RegistryHelper.registerEnchantments("stunning",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(3)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(20)
                    .minCost(6)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.WEAPON)
            ));
    public static RegistryObject<Enchantment> EDUCATION = RegistryHelper.registerEnchantments("education",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(10)
                    .minCost(10)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.WEAPON)
            ));
    public static RegistryObject<Enchantment> PHYSICAL_PROTECTION = RegistryHelper.registerEnchantments("physical_protection",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(10)
                    .minCost(10)
                    .rarity(Enchantment.Rarity.COMMON)
                    .category(EnchantmentCategory.WEAPON)
            ));

    public static RegistryObject<Enchantment> SPELL_PROTECTION = RegistryHelper.registerEnchantments("spell_protection",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(10)
                    .minCost(10)
                    .rarity(Enchantment.Rarity.COMMON)
                    .category(EnchantmentCategory.WEAPON)
            ));

    public static RegistryObject<Enchantment> LIFESTEAL = RegistryHelper.registerEnchantments("lifesteal",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(10)
                    .minCost(10)
                    .rarity(Enchantment.Rarity.UNCOMMON)
                    .category(EnchantmentCategory.WEAPON)
            ));

    public static RegistryObject<Enchantment> BUTCHERING = RegistryHelper.registerEnchantments("butchering",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(10)
                    .minCost(10)
                    .rarity(Enchantment.Rarity.COMMON)
                    .category(EnchantmentCategory.WEAPON)
            ));

    public static RegistryObject<Enchantment> AQUATIC_BANE = RegistryHelper.registerEnchantments("aquatice_bane",
            () -> new ExtendedDamageEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(10)
                    .minCost(6)
                    .rarity(Enchantment.Rarity.UNCOMMON)
                    .category(EnchantmentCategory.WEAPON)
            ));

    public static RegistryObject<Enchantment> ILLAGER_BANE = RegistryHelper.registerEnchantments("illager_bane",
            () -> new ExtendedDamageEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(10)
                    .minCost(10)
                    .rarity(Enchantment.Rarity.UNCOMMON)
                    .category(EnchantmentCategory.WEAPON)
            ));

    public static RegistryObject<Enchantment> VOID_BANE = RegistryHelper.registerEnchantments("void_bane",
            () -> new ExtendedDamageEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(10)
                    .minCost(12)
                    .rarity(Enchantment.Rarity.UNCOMMON)
                    .category(EnchantmentCategory.WEAPON)
            ));

    public static RegistryObject<Enchantment> ECHO = RegistryHelper.registerEnchantments("echo",
            () -> new ExtendedDamageEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(20)
                    .minCost(12)
                    .rarity(Enchantment.Rarity.VERY_RARE)
                    .category(EnchantmentCategory.WEAPON)
            ));

    public static RegistryObject<Enchantment> ARMOR_PENETRATION = RegistryHelper.registerEnchantments("armor_penetration",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(20)
                    .minCost(15)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.WEAPON)
                    .addAttributeModifier(BHAttributes.ARMOR_PENETRATION.get(), 0.05D, AttributeModifier.Operation.ADDITION)
            ));

    public static RegistryObject<Enchantment> DYNAMO_HIT = RegistryHelper.registerEnchantments("dyanmo_hit",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(3)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(12)
                    .minCost(15)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.WEAPON)
                    .incompatible(enchantment -> enchantment != BHEnchantments.CRITICAL_DAMAGE.get())
            ));

    public static RegistryObject<Enchantment> DRAGON_SLAYER = RegistryHelper.registerEnchantments("dragon_slayer",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(3)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(12)
                    .minCost(15)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.WEAPON)
            ));

    public static RegistryObject<Enchantment> CRITICAL_HIT = RegistryHelper.registerEnchantments("critical_hit",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(12)
                    .minCost(15)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.WEAPON)
                    .addAttributeModifier(BHAttributes.CRITICAL_CHANCE.get(), 0.10F, AttributeModifier.Operation.ADDITION)
            ));

    public static RegistryObject<Enchantment> CRITICAL_DAMAGE = RegistryHelper.registerEnchantments("critical_damage",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(2)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(12)
                    .minCost(15)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.WEAPON)
                    .addAttributeModifier(BHAttributes.CRITICAL_DAMAGE.get(), 0.10F, AttributeModifier.Operation.MULTIPLY_BASE)
            ));

    public static RegistryObject<Enchantment> VIBRANCY = RegistryHelper.registerEnchantments("vibrancy",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(AdvancedEnchantment.ARMOR_SLOTS)
                    .maxCost(12)
                    .minCost(15)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.ARMOR)
                    .addAttributeModifier(Attributes.MAX_HEALTH, 0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            ));

    public static RegistryObject<Enchantment> SWIFTNESS = RegistryHelper.registerEnchantments("swiftness",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(AdvancedEnchantment.ARMOR_SLOTS)
                    .maxCost(12)
                    .minCost(15)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.ARMOR)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, 0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL)
            ));

    public static RegistryObject<Enchantment> SPELL_BLADE = RegistryHelper.registerEnchantments("spell_blade",
            () -> new AdvancedEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(2)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(33)
                    .minCost(22)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.WEAPON)
            ));

    public static RegistryObject<Enchantment> SMELTER = RegistryHelper.registerEnchantments("smelter",
            () -> new SmelterEnchantment(new AdvancedEnchantment.Builder()
                    .maxLevel(5)
                    .slot(new EquipmentSlot[] {EquipmentSlot.MAINHAND})
                    .maxCost(63)
                    .minCost(22)
                    .rarity(Enchantment.Rarity.RARE)
                    .category(EnchantmentCategory.DIGGER)
            ));

    public static void register(IEventBus eventBus) {
        RegistryEntries.ENCHANTMENTS.register(eventBus);
    }
}
