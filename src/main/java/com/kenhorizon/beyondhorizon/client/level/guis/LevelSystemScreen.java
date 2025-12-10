package com.kenhorizon.beyondhorizon.client.level.guis;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.client.entity.player.PlayerDataHandler;
import com.kenhorizon.beyondhorizon.client.keybinds.Keybinds;
import com.kenhorizon.beyondhorizon.client.level.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.level.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundConsumePointsPacket;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundSkillPointsPacket;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class LevelSystemScreen extends Screen {
    public enum Category {
        CLASS,
        TRAIT
    }
    public record SkillSets(int x, int y, RoleClass.AttributePoints attributePoints) {}

    private int posX;
    private int posY;
    private int imageW;
    private int imageH;
    private int scaledWindowWidth;
    private int scaledWindowHeight;
    private Player player;
    private LevelSystemScreen.Category category = Category.CLASS;
    private RoleClass role;
    public List<SkillSets> skillSets = new ArrayList<>();
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
        this.role = CapabilityCaller.roleClass(this.player);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderBackground(guiGraphics);
        int x = this.posX + 20;
        int y = this.posY + 10;
        RenderSystem.enableBlend();
        guiGraphics.blit(LOCATION, this.posX, this.posY, 0, 0, this.imageW, this.imageH);
        this.renderCategoryButtons(guiGraphics, this.category);
        BlitHelper.drawStrings(guiGraphics, player.getName(), x, y, ColorUtil.GRAY);
        RoleClass role = this.player.getCapability(BHCapabilties.ROLE_CLASS).orElseThrow(NullPointerException::new);
        guiGraphics.blit(LOCATION, this.posX + 126, this.posY + 10, 200, 0, 20, 12);
        guiGraphics.blit(LOCATION, this.posX + 149, this.posY + 10, 176, 12, 12, 12);
        BlitHelper.drawStrings(guiGraphics, String.format("%s", role.getPoints()), this.posX + 134, this.posY + 12, ColorUtil.WHITE, false);
        BlitHelper.drawStrings(guiGraphics, String.format("Level: %s", role.getLevel()), x, y + 10, ColorUtil.GRAY);

        if (this.category == Category.CLASS) {
            this.addButtonSkill(guiGraphics, this.posX, this.posY, RoleClass.AttributePoints.STRENGHT);
            this.addButtonSkill(guiGraphics, this.posX, this.posY + (33 * 1), RoleClass.AttributePoints.VITALITY);
            this.addButtonSkill(guiGraphics, this.posX, this.posY + (33 * 2), RoleClass.AttributePoints.CONSTITUION);
            this.addButtonSkill(guiGraphics, this.posX + 83, this.posY, RoleClass.AttributePoints.AGILITY);
            this.addButtonSkill(guiGraphics, this.posX + 83, this.posY + (33 * 1), RoleClass.AttributePoints.DEXERITY);
            this.addButtonSkill(guiGraphics, this.posX + 83, this.posY + (33 * 2), RoleClass.AttributePoints.INTELLIGENGE);
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

    private void addButtonSkill(GuiGraphics guiGraphics, int x, int y, RoleClass.AttributePoints attributePoints) {
        guiGraphics.blit(LOCATION, x + 7, y + 60, 0, 166, 79, 32);
        String text = attributePoints.getName();
        int pts = role.getPointOfSkills(attributePoints);
        String lvl = String.format("%s", pts);
        BlitHelper.drawStrings(guiGraphics, text, x + 12, y + 65, ColorUtil.WHITE, false);
        int colorPts = pts > 0 ? ColorUtil.WHITE : ColorUtil.combineRGB(255, 0, 0);
        BlitHelper.drawStrings(guiGraphics, "lvl:", x + 12, y + 80, ColorUtil.WHITE, false);
        BlitHelper.drawStrings(guiGraphics, lvl, x + 28, y + 80, colorPts, false);
        this.skillSets.add(new SkillSets(x + 7, y + 60, attributePoints));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        RoleClass role = CapabilityCaller.roleClass(this.player);
        if (!role.isAlreadyReachedRequiredLevel()) {
            return false;
        } else {
            int levelAddX = this.posX + 149;
            int levelAddY = this.posY + 10;
            if (mouseX >= levelAddX && mouseX <= levelAddX + 12 && mouseY >= levelAddY && mouseY <= levelAddY + 12) {
                if (this.player.experienceProgress > 0) {
                    NetworkHandler.sendToServer(new ServerboundConsumePointsPacket(this.player.getId(), 30));
                }
            }
            this.mouseClickedCategory(mouseX, mouseY);
            this.mouseSkillSets(this.skillSets, mouseX, mouseY);
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    private void mouseSkillSets(List<SkillSets> list, double mouseX, double mouseY) {
        if (role.getPoints() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LevelSystemScreen.SkillSets sets = list.get(i);
                if (mouseX >= sets.x() && mouseX <= sets.x() + 79 && mouseY >= sets.y() && mouseY <= sets.y() + 32) {
                    NetworkHandler.sendToServer(new ServerboundSkillPointsPacket(this.player.getId(), sets.attributePoints()));
                }
            }
        }
    }

    @Override
    public boolean keyPressed(int key, int scan, int modifiers) {
        if (key == Keybinds.LEVEL_SYSTEM.getKey().getValue()) {
            BeyondHorizon.PROXY.openScreen((Screen) null);
        }
        return super.keyPressed(key, scan, modifiers);
    }

    private void mouseClickedCategory(double mouseX, double mouseY) {
        List<LevelSystemScreen.Category> list = Arrays.stream(Category.values()).toList();
        for (int i = 0; i < list.size(); i++) {
            LevelSystemScreen.Category categorySelected = list.get(i);
            if (this.checkCategory(categorySelected, mouseX, mouseY)) {
                this.category = categorySelected;
            }
        }
    }
    private boolean checkCategory(LevelSystemScreen.SkillSets category, double mouseX, double mouseY) {
        int x = this.getTabX(category);
        int y = this.getTabY(category);
        return mouseX >= x && mouseX <= x + 26 && mouseY >= y && mouseY <= y + 28;
    }

    private boolean checkCategory(LevelSystemScreen.Category category, double mouseX, double mouseY) {
        int x = this.getTabX(category);
        int y = this.getTabY(category);
        return mouseX >= x && mouseX <= x + 26 && mouseY >= y && mouseY <= y + 28;
    }

    private int getTabX(LevelSystemScreen.SkillSets category) {
        List<LevelSystemScreen.Category> list = Arrays.stream(Category.values()).toList();
        int index = list.indexOf(category);
        return this.posX - 26;
    }

    private int getTabY(LevelSystemScreen.SkillSets category) {
        List<LevelSystemScreen.Category> list = Arrays.stream(Category.values()).toList();
        int index = list.indexOf(category);
        return this.posY + (6 + (28 * index));
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
