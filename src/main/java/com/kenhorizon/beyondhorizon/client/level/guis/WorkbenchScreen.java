package com.kenhorizon.beyondhorizon.client.level.guis;

import com.google.common.collect.Lists;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.inventory.WorkbenchMenu;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerBoundWorkbenchCraftPacket;
import com.kenhorizon.beyondhorizon.server.recipe.WorkbenchRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Locale;

public class WorkbenchScreen extends AbstractContainerScreen<WorkbenchMenu> {
    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    public static final int VISIBLE_ROW = 3;
    public static final int ROW_HEIGHT = 20;
    public static final ResourceLocation LOCATION = BeyondHorizon.resource("textures/gui/container/workbench.png");

    public WorkbenchScreen(WorkbenchMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseX);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int y = this.topPos + 8;
        int startIndexs = this.startIndex + 3;
        this.renderSrollBar(guiGraphics, mouseX, mouseY);
        this.renderRecipes(guiGraphics, y, mouseX, mouseY, startIndexs);
    }
    private float lerp(float speed, float current, float target) {
        return current + (target - current) * speed;
    }

    private void renderSrollBar(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int scrollOffset = (int) (36.0F * this.scrollOffs);
        int x = this.leftPos + 154;
        int y = this.topPos + 14;
        int scrollBar = y + (scrollOffset * (63 - 15));
        guiGraphics.blit(LOCATION, x, scrollBar, 176 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);

    }
    public void renderRecipes(GuiGraphics guiGraphics, int posY, int mouseX, int mouseY, int startIndex) {

        for (int i = this.startIndex; i < startIndex && i < this.menu.recipes.size(); ++i) {
            int index = i - this.startIndex;
            int x = this.leftPos + 40;
            int y = posY + (index * 20) + 2;

            WorkbenchRecipe recipe = menu.recipes.get(i);
            boolean isHovering = this.isHovering(7, y, 146, 20, mouseX, mouseY);
            guiGraphics.blit(LOCATION, this.leftPos + 8, y, 0, isHovering ? 206 : 166, 146, 20);
            float scaling = isHovering ? 1.4F : 1.0F;
            float scale = lerp(0.2F, 1.0F, scaling);
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            float pX = y + 8;
            float pY = y + 8;
            poseStack.translate(pX, pY, 0);
            poseStack.scale(scale, scale, 1F);
            poseStack.translate(-pX, -pY, 0);
            guiGraphics.renderItem(recipe.getResultItem(null), this.leftPos + 10, y);
            poseStack.popPose();
            for (int j = 0; j < recipe.getIngredients().size(); j++) {
                Ingredient ing = recipe.getIngredients().get(j);
                int need = recipe.getCounts().get(j);
                ItemStack example = ing.getItems()[0];
                guiGraphics.renderItem(example, x, y);
                guiGraphics.drawString(this.font, "x" + need, x + 8, y + 8, 0xFFFFFF);
                x += 30;
            }
        }
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.isScrollBarActive()) {
            int row = this.getOffscreenRows();
            float percent = (float) delta / (float) row;
            this.scrollOffs = Mth.clamp(this.scrollOffs - percent, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) row) + 0.5D) * 3;
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;
        int x = this.leftPos + 52;
        int y = this.topPos + 14;
        int startIndex = this.startIndex + 12;
        for (int i = this.startIndex; i < startIndex; ++i) {
            int index = i - this.startIndex;
            int iniCol = index % 3;
            int iniRow = index / 4;
            double guiButtonX = this.leftPos + 8 + iniCol * 48;
            double guiButtonY = this.topPos + 8 + iniRow * 20;
            for (int i0 = 0; i0 < menu.recipes.size(); i0++) {
                if (mouseX >= guiButtonX && mouseX <= guiButtonX + 146 && mouseY >= guiButtonY && mouseY <= guiButtonY + 20) {
                    WorkbenchRecipe recipe = menu.recipes.get(i0);
                    NetworkHandler.sendToServer(new ServerBoundWorkbenchCraftPacket(recipe.getId()) );
                }
            }
            double d0 = mouseX - (double) (x + index % 3 * 16);
            double d1 = mouseY - (double) (y + index / 3 * 18);
            if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, i)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, i);
                return true;
            }
        }

        x = this.leftPos + 119;
        y = this.topPos + 9;
        if (mouseX >= (double) x && mouseX < (double) (x + 12) && mouseY >= (double) y && mouseY < (double) (y + 54)) {
            this.scrolling = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int x = this.topPos + 154;
            int y = x + 14;
            this.scrollOffs = ((float) mouseY - (float) x - 7.5F) / ((float) (x - y) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5D) * 3;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    private boolean isScrollBarActive() {
        return this.menu.recipes.size() > 3;
    }

    protected int getOffscreenRows() {
        int row = 1;
        return (this.menu.recipes.size() + row - 1) / row - (row - 1);
    }
}
