package com.kenhorizon.beyondhorizon.client.level.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class BlitHelper {
    public static void containerIcons(GuiGraphics guiGraphics, ResourceLocation textures, ResourceLocation icons, int x, int y, int size) {
        containerIcons(guiGraphics, textures, icons, x, y, size, 16.0F);
    }
    public static void fill(GuiGraphics guiGraphics, int startX, int startY, int endX, int endY, int color, float alpha) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        guiGraphics.fill(startX, startY, endX, endY, color);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

    }
    public static void containerIcons(GuiGraphics guiGraphics, ResourceLocation textures, ResourceLocation icons, int x, int y, int size, float scale) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        poseStack.scale(scale, scale, scale);
        guiGraphics.blit(textures, x, y, 0, 0, 17, 17, 17, 17);
        int x1 = x + 4;
        int y1 = y + 4;
        guiGraphics.blit(icons, x1, y1, 0, 0, size, size, size, size);
        poseStack.popPose();
    }


    // DRAW ICONS
    public static void renderIcons(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, float scale) {
        renderIcons(guiGraphics, textures, x, y, scale, 255);
    }
    public static void renderIcons(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, float scale, int size) {
        renderIcons(guiGraphics, textures, x, y, scale, 0, 0, size, size, size, size);
    }
    public static void renderIcons(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, float scale, int textureWidth, int textureHeight) {
        renderIcons(guiGraphics, textures, x, y, scale, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }
    public static void renderIcons(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, float scale, int uo, int vo, int textureWidth, int textureHeight) {
        renderIcons(guiGraphics, textures, x, y, scale, uo, vo, textureWidth, textureHeight, textureWidth, textureHeight);
    }
    public static void renderIcons(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y) {
        renderIcons(guiGraphics, textures, x, y, 0, 0, 255);
    }

    public static void renderIcons(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, int size) {
        renderIcons(guiGraphics, textures, x, y, 0, 0, size);
    }
    public static void renderIcons(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, int u, int v, int size) {
        renderIcons(guiGraphics, textures, x, y, 16.0F, u, v, size, size, size, size);
    }
    public static void renderIcons(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, int textureWidth, int textureHeight) {
        renderIcons(guiGraphics, textures, x, y, 16.0F, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }
    public static void renderIcons(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, int uo, int vo, int textureWidth, int textureHeight) {
        renderIcons(guiGraphics, textures, x, y, 16.0F, uo, vo, textureWidth, textureHeight, textureWidth, textureHeight);
    }
    public static void renderIcons(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, int uo, int vo, int width, int height, int textureWidth, int textureHeight) {
        renderIcons(guiGraphics, textures, x, y, 16.0F, uo, vo, width, height, textureWidth, textureHeight);
    }

    public static void renderIcons(GuiGraphics guiGraphics, TextureAtlasSprite textures, int x, int y, float scale) {
        renderIcons(guiGraphics, textures, x, y, scale, 255);
    }
    public static void renderIcons(GuiGraphics guiGraphics, TextureAtlasSprite textures, int x, int y, float scale, int size) {
        renderIcons(guiGraphics, textures, x, y, scale, 0, 0, size, size, size, size);
    }
    public static void renderIcons(GuiGraphics guiGraphics, TextureAtlasSprite textures, int x, int y, float scale, int textureWidth, int textureHeight) {
        renderIcons(guiGraphics, textures, x, y, scale, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }
    public static void renderIcons(GuiGraphics guiGraphics, TextureAtlasSprite textures, int x, int y, float scale, int uo, int vo, int textureWidth, int textureHeight) {
        renderIcons(guiGraphics, textures, x, y, scale, uo, vo, textureWidth, textureHeight, textureWidth, textureHeight);
    }
    public static void renderIcons(GuiGraphics guiGraphics, TextureAtlasSprite textures, int x, int y) {
        renderIcons(guiGraphics, textures, x, y, 0, 0, 255);
    }
    public static void renderIcons(GuiGraphics guiGraphics, TextureAtlasSprite textures, int x, int y, int size) {
        renderIcons(guiGraphics, textures, x, y, 0, 0, size);
    }
    public static void renderIcons(GuiGraphics guiGraphics, TextureAtlasSprite textures, int x, int y, int u, int v, int size) {
        renderIcons(guiGraphics, textures, x, y, 16.0F, u, v, size, size, size, size);
    }
    public static void renderIcons(GuiGraphics guiGraphics, TextureAtlasSprite textures, int x, int y, int textureWidth, int textureHeight) {
        renderIcons(guiGraphics, textures, x, y, 16.0F, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }
    public static void renderIcons(GuiGraphics guiGraphics, TextureAtlasSprite textures, int x, int y, int uo, int vo, int textureWidth, int textureHeight) {
        renderIcons(guiGraphics, textures, x, y, 16.0F, uo, vo, textureWidth, textureHeight, textureWidth, textureHeight);
    }
    public static void renderIcons(GuiGraphics guiGraphics, TextureAtlasSprite textures, int x, int y, int uo, int vo, int width, int height, int textureWidth, int textureHeight) {
        renderIcons(guiGraphics, textures, x, y, 16.0F, uo, vo, width, height, textureWidth, textureHeight);
    }
    public static void renderIcons(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, float scale, int uo, int vo, int width, int height, int textureWidth, int textureHeight) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        poseStack.scale(scale, scale, scale);
        guiGraphics.blit(textures, x, y, uo, vo, width, height, textureWidth, textureHeight);
        poseStack.popPose();
    }
    public static void renderIcons(GuiGraphics guiGraphics, TextureAtlasSprite textures, int x, int y, float scale, int uo, int vo, int width, int height, int textureWidth, int textureHeight) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        poseStack.scale(scale, scale, scale);
        guiGraphics.blit(textures.atlasLocation(), x, y, uo, vo, width, height, textureWidth, textureHeight);
        poseStack.popPose();
    }
    // DRAW BLIT
    public static void draw(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, float scale) {
        draw(guiGraphics, textures, x, y, scale, 0, 0, 255 ,255, 255, 255);
    }
    public static void draw(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, float scale, int width, int height, int textureWidth, int textureHeight) {
        draw(guiGraphics, textures, x, y, scale, 0, 0, width ,height, textureWidth, textureHeight);
    }
    public static void draw(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, float scale, int width, int height, int size) {
        draw(guiGraphics, textures, x, y, scale, 0, 0, width ,height, size, size);
    }
    public static void draw(GuiGraphics guiGraphics, ResourceLocation textures, int x, int y, float scale, int uo, int vo, int width, int height, int textureWidth, int textureHeight) {
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        poseStack.scale(scale, scale, scale);
        guiGraphics.blit(textures, x, y, uo, vo, width, height, textureWidth, textureHeight);
        poseStack.popPose();
    }

    // DRAW STRINGS
    public static void drawStrings(GuiGraphics guiGraphics, Component text, float scale, int x, int y, int coloredText) {
        if (text.getStyle().getColor() != null) {
            coloredText = TextColor.fromRgb(text.getStyle().getColor().getValue()).getValue();
        } else {
            coloredText = ColorUtil.combineRGB(255, 255, 255);
        }
        drawStrings(guiGraphics, text, x, y, scale, coloredText, false, false);
    }
    public static void drawStrings(GuiGraphics guiGraphics, Component text, float scale, int x, int y, boolean border) {
        int coloredText = 0;
        if (text.getStyle().getColor() != null) {
            coloredText = TextColor.fromRgb(text.getStyle().getColor().getValue()).getValue();
        } else {
            coloredText = ColorUtil.combineRGB(255, 255, 255);
        }
        drawStrings(guiGraphics, text, x, y, scale, coloredText, border, !border);
    }
    public static void drawStrings(GuiGraphics guiGraphics, Component text, int x, int y, boolean border) {
        int coloredText = 0;
        if (text.getStyle().getColor() != null) {
            coloredText = TextColor.fromRgb(text.getStyle().getColor().getValue()).getValue();
        } else {
            coloredText = ColorUtil.combineRGB(255, 255, 255);
        }
        drawStrings(guiGraphics, text, x, y, 16.0F, coloredText, border, !border);
    }
    public static void drawStrings(GuiGraphics guiGraphics, Component text, float scale, int x, int y) {
        int coloredText = 0;
        if (text.getStyle().getColor() != null) {
            coloredText = TextColor.fromRgb(text.getStyle().getColor().getValue()).getValue();
        } else {
            coloredText = ColorUtil.combineRGB(255, 255, 255);
        }
        drawStrings(guiGraphics, text, x, y, scale, coloredText, false, true);
    }

    public static void drawStrings(GuiGraphics guiGraphics, Component text, int x, int y, int color) {
        int coloredText;
        if (text.getStyle().getColor() != null) {
            coloredText = TextColor.fromRgb(text.getStyle().getColor().getValue()).getValue();
        } else {
            coloredText = color;
        }
        drawStrings(guiGraphics, text, x, y, 16.0F, coloredText, false, false);
    }

    public static void drawStrings(GuiGraphics guiGraphics, Component text, int x, int y) {
        int coloredText = 0;
        if (text.getStyle().getColor() != null) {
            coloredText = TextColor.fromRgb(text.getStyle().getColor().getValue()).getValue();
        } else {
            coloredText = ColorUtil.combineRGB(255, 255, 255);
        }
        drawStrings(guiGraphics, text, x, y, 16.0F, coloredText, false, true);
    }

    public static void drawStrings(GuiGraphics guiGraphics, String text, int x, int y, int color) {
        drawStrings(guiGraphics, text, x, y, 16.0F, color, false, false);
    }
    public static void drawStrings(GuiGraphics guiGraphics, String text, int x, int y, int color, boolean borderOrdropShadow) {
        drawStrings(guiGraphics, text, x, y, 16.0F, color, borderOrdropShadow, !borderOrdropShadow);
    }

    public static void drawStrings(GuiGraphics guiGraphics, Component text, int x, int y, float scale, int color, boolean border, boolean dropShadow) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        poseStack.scale(scale, scale, scale);
        String componentString = text.getString();
        if (border) {
            guiGraphics.drawString(Minecraft.getInstance().font, componentString, x + 1, y, 0, false);
            guiGraphics.drawString(Minecraft.getInstance().font, componentString, x - 1, y, 0, false);
            guiGraphics.drawString(Minecraft.getInstance().font, componentString, x, y + 1, 0, false);
            guiGraphics.drawString(Minecraft.getInstance().font, componentString, x, y - 1, 0, false);
        }
        guiGraphics.drawString(Minecraft.getInstance().font, componentString, x, y, color, dropShadow);
        poseStack.popPose();
    }

    public static void drawStrings(GuiGraphics guiGraphics, String text, int x, int y, float scale, int color, boolean border, boolean dropShadow) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        poseStack.scale(scale, scale, scale);
        if (border) {
            guiGraphics.drawString(Minecraft.getInstance().font, text, x + 1, y, ColorUtil.combineRGB(0, 0, 0), false);
            guiGraphics.drawString(Minecraft.getInstance().font, text, x - 1, y, ColorUtil.combineRGB(0, 0, 0), false);
            guiGraphics.drawString(Minecraft.getInstance().font, text, x, y + 1, ColorUtil.combineRGB(0, 0, 0), false);
            guiGraphics.drawString(Minecraft.getInstance().font, text, x, y - 1, ColorUtil.combineRGB(0, 0, 0), false);
        }
        guiGraphics.drawString(Minecraft.getInstance().font, text, x, y, color, dropShadow);
        poseStack.popPose();
    }
}
