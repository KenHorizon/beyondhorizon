package com.kenhorizon.beyondhorizon.client.render.guis.guide_book;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class GuideBookIndexButton extends Button {

    public GuideBookIndexButton(int x, int y, Component buttonText, Button.OnPress btns) {
        super(x, y, 160, 32, buttonText, btns, DEFAULT_NARRATION);
        this.width = 160;
        this.height = 32;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (this.active) {
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            Font font = (Font) BeyondHorizon.PROXY.getFontRenderer();
            guiGraphics.blit(BeyondHorizon.resourceGui("guide_book/widgets.png"), this.getX(), this.getY(), 0, 0, this.width, this.height);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = Colors.WHITE;
            this.renderString(guiGraphics, font, i | Mth.ceil(this.alpha * 255.0F) << 24);
        }
    }
}