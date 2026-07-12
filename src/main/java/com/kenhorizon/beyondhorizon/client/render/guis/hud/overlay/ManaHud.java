package com.kenhorizon.beyondhorizon.client.render.guis.hud.overlay;

import com.kenhorizon.beyondhorizon.client.render.guis.hud.HudInfo;
import com.kenhorizon.beyondhorizon.client.render.guis.hud.HudSprites;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ManaHud extends HudOverlay{
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        this.hud.update();
        if (gui.getMinecraft().options.hideGui || !gui.shouldDrawSurvivalElements()) return;
        gui.setupOverlayRenderState(true, false);
        gui.getMinecraft().getProfiler().push("player_mana");
        int x = screenWidth / 2 - 91;
        int y = screenHeight - (gui.leftHeight + 21);
        String value = String.format("%.0f/%.0f", this.hud.mana, this.hud.maxMana);
        BlitHelper.drawIcons(guiGraphics, HudSprites.MANA, x, y - 1);
        BlitHelper.drawBorderedStrings(gui.getMinecraft().font, guiGraphics, value,x + (5 + 9), y, Colors.combineRGB(0, 148, 255));
        gui.getMinecraft().getProfiler().pop();
    }
}
