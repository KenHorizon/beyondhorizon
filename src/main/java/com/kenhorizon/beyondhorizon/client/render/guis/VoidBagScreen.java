package com.kenhorizon.beyondhorizon.client.render.guis;


import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.util.BlitHelper;
import com.kenhorizon.beyondhorizon.server.inventory.VoidBagMenu;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;

public class VoidBagScreen extends AbstractContainerScreen<VoidBagMenu> {
    public static final ResourceLocation LOCATION = BeyondHorizon.resourceGui("container/void_bag.png");
    private final RandomSource random = RandomSource.create();
    private final int counts = 64;
    private final int[] starTimerDefault = new int[counts];
    private final int[] starTimer = new int[counts];
    private final int[] starX = new int[counts];
    private final int[] starY = new int[counts];
    public VoidBagScreen(VoidBagMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < this.counts; i++) {
            this.starX[i] = this.random.nextIntBetweenInclusive(0, this.width);
            this.starY[i] = this.random.nextIntBetweenInclusive(0, this.height);
            this.starTimerDefault[i] = this.random.nextIntBetweenInclusive(Maths.sec(20), Maths.sec(40));
            this.starTimer[i] = this.starTimerDefault[i];

        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        for (int i = 0; i < this.counts; i++) {
            if (this.starTimer[i] <= 0) {
                this.starX[i] = this.random.nextIntBetweenInclusive(0, this.width);
                this.starY[i] = this.random.nextIntBetweenInclusive(0, this.height);
                this.starTimer[i] = this.starTimerDefault[i];
            }
            if (this.starTimer[i] > 0) this.starTimer[i]--;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        this.renderStars(guiGraphics,partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderStars(GuiGraphics guiGraphics, float delta) {
        for (int i = 0; i < this.counts; i++) {
            RenderSystem.enableBlend();
            float alpha = Mth.clamp((float) this.starTimer[i] / this.starTimerDefault[i], 0.0F, 1.0F);
            if (this.starTimer[i] % 10L == 0) {
                this.starsFlicker(guiGraphics, alpha, i);
            }
            this.stars(guiGraphics, alpha, i);
            RenderSystem.disableBlend();
        }
    }
    private void starsFlicker(GuiGraphics guiGraphics, float alpha, int i) {
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, alpha);
        guiGraphics.blit(LOCATION, this.starX[i], this.starY[i], 176, 0, 13, 13);
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
    }
    private void stars(GuiGraphics guiGraphics, float alpha, int i) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        guiGraphics.blit(LOCATION, this.starX[i], this.starY[i], 176, 0, 13, 13);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void stars(GuiGraphics guiGraphics, int x, int y) {
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(LOCATION, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }
}