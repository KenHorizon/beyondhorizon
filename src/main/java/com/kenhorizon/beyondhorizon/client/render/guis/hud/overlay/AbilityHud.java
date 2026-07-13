package com.kenhorizon.beyondhorizon.client.render.guis.hud.overlay;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.guis.hud.HudInfo;
import com.kenhorizon.beyondhorizon.client.render.guis.hud.HudSprites;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class AbilityHud extends HudOverlay {
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = gui.getMinecraft();
        Font font = mc.font;
        this.hud.update();
        if (gui.getMinecraft().options.hideGui) return;
        gui.setupOverlayRenderState(true, false);
        mc.getProfiler().push("ability_hud");
        float casttimeFactor = ((float) this.hud.casttime / this.hud.maxcasttime);
        String casttime = String.format("%.0f%%", 100.0F * ((float) this.hud.casttime / this.hud.maxcasttime));
        int castTimeW = font.width(casttime);
        if (casttimeFactor > 0.0F) {
            int castTimeX = (screenWidth - 79) / 2;
            int castTimeY = screenHeight - (gui.leftHeight - 12);
            BlitHelper.drawBlit(guiGraphics, HudSprites.CAST_TIME_BACKGROUND, castTimeX, castTimeY, 0,0, 79, 4,79, 4);
            BlitHelper.drawBlit(guiGraphics, HudSprites.CAST_TIME, castTimeX, castTimeY, 0,0, (int) (79 * casttimeFactor), 4,79, 4);
            BlitHelper.drawBlit(guiGraphics, HudSprites.CAST_TIME_OVERLAY, castTimeX - 22, castTimeY - 6, 0,0,124, 16,124, 16);
            BlitHelper.drawBorderedStrings(font, guiGraphics, casttime,castTimeX + (16 + castTimeW / 2), castTimeY - (font.lineHeight + 3), Colors.combineRGB(0, 148, 255));
        }
        if (!this.hud.selectedAbility.isEmpty()) {
            String skills = String.format("%s", this.hud.selectedAbility);
            BlitHelper.drawBorderedStrings(font, guiGraphics, skills,20, (this.hud.scaledWindowHeight - 20), Colors.GOLD);
        }
        mc.getProfiler().pop();
    }
}
