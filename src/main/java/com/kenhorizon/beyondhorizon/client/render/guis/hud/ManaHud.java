package com.kenhorizon.beyondhorizon.client.render.guis.hud;

import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ManaHud implements IGuiOverlay {
    private final HudInfo hud = new HudInfo();

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (gui.getMinecraft().options.hideGui || !gui.shouldDrawSurvivalElements()) return;
        gui.setupOverlayRenderState(true, false);
        gui.getMinecraft().getProfiler().push("player_mana");
        this.hud.update();
        int x = screenWidth / 2 - 91;
        int y = screenHeight - (gui.leftHeight + 21);
        String health = String.format("%.0f/%.0f", this.hud.mana, this.hud.maxMana);
        BlitHelper.draw(guiGraphics, HudSprites.MANA, x, y - 1, 9.0F, 9, 9, 9, 9);
        BlitHelper.drawStrings(guiGraphics, health,x + (5 + 9), y, ColorUtil.combineRGB(0, 148, 255), true);
        gui.getMinecraft().getProfiler().pop();
    }
}
