package com.kenhorizon.beyondhorizon.client;


import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.keybinds.Keybinds;
import com.kenhorizon.beyondhorizon.client.render.BHBossBar;
import com.kenhorizon.beyondhorizon.client.render.guis.LevelSystemScreen;
import com.kenhorizon.beyondhorizon.client.render.guis.accessory.AccessorySlotButton;
import com.kenhorizon.beyondhorizon.client.render.guis.accessory.AccessorySlotScreen;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.AttributeTooltips;
import com.kenhorizon.beyondhorizon.client.sound.BossMusicPlayer;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.api.event.DamageTiltEvent;
import com.kenhorizon.beyondhorizon.server.entity.BHBossInfo;
import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClientEventHandler {

    @SubscribeEvent
    public void onDebugInformation(CustomizeGuiOverlayEvent.DebugText event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft minecraft = Minecraft.getInstance();
        var leftInfo = event.getLeft();
        var rightInfo = event.getRight();
        if (BHConfigs.REDUCE_DEBUG && minecraft.options.renderDebug) {
            leftInfo.clear();
            renderNewDebug(guiGraphics);
        }
    }

    private void renderNewDebug(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        List<String> list = new ArrayList<>();
        Entity entity = minecraft.getCameraEntity();
        BlockPos blockpos = entity.blockPosition();
        Direction direction = entity.getDirection();
        String directionText;
        switch (direction) {
            case NORTH:
                directionText = "Towards negative Z";
                break;
            case SOUTH:
                directionText = "Towards positive Z";
                break;
            case WEST:
                directionText = "Towards negative X";
                break;
            case EAST:
                directionText = "Towards positive X";
                break;
            default:
                directionText = "Invalid";
        }
        ChunkPos chunkpos = new ChunkPos(blockpos);
        Level level = entity.level();
        list.add(String.format(Locale.ROOT, "FPS: %s", minecraft.getFps()));
        list.add(String.format(Locale.ROOT, "XYZ: %.2f / Y: %.2f /Z: %.2f", entity.getX(), entity.getY(), entity.getZ()));
        list.add(String.format(Locale.ROOT, "Facing: %s (%s) (%.1f / %.1f)", direction, directionText, Mth.wrapDegrees(entity.getYRot()), Mth.wrapDegrees(entity.getXRot())));

        int top = 2;
        for (String msg : list) {
            if (msg != null && !msg.isEmpty())
            {
//                guiGraphics.fill(1, top - 1, 2 + font.width(msg) + 1, top + font.lineHeight - 1, -1873784752);
                guiGraphics.drawString(font, msg, 2, top, 14737632, false);
            }
            top += font.lineHeight;
        }
    }

    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END) {
            BossMusicPlayer.tick();
        }
        if (event.level.isClientSide) {
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
    }

    @SubscribeEvent
    public void onMovementInput(MovementInputUpdateEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null) {
            if (player.hasEffect(BHEffects.CURSED.get()) || player.hasEffect(BHEffects.FEAR.get())) {
                if (minecraft.options.keyDown.isDown()) {
                    event.getInput().forwardImpulse += 2F;
                }
                if (minecraft.options.keyLeft.isDown()) {
                    event.getInput().leftImpulse -= 2F;
                }
                if (minecraft.options.keyRight.isDown()) {
                    event.getInput().leftImpulse += 2F;
                }
                if (minecraft.options.keyUp.isDown()) {
                    event.getInput().forwardImpulse -= 2F;
                }
            }
        }
    }

    @SubscribeEvent
    public void registerCustomBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        BHBossInfo.BossBar entry = ClientProxy.BOSS_BAR_REGISTRY.getOrDefault(event.getBossEvent().getId(), null);
        if (entry == null) return;
        BHBossBar bossBar = BHBossBar.BOSS_BARS.getOrDefault(entry, null);
        event.setCanceled(true);
        bossBar.renderBossBar(event);
    }

    @SubscribeEvent
    public void addTootipOnItems(ItemTooltipEvent event) {
        final List<Component> additions = new ArrayList<>();
        Player player = event.getEntity();
        List<Component> tooltip = event.getToolTip();
        TooltipFlag flag = event.getFlags();
        boolean isAdvanced = event.getFlags().isAdvanced();
        ItemStack itemStack = event.getItemStack();
        AttributeTooltips attributeTooltips = new AttributeTooltips();
        attributeTooltips.addTooltips(itemStack, player, flag, tooltip);
    }

    @SubscribeEvent
    public void onKeyPressClient(InputEvent.Key event) {
        if (event.getKey() == Keybinds.LEVEL_SYSTEM.getKey().getValue() && BeyondHorizon.PROXY.isKeyPressed(Keybinds.LEVEL_SYSTEM)) {
            BeyondHorizon.PROXY.openScreen(new LevelSystemScreen());
        }
    }
    @SubscribeEvent
    public void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity player = minecraft.getCameraEntity();
        float partialTick = minecraft.getPartialTick();
        float delta = minecraft.getFrameTime();
        float ticksExistedDelta = player.tickCount + delta;
        if (BHConfigs.SCREEN_SHAKE && !minecraft.isPaused()) {
            float shakeAmplitude = 0;
            for (CameraShake cameraShake : player.level().getEntitiesOfClass(CameraShake.class, player.getBoundingBox().inflate(64))) {
                if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                    shakeAmplitude += cameraShake.getShakeAmount((Player) player, delta);
                    shakeAmplitude *= (Mth.clamp((float) BHConfigs.SCREEN_SHAKE_AMOUNT / 100, 0.0F, 1.0F));
                }
            }
            if (shakeAmplitude > 1.0F) shakeAmplitude = 1.0F;
            event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
            event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
            event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));
        }
        if (player instanceof LivingEntity entity && entity.hasEffect(BHEffects.STUN.get())) {
            event.setRoll((float) (Math.sin((player.tickCount + partialTick) * 0.2F) * 10F));
        }
    }
    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        Screen eventScreen = event.getScreen();
        RecipeBookComponent component = new RecipeBookComponent();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        if (eventScreen instanceof InventoryScreen || eventScreen instanceof CreativeModeInventoryScreen) {
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) eventScreen;
            boolean isCreative = eventScreen instanceof CreativeModeInventoryScreen;
            int x = (gui.width - gui.getXSize()) / 2;
            int y = (gui.height - gui.getYSize()) / 2;
            x += isCreative ? 173 : 58;
            y += isCreative ? 65 : 8;
            if (component.isVisible()) {
                x += 10;
                y += 10;
            }
            event.addListener(new AccessorySlotButton(eventScreen, x, y));
        }
        if (eventScreen instanceof AccessorySlotScreen) {
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) eventScreen;
            int x = (gui.width - gui.getXSize()) / 2;
            int y = (gui.height - gui.getYSize()) / 2;
            event.addListener(new AccessorySlotButton(eventScreen, x - 40, y + 4));
        }
    }

    @SubscribeEvent
    public void onInventoryGuiDrawBackground(ScreenEvent.Render.Pre event) {
        if (!(event.getScreen() instanceof InventoryScreen gui)) {
            return;
        }
        gui.xMouse = event.getMouseX();
        gui.yMouse = event.getMouseY();
    }
}
