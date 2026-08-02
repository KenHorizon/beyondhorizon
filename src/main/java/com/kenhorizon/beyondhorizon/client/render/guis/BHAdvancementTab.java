package com.kenhorizon.beyondhorizon.client.render.guis;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import java.util.Locale;

public class BHAdvancementTab {
    private static final float MAX_TRANSITION_TIME = 25F;
    private static Type hoverType = Type.DEFAULT;
    private static Type previousHoverType = Type.DEFAULT;
    private static float hoverChangeProgress = MAX_TRANSITION_TIME;
    private static float previousHoverChangeProgress = MAX_TRANSITION_TIME;
    private static int windowWidth;
    private static int windowHeight;

    private static boolean[][] foregroundBlocks;

    public static boolean isBH(Advancement root) {
        return root.getId().getNamespace().equals(BeyondHorizon.ID);
    }

    public static void renderTabBackground(GuiGraphics guiGraphics, int topX, int topY, DisplayInfo displayInfo, double scrollX, double scrollY) {
        float partialTick = Minecraft.getInstance().getPartialTick();
        float hoverProgress = getHoverChangeAmount(partialTick);
        float priorHoverProgress = 1F - hoverProgress;
        int fastColor = FastColor.ARGB32.lerp(hoverProgress, previousHoverType.backgroundColor, hoverType.backgroundColor);
        guiGraphics.fill(0, 0, windowWidth + 100, windowHeight, fastColor | -16777216);
        renderTabBackgroundForType(guiGraphics, topX, topY, partialTick, scrollX, scrollY, previousHoverType, priorHoverProgress);
        renderTabBackgroundForType(guiGraphics, topX, topY, partialTick, scrollX, scrollY, hoverType, hoverProgress);
    }

    private static void renderTabBackgroundForType(GuiGraphics guiGraphics, int topX, int topY, float partialTick, double scrollX, double scrollY, Type type, float alpha) {
        guiGraphics.pose().pushPose();
        if (type != Type.DEFAULT) {
            int i = (int) Math.round(scrollX);
            int j = (int) Math.round(scrollY);
            for (int parallaxX = -1; parallaxX <= (windowWidth + 128) / 128; parallaxX++) {
                for (int parallaxY = -1; parallaxY <= (windowWidth + 128) / 128; parallaxY++) {
                    BlitHelper.blitWithColor(guiGraphics, type.background1, parallaxX * 128 + i / 4, parallaxY * 128 + j / 4, 0.0F, 0.0F, 128, 128, 128, 128, 1F, 1F, 1F, alpha);
                    BlitHelper.blitWithColor(guiGraphics, type.background2, parallaxX * 128 + i / 2 - 1, parallaxY * 128 + j / 2, 0.0F, 0.0F, 128, 128, 128, 128, 1F, 1F, 1F, alpha);
                }
            }
        }
        guiGraphics.pose().popPose();
    }

    public static void tick() {
        previousHoverChangeProgress = hoverChangeProgress;
        if (previousHoverType != hoverType) {
            if (hoverChangeProgress < MAX_TRANSITION_TIME) {
                hoverChangeProgress += 1F;
            } else if (hoverChangeProgress > MAX_TRANSITION_TIME) {
                previousHoverType = hoverType;
            }
        } else {
            hoverChangeProgress = MAX_TRANSITION_TIME;
        }
    }

    private static float getHoverChangeAmount(float partialTick) {
        return (previousHoverChangeProgress + (hoverChangeProgress - previousHoverChangeProgress) * partialTick) / MAX_TRANSITION_TIME;
    }

    public static void setHoverType(Type type) {
        if (hoverChangeProgress >= MAX_TRANSITION_TIME && type != hoverType) {
            previousHoverChangeProgress = 0.0F;
            hoverChangeProgress = 0.0F;
            previousHoverType = hoverType;
            hoverType = type;
        }
    }

    public static void setDimensions(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    public enum Type {
        DEFAULT(ResourceLocation.fromNamespaceAndPath(BeyondHorizon.ID,String.format("%s/root", BeyondHorizon.ID)), 0),
        MAIN_STORY(BeyondHorizon.resource(String.format("%s/discover_arcane", BeyondHorizon.ID)), 0XF795CA);

        ResourceLocation root;

        private final int backgroundColor;
        private final ResourceLocation background1;
        private final ResourceLocation background2;


        Type(ResourceLocation root, int backgroundColor) {
            this.root = root;
            this.backgroundColor = backgroundColor;
            this.background1 = generateTexture("background1");
            this.background2 = generateTexture("background2");
        }

        private ResourceLocation generateTexture(String type) {
            return this == DEFAULT ? null : ResourceLocation.fromNamespaceAndPath(BeyondHorizon.ID, "textures/misc/advancement/" + this.name().toLowerCase(Locale.ROOT) + "_" + type + ".png");
        }

        private static Type getDirectType(Advancement advancement) {
            for (Type type : values()) {
                if (type.root.equals(advancement.getId())) {
                    return type;
                }
            }
            return DEFAULT;
        }

        public static Type forAdvancement(Advancement advancement) {
            Type direct = getDirectType(advancement);
            Advancement next = advancement;
            while (direct == DEFAULT && next.getParent() != null) {
                next = next.getParent();
                direct = getDirectType(next);
            }
            return direct;
        }

        public static boolean isTreeNodeUnlocked(AdvancementWidget advancementWidget) {
            if (advancementWidget.progress.isDone()) {
                return true;
            }
            Type direct = getDirectType(advancementWidget.advancement);
            AdvancementWidget next = advancementWidget;
            while (direct == DEFAULT && next.advancement.getParent() != null) {
                next = next.parent;
                direct = getDirectType(next.advancement);
            }
            return direct == DEFAULT || next.progress != null && next.progress.isDone();
        }
    }
}
