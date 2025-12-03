package com.kenhorizon.beyondhorizon.client.level.tooltips;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings({"deprecation", "removal"})
public class IconAttributesTooltip implements ClientTooltipComponent, TooltipComponent {
    protected ResourceLocation texture;
    protected FormattedText text;

    public IconAttributesTooltip(FormattedText text, ResourceLocation texture) {
        this.text = text;
        this.texture = texture;
    }

    public int getRenderHeight() {
        return 22;
    }

    public int getRenderWidth() {
        return 22;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getWidth(Font font) {
        return getRenderWidth();
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        guiGraphics.flush();
        float scale = 0.50F;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        poseStack.scale(scale, scale, scale);
        poseStack.translate(x, y, 0);
        guiGraphics.blit(this.texture, x, y, 0, 0, 20, 20, 20, 20);
        poseStack.popPose();
    }

    public static void registerFactory() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(IconAttributesTooltip::onRegisterTooltipEvent);
    }

    private static void onRegisterTooltipEvent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(IconAttributesTooltip.class, x -> x);
    }
}
