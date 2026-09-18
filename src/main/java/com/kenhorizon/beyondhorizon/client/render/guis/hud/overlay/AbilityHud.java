package com.kenhorizon.beyondhorizon.client.render.guis.hud.overlay;

import com.kenhorizon.beyondhorizon.client.api.IStackIconOverlay;
import com.kenhorizon.beyondhorizon.client.render.guis.hud.HudSprites;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.client.util.ResourceUtils;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItem;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryStackHandler;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagInstance;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTags;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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
        this.renderEffectIcons(mc, gui, guiGraphics, partialTick);
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
                            guiGraphics.fill(x, y, x + 22, (int) (y + (22 * pr)), Colors.combineARGB(100, colors[0], colors[1], colors[2]));
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
    public void renderEffectIcons(Minecraft mc, ForgeGui gui, GuiGraphics guiGraphics, float partialTicks) {
        mc.getProfiler().push("effectIcons");
        var player = mc.player;
        RenderSystem.enableBlend();
        this.hud.update();
        int xPos = 0;
        IAccessoryStackHandler handler = Capabilities.accessory(player);
        var stackable = Capabilities.stackable(player);
        if (handler != null) {
            var stacks = handler.getStacks();
            for (int i = 0; i < stacks.getSlots(); i++) {
                ItemStack stack = stacks.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() instanceof IAccessoryItem accessoryItems) {
                    for (var accessory : accessoryItems.getAccessories()) {
                        if (accessory instanceof IStackIconOverlay overlay) {
                            var tag = stackable.makeInstance(overlay.getStacks());
                            if (tag == null) continue;
                            if (tag.hasNoStacks()) continue;
                            renderStackableTags(mc, gui, guiGraphics, tag, xPos);
                            xPos++;
                        }
                    }
                }
            }
        }
        if (stackable != null) {
            for (StackableTags tag : stackable.getStackableTags()) {
                if (tag.hasNoStacks() || !StackableTagInstance.getRenderAlways().contains(tag.getName())) continue;
                renderStackableTags(mc, gui, guiGraphics, tag, xPos);
                xPos++;
            }
        }
        mc.getProfiler().pop();

    }

    private void renderStackableTags(Minecraft mc, ForgeGui gui, GuiGraphics guiGraphics, StackableTags tag, int xPos) {
        ResourceLocation getAllIcons = BeyondHorizon.resourceGui("sprites/icon/effects/" + tag.getName() + ".png");
        if (ResourceUtils.getImage(getAllIcons)) {
            int x = this.hud.scaledWindowWidth / 2 - 91 + (26 * xPos);
            int y = this.hud.scaledWindowHeight - (gui.leftHeight + 52);
            String value = String.format("%s", tag.getStack());
            BlitHelper.drawBlit(guiGraphics, HudSprites.ICON_BACKGROUND, x, y -1, 0, 0, 24, 24, 24, 24);
            BlitHelper.drawBlit(guiGraphics, getAllIcons, x, y - 1, 0, 0, 24, 24, 24, 24);
            int valueLenght = value.length();
            BlitHelper.drawBorderedStrings(gui.getMinecraft().font, guiGraphics, value,x + (2 + 9) - (valueLenght / 2), y + 12, Colors.WHITE);
            RenderSystem.disableBlend();
        }
    }
}
