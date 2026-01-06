package com.kenhorizon.beyondhorizon.client.render.guis.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.inventory.AccessoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

import java.awt.*;

public class AccessorySlotScreen extends EffectRenderingInventoryScreen<AccessoryMenu> implements RecipeUpdateListener {
    public static final ResourceLocation ACCESSORY_LOCATION = BeyondHorizon.resourceGui("container/accessory.png");
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = ResourceLocation.parse("textures/gui/recipe_button.png");
    private final RecipeBookComponent recipeBookGui = new RecipeBookComponent();
    public float xMouse;
    public float yMouse;
    private boolean widthTooNarrow;
    private boolean buttonClicked;
    public AccessorySlotScreen(AccessoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, Component.translatable("container.crafting"));
        this.titleLabelX = 97;
        this.inventoryLabelY = 999;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.recipeBookGui.tick();
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 378;
        this.recipeBookGui.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.updateScreenPosition();
        this.addWidget(this.recipeBookGui);
        this.setInitialFocus(this.recipeBookGui);
        if (this.getMinecraft().player != null && this.getMinecraft().player.isCreative() && this.recipeBookGui.isVisible()) {
            this.recipeBookGui.toggleVisibility();
            this.updateScreenPosition();
        }
        this.addRenderableWidget(new ImageButton(this.leftPos + 129, this.height / 2 - 22, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (button) -> {
            this.recipeBookGui.toggleVisibility();
            this.updateScreenPosition();
            this.leftPos = this.recipeBookGui.updateScreenPosition(this.width, this.imageWidth);
            button.setPosition(this.leftPos + 129, this.height / 2 - 22);
            this.buttonClicked = true;
        }));
    }

    private void updateScreenPosition() {
        int i;
        if (this.recipeBookGui.isVisible() && !this.widthTooNarrow) {
            int offset = 148;
            i = 177 + (this.width - this.imageWidth - offset) / 2;
        } else {
            i = (this.width - this.imageWidth) / 2;
        }
        this.leftPos = i;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeBookGui.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.recipeBookGui);
            this.updateScreenPosition();
            return true;
        } else {
            return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean keyPressed(int keycode, int scanCode, int modifiers) {
        if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
            this.recipeBookGui.toggleVisibility();
            this.updateScreenPosition();
            return true;
        } else {
            this.updateScreenPosition();
            return super.keyPressed(keycode, scanCode, modifiers);
        }
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
    protected void slotClicked(Slot slot, int id, int mouseButton, ClickType clickType) {
        super.slotClicked(slot, id, mouseButton, clickType);
        this.recipeBookGui.slotClicked(slot);
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int mouseButton) {
        boolean flag = mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.imageWidth) || mouseY >= (double)(top + this.imageHeight);
        return this.recipeBookGui.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseButton) && flag;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
            this.renderBg(graphics, partialTick, mouseX, mouseY);
            this.recipeBookGui.render(graphics, mouseX, mouseY, partialTick);
        } else {
            this.recipeBookGui.render(graphics, mouseX, mouseY, partialTick);
            super.render(graphics, mouseX, mouseY, partialTick);
            this.recipeBookGui.renderGhostRecipe(graphics, this.leftPos, this.topPos, false, partialTick);
        }
        this.renderTooltip(graphics, mouseX, mouseY);
        this.recipeBookGui.renderTooltip(graphics, this.leftPos, this.topPos, mouseX, mouseY);
        this.xMouse = (float) mouseX;
        this.yMouse = (float) mouseY;
    }

    @Override
    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.isHovering(x, y, width, height, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        graphics.blit(InventoryScreen.INVENTORY_LOCATION, x, y, 0, 0, this.imageWidth, this.imageHeight);
        graphics.blit(ACCESSORY_LOCATION, x - 32, y,  0, 0, 32, 122);
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, x + 51, y + 75, 30, (float) (x + 51) - mouseX, (float) (y + 75 - 50) - mouseY, getMinecraft().player);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookGui.recipesUpdated();
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookGui;
    }
}