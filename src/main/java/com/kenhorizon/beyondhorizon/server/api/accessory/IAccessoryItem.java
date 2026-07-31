package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.item.base.AccessoryItem;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public interface IAccessoryItem {

    IAccessory defaultInstance = () -> ItemStack.EMPTY;

    boolean has(Accessory skill);

    List<Accessory> getAccessories();

    Multimap<Attribute, AttributeModifier> getAttributeModifiers();

    AccessoryItemGroup getItemGroup();

    /**
     * Check if the itemstack inside accessory slot and outisde are sharing tags
     * */
    default boolean checkIfSharingGroupTogether(ItemStack inSlot, ItemStack outside) {
        if (!(inSlot.getItem() instanceof IAccessoryItem inSlotAccessory)) return false;
        if (!(outside.getItem() instanceof IAccessoryItem outsideSlotAccessory)) return false;
        return inSlotAccessory.getItemGroup() == outsideSlotAccessory.getItemGroup();
    }


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

    default boolean makePiglinsNeutral(Player player) {
        return defaultInstance.makePiglinsNeutral(player);
    }

    default boolean canWalkOnPoweredSnow(Player player) {
        return defaultInstance.canWalkOnPoweredSnow(player);
    }

    default boolean isFreezeImmune(Player player) {
        return defaultInstance.isFreezeImmune(player);
    }

    default boolean isEndermanMask(Player player, EnderMan enderMan) {
        return defaultInstance.isEndermanMask(player, enderMan);
    }
}
