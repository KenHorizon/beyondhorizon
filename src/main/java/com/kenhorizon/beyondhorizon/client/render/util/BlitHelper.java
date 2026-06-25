package com.kenhorizon.beyondhorizon.client.render.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class BlitHelper {
    public static void drawScaleBlit(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, float scale, int uo, int vo, int width, int height) {
        drawScaleBlit(guiGraphics, textures, x, y, scale, uo, vo, width ,height, 256, 256);
    }
    public static void drawScaleBlit(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, float scale, int uo, int vo, int width, int height, int textureWidth, int textureHeight) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        poseStack.scale(scale, scale, scale);
        guiGraphics.blit(textures, x, y, uo, vo, width, height, textureWidth, textureHeight);
        poseStack.popPose();
    }


    public static void drawBlit(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, int uo, int vo, int width, int height) {
        drawBlit(guiGraphics, textures, x, y, uo, vo, width, height, 256, 256, 0xFFFFFFFF);
    }

    public static void drawBlit(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, int uo, int vo, int width, int height, int color) {
        drawBlit(guiGraphics, textures, x, y, uo, vo, width, height, 256, 256, color);
    }

    public static void drawBlit(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, int uo, int vo, int width, int height, int textureWidth, int textureHeight) {
        drawBlit(guiGraphics, textures, x, y, uo, vo, width, height, textureWidth, textureHeight, 0xFFFFFFFF);
    }

    public static void drawBlit(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, int uo, int vo, int width, int height, int textureWidth, int textureHeight, int color) {
        RenderSystem.enableBlend();
        float[] colors = ColorUtil.getFARGB(color);
        RenderSystem.setShaderColor(colors[0], colors[1], colors[2], colors[3]);
        guiGraphics.blit(textures, x, y, uo, vo, width, height, textureWidth, textureHeight);
        RenderSystem.disableBlend();
    }
    // DRAW STRINGS

    public static void drawStrings(GuiGraphics guiGraphics, Component text, int x, int y, boolean border) {
        int coloredText = 0;
        if (text.getStyle().getColor() != null) {
            coloredText = TextColor.fromRgb(text.getStyle().getColor().getValue()).getValue();
        } else {
            coloredText = ColorUtil.combineRGB(255, 255, 255);
        }
        drawStrings(guiGraphics, text, x, y, coloredText, border, !border);
    }

    public static void drawStrings(GuiGraphics guiGraphics, Component text, int x, int y, int color) {
        int coloredText;
        if (text.getStyle().getColor() != null) {
            coloredText = TextColor.fromRgb(text.getStyle().getColor().getValue()).getValue();
        } else {
            coloredText = color;
        }
        drawStrings(guiGraphics, text, x, y, coloredText, false, false);
    }

    public static void drawStrings(GuiGraphics guiGraphics, Component text, int x, int y) {
        int coloredText = 0;
        if (text.getStyle().getColor() != null) {
            coloredText = TextColor.fromRgb(text.getStyle().getColor().getValue()).getValue();
        } else {
            coloredText = ColorUtil.combineRGB(255, 255, 255);
        }
        drawStrings(guiGraphics, text, x, y, coloredText, false, true);
    }

    public static void drawStrings(GuiGraphics guiGraphics, String text, int x, int y, int color) {
        drawStrings(guiGraphics, text, x, y, color, false, false);
    }
    public static void drawStrings(GuiGraphics guiGraphics, String text, int x, int y, int color, boolean borderOrdropShadow) {
        drawStrings(guiGraphics, text, x, y, color, borderOrdropShadow, !borderOrdropShadow);
    }

    public static void drawStrings(GuiGraphics guiGraphics, Component text, int x, int y, int color, boolean border, boolean dropShadow) {
        drawStrings(guiGraphics, text.getString(), x, y, color, border, dropShadow);
    }

    public static void drawStrings(GuiGraphics guiGraphics, String text, int x, int y, int color, boolean border, boolean dropShadow) {
        if (border) {
            guiGraphics.drawString(Minecraft.getInstance().font, text, x + 1, y, ColorUtil.combineRGB(0, 0, 0), false);
            guiGraphics.drawString(Minecraft.getInstance().font, text, x - 1, y, ColorUtil.combineRGB(0, 0, 0), false);
            guiGraphics.drawString(Minecraft.getInstance().font, text, x, y + 1, ColorUtil.combineRGB(0, 0, 0), false);
            guiGraphics.drawString(Minecraft.getInstance().font, text, x, y - 1, ColorUtil.combineRGB(0, 0, 0), false);
        }
        guiGraphics.drawString(Minecraft.getInstance().font, text, x, y, color, dropShadow);
    }
}
