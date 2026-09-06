package com.kenhorizon.beyondhorizon.server.enchantment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryHelper;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessorySlotContext;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.init.BHEnchantments;
import com.kenhorizon.beyondhorizon.server.item.base.weapons.MagicWeaponBaseItem;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageHandler;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.server.tags.BHEntityTypeTags;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;

@SuppressWarnings({"unchecked"})
public class AdvancedEnchantment extends Enchantment implements IAdditionalEnchantment, IAttributeEnchantment {
    public static final Map<String, UUID> UUIDS = new HashMap<>();
    public static final String ENCHANTMENT_TAGS = "enchantment_attributes";
    public static final String ENCHANTMENT_UUID = "enchantments";

    protected final int maxLevel;
    protected final int maxCost;
    protected final int minCost;
    protected final boolean isCursed;
    protected final Predicate<Enchantment> incompatibleEnchantments;
    public static final EnchantmentCategory CATEGORY_ALL = EnchantmentCategory.create("ALL", item -> {
        return true;
    });
    public static final EnchantmentCategory MAGIC_WEAPON = EnchantmentCategory.create("MAGIC_WEAPON", item -> {
        return item instanceof MagicWeaponBaseItem;
    });
    public static final EquipmentSlot[] SLOT_ALL = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND};
    public static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();

    public AdvancedEnchantment(Builder builder) {
        super(builder.rarity, builder.category, builder.slot);
        this.maxLevel = builder.maxLevel;
        this.maxCost = builder.maxCost;
        this.minCost = builder.minCost;
        this.isCursed = builder.isCursed;
        this.incompatibleEnchantments = builder.incomaptibleEnchantment;
        this.attributeModifiers = builder.attributeModifiers;
    }

    @Override
    public void onHitAttack(int level, DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {

        var random = attacker.getRandom();
        if (this == BHEnchantments.SPELL_BLADE.get()) {
            float applyDamage = damageDealt * (0.15F * (level + 1));
            if (source.is(BHDamageTypeTags.PHYSICAL_DAMAGE)) {
                target.invulnerableTime = 0;
                target.hurt(BHDamageTypes.magicDamage(attacker), applyDamage);
            }
        }
        if (this == BHEnchantments.ECHO.get()) {
            float chances = 10.0F + (5.0F * level);
            if (random.nextFloat() * 100.0F <= chances) {
                target.invulnerableTime = 0;
                target.hurt(BHDamageTypes.physicalDamage(attacker), damageDealt / 2);
            }

        }
        if (this == BHEnchantments.STUNNING.get()) {
            float chances = 5.0F + (5.0F * level);
            if (random.nextFloat() * 100.0F <= chances) {
                target.addEffect(new MobEffectInstance(BHEffects.STUN.get(), Maths.sec(1)));
            }
        }
    }

    @Override
    public int modifyExprienceDrop(int level, int dropExperience, LivingEntity target, Player player) {
        if (target == null || player == null) return dropExperience;
        if (this == BHEnchantments.EDUCATION.get()) {
            return (int) (dropExperience * (0.25F * level));
        }
        return dropExperience;
    }


    @Override
    public float postMigitationDamage(int level, float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        RandomSource random = attacker.getRandom();

        if (this == BHEnchantments.PHYSICAL_PROTECTION.get()) {
            if (source.is(BHDamageTypeTags.PHYSICAL_DAMAGE)) {
                damageDealt *= (0.05F * level);
            }
        }

        if (this == BHEnchantments.SPELL_PROTECTION.get()) {
            if (source.getDirectEntity() == target && source.is(BHDamageTypeTags.MAGIC_DAMAGE)) {
                damageDealt *= (0.07F * level);
            }
        }

        if (this == BHEnchantments.DYNAMO_HIT.get()) {
            if (attacker instanceof Player player) {
                PlayerData playerData = Capabilities.data(player);
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

    public static int getDrawSpeed(LivingEntity shooter, int drawSpeed) {
        int level = EnchantmentHelper.getEnchantmentLevel(BHEnchantments.DRAW_SPEED.get(), shooter);
        if (level <= 4) {
            boolean flag = drawSpeed % (5 - level) == 0;
            return flag ? 1 : 0;
        } else {
            return drawSpeed - EnchantmentHelper.getEnchantmentLevel(BHEnchantments.DRAW_SPEED.get(), shooter) - (8 + level);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(UUID uuid, ItemStack stack) {
        return this.attributeModifiers;
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
        public Builder addAttributeModifier(Attribute attribute, double amount, AttributeModifier.Operation operation) {
            AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID(),"Enchantment Attribute Modifier", amount, operation);
            this.attributeModifiers.put(attribute, attributeModifier);
            return this;
        }
    }
}