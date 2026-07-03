package com.kenhorizon.beyondhorizon.client.render.guis;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.inventory.WorkbenchMenu;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundWorkbenchCraftPacket;
import com.kenhorizon.beyondhorizon.server.recipe.WorkbenchRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchScreen extends AbstractContainerScreen<WorkbenchMenu> {
    public record Indexes(int posX, int posY) {}
    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    private int index;
    public static int POS_Y = 24;
    public static int ROW = 5;
    public static int COLUMN = 5;
    public static int PADDING_INGREDIENTS = 22;
    public static int PADDING_Y = 24;
    public static final ResourceLocation RESOURCE_GUI = BeyondHorizon.resourceGui("container/workbench.png");
    public static final ResourceLocation RESOURCE_GUI_RECIPE = BeyondHorizon.resourceGui("container/workbench_recipe.png");
    public static final ResourceLocation RESOURCE_GUI_INGRE = BeyondHorizon.resourceGui("container/workbench_recipe_ingredients.png");
    private int timesInventoryChanged;
    private boolean widthTooNarrow;
    private WorkbenchRecipe selectedRecipes = null;
    private boolean enableHelp = false;
    private Indexes ingredients;
    private Indexes recipeItems;

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
        int x = this.leftPos + 7;
        int y = this.topPos + POS_Y;
        this.recipeItems = new Indexes(x - (this.imageWidth + 6), y);
        this.ingredients = new Indexes(x + (this.imageWidth - 6), y);
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
        this.index = this.startIndex + (COLUMN * ROW);
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
        int craftY = y + 52;
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
            guiGraphics.drawCenteredString(this.font, com, x, y, Colors.WHITE);
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
            guiGraphics.drawCenteredString(this.font, mutableComponent, craftX + (craftTextW + (120 / 2) - 2), craftTextY, Colors.WHITE);
            this.createLabel(guiGraphics, Tooltips.TOOLTIP_WORKBENCH_INGREDIENTS, this.leftPos + this.imageWidth + 20, this.topPos - 6, 64, 18);
        } else {
            guiGraphics.blitNineSliced(RESOURCE_GUI, craftX, craftY, 120, 14, 20 , 4, 200, 20, 0, 204);
            guiGraphics.drawCenteredString(this.font, mutableComponent, craftX + (craftTextW + (120 / 2) - 2), craftTextY, Colors.WHITE);
        }
        this.createLabel(guiGraphics, Tooltips.TOOLTIP_WORKBENCH_ITEMS, this.leftPos - this.imageWidth + 20, this.topPos - 8, 64, 18);
        if (this.enableHelp) {
            if (this.onHoveredSlot(this.leftPos + 156, this.topPos + 4, 15, 12, mouseX, mouseY)) {
                guiGraphics.blit(RESOURCE_GUI, this.leftPos + 156, this.topPos + 4, 212, 0, 12, 15);
            } else {
                guiGraphics.blit(RESOURCE_GUI, this.leftPos + 156, this.topPos + 4, 200, 0, 12, 15);
            }
        }
        if (this.isScrollBarActive()) {
            this.renderSrollBar(guiGraphics);
        }
    }

    private void createLabel(GuiGraphics guiGraphics, String text, int x, int y, int width, int height) {
        Component mutableComponent = Component.translatable(text);
        String craftText = mutableComponent.getString();
        int craftTextW = craftText.length();
        guiGraphics.blitNineSliced(RESOURCE_GUI, x, y, width + craftTextW, height, 20, 4, 200, 20, 0, 184);
        guiGraphics.drawCenteredString(this.font, craftText, x + (craftTextW + (width / 2) - 2), y + 4, Colors.WHITE);
    }
    
    private void createLabel(GuiGraphics guiGraphics, Component text, int x, int y, int width, int height) {
        String craftText = text.getString();
        int craftTextW = craftText.length();
        guiGraphics.blitNineSliced(RESOURCE_GUI, x, y, width + craftTextW, height, 20 , 4, 200, 20, 0, 184);
        guiGraphics.drawCenteredString(this.font, craftText, x + (craftTextW + (width / 2) - 2), y+4, Colors.WHITE);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos + 7;
        int y = this.topPos + POS_Y;
        int startIndexs = this.startIndex + (COLUMN * ROW);
        guiGraphics.blit(RESOURCE_GUI_RECIPE, this.leftPos - this.imageWidth + 1, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (this.selectedRecipes != null) {
            guiGraphics.blit(RESOURCE_GUI_INGRE, this.leftPos + this.imageWidth - 1, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
            this.renderIngredients(guiGraphics);
        }
        guiGraphics.blit(RESOURCE_GUI, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.renderRecipes(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        List<WorkbenchRecipe> list = this.menu.recipes;
        int itemTooltipsX = this.leftPos + 7;
        int itemTooltipsY = this.topPos + POS_Y;
        renderItemsTooltips(guiGraphics, x, y, list);
        if (this.selectedRecipes != null) {
            int recX = (this.leftPos + 7) + (this.imageWidth + 12);
            int recY = (this.topPos + POS_Y) + 2;
            this.renderTooltipIngredients(guiGraphics, x, y);
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

    private void renderTooltipIngredients(GuiGraphics guiGraphics, int posx, int posy) {
        int size = 25;
        for (int i = this.startIndex; i < this.index && i < this.selectedRecipes.getIngredients().size(); i++) {
            int index = i - this.startIndex;
            int x = (this.ingredients.posX + index % ROW * size) + 10;
            int var01 = index / ROW;
            int y = (this.ingredients.posY + var01 * size + 2);
            if (posx >= x && posx <= x + size && posy >= y && posy <= y + size) {
                ItemStack itemStack = this.selectedRecipes.getIngredients().get(i).getItems()[0];
                guiGraphics.renderTooltip(this.font, itemStack, x, y);
            }
        }
    }


    public void renderRecipes(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int size = 25;
        for (int i = this.startIndex; i < this.index && i < this.menu.recipes.size(); ++i) {
            int index = i - this.startIndex;
            int x = (this.recipeItems.posX  + index % ROW * size) + 10;
            int var0001 = index / ROW;
            int y = (this.recipeItems.posY + var0001 * size + 2);
            boolean isHovering = this.onHoveredSlot(x, y, size, size, mouseX, mouseY);
            WorkbenchRecipe recipe = this.menu.recipes.get(i);
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            guiGraphics.blit(RESOURCE_GUI, x, y, 176, 76, size, size);
            guiGraphics.renderItem(recipe.getResultItem(this.minecraft.level.registryAccess()), x + 4, y + 4);
            if (isHovering) {
                guiGraphics.blit(RESOURCE_GUI, x , y, 176, 101, size, size);
            }
            poseStack.popPose();
        }
    }

    private void renderItemsTooltips(GuiGraphics guiGraphics, int mX, int mY, List<WorkbenchRecipe> list) {
        int size = 25;
        for (int i = this.startIndex; i < this.index && i < this.menu.recipes.size(); i++) {
            int index = i - this.startIndex;
            int x = (this.recipeItems.posX + index % ROW * size) + 10;
            int var0001 = index / ROW;
            int y = (this.recipeItems.posY + var0001 * size + 2);
            boolean isHovering = this.onHoveredSlot(x, y, size - 1, size - 1, mX, mY);
            if (isHovering) {
                guiGraphics.renderTooltip(this.font, list.get(i).getResultItem(this.minecraft.level.registryAccess()), mX, mY);
            }
        }
    }


    private void renderSrollBar(GuiGraphics guiGraphics) {
        int scrollOffset = (int) (128.0F * this.scrollOffs);
        int x = this.leftPos;
        int y = this.topPos + POS_Y;
        guiGraphics.blit(RESOURCE_GUI, x, y + scrollOffset, 176 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
    }

    public void renderIngredients(GuiGraphics guiGraphics) {
        WorkbenchRecipe recipe = this.selectedRecipes;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        int size = 25;
        for (int i = this.startIndex; i < this.index && i < recipe.getIngredients().size(); ++i) {
            int index = i - this.startIndex;
            int x1 = (this.ingredients.posX + index % ROW * size) + 10;
            int var0001 = index / ROW;
            int y1 = this.ingredients.posY + var0001 * size + 2;
            Ingredient ing = recipe.getIngredients().get(i);
            int need = recipe.getCounts().get(i);
            ItemStack item = ing.getItems()[0];
            guiGraphics.renderItem(item, x1, y1);
            poseStack.pushPose();
            poseStack.translate(0, 0, 200);
            if (!(need == 1 || need == 0)) {
                BlitHelper.drawBorderedStrings(minecraft.font, guiGraphics, String.format("%s", need), x1 + 4, y1 + 10);
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.isScrollBarActive()) {
            int row = this.getOffscreenRows();
            float percent = (float) delta / (float) row;
            this.scrollOffs = Mth.clamp(this.scrollOffs - percent, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) row) + 0.5D) * ROW;
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
        int craftSize = 25;
        for (int i = this.startIndex; i < startIndex && i < this.menu.recipes.size(); ++i) {
            int index = i - this.startIndex;

            int x = this.leftPos + 7;
            int y = this.topPos + POS_Y;
            int x0 = ((x - (this.imageWidth - 6)) + index % ROW * craftSize) + 10;
            int var0001 = index / ROW;
            int y0 = y + var0001 * craftSize + 2;
            if (mouseX >= x0 && mouseX <= x0 + craftSize && mouseY >= y0 && mouseY <= y0 + craftSize) {
                this.selectedRecipes = this.menu.recipes.get(i);
                return true;
            }
            int x1 = this.leftPos + 52;
            int y1 = this.topPos + 14;
            double d0 = mouseX - (double) (x1 + index % COLUMN * 16);
            double d1 = mouseY - (double) (y1 + index / COLUMN * 18);
            if (d0 >= 0.0D && d1 >= 0.0D && d0 < craftSize && d1 < craftSize && this.menu.clickMenuButton(this.minecraft.player, i)) {
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
        var inv = player.getInventory();
        boolean flag = false;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack slot = inv.getItem(i);
            if (player.getInventory().contains(slot)) {
                count += slot.getCount();
                flag = ingredient.test(slot) && count >= needed && player.getInventory().contains(slot);
            }
        }
        return flag;
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
