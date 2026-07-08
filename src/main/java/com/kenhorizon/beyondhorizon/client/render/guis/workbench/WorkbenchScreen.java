package com.kenhorizon.beyondhorizon.client.render.guis.workbench;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.guis.IRecipeUpdateListener;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.inventory.WorkbenchMenu;
import com.kenhorizon.beyondhorizon.server.item.recipe.WorkbenchRecipe;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundExtendedPlaceRecipePacket;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.GhostRecipe;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WorkbenchScreen extends AbstractContainerScreen<WorkbenchMenu> implements PlaceRecipe<Ingredient>, IRecipeUpdateListener {
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
    private boolean isVisible;
    private WorkbenchPageButton prevPage;
    private WorkbenchPageButton nextPage;
    private Indexes recipesIndexes;
    private List<WorkbenchRecipe> recipes = ImmutableList.of();
    @Nullable
    private WorkbenchRecipeButton hoveredButton;
    private final StackedContents stackedContents = new StackedContents();
    private List<WorkbenchRecipeButton> buttons = Lists.newArrayListWithCapacity(20);
    private final GhostRecipe ghostRecipe = new GhostRecipe();
    private int currentPage = 0;
    private int totalPages = 0;
    private int buttonpadding = 50;
    private int timesInventoryChanged = 0;
    private final Set<Recipe<?>> craftable = Sets.newHashSet();
    private final Set<Recipe<?>> fitsDimensions = Sets.newHashSet();

    public WorkbenchScreen(WorkbenchMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 166;
        for (int i = 0; i < 20; i++) {
            this.buttons.add(new WorkbenchRecipeButton());
        }
        this.recipes = menu.recipes;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
        if (visible) {
            this.initVisual();
        }
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void toggleVisibility() {
        this.setVisible(!this.isVisible());
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 378;
        this.xOffset = this.widthTooNarrow ? 0 : 86;
        this.timesInventoryChanged = this.minecraft.player.getInventory().getTimesChanged();
        this.titleLabelX = 29;
        int x = (this.width - 206) / 2 - this.xOffset;
        int y = (this.height - 166) / 2;
        this.leftPos = this.updateScreenPosition();
        this.recipesIndexes = new Indexes(x, y);
        this.addRenderableWidget(new ImageButton(this.leftPos + 129, this.height / 2 - 32, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (button) -> {
            this.toggleVisibility();
            this.leftPos = this.updateScreenPosition();
            button.setPosition(this.leftPos + 129, this.height / 2 - 32);
            this.buttonClicked = true;
        }));
        int btnX = (176 - (buttonpadding * 2)) / 2;
        this.prevPage = new WorkbenchPageButton((this.width - btnX) / 2 - this.xOffset - buttonpadding,  y + 137, false, 0, (onPress) -> {});
        this.nextPage = new WorkbenchPageButton((this.width - btnX) / 2 - this.xOffset + buttonpadding, y + 137, true, 0, (onPress) -> {});
        this.addRenderableWidget(this.prevPage);
        this.addRenderableWidget(this.nextPage);
        this.prevPage.visible = false;
        this.nextPage.visible = false;
        if (this.isVisible()) {
            this.initVisual();
        }
    }

    @Override
    public void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
        if (DEBUG) {
            BeyondHorizon.LOGGER.debug("Setting up the ghost recipe!");
        }
        ItemStack stacks = recipe.getResultItem(this.minecraft.level.registryAccess());
        this.ghostRecipe.setRecipe(recipe);
        this.ghostRecipe.addIngredient(Ingredient.of(stacks), (slots.get(0)).x, (slots.get(0)).y);
        this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), recipe, recipe.getIngredients().iterator(), 0);

    }


    @Override
    public void addItemToSlot(Iterator<Ingredient> ing, int slots, int max, int y, int x) {
        Ingredient ingredient = ing.next();
        if (!ingredient.isEmpty()) {
            Slot slot = this.menu.slots.get(slots);
            this.ghostRecipe.addIngredient(ingredient, slot.x, slot.y);
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
        this.stackedContents.clear();
        this.minecraft.player.getInventory().fillStackedContents(this.stackedContents);
        this.menu.fillCraftSlotsStackedContents(this.stackedContents);
        BeyondHorizon.LOGGER.debug("Stacked Content= {} : {}", this.stackedContents, this.stackedContents.contents);
        this.updateCollections(false);
    }

    private void updateCollections(boolean resetPageNumber) {
        this.canCraft(this.stackedContents, this.menu.getGridWidth(), this.menu.getGridHeight());
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
        if (this.isVisible() && !this.widthTooNarrow) {
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
        if (this.isVisible()) {
            if (this.timesInventoryChanged != this.minecraft.player.getInventory().getTimesChanged()) {
                this.updateStackedContents();
                this.timesInventoryChanged = this.minecraft.player.getInventory().getTimesChanged();
            }
        }
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
        if (this.isVisible()) {
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
        if (this.isVisible()) {
            guiGraphics.pose().pushPose();
            this.renderPanels(guiGraphics, this.recipesIndexes.x, this.recipesIndexes.y);
            this.createLabel(guiGraphics, DISPLAY_RECIPES_ITEMS, this.recipesIndexes.x, this.recipesIndexes.y - 16, 64, 24);
            String s = String.format("%s/%s", this.currentPage + 1, this.totalPages);
            int i = this.minecraft.font.width(s);
            int btnX = (176 - (buttonpadding * 2)) / 2;
            guiGraphics.drawString(this.font, s, this.recipesIndexes.x + (i / 2) + btnX + (buttonpadding / 2), this.recipesIndexes.y + 144, Colors.WHITE);
            this.ghostRecipe.render(guiGraphics, minecraft, this.leftPos, this.topPos, false, partialTick);
            guiGraphics.pose().popPose();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isVisible() && !this.minecraft.player.isSpectator()) {
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
                Recipe<?> recipe = this.hoveredButton.getRecipeItem();
                if (recipe != null) {
                    if (recipe == this.ghostRecipe.getRecipe() && !this.isCraftable(recipe)) {
                        return false;
                    }
                    this.ghostRecipe.clear();
                    NetworkHandler.sendToServer(new ServerboundExtendedPlaceRecipePacket(this.minecraft.player.containerMenu.containerId, recipe, Screen.hasShiftDown()));
                    if (!this.isOffsetNextToMainGUI()) {
                        this.setVisible(false);
                    }
                }
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return (!this.widthTooNarrow || !this.isVisible()) && super.mouseClicked(mouseX, mouseY, button);
    }

    public void canCraft(StackedContents stackedContents, int w, int h) {
        for(Recipe<?> recipe : this.recipes) {
            boolean flag = recipe.canCraftInDimensions(w, h);
            if (flag) {
                this.fitsDimensions.add(recipe);
            } else {
                this.fitsDimensions.remove(recipe);
            }

            if (flag && stackedContents.canCraft(recipe, (IntList)null)) {
                this.craftable.add(recipe);
            } else {
                this.craftable.remove(recipe);
            }
        }

    }

    public boolean isCraftable(Recipe<?> recipe) {
        return this.craftable.contains(recipe);
    }

    @Override
    public boolean keyPressed(int keycode, int scanCode, int modifiers) {
        if (this.isVisible() && this.widthTooNarrow) {
            if (!this.minecraft.player.isSpectator()) {
                if (keycode == 256 && !this.isOffsetNextToMainGUI()) {
                    this.setVisible(false);
                    return true;
                }
            }

            this.toggleVisibility();
            this.updateScreenPosition();
            return true;
        } else {
            this.updateScreenPosition();
            return super.keyPressed(keycode, scanCode, modifiers);
        }

    }
    private boolean isOffsetNextToMainGUI() {
        return this.xOffset == 86;
    }

    @Override
    protected void slotClicked(Slot slot, int id, int mouseButton, ClickType clickType) {
        if (slot != null && slot.index < this.menu.getSize()) {
            this.ghostRecipe.clear();
            if (this.isVisible()) {
                this.updateStackedContents();
            }
        }
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
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (this.minecraft.screen != null && this.hoveredButton != null) {
            guiGraphics.renderComponentTooltip(this.minecraft.font, this.hoveredButton.getTooltipText(), x, y);
        }
        this.renderGhostRecipeTooltip(guiGraphics, this.leftPos + 9, this.topPos + 9, x, y);
        super.renderTooltip(guiGraphics, x, y);
    }
    private void renderGhostRecipeTooltip(GuiGraphics graphics, int pX, int pY, int mX, int mY) {
        ItemStack itemstack = null;

        for(int i = 0; i < this.ghostRecipe.size(); ++i) {
            GhostRecipe.GhostIngredient ghostrecipe$ghostingredient = this.ghostRecipe.get(i);
            int j = ghostrecipe$ghostingredient.getX() + pX;
            int k = ghostrecipe$ghostingredient.getY() + pY;
            if (mX >= j && mY >= k && mX < j + 16 && mY < k + 16) {
                itemstack = ghostrecipe$ghostingredient.getItem();
            }
        }

        if (itemstack != null && this.minecraft.screen != null) {
            graphics.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, itemstack), mX, mY, itemstack);
        }

    }
    private boolean onHoveredSlot(int x, int y, int w, int h, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    private void updateStackedContents() {
        this.stackedContents.clear();
        this.minecraft.player.getInventory().fillStackedContents(this.stackedContents);
        this.menu.fillCraftSlotsStackedContents(this.stackedContents);
        this.updateCollections(false);
    }


}
