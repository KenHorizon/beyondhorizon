package com.kenhorizon.beyondhorizon.client.render.guis.hud.overlay;

import com.kenhorizon.beyondhorizon.client.render.guis.sprites.IconSmallSprites;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class ArmorHud extends HudOverlay {
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = gui.getMinecraft();
        Font font = mc.font;
        this.hud.update();
        if (drawInCreative(gui)) return;
        gui.setupOverlayRenderState(true, false);
        mc.getProfiler().push("beyondhorizon:custom_hud_armor");
        int x = this.hud.scaledWindowWidth / 2 - 91;
        int y = this.hud.scaledWindowHeight - (gui.leftHeight + 11);
        String value = String.format("%.0f", this.hud.armor);
        BlitHelper.drawIcons(guiGraphics, IconSmallSprites.ARMOR, x, y - 1);
        BlitHelper.drawBorderedStrings(font, guiGraphics, value,x + (5 + 9), y, Colors.WHITE);
        mc.getProfiler().pop();
    }
}
