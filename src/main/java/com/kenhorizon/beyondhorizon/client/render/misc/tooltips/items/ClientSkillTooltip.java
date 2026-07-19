package com.kenhorizon.beyondhorizon.client.render.misc.tooltips.items;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.skills.Skills;
import com.kenhorizon.beyondhorizon.server.item.ItemAbilityType;
import com.kenhorizon.beyondhorizon.server.item.tooltips.SkillTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.joml.Matrix4f;

import java.awt.*;

public class ClientSkillTooltip implements ClientTooltipComponent {
    public static final ResourceLocation ICON = BeyondHorizon.resourceGui("tooltips/ability_items/icons.png");
    public static final ResourceLocation BOX = BeyondHorizon.resourceGui("tooltips/ability_items/box.png");
    public static final int DESC_WRAP_WIDTH = 320;
    public static final int ICON_HEIGHT = 16;
    public static final int ICON_OFFSETED_HEIGHT = ICON_HEIGHT + 8;
    private final ISkillItems skills;
    public static int menu;
    public static int scroll;

    public ClientSkillTooltip(SkillTooltip skillTooltip) {
        this.skills = skillTooltip.getSkills();
    }

    private void renderBox(GuiGraphics gui, int x, int y, int offsets) {
        gui.pose().pushPose();
        gui.pose().translate(x, y, 0);
        gui.pose().scale(0.65F, 0.65F, 0.65F);
        gui.pose().translate(-x, -y, 0);
        gui.blit(BOX, x, y, 0, offsets * 24, 24, 24, 24, 72);
        gui.pose().popPose();
    }

    private void renderIcons(GuiGraphics gui,int x,int y, int offsets) {
        gui.pose().pushPose();
        gui.pose().translate(x, y, 0);
        gui.pose().scale(0.65F, 0.65F, 0.65F);
        gui.pose().translate(-x, -y, 0);
        gui.blit(ICON, x, y, 0, offsets * 24, 24, 24, 32, 64);
        gui.pose().popPose();
    }

    private void renderActiveBox(GuiGraphics gui,int x,int y) {
        this.renderBox(gui, x, y, 1);
    }

    private void renderPassiveBox(GuiGraphics gui,int x,int y) {
        this.renderBox(gui, x, y, 2);
    }

    private void renderBox(GuiGraphics gui,int x,int y) {
        this.renderBox(gui, x, y, 0);
    }

    private void renderIconPassive(GuiGraphics gui,int x,int y) {
        this.renderIcons(gui, x, y, 0);
    }

    private void renderIconActive(GuiGraphics gui,int x,int y) {
        this.renderIcons(gui, x, y, 1);
    }

    @Override
    public int getHeight() {
        return this.descriptionWordWrapHeight() + 20;
    }

    @Override
    public int getWidth(Font font) {
        return (DESC_WRAP_WIDTH);
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
//        int append = 0;
//        int appenedDesc = 0;
//        for (var skill : this.skills.getSkills()) {
//            if (skill.getType() == ItemAbilityType.NONE) continue;
//            if (skill.isPassive()) {
//                this.renderPassiveBox(graphics, x + 4, y + (appenedDesc * 9) + (9 * append));
//                this.renderIconPassive(graphics, x, y + (appenedDesc * 9) + (9 * append));
//            } else {
//                this.renderActiveBox(graphics, x + 4, y + (appenedDesc * 9) + (9 * append));
//                this.renderIconActive(graphics, x, y + (appenedDesc * 9) + (9 * append));
//            }
//            for (var tooltips : skill.addTooltip()) {
//                appenedDesc+=this.textLine(font, tooltips, DESC_WRAP_WIDTH);
//            }
//            append++;
//        }
//        graphics.renderOutline(x - 10, y + ICON_OFFSETED_HEIGHT - 4, getWidth(font) + 20,
//                7 + this.descriptionWordWrapHeight() + 13, Colors.WHITE);

    }

    @Override
    public void renderText(Font font, int mX, int mY, Matrix4f matrix4f, MultiBufferSource.BufferSource buffer) {
        Matrix4f copyMatrix4f = new Matrix4f(matrix4f);
        copyMatrix4f.translate(mX, mY, 0);
        copyMatrix4f.scale(1.0F);
        copyMatrix4f.translate(-mX, -mY, 0);
        int appened = 0;
        int appenedDesc = 0;
        Player player = BeyondHorizon.PROXY.clientPlayer();
        ItemStack itemStack = PlayerData.getHeldingItem(player);
        for (var skill : this.skills.getSkills()) {
            if (skill.getType() == ItemAbilityType.NONE) continue;
            var desc = skill.addTooltip();
            for (var tooltips : desc) {
                this.textWrap(font, tooltips, mX, mY + (9 * appenedDesc), appened, DESC_WRAP_WIDTH, -1, matrix4f, buffer);
                appenedDesc+=this.textLine(font, tooltips, DESC_WRAP_WIDTH);
            }
            appened++;
        }
    }

    public int height(Skill skill) {
        Minecraft mc = Minecraft.getInstance();
        int descWWH = 0;
        Player player = BeyondHorizon.PROXY.clientPlayer();
        var desc = skill.addTooltipDescription(PlayerData.getHeldingItem(player));
        for (var tooltips : desc) {
            descWWH += mc.font.wordWrapHeight(tooltips, DESC_WRAP_WIDTH);
        }
        return descWWH;
    }

    public int descriptionWordWrapHeight() {
        Minecraft mc = Minecraft.getInstance();
        int descWWH = 0;
        for (var skill : this.skills.getSkills()) {
            if (skill.getType() == ItemAbilityType.NONE) continue;
            var desc = skill.addTooltip();
            for (var tooltips : desc) {
                int textLine = this.textLine(mc.font, tooltips, DESC_WRAP_WIDTH);
                descWWH += mc.font.wordWrapHeight(tooltips, DESC_WRAP_WIDTH) + textLine;
            }
        }
        return descWWH;
    }

    public static void registerFactory() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSkillTooltip::onRegisterTooltipEvent);
    }

    private static void onRegisterTooltipEvent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(SkillTooltip.class, ClientSkillTooltip::new);
    }

    public static void manageTooltipScrolling(double delta) {
        if (delta < 0) {
            scroll = scroll + 1 == Integer.MAX_VALUE ? 0 : scroll + 1;
        }
        if (delta > 0) {
            scroll = scroll - 1 == 0 ? Integer.MAX_VALUE : scroll - 1;
        }
    }

    public static void manageKeyPress(double keyCode) {
        if (keyCode == 340 || keyCode == 341) {
            menu = menu + 1 == Integer.MAX_VALUE ? 0 : menu + 1;
        }
    }

    public void textWrap(Font font, FormattedText text, int x, int y, int appened, int lineWidth, int color, Matrix4f matrix4f, MultiBufferSource.BufferSource buffer) {
        for (FormattedCharSequence formattedcharsequence : font.split(text, lineWidth)) {
            Matrix4f copyMatrix = new Matrix4f(matrix4f);
            copyMatrix.translate(x, y, 0);
            copyMatrix.scale(1.0F);
            copyMatrix.translate(-x, -y, 0);
            font.drawInBatch(formattedcharsequence, x, y + (9 * appened), color, true, copyMatrix, buffer, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
            y += 9;
        }
    }

    public int textLine(Font font, FormattedText text,int lineWidth) {
        int line = 0;
        for (FormattedCharSequence formattedcharsequence : font.split(text, lineWidth)) {
            line++;
        }
        return line;
    }
}
