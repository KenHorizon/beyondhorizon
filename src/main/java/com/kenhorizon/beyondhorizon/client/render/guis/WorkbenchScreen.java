package com.kenhorizon.beyondhorizon.client.render.guis;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
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
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkbenchScreen extends AbstractContainerScreen<WorkbenchMenu> {
    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    public static int POS_Y = 24;
    public static int ROW = 6;
    public static int COLUMN = 6;
    public static int PADDING_INGREDIENTS = 22;
    public static int PADDING_Y = 24;
    public static final ResourceLocation RESOURCE_GUI = BeyondHorizon.resourceGui("container/workbench.png");
    public static final ResourceLocation RESOURCE_GUI_RECIPE = BeyondHorizon.resourceGui("container/workbench_recipe.png");
    public static final ResourceLocation RESOURCE_GUI_INGRE = BeyondHorizon.resourceGui("container/workbench_recipe_ingredients.png");
    private int timesInventoryChanged;
    private boolean widthTooNarrow;
    private WorkbenchRecipe selectedRecipes = null;
    private boolean enableHelp = false;

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
        this.widthTooNarrow = this.width < 378;
        this.timesInventoryChanged = this.minecraft.player.getInventory().getTimesChanged();
        this.initVisuals();
    }

    private void updateScreenPosition() {
        int i;
        if (!this.widthTooNarrow) {
            int offset = 148;
            i = 177 + (this.width - this.imageWidth - offset) / 2;
        } else {
            i = (this.width - this.imageWidth) / 2;
        }
        this.leftPos = i;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (this.selectedRecipes != null) {
            BeyondHorizon.LOGGER.debug("Selected Recipes is {}", this.selectedRecipes.getId());
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        this.selectedRecipes = null;
    }

    public void updateContent() {
        if (this.timesInventoryChanged != this.minecraft.player.getInventory().getTimesChanged()) {
            this.updateStackedContents();
            this.timesInventoryChanged = this.minecraft.player.getInventory().getTimesChanged();
        }
    }

    private void updateStackedContents() {

    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        int x = this.leftPos + (this.imageWidth/2);
        int y = this.topPos;
        int craftX = this.leftPos + (this.imageWidth / 2 - (120 / 2));
        int craftY = y + 50;
        int craftTextY = craftY + 2;
        Component mutableComponent = Component.translatable(Tooltips.TOOLTIP_WORKBENCH_FORGE);
        String craftText = mutableComponent.getString();
        int craftTextW = craftText.length();
        if (this.selectedRecipes != null) {

            Minecraft mc = Minecraft.getInstance();
            var player = mc.player;
            boolean flag = this.foundRecipes(player, this.selectedRecipes);
            Component com = this.selectedRecipes.getResultItem(this.minecraft.level.registryAccess()).getItem().getDescription();
            String displayNameRecipe = com.toString();
            guiGraphics.drawCenteredString(this.font, com, x, y, ColorUtil.WHITE);
            int nameLenght = displayNameRecipe.length();
            int guiW = 54 + nameLenght;
            guiGraphics.blitNineSliced(RESOURCE_GUI, this.leftPos + (this.imageWidth / 2 - (guiW / 2)), y - 2, guiW, 12, 20 , 4, 200, 20, 0, 184);
            guiGraphics.renderItem(this.selectedRecipes.getResultItem(this.minecraft.level.registryAccess()), this.leftPos + 80, this.topPos + 25);
            if (!flag) {
                guiGraphics.blitNineSliced(RESOURCE_GUI, craftX, craftY, 120, 14, 20 , 4, 200, 20, 0, 204);
            } else if (this.onHoveredSlot(this.leftPos + (this.imageWidth / 2 - (120 / 2)), 120, 12, y - 60, mouseX, mouseY)) {
                guiGraphics.blitNineSliced(RESOURCE_GUI, craftX, craftY, 120, 14, 20 , 4, 200, 20, 0, 224);
            } else {
                guiGraphics.blitNineSliced(RESOURCE_GUI, craftX, craftY, 120, 14, 20 , 4, 200, 20, 0, 184);
            }
            guiGraphics.drawCenteredString(this.font, mutableComponent, craftX + (craftTextW + (120 / 2) - 2), craftTextY, ColorUtil.WHITE);
            this.createLabel(guiGraphics, Tooltips.TOOLTIP_WORKBENCH_INGREDIENTS, this.leftPos + this.imageWidth + 20, this.topPos - 6, 64, 18);
        } else {
            guiGraphics.blitNineSliced(RESOURCE_GUI, craftX, craftY, 120, 14, 20 , 4, 200, 20, 0, 204);
            guiGraphics.drawCenteredString(this.font, mutableComponent, craftX + (craftTextW + (120 / 2) - 2), craftTextY, ColorUtil.WHITE);
        }
        this.createLabel(guiGraphics, Tooltips.TOOLTIP_WORKBENCH_ITEMS, this.leftPos - this.imageWidth + 20, this.topPos - 6, 64, 18);
        if (this.enableHelp) {
            if (this.onHoveredSlot(this.leftPos + 156, this.topPos + 4, 15, 12, mouseX, mouseY)) {
                guiGraphics.blit(RESOURCE_GUI, this.leftPos + 156, this.topPos + 4, 212, 0, 12, 15);
            } else {
                guiGraphics.blit(RESOURCE_GUI, this.leftPos + 156, this.topPos + 4, 200, 0, 12, 15);
            }
        }
    }

    private void createLabel(GuiGraphics guiGraphics, String text, int x, int y, int width, int height) {
        Component mutableComponent = Component.translatable(text);
        String craftText = mutableComponent.getString();
        int craftTextW = craftText.length();
        guiGraphics.blitNineSliced(RESOURCE_GUI, x, y, width + craftTextW, height, 20, 4, 200, 20, 0, 184);
        guiGraphics.drawCenteredString(this.font, craftText, x + (craftTextW + (width / 2) - 2), y + 4, ColorUtil.WHITE);
    }
    
    private void createLabel(GuiGraphics guiGraphics, Component text, int x, int y, int width, int height) {
        String craftText = text.getString();
        int craftTextW = craftText.length();
        guiGraphics.blitNineSliced(RESOURCE_GUI, x, y, width + craftTextW, height, 20 , 4, 200, 20, 0, 184);
        guiGraphics.drawCenteredString(this.font, craftText, x + (craftTextW + (width / 2) - 2), y+4, ColorUtil.WHITE);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos + 7;
        int y = this.topPos + POS_Y;
        guiGraphics.blit(RESOURCE_GUI_RECIPE, this.leftPos - this.imageWidth + 12, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (this.selectedRecipes != null) {
            guiGraphics.blit(RESOURCE_GUI_INGRE, this.leftPos + this.imageWidth - 12, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
            this.renderIngredients(guiGraphics, x, y);
        }
        guiGraphics.blit(RESOURCE_GUI, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int startIndexs = this.startIndex + (COLUMN * ROW);
        if (this.isScrollBarActive()) {
            this.renderSrollBar(guiGraphics, mouseX, mouseY);
        }
        this.renderRecipes(guiGraphics, x, y, mouseX, mouseY, startIndexs);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        int startIndex = this.startIndex + (COLUMN * ROW);
        List<WorkbenchRecipe> list = this.menu.recipes;
        int startX = ((this.leftPos + 7) - (this.imageWidth - 6)) + 8;
        int startY = this.topPos + POS_Y + 8;
        for (int i = this.startIndex; i < startIndex && i < this.menu.recipes.size(); i++) {
            int index = i - this.startIndex;
            int posX = startX + index % ROW * 24;
            int var0001 = index / ROW;
            int posY = startY + var0001 * 24 + 2;
            if (x >= posX && x <= posX + 16 && y >= posY && y <= posY + 16) {
                guiGraphics.renderTooltip(this.font, list.get(i).getResultItem(this.minecraft.level.registryAccess()), x, y);
            }
        }
        if (this.selectedRecipes != null) {
            int recX = (this.leftPos + 7) + (this.imageWidth + 12);
            int recY = (this.topPos + POS_Y) + 2;
            this.renderTooltipIngredients(guiGraphics, recX, recY, x, y);
        }
        if (this.enableHelp) {
            int helpX = this.leftPos + 156;
            int helpY = this.topPos + 4;
            int helpW = 15;
            int helpH = 12;
            if (x >= helpX && x <= helpX + helpW && y >= helpY && y <= helpY + helpH) {
                List<Component> components = new ArrayList<>();
                components.add(Component.translatable(Tooltips.TOOLTIP_WORKBENCH_HELP_0));
                components.add(Component.translatable(Tooltips.TOOLTIP_WORKBENCH_HELP_1));
                guiGraphics.renderComponentTooltip(this.font, components, x, y);
            }
        }
    }

    private void renderTooltipIngredients(GuiGraphics guiGraphics, int recipesItemX, int recipesItemY, int x, int y) {
        for (int j = 0; j < this.selectedRecipes.getIngredients().size(); j++) {
            if (x >= recipesItemX && x <= recipesItemX + 24 && y >= recipesItemY && y <= recipesItemY + 24) {
                ItemStack itemStack = this.selectedRecipes.getIngredients().get(j).getItems()[0];
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

    public void renderIngredients(GuiGraphics guiGraphics, int posX, int posY) {
        int x = posX + (this.imageWidth + 12);
        int y = posY + 2;
        WorkbenchRecipe recipe = this.selectedRecipes;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        for (int j = 0; j < recipe.getIngredients().size(); j++) {
            Ingredient ing = recipe.getIngredients().get(j);
            int need = recipe.getCounts().get(j);
            ItemStack item = ing.getItems()[0];
            guiGraphics.renderItem(item, x, y + 2);
            poseStack.pushPose();
            poseStack.translate(0, 0, 200);
            if (!(need == 1 || need == 0)) {
                BlitHelper.drawStrings(guiGraphics, String.format("%s", need), x + 4, y + 10, ColorUtil.combineRGB(255, 255, 255), false);
            }
            poseStack.popPose();
            x += PADDING_INGREDIENTS;
        }
        poseStack.popPose();
    }
    public void renderRecipes(GuiGraphics guiGraphics, int posX, int posY, int mouseX, int mouseY, int startIndex) {
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
        int startIndex = this.startIndex + (COLUMN * ROW);
        int craftW = 24;
        int craftH = 24;
        for (int i = this.startIndex; i < startIndex && i < this.menu.recipes.size(); ++i) {
            int index = i - this.startIndex;
            int x0 = ((this.leftPos + 7) - (this.imageWidth - 6)) + index % ROW * 24;
            int var0001 = index / ROW;
            int y0 = (this.topPos + POS_Y) + var0001 * 24 + 2;
            if (mouseX >= x0 && mouseX <= x0 + craftW && mouseY >= y0 && mouseY <= y0 + craftH) {
                this.selectedRecipes = this.menu.recipes.get(i);
                break;
            }
            int x1 = this.leftPos + 52;
            int y1 = this.topPos + 14;
            double d0 = mouseX - (double) (x1 + index % COLUMN * 16);
            double d1 = mouseY - (double) (y1 + index / COLUMN * 18);
            if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, i)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, i);
                return true;
            }
        }

        if (this.selectedRecipes != null) {
            int craftX = this.leftPos + (this.imageWidth / 2 - (120 / 2));
            int craftY = this.topPos + 50;
            Minecraft mc = Minecraft.getInstance();
            var player = mc.player;
            if (this.foundRecipes(player, this.selectedRecipes)) {
                if (mouseX >= craftX && mouseX <= craftX + 120 && mouseY >= craftY && mouseY <= craftY + 14) {
                    NetworkHandler.sendToServer(new ServerboundWorkbenchCraftPacket(this.selectedRecipes.getId()));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    private boolean foundRecipes(Player player, WorkbenchRecipe recipe) {
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ing = recipe.getIngredients().get(i);
            int need = recipe.getCounts().get(i);
            if (this.hasEnoughItems(player, ing, need)) {
                return true;
            }
        }
        return false;
    }
    private boolean hasEnoughItems(Player player, Ingredient ingredient, int needed) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slot = player.getInventory().getItem(i);
            if (ingredient.test(slot)) {
                count += slot.getCount();
                if (count >= needed) return true;
            }
        }
        return false;
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
