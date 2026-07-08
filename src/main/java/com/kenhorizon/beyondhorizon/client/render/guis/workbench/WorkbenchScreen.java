package com.kenhorizon.beyondhorizon.client.render.guis.workbench;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.inventory.WorkbenchMenu;
import com.kenhorizon.beyondhorizon.server.recipe.WorkbenchRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeButton;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nullable;
import java.util.List;

public class WorkbenchScreen extends AbstractContainerScreen<WorkbenchMenu> {
    public record Indexes(int x, int y) {}
    private static final boolean DEBUG = true;
    public static final ResourceLocation RESOURCE_GUI = BeyondHorizon.resourceGui("workbench/workbench.png");
    public static final ResourceLocation RESOURCE_PANEL = BeyondHorizon.resourceGui("panels.png");
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = ResourceLocation.parse("textures/gui/recipe_button.png");
    private static final MutableComponent DISPLAY_RECIPES_ITEMS = Component.translatable(Tooltips.TOOLTIP_WORKBENCH_ITEMS);
    private boolean buttonClicked;
    public float xMouse;
    public float yMouse;
    private int xOffset;
    private boolean widthTooNarrow;
    private boolean panelVisible;
    private WorkbenchPageButton prevPage;
    private WorkbenchPageButton nextPage;
    private Indexes recipesIndexes;
    private List<WorkbenchRecipe> recipes = ImmutableList.of();
    @Nullable
    private WorkbenchRecipeButton hoveredButton;
    private WorkbenchRecipe selectedRecipes;
    private List<WorkbenchRecipeButton> buttons = Lists.newArrayListWithCapacity(20);
    private int currentPage = 0;
    private int totalPages = 0;
    private int buttonpadding = 50;

    public WorkbenchScreen(WorkbenchMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 166;
        for (int i = 0; i < 20; i++) {
            this.buttons.add(new WorkbenchRecipeButton());
        }
        this.recipes = menu.recipes;
    }

    public void setPanelVisible(boolean visible) {
        this.panelVisible = visible;
        if (visible) {
            this.initVisual();
        }
    }

    public boolean isPanelVisible() {
        return this.panelVisible;
    }

