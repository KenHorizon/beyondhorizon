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

public class AbilityHud implements IGuiOverlay {
    protected final HudInfo hud = new HudInfo();

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = gui.getMinecraft();
        Font font = mc.font;
        this.hud.update();
        if (!this.hud.isHeldingSkillItems && (gui.getMinecraft().options.hideGui || !gui.shouldDrawSurvivalElements())) return;
        gui.setupOverlayRenderState(true, false);
        mc.getProfiler().push("ability_hud");
        int x = screenWidth / 2;
        int y = screenHeight - (gui.leftHeight + 21);
        float casttimeFactor = ((float) this.hud.casttime / this.hud.maxcasttime);
        String casttime = String.format("%.0f%%", 100.0F * ((float) this.hud.casttime / this.hud.maxcasttime));
        int castTimeW = font.width(casttime);
        if (casttimeFactor > 0.0F) {
            BlitHelper.drawBlit(guiGraphics, HudSprites.CAST_TIME_BACKGROUND, x, y, 0,0, 79, 4,79, 4);
            BlitHelper.drawBlit(guiGraphics, HudSprites.CAST_TIME, x, y, 0,0, (int) (79 * casttimeFactor), 4,79, 4);
            BlitHelper.drawBlit(guiGraphics, HudSprites.CAST_TIME_OVERLAY, x - 22, y - 6, 0,0,124, 16,124, 16);
            BlitHelper.drawBorderedStrings(font, guiGraphics, casttime,x + (14 + castTimeW / 2), y - (font.lineHeight + 3), Colors.combineRGB(0, 148, 255));
        }
        if (!this.hud.selectedAbility.isEmpty()) {
            String skills = String.format("%s", this.hud.selectedAbility);
            BlitHelper.drawBorderedStrings(font, guiGraphics, skills,20, (this.hud.scaledWindowHeight - 20));
        }
        mc.getProfiler().pop();
    }
}
