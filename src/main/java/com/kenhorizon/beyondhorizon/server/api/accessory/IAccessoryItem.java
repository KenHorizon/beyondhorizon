package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.item.base.AccessoryItem;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public interface IAccessoryItem {

    IAccessory defaultInstance = () -> ItemStack.EMPTY;

    boolean has(Accessory skill);

    List<Accessory> getAccessories();

    Multimap<Attribute, AttributeModifier> getAttributeModifiers();

    /**
     * Check if the itemstack inside accessory slot and outisde are have basic tags
     * */
    default boolean checkIfBasic(ItemStack inSlot, ItemStack outside) {
        return ((AccessoryItem) inSlot.getItem()).isBasic() == ((AccessoryItem) outside.getItem()).isBasic();
    }

    /**
     * Check if the itemstack inside accessory slot and outisde are sharing tags
     * */
    default boolean checkIfSharingGroupTogether(ItemStack inSlot, ItemStack outside) {
        return ((AccessoryItem) inSlot.getItem()).getItemGroup().equals(((AccessoryItem) outside.getItem()).getItemGroup());
    }
    default boolean checkIfNameLimitation(ItemStack inSlot, ItemStack outside) {
        return ((AccessoryItem) inSlot.getItem()).isNameLimitation() == ((AccessoryItem) outside.getItem()).isNameLimitation();
    }

    AccessoryItemGroup getItemGroup();

    default boolean isBasic() {
        return this.getItemGroup() == AccessoryItemGroup.NONE;
    }

    default boolean isNameLimitation() {
        return this.getItemGroup() == AccessoryItemGroup.UNIQUE;
    }

    default boolean hasCapability(ItemStack stack) {
        return true;
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(UUID uuid, ItemStack stack) {
        return this.getAttributeModifiers(stack);
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        return defaultInstance.getAttributeModifiers(stack);
    }

    default boolean makePiglinsNeutral() {
        return defaultInstance.makePiglinsNeutral();
    }
}
