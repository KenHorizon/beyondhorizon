package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.api.data.IItemProperties;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.item.ItemAbilityType;
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
    public enum ManaCostType {
        DEFAULT,
        PERCENTAGE
    }
    private float magnitude;
    private float level;
    protected WeaponActiveSkills.ManaCostType manaCostType = ManaCostType.DEFAULT;

    public WeaponActiveSkills() {
        super(ItemAbilityType.ACTIVE);
        this.isSkill = true;
    }

    public WeaponActiveSkills.ManaCostType getManaCostType() {
        return manaCostType;
    }

    @Override
    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        String tooltips;
        if (this.getManaCostType() == WeaponActiveSkills.ManaCostType.PERCENTAGE) {
            tooltips = Tooltips.TOOLTIP_MANA_COST_PERCENTAGES;
        } else {
            tooltips = Tooltips.TOOLTIP_MANA_COST;
        }
        list.add(Component.translatable(tooltips, this.getManaCost()).withStyle(ChatFormatting.UNDERLINE));
        if (this.getCooldown() > 0) {
            list.add(Component.translatable(Tooltips.TOOLTIP_COOLDOWN, Maths.tickToSeconds(this.getCooldown())).withStyle(ChatFormatting.UNDERLINE));
        }
        if (this.appendTooltips(itemStack).isEmpty()) {
            list.add(Component.translatable(createId(), Maths.format(this.getMagnitude())));
        } else {
            list.addAll(this.appendTooltips(itemStack));
        }
        return list;
    }

    protected List<MutableComponent> appendTooltips(ItemStack itemStack) {
        return List.of();
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
