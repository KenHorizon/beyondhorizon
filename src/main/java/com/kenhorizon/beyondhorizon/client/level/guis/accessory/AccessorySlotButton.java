package com.kenhorizon.beyondhorizon.client.level.guis.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundAccessoryInventoryPacket;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundInventoryPacket;
import com.kenhorizon.beyondhorizon.client.level.tooltips.Tooltips;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AccessorySlotButton extends Button {
    private final Screen parentGui;
    protected static final ResourceLocation BUTTON = BeyondHorizon.resourceGui("button/accessory.png");
    protected static final ResourceLocation CANCEL = BeyondHorizon.resourceGui("button/accessory.png");

    public AccessorySlotButton(Screen parentGui, int x, int y) {
        super(x, y, 16, 16, CommonComponents.EMPTY, pButton -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                ItemStack stack = mc.player.containerMenu.getCarried();
                mc.player.containerMenu.setCarried(ItemStack.EMPTY);
                if (parentGui instanceof AccessorySlotScreen) {
                    InventoryScreen inventory = new InventoryScreen(mc.player);
                    mc.setScreen(inventory);
                    mc.player.containerMenu.setCarried(stack);
                    NetworkHandler.sendToServer(new ClientboundInventoryPacket(stack));
                } else {
                    if (parentGui instanceof InventoryScreen inventoryScreen) {
                        RecipeBookComponent recipeBookGui = inventoryScreen.getRecipeBookComponent();

                        if (recipeBookGui.isVisible()) {
                            recipeBookGui.toggleVisibility();
                        }
                    }
                    NetworkHandler.sendToServer(new ClientboundAccessoryInventoryPacket(stack));
                }
            }
        }, DEFAULT_NARRATION);
        this.parentGui = parentGui;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        int offset = 0;
        if (isHovered()) {
            offset = 16;
        }
        if (this.parentGui instanceof InventoryScreen inventoryScreen) {
            RecipeBookComponent recipeBookComponent = inventoryScreen.getRecipeBookComponent();
            this.setX(((InventoryScreen) parentGui).getGuiLeft() + 58);
            if (mouseX >= getX() && mouseX <= getX() + 16 && mouseY >= getY() && mouseY <= getY() + 16) {
                List<Component> text = new ArrayList<>();
                text.add(Component.translatable(Tooltips.TOOLTIP_ACCESSORY));
                graphics.renderComponentTooltip(mc.font, text, mouseX, mouseY);
            }
            graphics.blit(BUTTON, this.getX(), this.getY(), 0, offset, 16, 16, 16, 32);
        }
        if (this.parentGui instanceof AccessorySlotScreen) {
            if (mouseX >= getX() && mouseX <= getX() + 16 && mouseY >= getY() && mouseY <= getY() + 16) {
                List<Component> text = new ArrayList<>();
                text.add(Component.translatable(Tooltips.TOOLTIP_INVENTORY));
                graphics.renderComponentTooltip(mc.font, text, mouseX, mouseY);
            }
            graphics.blit(CANCEL, this.getX(), this.getY(), 0, offset, 16, 16, 16, 32);

        }
        if (this.parentGui instanceof CreativeModeInventoryScreen creativInventory) {
            if (mouseX >= getX() && mouseX <= getX() + 16 && mouseY >= getY() && mouseY <= getY() + 16) {
                List<Component> text = new ArrayList<>();
                text.add(Component.translatable(Tooltips.TOOLTIP_ACCESSORY));
                graphics.renderComponentTooltip(mc.font, text, mouseX, mouseY);
            }
            boolean isOnInventoryTab = creativInventory.isInventoryOpen();
            this.active = isOnInventoryTab;
            if (!isOnInventoryTab) {
                return;
            }
            graphics.blit(BUTTON, this.getX(), this.getY(), 0, offset, 16, 16, 16, 32);
        }
    }
}