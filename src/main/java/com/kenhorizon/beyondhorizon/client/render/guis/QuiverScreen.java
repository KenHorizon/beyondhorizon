package com.kenhorizon.beyondhorizon.client.render.guis;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.QuiverItemStackHandler;
import com.kenhorizon.beyondhorizon.server.inventory.QuiverMenu;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundQuiverSelectedArrowPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class QuiverScreen extends AbstractContainerScreen<QuiverMenu> {
    public static final ResourceLocation QUIVER = BeyondHorizon.resourceGui("container/quiver.png");
    public QuiverScreen(QuiverMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, Component.empty());
        this.imageHeight = 133;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(QUIVER, x, y, 0, 0, this.imageWidth, this.imageHeight);
        if (menu.getHandler() instanceof QuiverItemStackHandler handler) {
            int selectedSlot = handler.getSelectedSlot();
            graphics.blit(QUIVER, x + 40 + (18 * selectedSlot), y + 9, 176, 0, 24, 24);
        }
    }
// 40 - 58 - 76 - 94 - 112
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (menu.getHandler() instanceof QuiverItemStackHandler handler) {
            int slots = (int) (handler.getSelectedSlot() - delta);
            if (slots >= handler.getSlots()) {
                slots = 0;
            }
            if (slots < 0) {
                slots = handler.getSlots() - 1;
            }
            handler.setSelectedSlot(slots);
            NetworkHandler.sendToServer(new ServerboundQuiverSelectedArrowPacket(handler.getSelectedSlot()));
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    protected int getSelectedRows(QuiverItemStackHandler handler) {
        int max = handler.getSlots();
        int totalOccupiedSlots = handler.getTotalOccupiedSlots();
        BeyondHorizon.LOGGER.debug("{} : {}", max, totalOccupiedSlots);
        return (totalOccupiedSlots + max - 1) / max - 1;
    }
}
