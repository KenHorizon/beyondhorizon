package com.kenhorizon.beyondhorizon.server.enchantment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEnchantments;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageHandler;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.server.tags.BHEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Predicate;

public class AdvancedEnchantment extends Enchantment implements IAdditionalEnchantment {
    protected final int maxLevel;
    protected final int maxCost;
    protected final int minCost;
    protected final boolean isCursed;
    protected final Predicate<Enchantment> incompatibleEnchantments;
    public static final EnchantmentCategory CATEGORY_ALL = EnchantmentCategory.create("ALL", item -> {
        return true;
    });
    public static final EquipmentSlot[] SLOT_ALL = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND};
    public static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public AdvancedEnchantment(Builder builder) {
        super(builder.rarity, builder.category, builder.slot);
        this.maxLevel = builder.maxLevel;
        this.maxCost = builder.maxCost;
        this.minCost = builder.minCost;
        this.isCursed = builder.isCursed;
        this.incompatibleEnchantments = builder.incomaptibleEnchantment;
        ((IAttributeEnchantment) this).perLevel(builder.perLevel);
        ((IAttributeEnchantment) this).getAttributeModifiers().putAll(builder.attributeModifiers);
    }

    @Override
    public void onHitAttack(int level, DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        if (this == BHEnchantments.SPELL_BLADE.get()) {
            if (source.is(BHDamageTypeTags.PHYSICAL_DAMAGE)) {
                float applyDamage = damageDealt * (0.5F * (level + 1));
                target.hurt(BHDamageTypes.magicDamage(attacker), applyDamage);
            }
        }
    }

    @Override
    public float postMigitationDamage(int level, float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        RandomSource random = attacker.getRandom();
        if (this == BHEnchantments.DYNAMO_HIT.get()) {
            if (attacker instanceof Player player) {
                PlayerData playerData = CapabilityCaller.data(player);
                if (playerData.isCrit()) {
                    damageDealt *= 2.0F;
                } else {
                    damageDealt *= 0.75F;
                }
            } else {
                if (attacker.getRandom().nextBoolean()) {
                    damageDealt *= 1.5F; // Base default of critical damage
                }
            }
        }
        if (this == BHEnchantments.DRAGON_SLAYER.get()) {
            damageDealt *= 1.5F;
            if (target instanceof EnderDragon) {
                damageDealt *= 2.25F;
            }
        }
        if (this == BHEnchantments.LIFESTEAL.get()) {
            attacker.heal((float) (damageDealt * (0.05F * (level + 1))));
        }
        if (this == BHEnchantments.ILLAGER_BANE.get()) {
            if (target.getMobType() == MobType.ILLAGER) {
                damageDealt = DamageHandler.multiplier(damageDealt, 0.10F * (level + 1));
            }
        }
        if (this == BHEnchantments.AQUATIC_BANE.get()) {
            float applyDamage = 0.10F * (level + 1);
            if (target.isEyeInFluidType(ForgeMod.WATER_TYPE.get()) || target.isUnderWater() || target.isInWater()) {
                damageDealt = DamageHandler.multiplier(damageDealt, applyDamage);
            } else if (target.level().isThundering() || target.level().isRaining()) {
                BlockPos pos = target.blockPosition();
                if (target.level().canSeeSky(pos)) {
                    damageDealt = DamageHandler.multiplier(damageDealt, applyDamage);
                }
            }
        }
        if (this == BHEnchantments.VOID_BANE.get()) {
            boolean applyEffect = target.getType().is(BHEntityTypeTags.VOID_BANE_AFFECTED);
            if (applyEffect) {
                damageDealt = DamageHandler.multiplier(damageDealt, 0.10F * (level + 1));
            }
        }
        if (this == BHEnchantments.BUTCHERING.get()) {
            if (target instanceof Animal) {
                damageDealt = DamageHandler.additional(damageDealt, 2 * (level + 1));
            }
        }
        return damageDealt;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public int getMinLevel() {
        return super.getMinLevel();
    }

    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + this.maxCost;
    }

    @Override
    public int getMinCost(int level) {
        return this.minCost * level * 10;
    }

    @Override
    public boolean isCurse() {
        return this.isCursed;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        if (this.getIncompatibleEnchantments() == null) {
            return super.checkCompatibility(other);
        } else {
            return  this.getIncompatibleEnchantments().test(other);
        }
    }

    public Predicate<Enchantment> getIncompatibleEnchantments() {
        return incompatibleEnchantments;
    }

    public static class Builder {
        int maxLevel = 1;
        int xpCost = 0;
        int maxCost = 0;
        int minCost = 0;
        boolean isCursed = false;
        boolean isTradeable = true;
        boolean isTreasureOnly = false;
        boolean isDiscoverable = true;
        Enchantment.Rarity rarity = Rarity.COMMON;
        EnchantmentCategory category = AdvancedEnchantment.CATEGORY_ALL;
        EquipmentSlot[] slot = AdvancedEnchantment.SLOT_ALL;
        Predicate<Enchantment> incomaptibleEnchantment;
        Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
        double perLevel = 0.0F;

        public Builder xpCost(int xpCost) {
            this.xpCost = xpCost;
            return this;
        }
        public Builder maxLevel(int maxLevel) {
            this.maxLevel = maxLevel;
            return this;
        }
        public Builder maxCost(int maxCost) {
            this.maxCost = maxCost;
            return this;
        }
        public Builder minCost(int minCost) {
            this.minCost = minCost;
            return this;
        }
        public Builder isCursed(boolean b) {
            this.isCursed = b;
            return this;
        }
        public Builder isTradeable(boolean b) {
            this.isTradeable = b;
            return this;
        }
        public Builder isTreasureOnly(boolean b) {
            this.isTreasureOnly = b;
            return this;
        }
        public Builder isDiscoverable(boolean b) {
            this.isDiscoverable = b;
            return this;
        }
        public Builder rarity(Enchantment.Rarity rarity) {
            this.rarity = rarity;
            return this;
        }
        public Builder category(EnchantmentCategory category) {
            this.category = category;
            return this;
        }
        public Builder slot(EquipmentSlot[] slot) {
            this.slot = slot;
            return this;
        }
        public Builder incompatible(Predicate<Enchantment> incomaptibleEnchantment) {
            this.incomaptibleEnchantment = incomaptibleEnchantment;
            return this;
        }

        /**
         * Handle by {@link IAttributeEnchantment} allow to add attributes for assigned enchantments
         * */
        public Builder addAttributeModifier(Attribute attribute, String uuid, double amount, AttributeModifier.Operation operation) {
            AttributeModifier attributeModifier = new AttributeModifier(UUID.fromString(uuid), "Enchantment Attribute Modifier", amount, operation);
            this.attributeModifiers.put(attribute, attributeModifier);
            return this;
        }
        public Builder addAttributeModifier(Attribute attribute, String uuid, double amount, double perLevel, AttributeModifier.Operation operation) {
            AttributeModifier attributeModifier = new AttributeModifier(UUID.fromString(uuid), "Enchantment Attribute Modifier", amount, operation);
            this.perLevel = perLevel;
            this.attributeModifiers.put(attribute, attributeModifier);
            return this;
        }
    }
}