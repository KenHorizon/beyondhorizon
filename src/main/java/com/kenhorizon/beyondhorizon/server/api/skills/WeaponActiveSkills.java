package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.api.data.IItemProperties;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.level.utils.AttributeUtils;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.*;

public abstract class WeaponActiveSkills extends Skill implements IAttack, IEntityProperties, IItemProperties {

    public enum DamageTypes {
        PHYISCAL_DAMAGE("Physical Damage"),
        MAGIC_DAMAGE("Magic Damage"),
        TRUE_DAMAGE("True Damage");

        final String description;
        DamageTypes(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
    public enum ManaCostType {
        DEFAULT,
        PERCENTAGE
    }
    private float magnitude;
    private float level;
    protected WeaponActiveSkills.ManaCostType manaCostType = ManaCostType.DEFAULT;
    public WeaponActiveSkills() {
        super(Type.ACTIVE);
        this.isSkill = true;
    }

    public WeaponActiveSkills.ManaCostType getManaCostType() {
        return manaCostType;
    }

    @Override
    protected List<MutableComponent> tooltipDescriptionList(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        String tooltips;
        if (this.getManaCostType() == WeaponActiveSkills.ManaCostType.PERCENTAGE) {
            tooltips = Tooltips.TOOLTIP_MANA_COST_PERCENTAGES;
        } else {
            tooltips = Tooltips.TOOLTIP_MANA_COST;
        }
        list.add(Component.translatable(tooltips, this.getManaCost()).withStyle(ChatFormatting.UNDERLINE));
        if (this.getCooldown() > 0) {
            list.add(Component.translatable(Tooltips.TOOLTIP_COOLDOWN, (int) (this.getCooldown() / 20)).withStyle(ChatFormatting.UNDERLINE));
        }
        if (this.tooltipDescriptions(itemStack).isEmpty()) {
            list.add(Component.translatable(createId(), Maths.format(this.getMagnitude())));
        } else {
            list.addAll(this.tooltipDescriptions(itemStack));
        }
        return list;
    }

    protected List<MutableComponent> tooltipDescriptions(ItemStack itemStack) {
        return List.of();
    }

    public boolean dealDamage(LivingEntity taret, LivingEntity player, float damage, WeaponActiveSkills.DamageTypes damageTypes) {
        return this.dealDamage(taret, player, damage, false, damageTypes);
    }

    public boolean dealDamage(LivingEntity target, LivingEntity holder, float damage, boolean knockback, WeaponActiveSkills.DamageTypes damageTypes) {
        switch (damageTypes) {
            case TRUE_DAMAGE -> {
                return target.hurt(BHDamageTypes.trueDamage(holder, knockback), damage);
            }
            case MAGIC_DAMAGE -> {
                return target.hurt(BHDamageTypes.magicDamage(holder, knockback), damage);
            }
            default -> {
                return target.hurt(BHDamageTypes.physicalDamage(holder, knockback), damage);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(ItemStack itemStack, Level level, Player player, InteractionHand hand) {
        PlayerData playerData = PlayerData.getInstance(player);
        if (!level.isClientSide()) {
            if (playerData.isOnCooldown(this.getId())) {
                player.displayClientMessage(Component.translatable(Tooltips.TOOLTIP_ON_COOLDOWN).withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(itemStack);
            }
            if (playerData.getMana() <= this.getManaCost()) {
                player.displayClientMessage(Component.translatable(Tooltips.TOOLTIP_NOT_ENOUGH_MANA).withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(itemStack);
            } else if (playerData.getMana() >= this.getManaCost()) {
                this.abilityUse(itemStack, level, player, hand);
                return InteractionResultHolder.consume(itemStack);
            } else {
                player.stopUsingItem();
                return InteractionResultHolder.fail(itemStack);
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    protected void addCooldownManaCost(Player player) {
        PlayerData playerData = PlayerData.getInstance(player);
        try {
            playerData.addCooldown(this.getId(), this.getCooldown());
            if (!player.isCreative()) {
                playerData.removeMana(this.getManaCost());
            }
        } catch (Exception e) {
            BeyondHorizon.LOGGER.warn("Player data is null! returning!!");
        }
    }

    public float getDuration(int duration) {
        float ticks = (float) duration / 20.0F;
        ticks = (ticks * ticks + ticks * 2.0F) / 3.0F;
        if (ticks > 1.0F) {
            ticks = 1.0F;
        }
        return ticks;
    }
    //
    protected void hurtEntities(LivingEntity user, float radius, float damage, float knockbackAmount, boolean noKnockback, DamageTypes damageTypes) {
        AABB aabb = user.getBoundingBox().inflate(radius, radius, radius);
        for (LivingEntity target : user.level().getEntitiesOfClass(LivingEntity.class, aabb)) {
            float reach = user.getBbWidth() + target.getBbWidth();
            if (target.isAlive() && !target.isInvulnerable() && reach < 3) {
                if (target == user) continue;
                if (this.dealDamage(target, user, damage, noKnockback, damageTypes)) {
                    if (!noKnockback) {
                        knockbackTarget(user, target, knockbackAmount, user.getX() - target.getX(), user.getZ() - target.getZ(), noKnockback);
                    }
                }
            }
        }
    }
    protected void sweepHurtEntities(LivingEntity target, LivingEntity user, float radius, float damage, boolean noKnockback, DamageTypes damageTypes) {
        for (LivingEntity livingentity : user.level().getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(1.0D + radius, 0.25D, 1.0D + radius))) {
            var reach = user.getAttributeValue(ForgeMod.ENTITY_REACH.get());
            if (livingentity != user && livingentity != target && !user.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStand) || !((ArmorStand)livingentity).isMarker()) && user.distanceToSqr(livingentity) < reach * reach) {
                if (!noKnockback) {
                    livingentity.knockback((double) 0.4F, (double) Mth.sin(user.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(user.getYRot() * ((float)Math.PI / 180F))));
                }
                this.dealDamage(target, user, damage, noKnockback, damageTypes);
            }
        }
    }

    protected void stab(LivingEntity user, double range, float damage, DamageTypes damageTypes) {
        Vec3 srcVec = user.getEyePosition();
        Vec3 lookVec = user.getViewVector(1.0F);
        Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
        float var9 = 1.0F;
        List<Entity> possibleList = user.level().getEntities(user, user.getBoundingBox().expandTowards(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range).inflate(var9, var9, var9));
        boolean flag = false;
        for (Entity entity : possibleList) {
            if (entity instanceof LivingEntity livingEntity) {
                float borderSize = 0.5F;
                AABB collisionBB = entity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
                Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);
                if (collisionBB.contains(srcVec)) {
                    flag =true;
                } else if (interceptPos.isPresent()) {
                    flag =true;
                }
                if (flag) {
                    boolean flag1 = this.dealDamage(livingEntity, user, damage, damageTypes);
                    if (flag1) {
                        entity.invulnerableTime = - 20;
                        int j = EnchantmentHelper.getFireAspect(user);
                        if (j > 0 && !entity.isOnFire()) {
                            entity.setSecondsOnFire(j * 4);
                        }
                    }
                }
            }
        }
    }

    protected void knockbackTarget(LivingEntity user, LivingEntity target, double strength, double x, double z, boolean ignoreResistance) {
        if (!ignoreResistance) {
            strength *= 1.0D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        }
        if (!(strength <= 0.0D)) {
            user.hasImpulse = true;
            Vec3 vec3 = user.getDeltaMovement();
            Vec3 vec31 = (new Vec3(x, 0.0D, z)).normalize().scale(strength);
            target.setDeltaMovement(vec3.x / 2.0D - vec31.x, user.onGround() ? Math.min(0.4D, vec3.y / 2.0D + strength) : vec3.y, vec3.z / 2.0D - vec31.z);
        }
    }
     //
    @Override
    public Optional<IAttack> attack() {
        return Optional.of(this);
    }

    @Override
    public Optional<IEntityProperties> entityProperties() {
        return Optional.of(this);
    }

    @Override
    public Optional<IItemProperties> itemProperties() {
        return Optional.of(this);
    }

    public float getMagnitude() {
        return magnitude;
    }

    public float getLevel() {
        return level;
    }
    
    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }
    
    public void setLevel(float level) {
        this.level = level;
    }

    public abstract int getCooldown();

    public abstract int getManaCost();

    public void abilityUse(ItemStack itemStack, Level level, Player user, InteractionHand hand) {

    }


    protected float additionalDamage(Player player, ItemStack itemStack) {
        return 0;
    }

    protected double getScaleBonusAttribute(Player player, Attribute attribute, float scaleDamage) {
        return this.getScaleAttribute(player, attribute, scaleDamage, true);
    }
    protected double getScaleTotalAttribute(Player player, Attribute attribute, float scaleDamage) {
        return this.getScaleAttribute(player, attribute, scaleDamage, false);
    }
    private double getScaleAttribute(Player player, Attribute attribute, float scaleDamage, boolean getBonus) {
        return getBonus ? AttributeUtils.getBonus(player, attribute) * scaleDamage : player.getAttributeValue(attribute) * scaleDamage;
    }
}
