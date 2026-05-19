package com.kenhorizon.beyondhorizon.client.render.guis;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.inventory.WorkbenchMenu;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundWorkbenchCraftPacket;
import com.kenhorizon.beyondhorizon.server.recipe.WorkbenchRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.List;

public class WorkbenchScreen extends AbstractContainerScreen<WorkbenchMenu> {
    private float scrollOffs;
    private boolean visible;
    private boolean scrolling;
    private int startIndex;
    public static int POS_Y = 24;
    public static int ROW = 6;
    public static int COLUMN = 6;
    public static int PADDING_INGREDIENTS = 22;
    public static int PADDING_Y = 24;
    public static final ResourceLocation RESOURCE_GUI = BeyondHorizon.resourceGui("container/workbench.png");
    public static final ResourceLocation RESOURCE_GUI_RECIPE = BeyondHorizon.resourceGui("container/workbench_recipe.png");
    private int timesInventoryChanged;
    private static final Component SEARCH_HINT = Component.translatable("gui.recipebook.search_hint").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);

    public WorkbenchScreen(WorkbenchMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    public void initVisuals() {
    }

    @Override
    protected void init() {
        super.init();
        this.timesInventoryChanged = this.minecraft.player.getInventory().getTimesChanged();
        this.initVisuals();
    }

    @Override
    protected void containerTick() {
        super.containerTick();

    }

    public void updateContent() {
        if (this.timesInventoryChanged != this.minecraft.player.getInventory().getTimesChanged()) {
            this.updateStackedContents();
            this.timesInventoryChanged = this.minecraft.player.getInventory().getTimesChanged();
        }
    }

    private void updateStackedContents() {
    }

    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderBackground(guiGraphics);
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RESOURCE_GUI_RECIPE, this.leftPos - this.imageWidth + 12, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.blit(RESOURCE_GUI, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int x = this.leftPos + 7;
        int y = this.topPos + POS_Y;
        int startIndexs = this.startIndex + (COLUMN * ROW);
        this.renderSrollBar(guiGraphics, mouseX, mouseY);
        this.renderRecipes(guiGraphics, x, y, mouseX, mouseY, startIndexs);
    }
    private float lerp(float speed, float current, float target) {
        return current + (target - current) * speed;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        int startIndex = this.startIndex + (COLUMN * ROW);
        List<WorkbenchRecipe> list = this.menu.recipes;
        int startX = ((this.leftPos + 7) - (this.imageWidth - 6));
        int startY = this.topPos + POS_Y;
        for (int i = this.startIndex; i < startIndex && i < this.menu.recipes.size(); i++) {
            int index = i - this.startIndex;
            int posX = startX + index % ROW * 24;
            int var0001 = index / ROW;
            int posY = startY + var0001 * 24 + 2;
            if (x >= posX && x <= posX + 24 && y >= posY && y <= posY + 24) {
                guiGraphics.renderTooltip(this.font, list.get(i).getResultItem(this.minecraft.level.registryAccess()), x, y);
            }
//            this.renderTooltipIngredients(guiGraphics, posX, posY, x, y, index, i);
        }
    }

    private void renderTooltipIngredients(GuiGraphics guiGraphics, int recipesItemX, int recipesItemY, int x, int y, int index, int i) {
        WorkbenchRecipe recipe = menu.recipes.get(i);
        for (int j = 0; j < recipe.getIngredients().size(); j++) {
            if (x >= recipesItemX && x <= recipesItemX + 24 && y >= recipesItemY && y <= recipesItemY + 24) {
                ItemStack itemStack = recipe.getIngredients().get(j).getItems()[0];
                guiGraphics.renderTooltip(this.font, itemStack, x, y);
            }
            recipesItemX += PADDING_INGREDIENTS;
        }
    }

    private void renderSrollBar(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int scrollOffset = (int) (45.0F * this.scrollOffs);
        int x = this.leftPos - 12;
        int y = this.topPos + POS_Y;
        guiGraphics.blit(RESOURCE_GUI, x, y + scrollOffset, 176 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
    }
//    public void renderRecipes(GuiGraphics guiGraphics, int posX, int posY, int mouseX, int mouseY, int startIndex) {
//        for (int i = this.startIndex; i < startIndex && i < this.menu.recipes.size(); ++i) {
//            int index = i - this.startIndex;
//            int x = posX - (this.imageWidth + 12);
//            int y = posY + (index * PADDING_Y) + 2;
//            WorkbenchRecipe recipe = menu.recipes.get(i);
//            boolean isHovering = this.isHovering(8, -POS_Y + (index * PADDING_Y), 176, 20, mouseX, mouseY);
//            float scaling = isHovering ? 2.4F : 1.0F;
//            float scale = this.lerp(0.2F, 1.0F, scaling);
//            PoseStack poseStack = guiGraphics.pose();
//            poseStack.pushPose();
//            float pX = x + 8;
//            float pY = y + 8;
//            poseStack.translate(pX, pY, 0);
//            poseStack.scale(scale, scale, 1.0F);
//            poseStack.translate(-pX, -pY, 0);
//            guiGraphics.blitNineSliced(RESOURCE_GUI, x + 7, y - 2, 24, 24, 20, 4, 200, 20, 0, 184);
//            guiGraphics.renderItem(recipe.getResultItem(null), x + 10, y + 2);
//            guiGraphics.blit(RESOURCE_GUI, x + 10, y + 1, 0, 166, 18, 18);
//            for (int j = 0; j < recipe.getIngredients().size(); j++) {
//                Ingredient ing = recipe.getIngredients().get(j);
//                int need = recipe.getCounts().get(j);
//                ItemStack item = ing.getItems()[0];
//                guiGraphics.renderItem(item, x, y + 2);
//                poseStack.pushPose();
//                poseStack.translate(0, 0, 200);
//                if (!(need == 1 || need == 0)) {
//                    BlitHelper.drawStrings(guiGraphics, String.format("%s", need), x + 4, y + 10, ColorUtil.combineRGB(255, 255, 255), false);
//                }
//                poseStack.popPose();
//                x += PADDING_INGREDIENTS;
//            }
//            poseStack.popPose();
//        }
//    }
    public void renderRecipes(GuiGraphics guiGraphics, int posX, int posY, int mouseX, int mouseY, int startIndex) {
        int startX = ((this.leftPos + 7) - (this.imageWidth - 6));
        int startY = this.topPos + POS_Y;
        for (int i = this.startIndex; i < startIndex && i < this.menu.recipes.size(); ++i) {
            int index = i - this.startIndex;
            int x = (posX - (this.imageWidth - 6)) + index % ROW * 24;
            int var0001 = index / ROW;
            int y = posY + var0001 * 24 + 2;
            boolean isHovering = this.onHoveredSlot(x, y, 24, 24, mouseX, mouseY);
            WorkbenchRecipe recipe = this.menu.recipes.get(i);
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            guiGraphics.blitNineSliced(RESOURCE_GUI, x + 7, y - 2, 24, 24, 20, 4, 200, 20, 0, 184);
            guiGraphics.renderItem(recipe.getResultItem(this.minecraft.level.registryAccess()), x + 10, y + 2);
            guiGraphics.blit(RESOURCE_GUI, x + 10, y + 1, 0, 166, 18, 18);
            if (isHovering) {
                guiGraphics.blit(RESOURCE_GUI, x + 7, y - 2, 176, 15, 24, 24);
            }
            poseStack.popPose();
        }
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.isScrollBarActive()) {
            int row = this.getOffscreenRows();
            float percent = (float) delta / (float) row;
            this.scrollOffs = Mth.clamp(this.scrollOffs - percent, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) row) + 0.5D) * COLUMN;
        }
        return true;
    }
    private boolean onHoveredSlot(int x, int y, int w, int h, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;
        int startIndex = this.startIndex + COLUMN;
        for (int i = this.startIndex; i < startIndex; ++i) {
            int index = i - this.startIndex;
            double guiButtonX = this.leftPos + 8;
            double guiButtonY = this.topPos + POS_Y + (PADDING_Y * index);
            for (int i0 = 0; i0 < menu.recipes.size(); i0++) {
                if (mouseX >= guiButtonX && mouseX <= guiButtonX + 176 && mouseY >= guiButtonY && mouseY <= guiButtonY + PADDING_Y) {
                    WorkbenchRecipe recipe = menu.recipes.get(i0);
                    NetworkHandler.sendToServer(new ServerboundWorkbenchCraftPacket(recipe.getId()) );
                }
            }
            int x = this.leftPos + 52;
            int y = this.topPos + 14;
            double d0 = mouseX - (double) (x + index % COLUMN * 16);
            double d1 = mouseY - (double) (y + index / COLUMN * 18);
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
            this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5D) * COLUMN;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    private boolean isScrollBarActive() {
        return this.menu.recipes.size() > (COLUMN * ROW);
    }

    protected int getOffscreenRows() {
        int row = ROW;
        return (this.menu.recipes.size() + row - 1) / row - (row - 1);
    }
}
