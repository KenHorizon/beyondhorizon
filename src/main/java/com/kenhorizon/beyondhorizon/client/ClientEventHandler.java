package com.kenhorizon.beyondhorizon.client;


import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.keybinds.Keybinds;
import com.kenhorizon.beyondhorizon.client.level.guis.hud.BHBossBar;
import com.kenhorizon.beyondhorizon.client.level.guis.LevelSystemScreen;
import com.kenhorizon.beyondhorizon.client.level.guis.accessory.AccessorySlotButton;
import com.kenhorizon.beyondhorizon.client.level.guis.accessory.AccessorySlotScreen;
import com.kenhorizon.beyondhorizon.client.level.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.configs.client.ModClientConfig;
import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.item.util.ItemStackUtil;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientEventHandler {
    @SubscribeEvent
    public void registerCustomBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        ResourceLocation bossRegistryName = ClientProxy.BOSS_BAR_REGISTRY.getOrDefault(event.getBossEvent().getId(), null);
        if (bossRegistryName == null) return;
        BHBossBar bossBar = BHBossBar.BOSS_BARS.getOrDefault(bossRegistryName, null);
        if (bossBar == null) return;
        event.setCanceled(true);
        bossBar.renderBossBar(event);
    }

    @SubscribeEvent
    public void addTootipOnItems(ItemTooltipEvent event) {
        final List<Component> additions = new ArrayList<>();
        List<Component> tooltip = event.getToolTip();
        boolean isAdvanced = event.getFlags().isAdvanced();
        ItemStack itemStack = event.getItemStack();
        float digSpeed = ItemStackUtil.getDestroySpeed(itemStack);
        if (digSpeed > 0.0F) {
            additions.add(CommonComponents.space().append(Component.translatable(Tooltips.TOOLTIP_MINING_SPEED, Maths.format1(digSpeed)).withStyle(ChatFormatting.DARK_GREEN)));
        }
        if (itemStack.isDamaged()) {
            additions.add(Component.translatable("item.durability", itemStack.getMaxDamage() - itemStack.getDamageValue(), itemStack.getMaxDamage()).withStyle(ChatFormatting.DARK_GRAY));
        }
        if (!additions.isEmpty()) {
            tooltip.addAll(ItemStackUtil.getInsertOffset(isAdvanced, tooltip.size(), itemStack), additions);
        }
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
        if (ModClientConfig.SCREEN_SHAKE.get() && !minecraft.isPaused()) {
            float shakeAmplitude = 0;
            for (CameraShake cameraShake : player.level().getEntitiesOfClass(CameraShake.class, player.getBoundingBox().inflate(64))) {
                if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                    shakeAmplitude += cameraShake.getShakeAmount((Player) player, delta);
                    shakeAmplitude *= (Mth.clamp((float) ModClientConfig.SCREEN_SHAKE_AMOUNT.get() / 100, 0.0F, 1.0F));
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
    public void onInventoryGui(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        RecipeBookComponent component = new RecipeBookComponent();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
            boolean isCreative = screen instanceof CreativeModeInventoryScreen;
            int x = (gui.width - gui.getXSize()) / 2;
            int y = (gui.height - gui.getYSize()) / 2;
            x += isCreative ? 173 : 58;
            y += isCreative ? 65 : 8;
            if (component.isVisible()) {
                x += 10;
                y += 10;
            }
            event.addListener(new AccessorySlotButton(screen, x, y));
        }
        if (screen instanceof AccessorySlotScreen) {
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
            int x = (gui.width - gui.getXSize()) / 2;
            int y = (gui.height - gui.getYSize()) / 2;
            event.addListener(new AccessorySlotButton(screen, x - 40, y + 4));
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
