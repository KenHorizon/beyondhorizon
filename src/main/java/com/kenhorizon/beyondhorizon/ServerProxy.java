package com.kenhorizon.beyondhorizon;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

public class ServerProxy {

    private static MinecraftServer minecraftServer;

    public void serverHandler() {}

    public void clientHandler() {}

    public void post() {}

    public void openScreen(Screen screen) {}

    public Player clientPlayer() {
        return null;
    }

    public float partialTicks() {
        return 0.0F;
    }

    public void renderBlockAndItemColors(RegisterColorHandlersEvent.Block event) {}

    public boolean isKeyDown(KeyMapping keyMapping) {
        return false;
    }

    public boolean isKeyPressed(KeyMapping keyMapping) {
        return false;
    }

    public void syncAccessoryToPlayer(int slot, ItemStack itemStack, ServerPlayer player) {}

    public Vec3 getCameraRotation() {
        return Vec3.ZERO;
    }

    public Object getCustomItemRenderer() {
        return null;
    }

    public Object getCustomArmorRenderer() {
        return null;
    }

    public void onAnimationHandler(int entityId, int index) {}

    public void playSound(AbstractSoundInstance instance) {}

    public Object getFontRenderer() {
        return null;
    }
}
