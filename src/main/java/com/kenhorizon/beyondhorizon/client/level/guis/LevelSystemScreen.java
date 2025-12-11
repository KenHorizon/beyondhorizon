package com.kenhorizon.beyondhorizon.client.level.guis;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.level.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.level.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundConsumePointsPacket;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundSkillPointsPacket;
import com.kenhorizon.beyondhorizon.server.skills.Skill;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.management.relation.Role;
import java.util.*;
import java.util.function.Predicate;

public class LevelSystemScreen extends Screen {
    public enum Category {
        ATTRIBUTES(RoleClass::isAlreadyReachedRequiredLevel),
        TRAIT(RoleClass::isUnlockedClassAndTraits),
        CLASS(RoleClass::isUnlockedClassAndTraits);

        private Predicate<RoleClass> levelRequired;
        Category(Predicate<RoleClass> levelRequired) {
            this.levelRequired = levelRequired;
        }
        public Predicate<RoleClass> getFilter() {
            return levelRequired;
        }
    }
    public record AttributePoint(int x, int y, RoleClass.AttributePoints attributePoints) {}
    public record AttributeRemovePoints(int x, int y, RoleClass.AttributePoints attributePoints) {}

    private int buttonCooldown;
    private final int buttonCooldownMax = 20;
    private int posX;
    private int posY;
    private int imageW;
    private int imageH;
    private int scaledWindowWidth;
    private int scaledWindowHeight;
    private Player player;
    private LevelSystemScreen.Category category = Category.ATTRIBUTES;
    private RoleClass role;
    public List<AttributePoint> attributePoints = new ArrayList<>();
    public List<AttributeRemovePoints> attributeRemovePoints = new ArrayList<>();
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
        boolean cantGainExp = role.getLevel() >= role.maxLevel;
        guiGraphics.blit(LOCATION, this.posX + 149, this.posY + 10, 176, cantGainExp ? 0 : 12, 12, 12);
        guiGraphics.blit(LOCATION, this.posX + 20, this.posY + 43, 79, 166, 131, 5);
        guiGraphics.blit(LOCATION, this.posX + 20, this.posY + 43, 79, 171, (int) (role.expProgress * 131), 5);
        String pts = String.format("%s", role.getPoints());
        BlitHelper.drawStrings(guiGraphics, pts, this.posX - (this.font.width(pts) / 2) + 134, this.posY + 12, ColorUtil.WHITE, false);
        String level = String.format("Level: %s", role.getLevel());
        BlitHelper.drawStrings(guiGraphics, level, x, y + 10, ColorUtil.GRAY);

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
        if (this.category == Category.TRAIT) {
            String warningText = String.format("You need to be level %s", Constant.LEVEL_SYSTEM_UNLOCKED);
            BlitHelper.drawStrings(guiGraphics, warningText, (this.scaledWindowWidth - this.font.width(warningText)) / 2, this.scaledWindowHeight / 2, ColorUtil.combineRGB(200, 0 , 0), true);
        }
        if (this.category == Category.CLASS) {
            String warningText = String.format("You need to be level %s", Constant.CLASS_SYSTEM_UNLOCKED);
            BlitHelper.drawStrings(guiGraphics, warningText, (this.scaledWindowWidth - this.font.width(warningText)) / 2, this.scaledWindowHeight / 2, ColorUtil.combineRGB(200, 0 , 0), true);
        }
    }

    private void renderCategoryButtons(GuiGraphics guiGraphics, LevelSystemScreen.Category category) {
        RoleClass role = CapabilityCaller.roleClass(this.player);
        int i = 0;
        role.getLevel();
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
        if (this.buttonCooldown > 0) {
            this.buttonCooldown--;
        }
    }

    private void addButtonSkill(GuiGraphics guiGraphics, int x, int y, RoleClass.AttributePoints attributePoints) {
        int pts = role.getPointOfSkills(attributePoints);
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
        guiGraphics.blit(LOCATION, x + 7 + 48, y + 64 + 10, 176, pts > 0 ? 12 : 0, 12, 12);
        poseStack.popPose();
        this.attributePoints.add(new AttributePoint(x + 7 + 48, y + 64 + 10, attributePoints));
        this.attributeRemovePoints.add(new AttributeRemovePoints(x + 7 + 64, y + 62 + 10, attributePoints));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        RoleClass role = CapabilityCaller.roleClass(this.player);
        if (!role.isAlreadyReachedRequiredLevel()) {
            return false;
        } else {
            int levelAddX = this.posX + 149;
            int levelAddY = this.posY + 10;
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
                return isUnlocked;
            }
            if (this.category == Category.CLASS || this.category == Category.TRAIT) {
                boolean isUnlocked = this.category.getFilter().test(role);
                return isUnlocked;
            }
            this.mouseClickedCategory(mouseX, mouseY);
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
            }
        }
    }

    private boolean checkCategory(AttributePoint category, double mouseX, double mouseY) {
        int x = this.getTabX(category);
        int y = this.getTabY(category);
        return mouseX >= x && mouseX <= x + 26 && mouseY >= y && mouseY <= y + 28;
    }

    private boolean checkCategory(LevelSystemScreen.Category category, double mouseX, double mouseY) {
        int x = this.getTabX(category);
        int y = this.getTabY(category);
        return mouseX >= x && mouseX <= x + 26 && mouseY >= y && mouseY <= y + 28;
    }

    private int getTabX(AttributePoint category) {
        List<LevelSystemScreen.Category> list = Arrays.stream(Category.values()).toList();
        int index = list.indexOf(category);
        return this.posX - 26;
    }

    private int getTabY(AttributePoint category) {
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
