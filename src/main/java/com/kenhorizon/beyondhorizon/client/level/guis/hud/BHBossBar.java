package com.kenhorizon.beyondhorizon.client.level.guis.hud;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom Boss bar
 * <p>Original Work
 * <a href="https://www.curseforge.com/members/bobmowzie/projects">Bob Mowzies</a> <br>
 * @author bobmowzies
 * */
public class BHBossBar {
    public static Map<ResourceLocation, BHBossBar> BOSS_BARS = new HashMap<>();
    static  {
        BOSS_BARS.put(ForgeRegistries.ENTITY_TYPES.getKey(BHEntity.BLAZING_INFERNO.get()), new BHBossBar(
                BeyondHorizon.resourceGui("sprites/bossbar/overlay/blazing_inferno.png"),
                15,
                9,
                15,
                -3,
                -8,
                256,
                23,
                32,
                ChatFormatting.YELLOW));
    }
    private final ResourceLocation container;
    private ResourceLocation base;
    private ResourceLocation overlay;
    private boolean hasOverlay;
    private int renderType;
    private int height;
    private int baseTextHeight;
    private int baseY;
    private int overlayX;
    private int overlayY;
    private int overlayWidth;
    private int overlayHeight;
    private int verticalIncrement;
    private ChatFormatting textColor;

    public BHBossBar(ResourceLocation container, ResourceLocation base, ResourceLocation overlay, int height, int baseY,
                     int baseTextHeight, int overlayX, int overlayY, int overlayWidth, int overlayHeight, int verticalIncrement, ChatFormatting chatFormatting) {
        this.container = container;
        this.verticalIncrement = verticalIncrement;
        this.base = base;
        this.overlay = overlay;
        this.height = height;
        this.baseY = baseY;
        this.hasOverlay = this.overlay != null;
        this.baseTextHeight = baseTextHeight;
        this.overlayX = overlayX;
        this.overlayY = overlayY;
        this.overlayWidth = overlayWidth;
        this.overlayHeight = overlayHeight;
        this.textColor = chatFormatting;
    }
    public BHBossBar(ResourceLocation overlay, int height, int baseY,
                     int baseTextHeight, int overlayX, int overlayY, int overlayWidth, int overlayHeight, int verticalIncrement, ChatFormatting chatFormatting) {
        this(HudSprites.BOSS_BAR_HUD_CONTAINER, HudSprites.BOSS_BAR_HUD,overlay, height, baseY, baseTextHeight, overlayX, overlayY, overlayWidth, overlayHeight, verticalIncrement, chatFormatting);
    }

    public void renderBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        Minecraft minecraft = Minecraft.getInstance();
        int baseY = this.baseY;
        GuiGraphics graphics = event.getGuiGraphics();
        int x = event.getX();
        int y = event.getY();
        int screenW = graphics.guiWidth();
        int screenH = minecraft.getWindow().getScreenHeight();
        int guiX = screenW / 2 - 91;
        int guiY = y - 9;
        minecraft.getProfiler().push("custom_bossbar");
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.base);
        this.drawBossBar(graphics, x + 1, y + baseY, event.getBossEvent());
        Component bossBarName = event.getBossEvent().getName().copy().withStyle(this.textColor);
        minecraft.getProfiler().pop();
        int fontLenght = Minecraft.getInstance().font.width(bossBarName);
        int textX = screenW / 2 - fontLenght / 2;
        int textY = guiY;
        graphics.drawString(minecraft.font, bossBarName, textX, textY, 16777215);
        if (this.hasOverlay) {
            minecraft.getProfiler().push("customBossBarOverlay");
            RenderSystem.setShaderTexture(0, this.overlay);
            graphics.blit(this.overlay, x + 1 + this.overlayX, y + this.overlayY + this.baseY, 0, 0, this.overlayWidth, this.overlayHeight, this.overlayWidth, this.overlayHeight);
            minecraft.getProfiler().pop();
        }
        event.setIncrement(this.verticalIncrement);
    }

    private void drawBossBar(GuiGraphics graphics, int x, int y, LerpingBossEvent event) {
        graphics.blit(this.container, x - 3, y, 0, 0, 182, this.height, 256, this.baseTextHeight);
        int progress = (int)(event.getProgress() * 183.0F);
        if (progress > 0) {
            graphics.blit(this.base, x, y, 0, this.height, progress, this.height, 256, this.baseTextHeight);
        }
    }
}
