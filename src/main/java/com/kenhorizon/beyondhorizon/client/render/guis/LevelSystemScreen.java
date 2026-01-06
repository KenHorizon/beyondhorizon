package com.kenhorizon.beyondhorizon.client.render.guis;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.api.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.api.classes.RoleClasses;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundClassSelectionPacket;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundConsumePointsPacket;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundSkillPointsPacket;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.*;
import java.util.function.Predicate;

public class LevelSystemScreen extends Screen {
    public enum Category {
        ATTRIBUTES(RoleClass::isAlreadyReachedRequiredLevel),
        CLASS(RoleClass::isUnlockedClassAndTraits),
        TRAIT(RoleClass::isUnlockedClassAndTraits);

        private Predicate<RoleClass> levelRequired;
        private boolean subCategory;
        Category(Predicate<RoleClass> levelRequired, boolean subCategory) {
            this.levelRequired = levelRequired;
            this.subCategory = subCategory;
        }
        Category(Predicate<RoleClass> levelRequired) {
            this(levelRequired, false);
        }
        public Predicate<RoleClass> getFilter() {
            return levelRequired;
        }

        public boolean isSubCategory() {
            return subCategory;
        }
    }

    public enum SubCategory {
        NONE,
        CLASS_INFO
    }

    public record AttributePoint(int x, int y, RoleClass.AttributePoints attributePoints) {}
    public record AttributeRemovePoints(int x, int y, RoleClass.AttributePoints attributePoints) {}
    public record SelectionClass(int x, int y, RoleClass roleClass) {}

    private int buttonCooldown;
    private final int buttonCooldownMax = 5;
    private int posX;
    private int posY;
    private int imageW;
    private int imageH;
    private int scaledWindowWidth;
    private int scaledWindowHeight;
    private Player player;
    private LevelSystemScreen.Category category = Category.ATTRIBUTES;
    private LevelSystemScreen.SubCategory subCategory = SubCategory.NONE;
    private RoleClass role;
    private RoleClass selectedRoles;
    public List<SelectionClass> selectionClass = new ArrayList<>();
    public List<AttributePoint> attributePoints = new ArrayList<>();
    public List<AttributeRemovePoints> attributeRemovePoints = new ArrayList<>();
    public static final ResourceLocation LOCATION = BeyondHorizon.resourceGui("level_system/level_system.png");
    public static final ResourceLocation SKILL_TREE = BeyondHorizon.resourceGui("level_system/skill_tree.png");

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
        if (this.subCategory == SubCategory.NONE) {
            BlitHelper.drawStrings(guiGraphics, player.getName(), x, y, ColorUtil.GRAY);
            guiGraphics.blit(LOCATION, this.posX + 126, this.posY + 10, 200, 0, 20, 12);
            boolean cantGainExp = role.getLevel() >= role.maxLevel;
            guiGraphics.blit(LOCATION, this.posX + 149, this.posY + 10, 176, cantGainExp ? 0 : 12, 12, 12);
            guiGraphics.blit(LOCATION, this.posX + 20, this.posY + 43, 79, 166, 131, 5);
            guiGraphics.blit(LOCATION, this.posX + 20, this.posY + 43, 79, 171, (int) (role.expProgress * 131), 5);
            String pts = String.format("%s", role.getPoints());
            BlitHelper.drawStrings(guiGraphics, pts, this.posX - (this.font.width(pts) / 2) + 134, this.posY + 12, ColorUtil.WHITE, false);
            String level = String.format("Level: %s", role.getLevel());
            String roleclass = String.format("Class: %s", role.getRoles().getName());
            BlitHelper.drawStrings(guiGraphics, level, x, y + 10, ColorUtil.GRAY);
            BlitHelper.drawStrings(guiGraphics, roleclass, x, y + 20, ColorUtil.GRAY);
        }

