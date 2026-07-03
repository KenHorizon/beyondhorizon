package com.kenhorizon.beyondhorizon.client.render.guis.hud;

import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ArmorHud implements IGuiOverlay {
    private final HudInfo hud = new HudInfo();

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = gui.getMinecraft();
        minecraft.getProfiler().push("armor");
        this.hud.update();
        int x = this.hud.scaledWindowWidth / 2 - 91;
        int y = this.hud.scaledWindowHeight - (39 + 11);
        String value = String.format("%.0f", this.hud.armor);
        BlitHelper.drawBlit(guiGraphics, HudSprites.ARMOR_FULL, x, y - 1, 0, 0, 9, 9, 9, 9);
        BlitHelper.drawBorderedStrings(gui.getMinecraft().font, guiGraphics, value,x + (5 + 9), y, Colors.WHITE);
        minecraft.getProfiler().pop();
    }
}
