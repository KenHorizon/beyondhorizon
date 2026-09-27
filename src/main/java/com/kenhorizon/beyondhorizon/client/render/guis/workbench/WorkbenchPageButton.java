package com.kenhorizon.beyondhorizon.client.render.guis.workbench;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WorkbenchPageButton extends Button {
    private final boolean right;
    private final int color;

    public WorkbenchPageButton(int x, int y, boolean right, int color, OnPress press) {
        super(x, y, 14, 18, Component.literal(""), press, DEFAULT_NARRATION);
        this.right = right;
        this.color = color;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (this.active) {
            ResourceLocation resourceLocation = BeyondHorizon.resourceGui("workbench/widgets.png");
            boolean flag = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
            int i = 0;
            int j = 32;
            if (flag) {
                j += 18;
            }

            if (!this.right) {
                i += 14;
            }
            j += color * 14;

            guiGraphics.blit(resourceLocation, this.getX(), this.getY(), i, j, width, height);
        }
    }
}