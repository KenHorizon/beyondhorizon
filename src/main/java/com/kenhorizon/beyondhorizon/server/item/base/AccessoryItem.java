package com.kenhorizon.beyondhorizon.server.item.base;

import com.google.common.collect.ImmutableList;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryBuilder;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItems;
import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import com.kenhorizon.beyondhorizon.client.level.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryItemGroup;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AccessoryItem extends BasicItem implements IAccessoryItems<AccessoryItem>, IReloadable {
    protected List<Accessory> accessories = ImmutableList.of();
    protected final AccessoryBuilder builder;
    protected AccessoryItemGroup accessoryItemGroup;
    public AccessoryItem(AccessoryItemGroup accessoryItemGroup, Properties properties, AccessoryBuilder builder) {
        super(properties.stacksTo(1));
        this.builder = builder;
        this.accessoryItemGroup = accessoryItemGroup;
        ReloadableHandler.addToReloadList(this);
    }

    public AccessoryItem(Properties properties, AccessoryBuilder builder) {
        this(AccessoryItemGroup.NONE, properties, builder);
    }

    public AccessoryItem(Properties properties) {
        this(AccessoryItemGroup.NONE, properties, AccessoryBuilder.NONE);
    }

    @Override
    public void reload() {
        ImmutableList.Builder<Accessory> builder = this.registerSkillOnItems();
        this.accessories = builder.build();
    }

    private ImmutableList.Builder<Accessory> registerSkillOnItems() {
        ImmutableList.Builder<Accessory> builder = ImmutableList.builder();
        builder.addAll(this.builder.getAccessories());
        return builder;
    }

    public AccessoryItemGroup getItemGroup() {
        return this.accessoryItemGroup;
    }

    public boolean noGroupItem() {
        return this.getItemGroup() == AccessoryItemGroup.NONE;
    }

    @Override
    public AccessoryItem getItem() {
        return this;
    }

    @Override
    public boolean isCompatible(ItemStack inSlot, ItemStack outside) {
        if (checkIsOnNoneCategory(inSlot, outside)) {
            return true;
        }
        return checkCompatible(inSlot, outside);
    }

    protected boolean checkIsOnNoneCategory(ItemStack inSlot, ItemStack outside) {
        return ((AccessoryItem) inSlot.getItem()).noGroupItem() || ((AccessoryItem) outside.getItem()).noGroupItem();
    }

    protected boolean checkCompatible(ItemStack inSlot, ItemStack outside) {
        return ((AccessoryItem) inSlot.getItem()).getItemGroup() != ((AccessoryItem) outside.getItem()).getItemGroup();
    }
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean isSelected) {
        if (entity instanceof Player player) {
            this.accessories.forEach((accessory) -> {
                accessory.IEntityProperties().ifPresent(callback -> {
                    callback.onItemUpdate(itemStack, level, player, slot, isSelected);
                });
            });
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        for (int i = 0; i < this.accessories.size(); i++) {
            Accessory accessory = this.accessories.get(i);
            if (i == 0) {
                if (this.getItemGroup() != AccessoryItemGroup.NONE) {
                    tooltip.add(Component.translatable(Tooltips.TOOLTIP_ACCESSORY).withStyle(ChatFormatting.GOLD).append(CommonComponents.space()).append(Component.translatable(Tooltips.TOOLTIP_ACCESSORY_TYPE).withStyle(ChatFormatting.GRAY)));
                } else {
                    tooltip.add(Component.translatable(Tooltips.TOOLTIP_ACCESSORY).withStyle(ChatFormatting.GOLD));
                }
            }
            if (!accessory.getAttributeModifiers().isEmpty()) {
                accessory.addTooltipAttributes(itemStack, tooltip);
            }
            accessory.addTooltip(itemStack, tooltip, this.accessories.size(), Utils.isShiftPressed(), i == 0);
        }
    }

    @Override
    public boolean has(Accessory skill) {
        return this.accessories.contains(skill);
    }

    @Override
    public List<Accessory> getAccessories() {
        return ImmutableList.copyOf(this.accessories);
    }
}
