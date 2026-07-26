package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.level.IAbilityInfo;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.api.IAttack;
import com.kenhorizon.beyondhorizon.server.api.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.init.BHChatformatting;
import com.kenhorizon.beyondhorizon.server.item.ItemAbilityType;
import com.kenhorizon.beyondhorizon.server.item.ManaCostType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public abstract class AccessoryActiveSkill extends Accessory implements IEntityProperties, IAttack, IAccessoryEvent, IAbilityInfo {
    protected ManaCostType manaCostType;
    protected boolean active;
    protected Map<Integer, Boolean> activeSlots = new HashMap<>();
    private double manaCost;
    private int castTime;
    private int maxCastTime;
    private int cooldown;

    public AccessoryActiveSkill(ManaCostType manaCostType, float magnitude, int level) {
        super(ItemAbilityType.ACTIVE, magnitude, level);
        this.manaCostType = manaCostType;
    }

    public AccessoryActiveSkill() {
        this(ManaCostType.DEFAULT);
    }

    public AccessoryActiveSkill(ManaCostType manaCostType) {
        super(ItemAbilityType.ACTIVE,0, 1);
        this.manaCostType = manaCostType;
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
    public Optional<IEntityProperties> entityProperties() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAttack> attack() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAccessoryEvent> accessory() {
        return Optional.of(this);
    }

    @Override
    protected void addTooltipDescriptionHeader(ItemStack itemStack, List<Component> tooltip) {
        Player player = BeyondHorizon.PROXY.clientPlayer();
        PlayerData playerData = Capabilities.data(player);
        if (player != null) {
            IAccessoryStackHandler handler = Capabilities.accessory(player);
            if (handler != null) {
                var stacks = handler.getStacks();
                if (stacks.contains(itemStack)) {
                    tooltip.add(this.addKeyBinds(stacks.whatSlots(itemStack)));
                }
            }
        }

        String tooltips;
        MutableComponent manaText = Component.literal("- ");
        MutableComponent cdText = Component.literal("- ");
        if (this.getManaCostType() == ManaCostType.PER_SECONDS) {
            tooltips = Tooltips.MANA_COST_PER_SECOND;
        } else if (this.getManaCostType() == ManaCostType.PERCENTAGE) {
            tooltips = Tooltips.MANA_COST_PERCENTAGES;
        } else {
            tooltips = Tooltips.MANA_COST;
        }
        tooltip.add(manaText.append(Component.translatable(tooltips, this.getManaCost()).withStyle(BHChatformatting.MANA)));
        if (this.getCooldown() > 0) {
            tooltip.add(cdText.append(Component.translatable(Tooltips.COOLDOWN, (int) (this.getCooldown() / 20)).withStyle(BHChatformatting.COOLDOWN)));
        }
    }

    public void setActive(boolean active, int slot) {
        this.active = active;
        this.activeSlots.put(slot, active);
    }

    public boolean isActive() {
        return active;
    }

    public void toggleActive(int slot) {
        this.setActive(!this.isActive(), slot);
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
    public void onUnequip(Player player, ItemStack itemStack, int slot) {
        this.activeSlots.remove(slot);
    }


    @Override
    public boolean onKeybindPressed(Player player, ItemStack itemStack, int slot) {
        PlayerData playerData = Capabilities.data(player);
        try {
            boolean canUse = playerData.isOnCooldown(this.getId());
            boolean flag = playerData.getMana() <= this.getManaCost();
            if (!flag && !playerData.isOnCooldown(this.getId())) {
                this.addCooldownManaCost(player);
                this.onActiveAbility(player, itemStack);
                this.toggleActive(slot);
            } else if (playerData.isOnCooldown(this.getId())) {
                player.displayClientMessage(Component.translatable(Tooltips.ON_COOLDOWN)
                        .append(CommonComponents.space())
                        .append(Component.translatable(Tooltips.COOLDOWN, (int)((this.getCooldown() / 20) * playerData.getCooldownPercent(this.getId())))).withStyle(ChatFormatting.RED), true);
            } else {
                player.displayClientMessage(Component.translatable(Tooltips.NOT_ENOUGH_MANA).withStyle(ChatFormatting.RED), true);
            }
        } catch (Exception e) {
            BeyondHorizon.LOGGER.warn("" + e);
        }
        return false;
    }

    protected void playerManaCost(PlayerData data, Player player) {
        if (this.getManaCostType() == ManaCostType.PERCENTAGE) {
            data.removeMana((int) (data.getMaxMana() / (Math.min(1.0F, this.getManaCost() / 100.0F))));
        } else {
            data.removeMana(this.getManaCost());
        }
    }


    protected void addCooldownManaCost(Player player) {
        PlayerData playerData = Capabilities.data(player);
        try {
            playerData.addCooldown(this.getId(), this.getCooldown());
            if (!player.isCreative()) {
                this.playerManaCost(playerData, player);
            }
        } catch (Exception e) {
            BeyondHorizon.LOGGER.warn("Player data is null! returning!!");
        }
    }


    public ManaCostType getManaCostType() {
        return manaCostType;
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        this.activeSlots.forEach((slots, active) -> {
            if (entity instanceof Player player) {
                PlayerData data = Capabilities.data(player);
                if (data != null && active && this.getManaCostType() == ManaCostType.PER_SECONDS) {
                    if (player.tickCount % 10 == 0) {
                        data.removeMana(this.getManaCost(), true);
                    }
                }
                this.onDurationAbility(player, itemStack, active);
            }
        });
    }

    public boolean manaNotEnough(Player player) {
        PlayerData playerData = Capabilities.data(player);
        try {
            return playerData.getMana() <= this.manaCost;
        } catch (Exception e) {
            BeyondHorizon.LOGGER.warn("Player Data is null");
//            throw new RuntimeException(e);
        }
        return false;
    }

    public void onActiveAbility(Player player, ItemStack itemStack) {

    }

    public void onDurationAbility(Player player, ItemStack itemStac, boolean active) {

    }
}
