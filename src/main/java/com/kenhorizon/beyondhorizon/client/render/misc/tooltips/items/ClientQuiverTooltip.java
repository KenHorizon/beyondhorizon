package com.kenhorizon.beyondhorizon.client.render.misc.tooltips.items;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.QuiverItemStackHandler;
import com.kenhorizon.beyondhorizon.server.item.tooltips.QuiverTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientQuiverTooltip implements ClientTooltipComponent {
    public static final ResourceLocation TEXTURE_LOCATION = BeyondHorizon.resourceGui("tooltips/quiver.png");
    private final NonNullList<ItemStack> items;

    public ClientQuiverTooltip(QuiverTooltip items) {
        this.items = items.getItems();
    }

    @Override
    public int getHeight() {
        return 39;
    }

    @Override
    public int getWidth(Font font) {
        return 176;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        graphics.blit(TEXTURE_LOCATION, x, y, 0, 0, this.getWidth(font), this.getHeight());
        Player player = BeyondHorizon.PROXY.clientPlayer();
        ItemStack stacks = player.getMainHandItem();
        stacks.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            if (handler instanceof QuiverItemStackHandler quiverItemStackHandler) {
                int index = 0;
                for (int r = 0; r < quiverItemStackHandler.getSlots(); r++) {
                    int itemX = 44 + x + r * 18;
                    int itemY = 13 + y;
                    this.renderItems(itemX, itemY, index++, graphics, font);
                }
                graphics.blit(TEXTURE_LOCATION, 40 + x +  (18 * quiverItemStackHandler.getSelectedSlot()), 9 + y, 176, 0, 24, 24);

            }
        });
    }

    private void renderItems(int x, int y, int index, GuiGraphics graphics, Font font) {
        ItemStack itemstack = this.items.get(index);
        graphics.renderItem(itemstack, x, y, index);
        graphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
    }

    public static void registerFactory() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientQuiverTooltip::onRegisterTooltipEvent);
    }

    private static void onRegisterTooltipEvent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(QuiverTooltip.class, ClientQuiverTooltip::new);
    }
}
