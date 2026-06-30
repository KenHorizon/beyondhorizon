package com.kenhorizon.beyondhorizon.client.render.guis.hud;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.api.IStackIconOverlay;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryStackHandler;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItem;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTags;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class GameHudDisplay extends Gui {
    private final static ResourceLocation ICON_BACKGROUND = BeyondHorizon.resourceGui("sprites/icon/effects/icon_backgrounds.png");
    private final HudInfo hud = new HudInfo();
    private final Minecraft minecraft;
    private int leftHeight = 39;
    public GameHudDisplay() {
        super(Minecraft.getInstance(), Minecraft.getInstance().getItemRenderer());
        this.minecraft = Minecraft.getInstance();
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onHealthBarRender(RenderGuiOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        this.renderEffectIcons(event.getGuiGraphics(), event.getPartialTick());

        if (BHConfigs.GAME_HUD == GameHuds.VANILLA || minecraft.options.hideGui || !this.shouldDrawSurvivalElements() || event.getOverlay() != VanillaGuiOverlay.PLAYER_HEALTH.type()) return;
        event.setCanceled(true);
        this.renderPlayerHearts(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onArmorRender(RenderGuiOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (BHConfigs.GAME_HUD == GameHuds.VANILLA || minecraft.options.hideGui || !this.shouldDrawSurvivalElements() || event.getOverlay() != VanillaGuiOverlay.ARMOR_LEVEL.type()) return;
        event.setCanceled(true);
        this.renderArmor(event.getGuiGraphics(), event.getPartialTick());
    }
    public void renderEffectIcons(GuiGraphics guiGraphics, float partialTicks) {
        minecraft.getProfiler().push("effectIcons");
        var player = minecraft.player;
        RenderSystem.enableBlend();
        this.hud.update();
        var stackableTags = Capabilities.stackable(player);
        int xPos = 0;
        IAccessoryStackHandler handler = Capabilities.accessory(player);
        if (stackableTags != null) {
            if (handler != null) {
                var stacks = handler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (!stack.isEmpty() && stack.getItem() instanceof IAccessoryItem accessoryItems) {
                        for (var accessory : accessoryItems.getAccessories()) {
                            if (accessory instanceof IStackIconOverlay overlay) {
                                List<StackableTags> list = new ArrayList<>();
                                list.add(overlay.getStacks());
                                for (var allTags : list) {
                                    ResourceLocation getAllIcons = BeyondHorizon.resourceGui("sprites/icon/effects/" + allTags.getName() + ".png");
                                    if (allTags.hasStacks()) {
                                        int x = this.hud.scaledWindowWidth / 2 - 91 + (26 * xPos);
                                        int y = this.hud.scaledWindowHeight - (this.getForgeGui().leftHeight + 52);
                                        String value = String.format("%s", allTags.getStack());
                                        BlitHelper.drawBlit(guiGraphics, ICON_BACKGROUND, x, y -1, 0, 0, 24, 24, 24, 24);
                                        BlitHelper.drawBlit(guiGraphics, getAllIcons, x, y - 1, 0, 0, 24, 24, 24, 24);
                                        int valueLenght = value.length();
                                        BlitHelper.drawStrings(guiGraphics, value,x + (2 + 9) - (valueLenght / 2), y + 12, ColorUtil.WHITE, true);
                                        RenderSystem.disableBlend();
                                        xPos++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        minecraft.getProfiler().pop();

    }
    public void renderArmor(GuiGraphics guiGraphics, float partialTicks) {
        minecraft.getProfiler().push("armor");
        this.hud.update();
        int x = this.hud.scaledWindowWidth / 2 - 91;
        int y = this.hud.scaledWindowHeight - (this.leftHeight + 11);
        String value = String.format("%.0f", this.hud.armor);
        BlitHelper.drawBlit(guiGraphics, HudSprites.ARMOR_FULL, x, y - 1, 0, 0, 9, 9, 9, 9);
        BlitHelper.drawBorderedStrings(guiGraphics, value,x + (5 + 9), y, ColorUtil.WHITE);
        minecraft.getProfiler().pop();
    }

    public void renderPlayerHearts(GuiGraphics guiGraphics, float partialTicks) {
        this.minecraft.getProfiler().push("healthbar");
        this.hud.update();
        int x = this.hud.scaledWindowWidth / 2 - 91;
        int y = this.hud.scaledWindowHeight - this.leftHeight;
        if (this.hud.hasAbsroption) {
            String absorption = String.format("%.0f", this.hud.absorption);
            int abX = x + (5 + 9) - (24 + absorption.length());
            BlitHelper.drawBlit(guiGraphics, HudSprites.ABSROPTION, abX - (5 + 9), y -1, 0, 0, 9, 9, 9, 9);
            BlitHelper.drawStrings(guiGraphics, absorption, abX, y, ColorUtil.WHITE, true);
        }
        String health = String.format("%.0f/%.0f", this.hud.health, this.hud.maxHealth);
        BlitHelper.drawBlit(guiGraphics, HudSprites.HEALTH, x, y - 1, 0, 0, 9, 9, 9, 9);
        BlitHelper.drawBorderedStrings(guiGraphics, health,x + (5 + 9), y, ColorUtil.combineRGB(249, 87, 87));
        this.minecraft.getProfiler().pop();
    }

    public ForgeGui getForgeGui() {
        return (ForgeGui) Minecraft.getInstance().gui;
    }

    public boolean shouldDrawSurvivalElements() {
        return minecraft.gameMode.canHurtPlayer() && minecraft.getCameraEntity() instanceof Player;
    }
}
