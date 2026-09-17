package com.kenhorizon.beyondhorizon.client.render.guis.hud.overlay;

import com.kenhorizon.beyondhorizon.client.render.guis.hud.HudInfo;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public abstract class HudOverlay implements IGuiOverlay {
    private static final long LERP_MILLISECONDS = 100L;
    protected float progress;
    protected float targetPercent;
    protected long setTime;

    protected final HudInfo hud = new HudInfo();



    public void setProgress(float progress) {
        this.progress = this.getProgress();
        this.targetPercent = progress;
        this.setTime = Util.getMillis();
    }

    public float getProgress() {
        long i = Util.getMillis() - this.setTime;
        float f = Mth.clamp((float)i / LERP_MILLISECONDS, 0.0F, 1.0F);
        return Mth.lerp(f, this.progress, this.targetPercent);
    }

    protected static boolean drawInCreative(ForgeGui gui) {
        return gui.getMinecraft().options.hideGui || !gui.shouldDrawSurvivalElements();
    }

}
