package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityData;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AccessoryActiveSkill extends Accessory implements IEntityProperties, IAttack, IAccessoryEvent {
    public enum ManaCostType {
        DEFAULT,
        PER_SECOND,
        PERCENTAGE
    }
    protected ManaCostType manaCostType;
    protected boolean active;

    public AccessoryActiveSkill(ManaCostType manaCostType, float magnitude, int level) {
        super(Type.ACTIVE, magnitude, level);
        this.manaCostType = manaCostType;
    }

    public AccessoryActiveSkill() {
        this(ManaCostType.DEFAULT);
    }

    public AccessoryActiveSkill(ManaCostType manaCostType) {
        super(Type.ACTIVE,0, 1);
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
    protected List<MutableComponent> tooltipDescriptionList(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        if (player != null) {
            IAccessoryItemHandler handler = Capabilities.accessory(player);
            if (handler != null) {
                if (handler.contains(itemStack)) {
                    list.add(this.addKeyBinds(handler.whatSlots(itemStack)));
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

    protected int getManaCost() {
        return  0;
    }


    protected int getCooldown() {
        return 0;
    }

    @Override
    public void onChangePrevAccessorySlot(Player player, ItemStack itemStack) {
        this.active = false;
    }


    @Override
    public boolean onKeybindPressed(Player player, ItemStack itemStack, int slot) {
        PlayerData playerData = Capabilities.data(player);
        try {
            boolean canUse = playerData.isOnCooldown(this.getId());
            boolean flag = playerData.getMana() <= this.manaCost;
            CompoundTag nbt = EntityData.getOrCreateTag(player);
            if (!flag) {
                this.playerManaCost(playerData, player);
                this.onActiveAbility(player, itemStack);
                this.active = !this.active;
            } else {
                player.displayClientMessage(Component.translatable(Tooltips.TOOLTIP_NOT_ENOUGH_MANA).withStyle(ChatFormatting.RED), true);
            }
        } catch (Exception e) {
            BeyondHorizon.LOGGER.warn("Player Data is null");
//            throw new RuntimeException(e);
        }
        return false;
    }

    public ManaCostType getManaCostType() {
        return manaCostType;
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (entity instanceof Player player) {
            PlayerData data = Capabilities.data(player);
            if (data != null && this.active && this.getManaCostType() == ManaCostType.PER_SECOND) {
                if (player.tickCount % 10 == 0) {
                    data.removeMana(this.manaCost, true);
                }
            }

            this.onDurationAbility(player, itemStack, this.active);
        }
    }

    protected void playerManaCost(PlayerData data, Player player) {
        if (this.getManaCostType() == ManaCostType.PERCENTAGE) {
            data.removeMana((int) (data.getMaxMana() / (Math.min(1.0F, this.manaCost / 100.0F))));
        } else if (this.getManaCostType() == ManaCostType.PER_SECOND) {
            if (player.tickCount % 20 == 0) {
                data.removeMana(this.manaCost);
            }
        } else {
            data.removeMana(this.manaCost);
        }
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
