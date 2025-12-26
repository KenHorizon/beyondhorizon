package com.kenhorizon.beyondhorizon.server;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import javax.annotation.Nullable;

public class ServerProxy {

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

    public void syncAccessoryToPlayer(int slot, ItemStack itemStack, ServerPlayer serverPlayer) {}

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
}
