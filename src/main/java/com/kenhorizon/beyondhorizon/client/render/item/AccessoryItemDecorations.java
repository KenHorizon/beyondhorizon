package com.kenhorizon.beyondhorizon.client.render.item;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryActiveSkill;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryHelper;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;

public class AccessoryItemDecorations implements IItemDecorator {
    private final Item items;
    private final IAccessoryItem accessoryItem;
    private static final ResourceLocation TEXTURE_ACTIVE = BeyondHorizon.resourceGui("sprites/accessory_active_slot.png");
    private static final ResourceLocation TEXTURE_LOCKED = BeyondHorizon.resourceGui("sprites/item_locked.png");
    public AccessoryItemDecorations(Item items, IAccessoryItem accessoryItem) {
        this.items = items;
        this.accessoryItem = accessoryItem;
    }

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        Player player = BeyondHorizon.PROXY.clientPlayer();
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, 0, 255);
        for (Accessory accessory : this.accessoryItem.getAccessories()) {
            if (accessory instanceof AccessoryActiveSkill) {
                guiGraphics.blit(TEXTURE_ACTIVE, xOffset, yOffset, 0, 0, 16, 16, 16, 16);
            }
        }
        AccessoryHelper.getInventory(player).ifPresent(handler -> {
            if (!AccessoryHelper.isValid(stack, handler)) {
                guiGraphics.blit(TEXTURE_LOCKED, xOffset, yOffset, 0, 0, 18, 18, 18, 18);
            }
        });
        poseStack.popPose();
        return true;
    }
}