    public void togglePanelVisibility() {
        this.setPanelVisible(!this.isPanelVisible());
        if (DEBUG) {
            BeyondHorizon.LOGGER.debug("[DEbug] Toggling Panel Visibility...");
        }
    }


    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 378;
        this.xOffset = this.widthTooNarrow ? 0 : 86;
        this.titleLabelX = 29;
        int x = (this.width - 206) / 2 - this.xOffset;
        int y = (this.height - 166) / 2;
        this.leftPos = this.updateScreenPosition();
        this.recipesIndexes = new Indexes(x, y);
        this.addRenderableWidget(new ImageButton(this.leftPos + 129, this.height / 2 - 32, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (button) -> {
            this.togglePanelVisibility();
            this.leftPos = this.updateScreenPosition();
            button.setPosition(this.leftPos + 129, this.height / 2 - 32);
            this.buttonClicked = true;
        }));
        int btnX = (176 - (buttonpadding * 2)) / 2;
        this.prevPage = new WorkbenchPageButton((this.width - btnX) / 2 - this.xOffset - (buttonpadding / 2),  y + 137, false, 0, (onPress) -> {});
        this.nextPage = new WorkbenchPageButton((this.width - btnX) / 2 - this.xOffset + buttonpadding, y + 137, true, 0, (onPress) -> {});
        this.addRenderableWidget(this.prevPage);
        this.addRenderableWidget(this.nextPage);
        this.prevPage.visible = false;
        this.nextPage.visible = false;
        if (this.isPanelVisible()) {
            this.initVisual();
        }
    }

    public void initVisual() {
        for (int i = 0; i < this.buttons.size(); i++) {
            var btn = this.buttons.get(i);
            int x = (this.recipesIndexes.x + 20) + i % 5 * 26;
            int var001 = i / 5;
            int y = this.recipesIndexes.y + 20 + var001 * 26;
            btn.setPosition(x, y);
        }
        this.updateCollections(false);
    }

    private void updateCollections(boolean resetPageNumber) {
        this.totalPages = (int)Math.ceil((double)this.recipes.size() / 20.0D);
        if (this.totalPages <= this.currentPage || resetPageNumber) {
            this.currentPage = 0;
        }
        this.updateButtonsForPage();
    }

    private void updateButtonsForPage() {
        int i = 20 * this.currentPage;

        for (int j = 0; j < this.buttons.size(); ++j) {
            WorkbenchRecipeButton btn = this.buttons.get(j);
            if (i + j < this.recipes.size()) {
                WorkbenchRecipe recipecollection = this.recipes.get(i + j);
                btn.init(recipecollection, this.recipes);
                btn.visible = true;
            } else {
                btn.visible = false;
            }
        }
        this.updateArrowButtons();
    }

    private void updateArrowButtons() {
        this.nextPage.visible = this.totalPages > 1 && this.currentPage < this.totalPages - 1;
        this.prevPage.visible = this.totalPages > 1 && this.currentPage > 0;
    }

    public int updateScreenPosition() {
        int i;
        if (this.isPanelVisible() && !this.widthTooNarrow) {
            int offset = 196;
            i = 176 + (this.width - this.imageWidth - offset) / 2;
        } else {
            i = (this.width - this.imageWidth) / 2;
        }
        return i;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
    }



    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(gui);
        super.render(gui, mouseX, mouseY, partialTick);
        this.renderPanels(gui, partialTick, mouseX, mouseY, this.recipesIndexes.x, this.recipesIndexes.y);
        this.renderTooltip(gui, mouseX, mouseY);
        this.xMouse = (float) mouseX;
        this.yMouse = (float) mouseY;

    }

    private void createLabel(GuiGraphics guiGraphics, String text, int x, int y, int width, int height) {
        this.createLabel(guiGraphics, Component.literal(text), x, y, width, height);
    }
    
    private void createLabel(GuiGraphics guiGraphics, Component text, int x, int y, int width, int height) {
        String craftText = text.getString();
        int craftTextW = this.font.width(craftText);
        guiGraphics.blitNineSliced(RESOURCE_GUI, x, y, width + craftTextW, height, 20 , 4, 200, 20, 0, 166);
        guiGraphics.drawCenteredString(this.font, craftText, x + craftTextW, y + (height / 2) - (this.font.lineHeight / 2), Colors.WHITE);
    }

    private void renderPanels(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(RESOURCE_PANEL, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }


    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    protected void renderPanels(GuiGraphics gui, float partialTick, int mouseX, int mouseY, int x, int y) {
        if (this.isPanelVisible()) {
            this.hoveredButton = null;
            for (WorkbenchRecipeButton recipebutton : this.buttons) {
                recipebutton.render(gui, mouseX, mouseY, partialTick);
                if (recipebutton.visible && recipebutton.isHoveredOrFocused()) {
                    this.hoveredButton = recipebutton;
                }
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RESOURCE_GUI, this.leftPos, (this.height - this.imageHeight) / 2, 0, 0, this.imageWidth, this.imageHeight);
        if (this.isPanelVisible()) {
            guiGraphics.pose().pushPose();
            this.renderPanels(guiGraphics, this.recipesIndexes.x, this.recipesIndexes.y);
            this.createLabel(guiGraphics, DISPLAY_RECIPES_ITEMS, this.recipesIndexes.x, this.recipesIndexes.y - 16, 64, 24);
            String s = String.format("%s/%s", this.currentPage + 1, this.totalPages);
            int i = this.minecraft.font.width(s);
            int btnX = (176 - (buttonpadding * 2)) / 2;
            guiGraphics.drawString(this.font, s, this.recipesIndexes.x + (i / 2) + btnX + (buttonpadding / 2), this.recipesIndexes.y + 144, Colors.WHITE);
            if (this.selectedRecipes != null) {
                guiGraphics.renderFakeItem(this.selectedRecipes.getResultItem(this.minecraft.level.registryAccess()), this.leftPos + 139, this.topPos + 23);
            }
            guiGraphics.pose().popPose();
        }
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isPanelVisible() && !this.minecraft.player.isSpectator()) {
            if (this.nextPage.mouseClicked(mouseX, mouseY, button)) {
                ++this.currentPage;
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                this.updateButtonsForPage();
                return true;
            } else if (this.prevPage.mouseClicked(mouseX, mouseY, button)) {
                --this.currentPage;
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                this.updateButtonsForPage();
                return true;
            } else if (this.hoveredButton != null && this.hoveredButton.mouseClicked(mouseX, mouseY, button)) {
                this.selectedRecipes = this.hoveredButton.getRecipeItem();
                return true;
            }
            this.updateCollections(true);
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return (!this.widthTooNarrow || !this.isPanelVisible()) && super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keycode, int scanCode, int modifiers) {
        if (this.isPanelVisible() && this.widthTooNarrow) {
            this.togglePanelVisibility();
            this.updateScreenPosition();
            return true;
        } else {
            this.updateScreenPosition();
            return super.keyPressed(keycode, scanCode, modifiers);
        }
    }


    @Override
    protected void slotClicked(Slot slot, int id, int mouseButton, ClickType clickType) {
        super.slotClicked(slot, id, mouseButton, clickType);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int mouseButton) {
        boolean flag = mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.imageWidth) || mouseY >= (double)(top + this.imageHeight);
        return flag;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
            this.updateScreenPosition();
            return true;
        } else {
            return super.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        this.selectedRecipes = null;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (this.minecraft.screen != null && this.hoveredButton != null) {
            guiGraphics.renderComponentTooltip(this.minecraft.font, this.hoveredButton.getTooltipText(), x, y);
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    private boolean onHoveredSlot(int x, int y, int w, int h, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
}
