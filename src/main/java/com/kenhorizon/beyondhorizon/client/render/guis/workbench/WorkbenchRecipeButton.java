package com.kenhorizon.beyondhorizon.client.render.guis.workbench;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.item.recipe.WorkbenchRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Iterator;
import java.util.List;

public class WorkbenchRecipeButton extends AbstractWidget {
    private static final ResourceLocation RECIPE_BOOK_LOCATION = BeyondHorizon.resourceGui("panels.png");
    private static final float ANIMATION_TIME = 15.0F;
    private static final int BACKGROUND_SIZE = 25;
    private float time;
    private float animationTime;
    private int currentIndex;
    private List<WorkbenchRecipe> recipes = ImmutableList.of();
    private ItemStack stacks;
    private WorkbenchRecipe selectedRecipes;

    public WorkbenchRecipeButton() {
        super(0, 0, BACKGROUND_SIZE, BACKGROUND_SIZE, Component.empty());
    }

    public void init(WorkbenchRecipe recipes, List<WorkbenchRecipe> list) {
        Minecraft mc = Minecraft.getInstance();
        this.selectedRecipes = recipes;
        this.recipes = list;
        this.stacks = recipes.getResultItem(mc.level.registryAccess());
    }

    public void initVisual() {
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int mX, int mY, float partialTick) {
        if (!Screen.hasControlDown()) {
            this.time += partialTick;
        }
        Minecraft mc = Minecraft.getInstance();
        int vw = 176;
        int vh = 0;
        if (this.isHovered()) {
            vh += 25;
        }
        boolean flag = this.animationTime > 0.0F;
        if (flag) {
            float scale = 1.0F + 0.1F * (float) Math.sin((double) (this.animationTime / ANIMATION_TIME * (float) Math.PI));
            gui.pose().pushPose();
            gui.pose().translate((float)(this.getX() + 8), (float)(this.getY() + 12), 0.0F);
            gui.pose().scale(scale, scale, 1.0F);
            gui.pose().translate((float)(-(this.getX() + 8)), (float)(-(this.getY() + 12)), 0.0F);
            this.animationTime -= partialTick;
        }
        this.currentIndex = Mth.floor(this.time / 30.0F) % this.recipes.size();
        gui.blit(RECIPE_BOOK_LOCATION, this.getX(), this.getY(), vw, vh, this.width, this.height);
        gui.renderFakeItem(this.stacks, this.getX() + 4, this.getY() + 4);
        if (flag) {
            gui.pose().popPose();
        }
    }

    public List<Component> getTooltipText() {
        List<Component> list = Lists.newArrayList(Screen.getTooltipFromItem(Minecraft.getInstance(), this.stacks));
        return list;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }

    @Override
    protected boolean isValidClickButton(int btn) {
        return btn == 0 || btn == 1;
    }

    public WorkbenchRecipe getRecipeItem() {
        return this.selectedRecipes;
    }
}