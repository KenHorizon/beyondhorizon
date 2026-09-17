package com.kenhorizon.beyondhorizon.client.render.guis.hud.overlay;

import com.kenhorizon.beyondhorizon.client.render.guis.hud.HudSprites;
import com.kenhorizon.beyondhorizon.client.render.guis.sprites.IconSmallSprites;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class HealthHud extends HudOverlay {
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = gui.getMinecraft();
        Font font = mc.font;
        this.hud.update();
        if (gui.getMinecraft().options.hideGui || !gui.shouldDrawSurvivalElements()) return;
        gui.setupOverlayRenderState(true, false);
        mc.getProfiler().push("beyondhorizon:custom_hud_health");
        int x = this.hud.scaledWindowWidth / 2 - 91;
        int y = this.hud.scaledWindowHeight - gui.leftHeight;
        if (this.hud.hasAbsroption) {
            String absorption = String.format("%.0f", this.hud.absorption);
            int abX = x + (5 + 5) - (24 + absorption.length());
            BlitHelper.drawIcons(guiGraphics, IconSmallSprites.ABSROPTION, abX - (5 + 9), y -1);
            BlitHelper.drawStrings(font, guiGraphics, absorption, abX, y, Colors.WHITE, true);
        }
        this.setProgress(Mth.clamp((float) (this.hud.health / this.hud.maxHealth), 0.0F, 1.0F));
        String health = String.format("%.0f/%.0f", this.hud.maxHealth * this.getProgress(), this.hud.maxHealth);
        BlitHelper.drawIcons(guiGraphics, IconSmallSprites.HEART, x, y - 1);
        BlitHelper.drawBorderedStrings(font, guiGraphics, health,x + (5 + 9), y, Colors.GREEN);
//        BlitHelper.drawBlit(guiGraphics, HudSprites.HUD_BAR, x, y - 5, 0, 0, 124, 16, 124, 16);
//        BlitHelper.drawBlit(guiGraphics, HudSprites.HUD_FILL, x + 4, y - 5, 0, 0, (int) (116 * getProgress()), 16, 116, 16, Colors.RED);
        mc.getProfiler().pop();
    }
}
