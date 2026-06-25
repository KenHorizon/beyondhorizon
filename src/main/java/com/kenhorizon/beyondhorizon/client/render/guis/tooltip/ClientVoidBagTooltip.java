package com.kenhorizon.beyondhorizon.client.render.guis.tooltip;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.inventory.VoidBagMenu;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientVoidBagTooltip implements ClientTooltipComponent {
    public static final ResourceLocation TEXTURE_LOCATION = BeyondHorizon.resourceGui("container/void_bag_tooltip.png");
    private final NonNullList<ItemStack> items;

    public ClientVoidBagTooltip(VoidBagTooltip items) {
        this.items = items.getItems();
    }

    @Override
    public int getHeight() {
        return 82;
    }

    @Override
    public int getWidth(Font font) {
        return 176;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        graphics.blit(TEXTURE_LOCATION, x, y, 0, 0, this.getWidth(font), this.getHeight());
        int index = 0;
        for (int r = 0; r < VoidBagMenu.ROWS; r++) {
            for (int s = 0; s < VoidBagMenu.SIZE; s++) {
                int itemX = 8 + x + s * 18;
                int itemY = 18 + y + r * 18;
                this.renderItems(itemX, itemY, index++, graphics, font);
            }
        }
    }

    private void renderItems(int x, int y, int index, GuiGraphics graphics, Font font) {
        ItemStack itemstack = this.items.get(index);
        graphics.renderItem(itemstack, x, y, index);
        graphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
    }

    public static void registerFactory() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientVoidBagTooltip::onRegisterTooltipEvent);
    }

    private static void onRegisterTooltipEvent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(VoidBagTooltip.class, ClientVoidBagTooltip::new);
    }
}