        if (this.category == Category.ATTRIBUTES) {
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
        if (this.category == Category.CLASS) {
            if (this.subCategory == SubCategory.CLASS_INFO) {
                guiGraphics.blit(LOCATION, this.posX + 129, this.posY + 12, 220, 0, 18, 18);
                BlitHelper.drawStrings(guiGraphics, this.selectedRoles.getRoles().getName(), this.posX + 7, this.posY + 60, ColorUtil.GRAY);
                for (int i = 0 ; i < this.selectedRoles.getRoleDescription().size(); i++) {
                    Component text = this.selectedRoles.getRoleDescription().get(i);
                    BlitHelper.drawStrings(guiGraphics, text, this.posX + 7, this.posY + 12 + (i * 9), ColorUtil.GRAY);
                }
            } else {
                this.addSelectionButton(guiGraphics, this.posX, this.posY, RoleClasses.ASSASSIN.get());
                this.addSelectionButton(guiGraphics, this.posX, this.posY + (33 * 1), RoleClasses.MARKSMAN.get());
                this.addSelectionButton(guiGraphics, this.posX, this.posY + (33 * 2), RoleClasses.CASTER.get());
                this.addSelectionButton(guiGraphics, this.posX + 83, this.posY, RoleClasses.STRIKER.get());
                this.addSelectionButton(guiGraphics, this.posX + 83, this.posY + (33 * 1), RoleClasses.VANGUARD.get());
                this.addSelectionButton(guiGraphics, this.posX + 83, this.posY + (33 * 2), RoleClasses.SUPPORT.get());
                if (!role.isUnlockedClassAndTraits()) {
                    guiGraphics.fill(this.posX, this.posY, this.posX + this.imageW, this.posY + this.imageH, ColorUtil.combineARGB(100, 0, 0,0));
                    String warningText = String.format("You need to be level %s", Constant.CLASS_SYSTEM_UNLOCKED);
                    BlitHelper.drawStrings(guiGraphics, warningText, (this.scaledWindowWidth - this.font.width(warningText)) / 2, this.scaledWindowHeight / 2, ColorUtil.combineRGB(200, 0 , 0), true);
                }
            }
        }
        if (this.category == Category.TRAIT) {
            String warningText = String.format("You need to be level %s", Constant.LEVEL_SYSTEM_UNLOCKED);
            BlitHelper.drawStrings(guiGraphics, warningText, (this.scaledWindowWidth - this.font.width(warningText)) / 2, this.scaledWindowHeight / 2, ColorUtil.combineRGB(200, 0 , 0), true);
        }
    }

    private void renderCategoryButtons(GuiGraphics guiGraphics, LevelSystemScreen.Category category) {
        RoleClass role = CapabilityCaller.roleClass(this.player);
        int i = 0;
        role.getLevel();
        for (LevelSystemScreen.Category categorys : LevelSystemScreen.Category.values()) {
            if (categorys.isSubCategory()) continue;
            i++;
            boolean selected = category == categorys;
            guiGraphics.blit(LOCATION, this.posX - 26, this.posY + (6 + (28 * (i - 1))), selected ? 203 : 176, 36, selected ? 32 : 26, 28);
            guiGraphics.blit(LOCATION, this.posX - 20, this.posY + (12 + (28 * (i - 1))), 176 + (16 * (i - 1)), 64, 16, 16);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.buttonCooldown > 0) {
            this.buttonCooldown--;
        }
    }

