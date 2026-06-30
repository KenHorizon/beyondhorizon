package com.kenhorizon.beyondhorizon.client.render.item;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryActiveSkill;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;

public class AccessoryItemDecorations<T extends Item> implements IItemDecorator {
    private final IAccessoryItem items;
    private static final ResourceLocation TEXTURE = BeyondHorizon.resourceGui("sprites/accessory_active_slot.png");
    public AccessoryItemDecorations(IAccessoryItem items) {
        this.items = items;
    }

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        for (Accessory accessory : items.getAccessories()) {
            if (accessory instanceof AccessoryActiveSkill) {
                guiGraphics.blit(TEXTURE, xOffset, yOffset, 18, 18, 0, 0, 18, 18);
            }
        }
        return true;
    }
}
