package com.kenhorizon.beyondhorizon.client.render.guis.hud.overlay;

import com.kenhorizon.beyondhorizon.client.render.guis.hud.HudInfo;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public abstract class HudOverlay implements IGuiOverlay {
    protected final HudInfo hud = new HudInfo();
}
