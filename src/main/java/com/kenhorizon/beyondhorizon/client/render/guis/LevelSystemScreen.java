package com.kenhorizon.beyondhorizon.client.render.guis;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.api.level_system.LevelSystem;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.world.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.world.network.packet.server.ServerboundConsumePointsPacket;
import com.kenhorizon.beyondhorizon.server.world.network.packet.server.ServerboundLevelPointsPacket;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class LevelSystemScreen extends Screen {
    public record AttributePoint(int x, int y, LevelSystem.AttributePoints attributePoints) {}
    public record AttributeRemovePoints(int x, int y, LevelSystem.AttributePoints attributePoints) {}

    private int buttonCooldown;
    private final int buttonCooldownMax = 5;
    private int posX;
    private int posY;
    private int imageW;
    private int imageH;
    private int scaledWindowWidth;
    private int scaledWindowHeight;
    private LevelSystem role;
    private Player player;
    private float expProgress;
    public List<AttributePoint> attributePoints = new ArrayList<>();
    public List<AttributeRemovePoints> attributeRemovePoints = new ArrayList<>();
    public static final ResourceLocation LOCATION = BeyondHorizon.resourceGui("level_system/level_system.png");

    public LevelSystemScreen() {
        super(Component.empty());
        this.imageW = 176;
        this.imageH = 166;
    }

    @Override
    protected void init() {
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(BHSounds.LEVEL_SYSTEM_OPEN.get(), 1.0F, 3.0F));
        this.player = BeyondHorizon.PROXY.clientPlayer();
        this.scaledWindowWidth = minecraft.getWindow().getGuiScaledWidth();
        this.scaledWindowHeight = minecraft.getWindow().getGuiScaledHeight();
        this.posX = (this.scaledWindowWidth - this.imageW) / 2;
        this.posY = (this.scaledWindowHeight - this.imageH) / 2;
        this.role = Capabilities.levelSystem(this.player);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderBackground(guiGraphics);
        int x = this.posX + 20;
        int y = this.posY + 10;
        RenderSystem.enableBlend();
        guiGraphics.blit(LOCATION, this.posX, this.posY, 0, 0, this.imageW, this.imageH);

        this.expProgress = this.role.expProgress;
        guiGraphics.blit(LOCATION, this.posX + 126, this.posY + 10, 200, 0, 20, 12);
        boolean cantGainExp = this.role.getLevel() >= this.role.maxLevel;
        guiGraphics.blit(LOCATION, this.posX + 149, this.posY + 10, 176, cantGainExp ? 0 : 12, 12, 12);
        guiGraphics.blit(LOCATION, this.posX + 20, this.posY + 43, 79, 166, 131, 6);
        guiGraphics.blit(LOCATION, this.posX + (20 - 6), this.posY + (42), 79, 178, 8, 8);
        guiGraphics.blit(LOCATION, this.posX + 20, this.posY + 43, 79, 172, (int) (this.expProgress * 131), 6);
        String pts = String.format("%s", this.role.getPoints());
        String level = "Lvl: ";
        String levelPTS = String.format("%s", this.role.getLevel());
        int levelString = level.length();
        String xpRequired = String.format("%s/%s", Maths.format(this.role.getExpProgress()), Maths.format(this.role.getXpNeededForNextLevel()));
        BlitHelper.drawStrings(minecraft.font, guiGraphics, xpRequired, this.posX + 20, this.posY + 34, Colors.GREEN);
        BlitHelper.drawStrings(minecraft.font,guiGraphics, pts, this.posX - (this.font.width(pts) / 2) + 136, this.posY + 12, Colors.WHITE);
        BlitHelper.drawStrings(minecraft.font,guiGraphics, player.getName(), x, y, Colors.WHITE);
        BlitHelper.drawStrings(minecraft.font,guiGraphics, level, x, y + 10, Colors.WHITE);
        BlitHelper.drawStrings(minecraft.font,guiGraphics, levelPTS, x + 10 + 4 + levelString, y + 10 , Colors.GREEN);

        this.addButtonSkill(guiGraphics, this.posX, this.posY, LevelSystem.AttributePoints.STRENGHT);
        this.addButtonSkill(guiGraphics, this.posX, this.posY + (33 * 1), LevelSystem.AttributePoints.VITALITY);
        this.addButtonSkill(guiGraphics, this.posX, this.posY + (33 * 2), LevelSystem.AttributePoints.CONSTITUION);
        this.addButtonSkill(guiGraphics, this.posX + 83, this.posY, LevelSystem.AttributePoints.AGILITY);
        this.addButtonSkill(guiGraphics, this.posX + 83, this.posY + (33 * 1), LevelSystem.AttributePoints.DEXERITY);
        this.addButtonSkill(guiGraphics, this.posX + 83, this.posY + (33 * 2), LevelSystem.AttributePoints.INTELLIGENGE);
        if (!this.role.isAlreadyReachedRequiredLevel()) {
            guiGraphics.fill(this.posX, this.posY, this.posX + this.imageW, this.posY + this.imageH, Colors.combineARGB(100, 0, 0, 0));
            String warningText = String.format("You need to be level %s", Constant.LEVEL_SYSTEM_UNLOCKED);
            BlitHelper.drawStrings(minecraft.font, guiGraphics, warningText, (this.scaledWindowWidth - this.font.width(warningText)) / 2, this.scaledWindowHeight / 2, Colors.combineRGB(200, 0, 0), true);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.buttonCooldown > 0) {
            this.buttonCooldown--;
        }
    }

    private void addButtonSkill(GuiGraphics guiGraphics, int x, int y, LevelSystem.AttributePoints attributePoints) {
        int pts = this.role.getPointOfSkills(attributePoints);
        int attributePts = this.role.getPoints();
        guiGraphics.blit(LOCATION, x + 7, y + 60, 0, 166, 79, 32);
        String text = attributePoints.getName();
        String lvl = String.format("%s", pts);
        BlitHelper.drawStrings(minecraft.font,guiGraphics, text, x + 12, y + 65, Colors.WHITE, false);
        int colorPts = pts > 0 ? Colors.WHITE : Colors.combineRGB(255, 0, 0);
        int lvlPY = y + 76;
        BlitHelper.drawStrings(minecraft.font,guiGraphics, "lvl:", x + 12, lvlPY, Colors.WHITE, false);
        BlitHelper.drawStrings(minecraft.font,guiGraphics, lvl, x + 34, lvlPY, colorPts, false);
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, 0, 200.0F);
        guiGraphics.blit(LOCATION, x + 7 + 48, y + 64 + 10, 176, attributePts > 0 ? 12 : 0, 12, 12);
        guiGraphics.blit(LOCATION, x + 7 + 62, y + 64 + 10, 188, pts > 0 ? 12 : 0, 12, 12);
        poseStack.popPose();
        this.attributePoints.add(new AttributePoint(x + 7 + 48, y + 64 + 10, attributePoints));
        this.attributeRemovePoints.add(new AttributeRemovePoints(x + 7 + 62, y + 64 + 10, attributePoints));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.role.isAlreadyReachedRequiredLevel()) {
            return false;
        } else {
            int levelAddX = this.posX + 149;
            int levelAddY = this.posY + 10;

            this.addPoints(mouseX, mouseY, levelAddX, levelAddY);
            this.attributeAddPoints(this.attributePoints, mouseX, mouseY);
            this.attributeRemovePoints(this.attributeRemovePoints, mouseX, mouseY);

            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    private void addPoints(double mouseX, double mouseY, int levelAddX, int levelAddY) {
        if (mouseX >= levelAddX && mouseX <= levelAddX + 12 && mouseY >= levelAddY && mouseY <= levelAddY + 12) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(BHSounds.LEVEL_SYSTEM_ADD.get(), 1.0F));
            NetworkHandler.sendToServer(new ServerboundConsumePointsPacket(this.player.getId(), 30));
        }
    }

    private void attributeAddPoints(List<AttributePoint> list, double mouseX, double mouseY) {
        if (this.role.getPoints() > 0) {
            for (int i = 0; i < list.size(); i++) {
                AttributePoint sets = list.get(i);
                if (this.buttonCooldown <= 0 && mouseX >= sets.x() && mouseX <= sets.x() + 12 && mouseY >= sets.y() && mouseY <= sets.y() + 12) {
                    NetworkHandler.sendToServer(new ServerboundLevelPointsPacket(this.player.getId(), sets.attributePoints(), 1));
                    this.buttonCooldown = this.buttonCooldownMax;
                }
            }
        }
    }

    private void attributeRemovePoints(List<AttributeRemovePoints> list, double mouseX, double mouseY) {
        for (AttributeRemovePoints sets : list) {
            int pts = this.role.getPointOfSkills(sets.attributePoints());
            if (pts > 0 && this.buttonCooldown == 0 && mouseX >= sets.x() && mouseX <= sets.x() + 12 && mouseY >= sets.y() && mouseY <= sets.y() + 12) {
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(BHSounds.LEVEL_SYSTEM_REMOVE.get(), 1.0F));
                NetworkHandler.sendToServer(new ServerboundLevelPointsPacket(this.player.getId(), sets.attributePoints(), -1));
                this.buttonCooldown = this.buttonCooldownMax;
            }
        }
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
