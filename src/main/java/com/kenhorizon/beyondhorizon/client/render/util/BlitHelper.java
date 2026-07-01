package com.kenhorizon.beyondhorizon.client.render.util;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class BlitHelper {
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
    // DRAW TEXT
    public static void drawStrings(Font font, GuiGraphics guiGraphics, String text, int x, int y) {
        drawStrings(font, guiGraphics, Component.literal(text), x, y, ColorUtil.WHITE, true);
    }

    public static void drawStrings(Font font, GuiGraphics guiGraphics, String text, int x, int y, int color) {
        drawStrings(font, guiGraphics, Component.literal(text), x, y, color, true);
    }


    public static void drawStrings(Font font, GuiGraphics guiGraphics, Component text, int x, int y) {
        drawStrings(font, guiGraphics, text.getString(), x, y, ColorUtil.WHITE, true);
    }

    public static void drawStrings(Font font, GuiGraphics guiGraphics, Component text, int x, int y, int color) {
        drawStrings(font, guiGraphics, text.getString(), x, y, color, true);
    }

    public static void drawStrings(Font font, GuiGraphics guiGraphics, Component text, int x, int y, int color, boolean dropShadow) {
        drawStrings(font, guiGraphics, text.getString(), x, y, color, dropShadow);
    }

    public static void drawStrings(Font font, GuiGraphics guiGraphics, String text, int x, int y, int color, boolean dropShadow) {
        guiGraphics.drawString(font, text, x, y, color, dropShadow);
    }

    // DRAW BORDER TEXT
    public static void drawBorderedStrings(Font font, GuiGraphics guiGraphics, String text, int x, int y) {
        drawBorderedStrings(font,guiGraphics, Component.literal(text), x, y, ColorUtil.LIGHT_GRAY, ColorUtil.BLACK);
    }
    public static void drawBorderedStrings(Font font, GuiGraphics guiGraphics, String text, int x, int y, int color) {
        drawBorderedStrings(font,guiGraphics, Component.literal(text), x, y, color, ColorUtil.BLACK);
    }
    public static void drawBorderedStrings(Font font, GuiGraphics guiGraphics, Component text, int x, int y) {
        drawBorderedStrings(font,guiGraphics, text, x, y, ColorUtil.LIGHT_GRAY, ColorUtil.BLACK);
    }
    public static void drawBorderedStrings(Font font, GuiGraphics guiGraphics, Component text, int x, int y, int color) {
        drawBorderedStrings(font,guiGraphics, text, x, y, color, ColorUtil.BLACK);
    }
    public static void drawBorderedStrings(Font font, GuiGraphics guiGraphics, Component text, int x, int y, int color, int borderColor) {
        for (int j = -1; j <= 1; ++j) {
            for (int k = -1; k <= 1; ++k) {
                if (j != 0 || k != 0) {
                    guiGraphics.drawString(font,text.getVisualOrderText(), x + j, y + k, borderColor, false);
                }
            }
        }
        guiGraphics.drawString(font,text.getVisualOrderText(), x, y, color, false);
        // Issues of rendering using minecraft.font where the text are visible on Screen UIs like Inventory, and others
//        font.drawInBatch8xOutline(text.getVisualOrderText(), x, y, color, borderColor, matrix4f, guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT);
    }
    // AC
    public static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation p_283377_, int p_281970_, int p_282111_, int p_283134_, int p_282778_, int p_281478_, int p_281821_, float r, float g, float b, float a) {
        blitWithColor(guiGraphics, p_283377_, p_281970_, p_282111_, 0, (float) p_283134_, (float) p_282778_, p_281478_, p_281821_, 256, 256, r, g, b, a);
    }

    public static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation p_283573_, int p_283574_, int p_283670_, int p_283545_, float p_283029_, float p_283061_, int p_282845_, int p_282558_, int p_282832_, int p_281851_, float r, float g, float b, float a) {
        blitWithColor(guiGraphics, p_283573_, p_283574_, p_283574_ + p_282845_, p_283670_, p_283670_ + p_282558_, p_283545_, p_282845_, p_282558_, p_283029_, p_283061_, p_282832_, p_281851_, r, g, b, a);
    }

    public static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation p_282034_, int p_283671_, int p_282377_, int p_282058_, int p_281939_, float p_282285_, float p_283199_, int p_282186_, int p_282322_, int p_282481_, int p_281887_, float r, float g, float b, float a) {
        blitWithColor(guiGraphics, p_282034_, p_283671_, p_283671_ + p_282058_, p_282377_, p_282377_ + p_281939_, 0, p_282186_, p_282322_, p_282285_, p_283199_, p_282481_, p_281887_, r, g, b, a);
    }

    public static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation p_283272_, int p_283605_, int p_281879_, float p_282809_, float p_282942_, int p_281922_, int p_282385_, int p_282596_, int p_281699_, float r, float g, float b, float a) {
        blitWithColor(guiGraphics, p_283272_, p_283605_, p_281879_, p_281922_, p_282385_, p_282809_, p_282942_, p_281922_, p_282385_, p_282596_, p_281699_, r, g, b, a);
    }

    private static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation p_282639_, int p_282732_, int p_283541_, int p_281760_, int p_283298_, int p_283429_, int p_282193_, int p_281980_, float p_282660_, float p_281522_, int p_282315_, int p_281436_, float r, float g, float b, float a) {
        blitWithColor(guiGraphics, p_282639_, p_282732_, p_283541_, p_281760_, p_283298_, p_283429_, (p_282660_ + 0.0F) / (float) p_282315_, (p_282660_ + (float) p_282193_) / (float) p_282315_, (p_281522_ + 0.0F) / (float) p_281436_, (p_281522_ + (float) p_281980_) / (float) p_281436_, r, g, b, a);
    }

    private static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation texture, int startX, int endX, int startY, int endY, int zLevel, float u0, float u1, float v0, float v1, float r, float g, float b, float a) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.vertex(matrix4f, (float) startX, (float) startY, (float) zLevel).color(r, g, b, a).uv(u0, v0).endVertex();
        bufferbuilder.vertex(matrix4f, (float) startX, (float) endY, (float) zLevel).color(r, g, b, a).uv(u0, v1).endVertex();
        bufferbuilder.vertex(matrix4f, (float) endX, (float) endY, (float) zLevel).color(r, g, b, a).uv(u1, v1).endVertex();
        bufferbuilder.vertex(matrix4f, (float) endX, (float) startY, (float) zLevel).color(r, g, b, a).uv(u1, v0).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }
}
