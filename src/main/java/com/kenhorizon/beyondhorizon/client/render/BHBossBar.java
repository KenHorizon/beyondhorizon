package com.kenhorizon.beyondhorizon.client.render;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.guis.hud.HudSprites;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.server.entity.BHBossInfo;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.libs.registry.RegistryHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom Boss bar
 * <p>Original Work
 * <a href="https://www.curseforge.com/members/bobmowzie/projects">Bob Mowzies</a> <br>
 * @author bobmowzies
 * */
public class BHBossBar {
    public static Map<BHBossInfo.BossBar, BHBossBar> BOSS_BARS = new HashMap<>();
    public static BHBossBar BLAZING_INFERNO = new BHBossBar(
            BeyondHorizon.resourceGui("sprites/bossbar/overlay/blazing_inferno.png"),
            15, 0, 9, 15, -3, -8, 182, 32,
            ChatFormatting.YELLOW);
    public static BHBossBar BLAZING_INFERNO_ENGRAGED = new BHBossBar(
            BeyondHorizon.resourceGui("sprites/bossbar/overlay/blazing_inferno_enraged.png"),
            15, 0, 9, 15, -3, -8, 182, 32,
            ChatFormatting.BLUE);
    public static BHBossBar PYROLLIGER = new BHBossBar(
            BeyondHorizon.resourceGui("sprites/bossbar/overlay/pyrolliger.png"),
            15, 0, 5, 15, -3, 0, 182, 32,
            ChatFormatting.RED);
    public static BHBossBar PYROLLIGER_MANA = new BHBossBar(
            BeyondHorizon.resourceGui("sprites/bossbar/overlay/pyrolliger_mana_container.png"),
            BeyondHorizon.resourceGui("sprites/bossbar/overlay/pyrolliger_mana_progress.png"),
            BeyondHorizon.resourceGui("sprites/bossbar/overlay/pyrolliger_mana.png"),
            15, 128 - 74, -6, 15, -5, 0, 68, 16,
            ChatFormatting.RED);
    static  {
        BOSS_BARS.put(new BHBossInfo.BossBar(0, RegistryHelper.getKeyOrThrow(BHEntity.BLAZING_INFERNO.get())), BLAZING_INFERNO);
        BOSS_BARS.put(new BHBossInfo.BossBar(1, RegistryHelper.getKeyOrThrow(BHEntity.BLAZING_INFERNO.get())), BLAZING_INFERNO_ENGRAGED);
        BOSS_BARS.put(new BHBossInfo.BossBar(2, RegistryHelper.getKeyOrThrow(BHEntity.PYROLLIGER.get())), PYROLLIGER);
        BOSS_BARS.put(new BHBossInfo.BossBar(3, RegistryHelper.getKeyOrThrow(BHEntity.PYROLLIGER.get())), PYROLLIGER_MANA);
    }

    private final ResourceLocation container;
    private ResourceLocation base;
    private ResourceLocation overlay;
    private boolean hasOverlay;
    private int renderType;
    private int height;
    private int baseTextHeight;
    private int baseX;
    private int baseY;
    private int overlayX;
    private int overlayY;
    private int overlayWidth;
    private int overlayHeight;
    private int verticalIncrement;
    private int bossbarProgress;
    private ChatFormatting textColor;

    public BHBossBar(ResourceLocation container, ResourceLocation base, ResourceLocation overlay, int height, int baseX, int baseY,
                     int baseTextHeight, int overlayX, int overlayY, int overlayWidth, int overlayHeight, int bossbarProgress, int verticalIncrement, ChatFormatting chatFormatting) {
        this.container = container;
        this.verticalIncrement = verticalIncrement;
        this.base = base;
        this.overlay = overlay;
        this.height = height;
        this.baseX = baseX;
        this.baseY = baseY;
        this.hasOverlay = this.overlay != null;
        this.baseTextHeight = baseTextHeight;
        this.overlayX = overlayX;
        this.overlayY = overlayY;
        this.overlayWidth = overlayWidth;
        this.overlayHeight = overlayHeight;
        this.bossbarProgress = bossbarProgress;
        this.textColor = chatFormatting;
    }
    public BHBossBar(ResourceLocation overlay, int height, int baseX, int baseY,
                     int baseTextHeight, int overlayX, int overlayY, int bossbarProgress, int verticalIncrement, ChatFormatting chatFormatting) {
        this(HudSprites.BOSS_BAR_HUD_CONTAINER, HudSprites.BOSS_BAR_HUD, overlay, height, baseX, baseY, baseTextHeight, overlayX, overlayY, 256, 23, bossbarProgress, verticalIncrement, chatFormatting);
    }
    public BHBossBar(ResourceLocation container, ResourceLocation hud, ResourceLocation overlay, int height, int baseX, int baseY,
                     int baseTextHeight, int overlayX, int overlayY, int bossbarProgress, int verticalIncrement, ChatFormatting chatFormatting) {
        this(container, hud ,overlay, height, baseX, baseY, baseTextHeight, overlayX, overlayY, 256, 23, bossbarProgress, verticalIncrement, chatFormatting);
    }
    public void renderBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        Minecraft minecraft = Minecraft.getInstance();
        int baseX = this.baseX;
        int baseY = this.baseY;
        GuiGraphics graphics = event.getGuiGraphics();
        int x = event.getX();
        int y = event.getY();
        int screenW = graphics.guiWidth();
        int screenH = minecraft.getWindow().getScreenHeight();
        int guiX = screenW / 2 - 91;
        int guiY = y - 9;
        float ageInTicks = minecraft.player.tickCount + event.getPartialTick();
        minecraft.getProfiler().push("beyondhorizon:bossbar");
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.base);
        this.drawBossBar(graphics, x + 1 + baseX, y + baseY, event.getBossEvent());
        Component bossBarName = event.getBossEvent().getName().copy();
        minecraft.getProfiler().pop();
        int fontLenght = Minecraft.getInstance().font.width(bossBarName);
        int textX = screenW / 2 - fontLenght / 2;
        int textY = guiY;
        BlitHelper.drawBorderedStrings(minecraft.font, graphics, bossBarName, textX, textY, this.textColor.getColor().intValue());
        if (this.hasOverlay) {
            minecraft.getProfiler().push("beyondhorizon:bossbar_overlay");
            RenderSystem.setShaderTexture(0, this.overlay);
            graphics.blit(this.overlay, x + 1 + this.overlayX + this.baseX, y + this.overlayY + this.baseY, 0, 0, this.overlayWidth, this.overlayHeight, this.overlayWidth, this.overlayHeight);
            minecraft.getProfiler().pop();
        }
        event.setIncrement(this.verticalIncrement);
    }

    private void drawBossBar(GuiGraphics graphics, int x, int y, LerpingBossEvent event) {
        graphics.blit(this.container, x - 3, y, 0, 0, this.bossbarProgress, this.height, 256, this.baseTextHeight);
        int progress = (int)( event.getProgress() * (this.bossbarProgress + 1));
        if (progress > 0) {
            graphics.blit(this.base, x, y, 0, this.height, progress, this.height, 256, this.baseTextHeight);
        }
    }
}
