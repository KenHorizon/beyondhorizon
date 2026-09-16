package com.kenhorizon.beyondhorizon.client.render.guis.hud.overlay;

import com.kenhorizon.beyondhorizon.client.render.guis.hud.HudSprites;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.client.util.ResourceUtils;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class AbilityHud extends HudOverlay {
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = gui.getMinecraft();
        Font font = mc.font;
        this.hud.update();
        if (gui.getMinecraft().options.hideGui) return;
        gui.setupOverlayRenderState(true, false);
        mc.getProfiler().push("ability_hud");
        float casttimeFactor = this.hud.casttime;
        String casttime = String.format("%.0f%%", 100.0F * casttimeFactor);
        int castTimeW = font.width(casttime);
        if (casttimeFactor > 0.0F && this.hud.casttimeReduction >= 1.0F) {
            int castTimeX = (screenWidth - 79) / 2;
            int castTimeY = screenHeight - (gui.leftHeight);
            BlitHelper.drawBlit(guiGraphics, HudSprites.CAST_TIME_BACKGROUND, castTimeX, castTimeY, 0,0, 79, 4,79, 4);
            BlitHelper.drawBlit(guiGraphics, HudSprites.CAST_TIME, castTimeX, castTimeY, 0,0, (int) (79 * casttimeFactor), 4,79, 4);
            BlitHelper.drawBlit(guiGraphics, HudSprites.CAST_TIME_OVERLAY, castTimeX - 22, castTimeY - 6, 0,0,124, 16,124, 16);
            BlitHelper.drawBorderedStrings(font, guiGraphics, casttime,castTimeX + (16 + castTimeW / 2), castTimeY - (font.lineHeight + 3), Colors.combineRGB(0, 148, 255));
        }
        if (!this.hud.abilityDisplayName.isEmpty()) {
            switch (BHConfigs.SKILL_DISPLAY) {
                case ICON -> {
                    ResourceLocation rl = BeyondHorizon.resource(String.format("textures/icons/skills/%s.png", this.hud.abilityName));
                    if (ResourceUtils.getImage(rl)) {
                        int x = 20;
                        int y = (this.hud.scaledWindowHeight - (20 + 24));
                        BlitHelper.drawBlit(guiGraphics, HudSprites.ICON_BOX, x, y, 0, 0, 24, 24, 24, 24);
                        BlitHelper.drawBlit(guiGraphics, rl, x, y, 0, 0, 24, 24, 24, 24);
                        PlayerData playerData = Capabilities.data(mc.player);
                        if (playerData != null && playerData.isOnCooldown(this.hud.abilityId)) {
                            int[] colors = Colors.getARGB(Colors.LIGHT_GRAY);
                            float pr = playerData.getCooldownPercent(this.hud.abilityId);
                            guiGraphics.fill(x, y, x + 24, (int) (y + (24 * pr)), Colors.combineARGB(100, colors[0], colors[1], colors[2]));
                        }
                    }
                }
                case TEXT -> {
                    int x = 20;
                    int y = (this.hud.scaledWindowHeight - (20));
                    String skills = String.format("%s", this.hud.abilityDisplayName);
                    BlitHelper.drawBorderedStrings(font, guiGraphics, skills,x, y, Colors.GOLD);
                }
                default -> {
                    break;
                }
            }
        }
        mc.getProfiler().pop();
    }
}
