package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityData;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageHandler;
import com.kenhorizon.beyondhorizon.server.level.utils.AttributeUtils;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class SinglePassiveAccessory extends AccessorySkill {
    public static String NBT_BRING_IT_DOWN = "bring_it_down";
    protected int bringItDownStacks = 0;
    protected boolean bringItDownSFX = false;

    private static final UUID BONUS_CRIT_DAMAGE = UUID.fromString("20c13c52-4226-4724-bf7a-b0ce3dbcf00a");

    public SinglePassiveAccessory() {
        super(0, 1);
    }

    public SinglePassiveAccessory(float magnitude, int level) {
        super(magnitude, level);
    }

    public SinglePassiveAccessory(float magnitude) {
        super(magnitude, 1);
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        if (this == Accessories.NULLIFY.get()) {
            return Component.translatable(this.createId(), MathUtils.format(this.getMagnitude() * 100.0F), MathUtils.format(this.getMagnitude() * 100.0F));
        }
        if (this == Accessories.STING.get()) {
            return Component.translatable(this.createId(), (int) this.getMagnitude());
        }
        if (this == Accessories.BRING_IT_DOWN.get()) {
            return Component.translatable(this.createId(), (int) this.getMagnitude(), MathUtils.format(Constant.BRING_IT_DOWN_INCREASED_DAMAGE * 100.0F));
        }
        if (this == Accessories.EXCORIATE.get()) {
            return Component.translatable(this.createId(), MathUtils.format(this.getMagnitude() * 100.0F));
        }
        if (this == Accessories.NIGHTSTALKER.get()) {
            return Component.translatable(this.createId(), MathUtils.format(this.getMagnitude() * 100.0F));
        }
        return super.tooltipDescription(itemStack);
    }


    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (this == Accessories.GHOUL.get()) {
            if (entity instanceof Player player) {
                player.causeFoodExhaustion(0.150F);
            }
        }

        if (this == Accessories.EXCORIATE.get()) {
            double crit = Math.min(this.getMagnitude(), entity.getAttributeValue(BHAttributes.CRITICAL_CHANCE.get()));
            if (crit <= 0) return;
            if (entity.tickCount % 5 == 0) {
                float randomCrit = entity.getRandom().nextInt((int) (crit * 100.0F)) / 100.0F;
//                BeyondHorizon.LOGGER.debug("{}", randomCrit);
                AttributeModifier bonusCritDamage = new AttributeModifier(BONUS_CRIT_DAMAGE, "Bonus crit damage", randomCrit, AttributeModifier.Operation.ADDITION);
                entity.getAttribute(BHAttributes.CRITICAL_DAMAGE.get()).removeModifier(BONUS_CRIT_DAMAGE);
                entity.getAttribute(BHAttributes.CRITICAL_DAMAGE.get()).addTransientModifier(bonusCritDamage);
            }
        }
        if (this == Accessories.FEATHER_FEET.get()) {
            entity.fallDistance = -1;
        }
        if (this == Accessories.JUMP_BOOST.get()) {
            entity.fallDistance -= entity.getMaxFallDistance() + (entity.getMaxFallDistance() * this.getMagnitude() * this.getLevel());
        }
        if (this == Accessories.ASCENSION.get()) {
            var attrs = entity.getAttributes();
            for (var att : ForgeRegistries.ATTRIBUTES) {
                boolean hasAttrs = attrs.hasAttribute(att);
                var getAtt = entity.getAttribute(att);
                if (hasAttrs && getAtt != null) {
                    getAtt.addTransientModifier(new AttributeModifier("Atttribute" + att.getDescriptionId(), 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
                }
            }
        }
    }


    @Override
    public void onChangePrevAccessorySlot(Player player, ItemStack itemStack) {
        BeyondHorizon.LOGGER.debug("Item has been taken? {}", itemStack.getItem());
        if (this == Accessories.EXCORIATE.get()) {
            player.getAttribute(BHAttributes.CRITICAL_DAMAGE.get()).removeModifier(BONUS_CRIT_DAMAGE);
        }
        if (this == Accessories.ASCENSION.get()) {
            var attrs = player.getAttributes();
            for (var att : ForgeRegistries.ATTRIBUTES) {
                boolean hasAttrs = attrs.hasAttribute(att);
                var getAtt = player.getAttribute(att);
                if (hasAttrs && getAtt != null) {
                    getAtt.removeModifier(new AttributeModifier("Atttribute" + att.getDescriptionId(), 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
                }
            }
        }
    }


    @Override
    public void onChangePostAccessorySlot(Player player, ItemStack itemStack) {
        BeyondHorizon.LOGGER.debug("Item has been put? {}", itemStack.getItem());
    }
    @Override
    public int onItemUseItem(ItemStack itemStack, int duration) {
        if (this == Accessories.GHOUL.get()) {
            if (itemStack.getItem().isEdible()) {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public void onEntityJump(LivingEntity entity, ItemStack itemStack) {
        if (this == Accessories.JUMP_BOOST.get()) {
            entity.fallDistance -= entity.getMaxFallDistance() + (entity.getMaxFallDistance() * this.getMagnitude() * this.getLevel());
            Vec3 vec3 = entity.getDeltaMovement();
            entity.setDeltaMovement(vec3.x, this.getJumpPower(entity) + (this.getJumpPower(entity) * (this.getMagnitude() * this.getLevel())), vec3.z);
        }
    }

    //TODO: copied from class LivingEntity.getJumpPower()
    protected float getJumpPower(LivingEntity entity) {
        return 0.42F * this.getBlockJumpFactor(entity, entity.level()) + entity.getJumpBoostPower();
    }

    protected float getBlockJumpFactor(LivingEntity entity, Level level) {
        float jumpFactor0 = level.getBlockState(entity.blockPosition()).getBlock().getJumpFactor();
        float jumpFactor1 = level.getBlockState(this.getOnPos(entity,0.500001F)).getBlock().getJumpFactor();
        return (double)jumpFactor0 == 1.0D ? jumpFactor1 : jumpFactor0;
    }

    protected BlockPos getOnPos(LivingEntity entity, float yOffset) {
        if (entity.mainSupportingBlockPos.isPresent()) {
            BlockPos blockpos = entity.mainSupportingBlockPos.get();
            if (!(yOffset > 1.0E-5F)) {
                return blockpos;
            } else {
                BlockState blockstate = entity.level().getBlockState(blockpos);
                return (!((double)yOffset <= 0.5D) || !blockstate.collisionExtendsVertically(entity.level(), blockpos, entity)) ? blockpos.atY(Mth.floor(entity.position().y - (double)yOffset)) : blockpos;
            }
        } else {
            int i = Mth.floor(entity.position().x);
            int j = Mth.floor(entity.position().y - (double)yOffset);
            int k = Mth.floor(entity.position().z);
            return new BlockPos(i, j, k);
        }
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        if (target == null || attacker == null) return;
        CompoundTag tagA = EntityData.getOrCreateTag(attacker);
        CompoundTag tagT = EntityData.getOrCreateTag(attacker);
        if (this == Accessories.BURN_EFFECT.get()) {
            target.setSecondsOnFire(Constant.FIRE_EFFECT);
        }
        if (this == Accessories.CORRUPTED_BITE.get()) {
            target.invulnerableTime = 0;
            double AP = attacker.getAttributeValue(BHAttributes.ABILITY_POWER.get());
            target.hurt(BHDamageTypes.magicDamage(attacker, null), (float) (AP * (this.getMagnitude() * this.getLevel())));
        }
        if (this == Accessories.NULLIFY.get()) {
            target.invulnerableTime = 0;
            for (ItemStack armor : target.getArmorSlots()) {
                if (armor.isEnchanted() && armor.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION) > 0) {
                    float damage = damageDealt * (this.getMagnitude() * this.getLevel());
                    target.hurt(BHDamageTypes.nullify(attacker, null), damage);
                }
            }
        }
        if (this == Accessories.DARK_SUN.get()) {
            double bonusAD = AttributeUtils.getBonus(attacker, Attributes.ATTACK_DAMAGE);
            double bonusAP = AttributeUtils.getBonus(attacker, BHAttributes.ABILITY_POWER.get());
            if (bonusAD >= bonusAP) {

                target.invulnerableTime = 0;
                float outputDamage = (damageDealt * this.getMagnitude());
                target.hurt(BHDamageTypes.trueDamage(attacker, null), outputDamage);
            }
        }
        if (this == Accessories.FADED_MOON.get()) {
            double bonusAD = AttributeUtils.getBonus(attacker, Attributes.ATTACK_DAMAGE);
            double bonusAP = AttributeUtils.getBonus(attacker, BHAttributes.ABILITY_POWER.get());
            if (bonusAP >= bonusAD) {
                target.invulnerableTime = 0;
                double mana = attacker.getAttributeValue(BHAttributes.MAX_MANA.get());
                float outputDamage = (float) (mana * this.getMagnitude());
                target.hurt(BHDamageTypes.magicDamage(attacker, null), outputDamage);
            }
        }
        if (this == Accessories.BRING_IT_DOWN.get()) {
            if (attacker instanceof Player player) {
                int xpLevel = player.experienceLevel;
                float baseDamage = (this.getMagnitude() * (xpLevel + 1));
                this.bringItDownStacks++;
//                BeyondHorizon.LOGGER.debug("Bring it down stacks: {} damage {}", this.bringItDownStacks, baseDamage);
                tagA.putInt(NBT_BRING_IT_DOWN, this.bringItDownStacks);
                if (this.bringItDownStacks == 2) {
                    this.bringItDownSFX = true;
                    attacker.level().playSound(null, target.getX(), target.getY(), target.getZ(), BHSounds.HEAVY_ATTACK.get(), SoundSource.MASTER, 1.0F, 1.0F);
                }
                if (this.bringItDownStacks >= 3) {
                    this.bringItDownSFX = false;
                    target.invulnerableTime = 0;
                    target.hurt(BHDamageTypes.physicalDamage(attacker, null), DamageHandler.missingHealth(target, baseDamage, Constant.BRING_IT_DOWN_INCREASED_DAMAGE));
                    tagA.putInt(NBT_BRING_IT_DOWN, 0);
                    this.bringItDownStacks = 0;
                }
            }
        }
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        if (this == Accessories.NULLIFY.get()) {
            for (ItemStack armor : target.getArmorSlots()) {
                if (armor.isEnchanted() && armor.getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION) > 0) {
                    return damageDealt * (this.getMagnitude() * this.getLevel());
                }
            }
        }

        if (this == Accessories.STING.get()) {
            return damageDealt + (this.getMagnitude() * this.getLevel());
        }
        if (this == Accessories.LIFE_SIPHON.get()) {
           return damageDealt + (target.getHealth() * (this.getMagnitude() * this.getLevel()));
        }
        return damageDealt;
    }

    @Override
    public void onEntityKilled(DamageSource damageSource, LivingEntity attacker, LivingEntity target) {
        if (this == Accessories.GHOUL.get()) {
            if (attacker instanceof Player player) {
                player.getFoodData().eat(5, 0);
            }
            attacker.addEffect(new MobEffectInstance(BHEffects.GHOUL_WILL.get(), MathUtils.sec(30), 0, true, true));
        }
    }

    @Override
    public boolean canEntiyReceiveDamage(Player player, LivingEntity target, DamageSource source) {
        if (this == Accessories.FIRE_IMMUNITY.get() && source.is(DamageTypeTags.IS_FIRE)) {
            return source.is(DamageTypes.HOT_FLOOR);
        }
        return false;
    }
}
