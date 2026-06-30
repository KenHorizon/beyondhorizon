package com.kenhorizon.beyondhorizon.server.item.base;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.api.accessory.*;
import com.kenhorizon.beyondhorizon.server.capability.AccessoryItemCap;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AccessoryItem extends BasicItem implements IAccessoryItem, IReloadable {
    protected List<Accessory> accessories = ImmutableList.of();
    protected final AccessoryBuilder builder;
    protected AccessoryItemGroup accessoryItemGroup;

    private final Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
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
        ImmutableList.Builder<Accessory> builder = this.build();
        this.accessories = builder.build();
    }

    private ImmutableList.Builder<Accessory> build() {
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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.modifiers;
    }

    @Override
    public boolean isCompatible(ItemStack inSlot, ItemStack outside) {
        if (checkIsOnNoneCategory(inSlot, outside)) {
            return true;
        }
        return checkCompatible(inSlot, outside);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        return IAccessoryItem.super.getAttributeModifiers(stack);
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
        int size = this.accessories.size();
        for (int i = 0; i < this.accessories.size(); i++) {
            Accessory accessory = this.accessories.get(i);
            if (i == 0) {
                if (this.getItemGroup() != AccessoryItemGroup.NONE) {
                    tooltip.add(Component.translatable(Tooltips.TOOLTIP_ACCESSORY).withStyle(ChatFormatting.GOLD).append(CommonComponents.space()).append(Component.translatable(Tooltips.TOOLTIP_ACCESSORY_TYPE).withStyle(ChatFormatting.GRAY)));
                } else {
                    tooltip.add(Component.translatable(Tooltips.TOOLTIP_ACCESSORY).withStyle(ChatFormatting.GOLD));
                }
            }
            UUID uuid = UUID.nameUUIDFromBytes("accessory".getBytes());
            Multimap<Attribute, AttributeModifier> map = AccessoryHelper.getAttributeModifiers(uuid, itemStack);
            if (!accessory.getAttributeModifiers().isEmpty()) {
                size--;
                accessory.addTooltipAttributes(itemStack, tooltip, map);
            }
            accessory.addTooltip(itemStack, tooltip, size, Utils.isShiftPressed(), i == 0);
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

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        this.accessories.forEach(accessory -> {
            map.putAll(accessory.registerAttributes(uuid, stack));
        });
//        BeyondHorizon.LOGGER.debug("Item is registered! Attribute Added {}", map);
        return map;
    }
}
