package com.kenhorizon.beyondhorizon.client;


import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.keybinds.Keybinds;
import com.kenhorizon.beyondhorizon.client.level.guis.hud.BHBossBar;
import com.kenhorizon.beyondhorizon.client.level.guis.LevelSystemScreen;
import com.kenhorizon.beyondhorizon.client.level.guis.accessory.AccessorySlotButton;
import com.kenhorizon.beyondhorizon.client.level.guis.accessory.AccessorySlotScreen;
import com.kenhorizon.beyondhorizon.client.level.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.item.util.ItemStackUtil;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
