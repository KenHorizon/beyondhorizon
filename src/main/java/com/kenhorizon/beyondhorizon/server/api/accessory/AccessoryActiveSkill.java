package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityData;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundAcessoryKeyPacket;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AccessoryActiveSkill extends Accessory implements IEntityProperties, IAttack, IAccessoryEvent {
    public enum ManaCostType {
        DEFAULT,
        PER_SECOND,
        PERCENTAGE
    }
    protected float keypressInterval = 0;
    protected float prevKeypressInterval = 0;
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
    public void onChangePrevAccessorySlot(Player player, ItemStack itemStack) {
        this.prevKeypressInterval = this.keypressInterval;
    }

    @Override
    public void onChangePostAccessorySlot(Player player, ItemStack itemStack) {
        this.keypressInterval = this.prevKeypressInterval;
    }

    @Override
    protected List<MutableComponent> tooltipDescriptionList(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        if (player != null) {
            var playerInv = player.getInventory();
            if (!playerInv.contains(itemStack)) {
                IAccessoryItemHandler handler = Capabilities.accessory(player);
                if (handler != null) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack matchedStack = handler.getStackInSlot(i);
                        if (ItemStack.isSameItem(matchedStack, itemStack)) {
                            list.add(this.addKeyBinds(i));
                        }
                    }
                }
            }

        }
        if (this.getManaCostType() == ManaCostType.PER_SECOND) {
            list.add(Component.translatable(Tooltips.TOOLTIP_MANA_COST_PER_SECOND, this.manaCost));
        } else if (this.getManaCostType() == ManaCostType.PERCENTAGE) {
            list.add(Component.translatable(Tooltips.TOOLTIP_MANA_COST_PERCENTAGES, this.manaCost));
        } else {
            list.add(Component.translatable(Tooltips.TOOLTIP_MANA_COST, this.manaCost));
        }
        list.add(Component.translatable(this.createId(0), MathUtils.format0(this.getMagnitude())));
        return list;
    }

    @Override
    public boolean onKeybindPressed(Player player, ItemStack itemStack, int slot) {
        PlayerData playerData = Capabilities.data(player);
        try {
            boolean flag = playerData.getMana() <= this.manaCost;
            CompoundTag nbt = EntityData.getOrCreateTag(player);
            if (!flag) {
                this.playerManaCost(playerData, player);
                this.onActiveAbility(player, itemStack, slot);
            } else {
                player.displayClientMessage(Component.translatable(Tooltips.TOOLTIP_MANA_NOT_ENOUGH).withStyle(ChatFormatting.RED), true);
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
    public void onItemUpdate(ItemStack itemStack, Level level, LivingEntity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player player) {
            PlayerData data = Capabilities.data(player);
            if (data != null && this.active && this.getManaCostType() == ManaCostType.PER_SECOND) {
                if (player.tickCount % 10 == 0) {
                    data.removeMana(this.manaCost, true);
                }
            }
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
    public abstract void onActiveAbility(Player player, ItemStack itemStack, int slot);
}
