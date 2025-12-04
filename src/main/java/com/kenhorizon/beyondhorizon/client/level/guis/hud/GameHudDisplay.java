package com.kenhorizon.beyondhorizon.client.level.guis.hud;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.level.util.BlitHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;

public class GameHudDisplay extends Gui {
    private final HudInfo hud = new HudInfo();
    private final Minecraft minecraft;
    public GameHudDisplay() {
        super(Minecraft.getInstance(), Minecraft.getInstance().getItemRenderer());
        this.minecraft = Minecraft.getInstance();
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onHealthBarRender(RenderGuiOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui || !this.shouldDrawSurvivalElements() || event.getOverlay() != VanillaGuiOverlay.PLAYER_HEALTH.type()) return;
        event.setCanceled(true);
        this.renderPlayerHearts(event.getGuiGraphics(), event.getPartialTick());
    }

    public void renderPlayerHearts(GuiGraphics guiGraphics, float partialTicks) {
        this.minecraft.getProfiler().push("healthbar");
        this.hud.update();
        int x = this.hud.scaledWindowWidth / 2 - 91;
        int y = this.hud.scaledWindowHeight - 39;
        if (this.hud.hasAbsroption) {BlitHelper.draw(guiGraphics, HudSprites.ABSROPTION, x, y + 9, 9.0F, 9, 9, 9, 9);

            BlitHelper.drawStrings(guiGraphics, String.format("%.0f", this.hud.absorption),x + (5 + 9), y + 10, true);
        }
        BlitHelper.draw(guiGraphics, HudSprites.HEALTH, x, y - 1, 9.0F, 9, 9, 9, 9);
        BlitHelper.drawStrings(guiGraphics, String.format("%.0f/%.0f", this.hud.health, this.hud.maxHealth),x + (5 + 9), y, true);
        this.minecraft.getProfiler().pop();
    }

    public ForgeGui getForgeGui() {
        return (ForgeGui) Minecraft.getInstance().gui;
    }
    public boolean shouldDrawSurvivalElements() {
        return minecraft.gameMode.canHurtPlayer() && minecraft.getCameraEntity() instanceof Player;
    }
}