    private void addButtonSkill(GuiGraphics guiGraphics, int x, int y, RoleClass.AttributePoints attributePoints) {
        int pts = role.getPointOfSkills(attributePoints);
        int attributePts = role.getPoints();
        guiGraphics.blit(LOCATION, x + 7, y + 60, 0, 166, 79, 32);
        String text = attributePoints.getName();
        String lvl = String.format("%s", pts);
        BlitHelper.drawStrings(guiGraphics, text, x + 12, y + 65, ColorUtil.WHITE, false);
        int colorPts = pts > 0 ? ColorUtil.WHITE : ColorUtil.combineRGB(255, 0, 0);
        BlitHelper.drawStrings(guiGraphics, "lvl:", x + 12, y + 80, ColorUtil.WHITE, false);
        BlitHelper.drawStrings(guiGraphics, lvl, x + 28, y + 80, colorPts, false);
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, 0, 200.0F);
        guiGraphics.blit(LOCATION, x + 7 + 62, y + 64 + 10, 188, pts > 0 ? 12 : 0, 12, 12);
        guiGraphics.blit(LOCATION, x + 7 + 48, y + 64 + 10, 176, attributePts > 0 ? 12 : 0, 12, 12);
        poseStack.popPose();
        this.attributePoints.add(new AttributePoint(x + 7 + 48, y + 64 + 10, attributePoints));
        this.attributeRemovePoints.add(new AttributeRemovePoints(x + 7 + 64, y + 62 + 10, attributePoints));
    }

    private void addSelectionButton(GuiGraphics guiGraphics, int x, int y, RoleClass roleClass) {
        boolean isMatched = roleClass.equals(this.role.getRoles());
        if (isMatched) {
            guiGraphics.blit(LOCATION, x + 7, y + 60, 0, 198, 79, 32);
        } else {
            guiGraphics.blit(LOCATION, x + 7, y + 60, 0, 166, 79, 32);
        }
        String format = String.format("%s", roleClass.getName());
        BlitHelper.drawStrings(guiGraphics, format, x + 12, y + 65, ColorUtil.WHITE, false);
        this.selectionClass.add(new SelectionClass(x + 7, y + 60, roleClass.getRoles()));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!role.isAlreadyReachedRequiredLevel()) {
            return false;
        } else {
            int levelAddX = this.posX + 149;
            int levelAddY = this.posY + 10;
            this.mouseClickedCategory(mouseX, mouseY);
            if (this.category == Category.ATTRIBUTES) {
                boolean isUnlocked = this.category.getFilter().test(role);
                if (isUnlocked) {
                    if (mouseX >= levelAddX && mouseX <= levelAddX + 12 && mouseY >= levelAddY && mouseY <= levelAddY + 12) {
                        if (this.player.experienceProgress > 0) {
                            NetworkHandler.sendToServer(new ServerboundConsumePointsPacket(this.player.getId(), 30));
                        }
                    }
                    this.mouseSkillSetsRemovePoints(this.attributeRemovePoints, mouseX, mouseY);
                    this.mouseSkillSets(this.attributePoints, mouseX, mouseY);
                }
            }
            if (this.category == Category.CLASS) {
                if (this.subCategory == SubCategory.CLASS_INFO) {
                    int backX = this.posX + 9;
                    int backY = this.posY + 11;
                    if (mouseX >= backX && mouseX <= backX + 12 && mouseY >= backY && mouseY <= backY + 12) {
                        this.subCategory = SubCategory.NONE;
                    }
                    int confirmX = this.posX + 128;
                    int confirmY = this.posY + 12;
                    if (mouseX >= confirmX && mouseX <= confirmX + 12 && mouseY >= confirmY && mouseY <= confirmY + 12) {
                        NetworkHandler.sendToServer(new ServerboundClassSelectionPacket(this.player.getId(), this.selectedRoles));
                    }
                }
                boolean isUnlocked = this.category.getFilter().test(role);
                boolean jobless = this.role.getRoles() == RoleClasses.NONE.get();
                this.mouseClassSelectionInfo(this.selectionClass, mouseX, mouseY);
            }
            if (this.category == Category.TRAIT) {
                boolean isUnlocked = this.category.getFilter().test(role);
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    private void mouseSkillSetsRemovePoints(List<AttributeRemovePoints> list, double mouseX, double mouseY) {
        if (role.getPoints() > 0) {
            for (int i = 0; i < list.size(); i++) {
                AttributeRemovePoints sets = list.get(i);
                if (this.buttonCooldown == 0 && mouseX >= sets.x() && mouseX <= sets.x() + 12 && mouseY >= sets.y() && mouseY <= sets.y() + 12) {
                    NetworkHandler.sendToServer(new ServerboundSkillPointsPacket(this.player.getId(), sets.attributePoints(), -1));
                    this.buttonCooldown = this.buttonCooldownMax;
                }
            }
        }
    }

    private void mouseClassSelectionInfo(List<SelectionClass> list, double mouseX, double mouseY) {
        for (int i = 0; i < list.size(); i++) {
            SelectionClass sets = list.get(i);
            if (this.buttonCooldown == 0 && mouseX >= sets.x() && mouseX <= sets.x() + 79 && mouseY >= sets.y() && mouseY <= sets.y() + 32) {
                this.subCategory = SubCategory.CLASS_INFO;
                this.selectedRoles = sets.roleClass();
                this.buttonCooldown = this.buttonCooldownMax;
            }
        }
    }


    private void mouseClassSelection(List<SelectionClass> list, double mouseX, double mouseY) {
        if (role.getPoints() > 0) {
            for (int i = 0; i < list.size(); i++) {
                SelectionClass sets = list.get(i);
                if (this.buttonCooldown == 0 && mouseX >= sets.x() && mouseX <= sets.x() + 12 && mouseY >= sets.y() && mouseY <= sets.y() + 12) {
                    NetworkHandler.sendToServer(new ServerboundClassSelectionPacket(this.player.getId(), sets.roleClass()));
                    this.buttonCooldown = this.buttonCooldownMax;
                }
            }
        }
    }

    private void mouseSkillSets(List<AttributePoint> list, double mouseX, double mouseY) {
        if (role.getPoints() > 0) {
            for (int i = 0; i < list.size(); i++) {
                AttributePoint sets = list.get(i);
                if (this.buttonCooldown == 0 && mouseX >= sets.x() && mouseX <= sets.x() + 12 && mouseY >= sets.y() && mouseY <= sets.y() + 12) {
                    NetworkHandler.sendToServer(new ServerboundSkillPointsPacket(this.player.getId(), sets.attributePoints(), 1));
                    this.buttonCooldown = this.buttonCooldownMax;
                }
            }
        }
    }

    private void mouseClickedCategory(double mouseX, double mouseY) {
        List<LevelSystemScreen.Category> list = Arrays.stream(Category.values()).toList();
        for (int i = 0; i < list.size(); i++) {
            LevelSystemScreen.Category categorySelected = list.get(i);
            if (this.checkCategory(categorySelected, mouseX, mouseY)) {
                this.category = categorySelected;
                this.subCategory = SubCategory.NONE;
            }
        }
    }

    private boolean checkCategory(LevelSystemScreen.Category category, double mouseX, double mouseY) {
        int x = this.getTabX(category);
        int y = this.getTabY(category);
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
    public boolean shouldCloseOnEsc() {
        return super.shouldCloseOnEsc();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
