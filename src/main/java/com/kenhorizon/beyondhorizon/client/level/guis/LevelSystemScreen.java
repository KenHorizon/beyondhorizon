package com.kenhorizon.beyondhorizon.client.level.guis;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.level.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.level.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.classes.IRoleClass;
import com.kenhorizon.beyondhorizon.server.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

public class LevelSystemScreen extends Screen {
    public enum Category {
        CLASS,
        TRAIT
    }
    private int posX;
    private int posY;
    private int imageW;
    private int imageH;
    private int scaledWindowWidth;
    private int scaledWindowHeight;
    private Player player;
    private LevelSystemScreen.Category category = Category.CLASS;
    public static final ResourceLocation LOCATION = BeyondHorizon.resource("textures/gui/level_system/level_system.png");

    public LevelSystemScreen() {
        super(Component.empty());
        this.imageW = 176;
        this.imageH = 166;
    }

    @Override
    protected void init() {
        super.init();
        this.player = BeyondHorizon.PROXY.clientPlayer();
        this.scaledWindowWidth = minecraft.getWindow().getGuiScaledWidth();
        this.scaledWindowHeight = minecraft.getWindow().getGuiScaledHeight();
        this.posX = (this.scaledWindowWidth - this.imageW) / 2;
        this.posY = (this.scaledWindowHeight - this.imageH) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderBackground(guiGraphics);
        int x = this.posX + 20;
        int y = this.posY + 10;
        IRoleClass roleCallback = CapabilityCaller.roleClass(this.player);
        RoleClass role = roleCallback.getInstance();
        RenderSystem.enableBlend();
        guiGraphics.blit(LOCATION, this.posX, this.posY, 0, 0, this.imageW, this.imageH);
        this.renderCategoryButtons(guiGraphics, this.category);
        BlitHelper.drawStrings(guiGraphics, player.getName(), x, y, false);
        guiGraphics.blit(LOCATION, this.posX + 126, this.posY + 10, 200, 0, 20, 12);
        guiGraphics.blit(LOCATION, this.posX + 149, this.posY + 10, 176, 0, 12, 12);
        BlitHelper.drawStrings(guiGraphics, String.format("%s", role.getLevel()), this.posX + 134, this.posY + 12, ColorUtil.combineRGB(0, 255, 0), true);

        if (this.category == Category.CLASS) {
            this.addButtonSkill(guiGraphics, this.posX, this.posY);
            this.addButtonSkill(guiGraphics, this.posX, this.posY + (33 * 1));
            this.addButtonSkill(guiGraphics, this.posX, this.posY + (33 * 2));
            this.addButtonSkill(guiGraphics, this.posX + 83, this.posY);
            this.addButtonSkill(guiGraphics, this.posX + 83, this.posY + (33 * 1));
            this.addButtonSkill(guiGraphics, this.posX + 83, this.posY + (33 * 2));
            if (!role.isAlreadyReachedRequiredLevel()) {
                guiGraphics.fill(this.posX, this.posY, this.posX + this.imageW, this.posY + this.imageH, ColorUtil.combineARGB(100, 0, 0,0));
                String warningText = String.format("You need to be level %s", Constant.LEVEL_SYSTEM_UNLOCKED);
                BlitHelper.drawStrings(guiGraphics, warningText, (this.scaledWindowWidth - this.font.width(warningText)) / 2, this.scaledWindowHeight / 2, ColorUtil.combineRGB(200, 0 , 0), true);
            }
        }
        if (this.category == Category.TRAIT) {
            String warningText = String.format("You need to be level %s", Constant.LEVEL_SYSTEM_UNLOCKED);
            BlitHelper.drawStrings(guiGraphics, warningText, (this.scaledWindowWidth - this.font.width(warningText)) / 2, this.scaledWindowHeight / 2, ColorUtil.combineRGB(200, 0 , 0), true);
        }
    }

    private void renderCategoryButtons(GuiGraphics guiGraphics, LevelSystemScreen.Category category) {
        int i = 0;
        for (LevelSystemScreen.Category categorys : LevelSystemScreen.Category.values()) {
            i++;
            boolean selected = category == categorys;
            guiGraphics.blit(LOCATION, this.posX - 26, this.posY + (6 + (28 * (i - 1))), selected ? 203 : 176, 36, selected ? 32 : 26, 28);
            guiGraphics.blit(LOCATION, this.posX - 20, this.posY + (12 + (28 * (i - 1))), 176 + (16 * (i - 1)), 64, 16, 16);
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    private void addButtonSkill(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(LOCATION, x + 7, y + 60, 0, 166, 79, 32);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        IRoleClass roleCallback = CapabilityCaller.roleClass(this.player);
        RoleClass role = roleCallback.getInstance();
        if (!role.isAlreadyReachedRequiredLevel()) {
            return false;
        } else {
            this.mouseClickedCategory(mouseX, mouseY);
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    private void mouseClickedCategory(double mouseX, double mouseY) {
        List<LevelSystemScreen.Category> list = Arrays.stream(Category.values()).toList();
        for (int i = 0; i < list.size(); i++) {
            LevelSystemScreen.Category categorySelected = list.get(i);
            BeyondHorizon.loggers().debug("Category {}", this.checkCategory(categorySelected, mouseX, mouseY));
            if (this.checkCategory(categorySelected, mouseX, mouseY)) {
                this.category = categorySelected;
            }
        }
    }

    private boolean checkCategory(LevelSystemScreen.Category category, double mouseX, double mouseY) {
        int x = this.getTabX(category);
        int y = this.getTabY(category);
        BeyondHorizon.loggers().debug("Category x{} y{} tX{} tY{}", x, y, mouseX, mouseY);
        return mouseX >= x && mouseX <= x + 26 && mouseY >= y && mouseY <= y + 28;
    }

    private int getTabX(LevelSystemScreen.Category category) {
        List<LevelSystemScreen.Category> list = Arrays.stream(Category.values()).toList();
        int index = list.indexOf(category);
        return this.posX - 26;
    }

    private int getTabY(LevelSystemScreen.Category category) {
        List<LevelSystemScreen.Category> list = Arrays.stream(Category.values()).toList();
        int index = list.indexOf(category);
        return this.posY + (6 + (28 * index));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
