package com.kenhorizon.beyondhorizon.client.level.guis;

import com.google.common.collect.Lists;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.level.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.level.util.ColorUtil;
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
    public static int PADDING_INGREDIENTS = 20;
    public static final ResourceLocation LOCATION = BeyondHorizon.resource("textures/gui/container/workbench.png");

    public WorkbenchScreen(WorkbenchMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.blit(LOCATION, this.leftPos, this.topPos - 10, 0, 206, 82, 16);
        int x = this.leftPos + 7;
        int y = this.topPos + 9;
        int startIndexs = this.startIndex + 3;
        this.renderSrollBar(guiGraphics, mouseX, mouseY);
        this.renderRecipes(guiGraphics, x, y, mouseX, mouseY, startIndexs);
    }
    private float lerp(float speed, float current, float target) {
        return current + (target - current) * speed;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        int index = this.startIndex + 3;
        List<WorkbenchRecipe> list = this.menu.recipes;
        for (int i = this.startIndex; i < index && i < this.menu.recipes.size(); i++) {
            int indexs = i - this.startIndex;
            int posX = this.leftPos + 7;
            int posY = this.topPos + 11 + (20 * indexs);
            if (x >= posX && x <= posX + 16 && y >= posY && y <= posY + 16) {
                guiGraphics.renderTooltip(this.font, list.get(i).getResultItem(this.minecraft.level.registryAccess()), x, y);
            }
            this.renderTooltipIngredients(guiGraphics, this.leftPos + 40, this.topPos + 11 + (indexs * 20), x, y, index, i);
        }
    }

    private void renderTooltipIngredients(GuiGraphics guiGraphics, int recipesItemX, int recipesItemY, int x, int y, int index, int i) {
        WorkbenchRecipe recipe = menu.recipes.get(i);
        for (int j = 0; j < recipe.getIngredients().size(); j++) {
            if (x >= recipesItemX && x <= recipesItemX + 16 && y >= recipesItemY && y <= recipesItemY + 16) {
                ItemStack itemStack = recipe.getIngredients().get(j).getItems()[0];
                guiGraphics.renderTooltip(this.font, itemStack, x, y);
            }
            recipesItemX += PADDING_INGREDIENTS;
        }
    }

    private void renderSrollBar(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int scrollOffset = (int) (45.0F * this.scrollOffs);
        int x = this.leftPos + 155;
        int y = this.topPos + 12;
        guiGraphics.blit(LOCATION, x, y + scrollOffset, 176 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
    }

    public void renderRecipes(GuiGraphics guiGraphics, int posX, int posY, int mouseX, int mouseY, int startIndex) {
        for (int i = this.startIndex; i < startIndex && i < this.menu.recipes.size(); ++i) {
            int index = i - this.startIndex;
            int x = posX + 40;
            int y = posY + (index * 20) + 2;
            WorkbenchRecipe recipe = menu.recipes.get(i);
            boolean isHovering = this.isHovering(8, 12 + 18 * index, 146, 19, mouseX, mouseY);
            guiGraphics.blit(LOCATION, this.leftPos + 7, y, 0, isHovering ? 186 : 166, 146, 20);
            float scaling = isHovering ? 1.4F : 1.0F;
            float scale = this.lerp(0.2F, 1.0F, scaling);
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            float pX = x + 8;
            float pY = y + 8;
            poseStack.translate(pX, pY, 0);
            poseStack.scale(scale, scale, 1.0F);
            poseStack.translate(-pX, -pY, 0);
            guiGraphics.renderItem(recipe.getResultItem(null), this.leftPos + 10, y + 2);
            poseStack.popPose();
            for (int j = 0; j < recipe.getIngredients().size(); j++) {
                Ingredient ing = recipe.getIngredients().get(j);
                int need = recipe.getCounts().get(j);
                ItemStack item = ing.getItems()[0];
                guiGraphics.renderItem(item, x, y + 2);
                poseStack.pushPose();
                poseStack.translate(0, 0, 200);
                if (!(need == 1 || need == 0)) {
                    BlitHelper.drawStrings(guiGraphics, "x" + need, x + 8, y + 8, ColorUtil.combineRGB(255, 255, 255), true);
                }
                poseStack.popPose();
                x += PADDING_INGREDIENTS;
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
        int startIndex = this.startIndex + 3;
        for (int i = this.startIndex; i < startIndex; ++i) {
            int index = i - this.startIndex;
            double guiButtonX = this.leftPos + 8;
            double guiButtonY = this.topPos + 12 + (20 * index);
            for (int i0 = 0; i0 < menu.recipes.size(); i0++) {
                if (mouseX >= guiButtonX && mouseX <= guiButtonX + 146 && mouseY >= guiButtonY && mouseY <= guiButtonY + 20) {
                    WorkbenchRecipe recipe = menu.recipes.get(i0);
                    NetworkHandler.sendToServer(new ServerBoundWorkbenchCraftPacket(recipe.getId()) );
                }
            }
            int x = this.leftPos + 52;
            int y = this.topPos + 14;
            double d0 = mouseX - (double) (x + index % 3 * 16);
            double d1 = mouseY - (double) (y + index / 3 * 18);
            if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, i)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, i);
                return true;
            }
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
