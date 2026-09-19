package com.kenhorizon.beyondhorizon.client.render.misc.tooltips.items;

import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.joml.Matrix4f;

@SuppressWarnings({"deprecation", "removal"})
public class ItemStackNameRarity implements ClientTooltipComponent, TooltipComponent {
    private final ItemStack stack;

    public ItemStackNameRarity(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public int getWidth(Font font) {
        return font.width(this.getStack().getDisplayName());
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource buffer) {
        TextColor result = this.getStack().getDisplayName().getStyle().getColor();
        int color1 = convert(255, result.getValue(), 1.0F);
        int color2 = convert(255, result.getValue(), 0.40F);
        BlitHelper.drawBorderedStrings(font, this.getStack().getHoverName(), x, y, color2, color1, matrix4f, buffer);
    }
    public int convert(int opacity, int color, float value) {
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = (color >> 0) & 255;
        float[] hsv = Colors.ARGBtoAHSV(opacity, r, g, b);
        return Colors.HSVtoRGB(hsv[1], hsv[2], value);
    }
    public static void registerFactory() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemStackNameRarity::onRegisterTooltipEvent);
    }
    private static void onRegisterTooltipEvent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ItemStackNameRarity.class, x -> x);
    }
}
