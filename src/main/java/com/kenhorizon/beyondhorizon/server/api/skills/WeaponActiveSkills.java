package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.api.data.IItemProperties;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.IAttack;
import com.kenhorizon.beyondhorizon.server.api.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.api.level.IAbilityInfo;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHChatformatting;
import com.kenhorizon.beyondhorizon.server.item.ItemAbilityType;
import com.kenhorizon.beyondhorizon.server.item.ManaCostType;
import com.kenhorizon.beyondhorizon.server.level.utils.AttributeUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

public abstract class WeaponActiveSkills extends Skill implements IAttack, IAbilityInfo, IEntityProperties, IItemProperties {
    private float magnitude;
    private float level;
    private double manaCost;
    private int castTime;
    private int maxCastTime;
    private int cooldown;
    protected ManaCostType manaCostType = ManaCostType.DEFAULT;

    public WeaponActiveSkills() {
        super(ItemAbilityType.ACTIVE);
        this.isSkill = true;
    }

    public ManaCostType getManaCostType() {
        return this.manaCostType;
    }

    @Override
    public void setManaCost(double manaCost) {
        this.manaCost = manaCost;
    }

    @Override
    public void setCastTime(int castTime) {
        if (castTime >= this.getMaxCastTime()) {
            castTime = this.getMaxCastTime();
        }
        this.castTime = castTime;
    }

    @Override
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public int getCastTime() {
        return this.castTime;
    }

    @Override
    public int getCooldown() {
        return this.cooldown;
    }

    @Override
    public double getManaCost() {
        return this.manaCost;
    }

    @Override
    public int getMaxCastTime() {
        return this.maxCastTime;
    }

    @Override
    public void setMaxCastTime(int maxCastTime) {
        this.maxCastTime = maxCastTime;
    }

    @Override
    protected void addTooltipDescriptionHeader(ItemStack itemStack, List<Component> tooltip) {
        String tooltips;
        Minecraft mc = Minecraft.getInstance();
        PlayerData playerData = Capabilities.data(mc.player);
        MutableComponent manaText = Component.literal("- ");
        MutableComponent cdText = Component.literal("- ");
        if (this.getManaCostType() == ManaCostType.PERCENTAGE) {
            tooltips = Tooltips.MANA_COST_PERCENTAGES;
        } else {
            tooltips = Tooltips.MANA_COST;
        }
        tooltip.add(manaText.append(Component.translatable(tooltips, this.getManaCost()).withStyle(BHChatformatting.MANA)));
        if (this.getCooldown() > 0) {
            tooltip.add(cdText.append(Component.translatable(Tooltips.COOLDOWN, (int) (this.getCooldown() / 20.0F)).withStyle(BHChatformatting.COOLDOWN)));
        }
    }

    @Override
    protected MutableComponent addTooltipTitle() {
        Minecraft mc = Minecraft.getInstance();
        PlayerData playerData = Capabilities.data(mc.player);
        MutableComponent active = this.spacing().append(Component.literal("[A]").withStyle(Tooltips.TOOLTIP[1]).append(this.spacing()));
        if (playerData != null && !this.isPassive()) {
            float factor = playerData.getCooldownPercent(this.getId());
            if (factor > 0.0F) {
                return active.append(Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.GRAY)
                        .append(CommonComponents.SPACE)
                                .append(Component.translatable(Tooltips.COOLDOWN_IN_NAME, (int) ((int) (this.getCooldown() / 20.0F) * factor)).withStyle(ChatFormatting.GOLD)));
            }
        }
        return super.addTooltipTitle();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(ItemStack itemStack, Level level, Player player, InteractionHand hand) {
        PlayerData playerData = Capabilities.data(player);
        if (!level.isClientSide()) {
            if (playerData.isOnCooldown(this.getId())) {
                player.displayClientMessage(Component.translatable(Tooltips.ON_COOLDOWN).withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(itemStack);
            }
            if (playerData.getMana() <= this.getManaCost()) {
                player.displayClientMessage(Component.translatable(Tooltips.NOT_ENOUGH_MANA).withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(itemStack);
            } else if (playerData.getMana() >= this.getManaCost()) {
                if (this.getMaxCastTime() > 0) {
                    this.setCastTime(this.getCastTime() + 1);
                }
                this.abilityUse(itemStack, level, player, hand);
                return InteractionResultHolder.consume(itemStack);
            } else {
                this.setCastTime(0);
                player.stopUsingItem();
                return InteractionResultHolder.fail(itemStack);
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    protected void addCooldownManaCost(Player player) {
        PlayerData playerData = Capabilities.data(player);
        try {
            playerData.addCooldown(this.getId(), this.getCooldown());
            if (!player.isCreative()) {
                playerData.removeMana(this.getManaCost());
            }
            this.setCastTime(0);
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

    public void abilityUse(ItemStack itemStack, Level level, Player user, InteractionHand hand) {

    }

    protected float additionalDamage(Player player, ItemStack itemStack) {
        return 0;
    }

    protected double getScaleBonus(Player player, Attribute attribute, float scaleDamage) {
        return AttributeUtils.getBonus(player, attribute) * scaleDamage;
    }

    protected double getScaleTotal(Player player, Attribute attribute, float scaleDamage) {
        return AttributeUtils.getValue(player, attribute) * scaleDamage;
    }
}
