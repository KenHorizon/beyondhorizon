package com.kenhorizon.beyondhorizon.client.render.guis.workbench;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.inventory.WorkbenchMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class WorkbenchScreen extends AbstractContainerScreen<WorkbenchMenu> {
    public record Indexes(int x, int y) {}
    public static int POS_Y = 24;
    public static int ROW = 5;
    public static int COLUMN = 5;
    public static int PADDING_INGREDIENTS = 22;
    public static int PADDING_Y = 24;
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
    private int recipePages = 0;

    public WorkbenchScreen(WorkbenchMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    public void setPanelVisible(boolean visible) {
        this.panelVisible = visible;
    }

    public boolean isPanelVisible() {
        return this.panelVisible;
    }

    public void togglePanelVisibility() {
        this.setPanelVisible(!this.isPanelVisible());
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
        this.prevPage = new WorkbenchPageButton(this.recipesIndexes.x,  y, false, 0, (onPress) -> {
            if (this.recipePages > 0) {
                this.recipePages--;
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
            }
        });
        this.addRenderableWidget(prevPage);
        this.nextPage = new WorkbenchPageButton(this.recipesIndexes.x, y, true, 0, (onPress) -> {
            this.recipePages++;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        });
        this.addRenderableWidget(nextPage);
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
        this.prevPage.visible = this.isPanelVisible();
        this.nextPage.visible = this.isPanelVisible();
        this.xMouse = (float) mouseX;
        this.yMouse = (float) mouseY;
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

    private void renderPanels(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(RESOURCE_PANEL, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }


    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RESOURCE_GUI, this.leftPos, (this.height - this.imageHeight) / 2, 0, 0, this.imageWidth, this.imageHeight);
        if (this.isPanelVisible()) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
            this.createLabel(guiGraphics, DISPLAY_RECIPES_ITEMS, this.recipesIndexes.x, this.recipesIndexes.y, 64, 24);
            this.renderPanels(guiGraphics, this.recipesIndexes.x, this.recipesIndexes.y);
            guiGraphics.pose().popPose();
        }
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.widthTooNarrow && this.isPanelVisible() ? true : super.mouseClicked(mouseX, mouseY, button);
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
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
    }

    private boolean onHoveredSlot(int x, int y, int w, int h, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
}
