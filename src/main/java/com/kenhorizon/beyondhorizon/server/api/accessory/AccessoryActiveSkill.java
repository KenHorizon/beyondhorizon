package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.api.IAttack;
import com.kenhorizon.beyondhorizon.server.api.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityData;
import com.kenhorizon.beyondhorizon.server.item.ItemAbilityType;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

public abstract class AccessoryActiveSkill extends Accessory implements IEntityProperties, IAttack, IAccessoryEvent {
    public enum ManaCostType {
        DEFAULT,
        PER_SECOND,
        PERCENTAGE
    }
    protected ManaCostType manaCostType;
    protected boolean active;
    protected Map<Integer, Boolean> activeSlots = new HashMap<>();

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
    public Optional<IEntityProperties> IEntityProperties() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAttack> IAttackCallback() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAccessoryEvent> IAccessory() {
        return Optional.of(this);
    }

    @Override
    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        PlayerData playerData = Capabilities.data(player);
        if (player != null) {
            IAccessoryStackHandler handler = Capabilities.accessory(player);
            if (handler != null) {
                var stacks = handler.getStacks();
                if (stacks.contains(itemStack)) {
                    list.add(this.addKeyBinds(stacks.whatSlots(itemStack)));
                }
            }
        }

        String tooltips;
        if (this.getManaCostType() == ManaCostType.PER_SECOND) {
            tooltips = Tooltips.TOOLTIP_MANA_COST_PER_SECOND;
        } else if (this.getManaCostType() == ManaCostType.PERCENTAGE) {
            tooltips = Tooltips.TOOLTIP_MANA_COST_PERCENTAGES;
        } else {
            tooltips = Tooltips.TOOLTIP_MANA_COST;
        }
        list.add(Component.translatable(tooltips, this.getManaCost()).withStyle(ChatFormatting.UNDERLINE));
        if (this.getCooldown() > 0) {
            list.add(Component.translatable(Tooltips.TOOLTIP_COOLDOWN, (int) (this.getCooldown() / 20)).withStyle(ChatFormatting.UNDERLINE));
        }
        list.add(Component.translatable(this.createId(0), Maths.format0(this.getMagnitude())));
        return list;
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

    protected double getManaCost() {
        return  0;
    }

    protected int getCooldown() {
        return 0;
    }

    protected int getCastTime() {
        return 0;
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
                player.displayClientMessage(Component.translatable(Tooltips.TOOLTIP_ON_COOLDOWN)
                        .append(CommonComponents.space())
                        .append(Component.translatable(Tooltips.TOOLTIP_COOLDOWN, (int)((this.getCooldown() / 20) * playerData.getCooldownPercent(this.getId())))).withStyle(ChatFormatting.RED), true);
            } else {
                player.displayClientMessage(Component.translatable(Tooltips.TOOLTIP_NOT_ENOUGH_MANA).withStyle(ChatFormatting.RED), true);
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
                if (data != null && active && this.getManaCostType() == ManaCostType.PER_SECOND) {
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
