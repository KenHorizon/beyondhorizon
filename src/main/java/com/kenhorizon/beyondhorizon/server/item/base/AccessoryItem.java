package com.kenhorizon.beyondhorizon.server.item.base;

import com.google.common.collect.ImmutableList;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.accessory.AccessoryBuilder;
import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import com.kenhorizon.beyondhorizon.server.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.accessory.AccessoryItemGroup;
import com.kenhorizon.beyondhorizon.server.accessory.IAccessoryItems;
import com.kenhorizon.beyondhorizon.server.util.Tooltips;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import net.minecraft.ChatFormatting;
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

    public AccessoryItemGroup getAccessoryItemGroup() {
        return this.accessoryItemGroup;
    }

    public boolean noGroupItem() {
        return this.getAccessoryItemGroup() == AccessoryItemGroup.NONE;
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
        return ((AccessoryItem) inSlot.getItem()).getAccessoryItemGroup() != ((AccessoryItem) outside.getItem()).getAccessoryItemGroup();
    }
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean isSelected) {
        if (entity instanceof Player player) {
            this.accessories.forEach((accessory) -> {
                accessory.IItemGeneric().ifPresent(callback -> {
                    callback.onItemUpdate(itemStack, level, player, slot, isSelected);
                });
            });
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        tooltip.add(Component.translatable(Tooltips.TOOLTIP_ACCESSORY).withStyle(ChatFormatting.GOLD));
        if (!this.accessories.isEmpty()) {
            this.accessories.forEach((accessory) -> {
                accessory.addTooltip(itemStack, tooltip, Utils.isShiftPressed());
            });
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
