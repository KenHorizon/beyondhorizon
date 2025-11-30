package com.kenhorizon.beyondhorizon.client;

import com.kenhorizon.beyondhorizon.server.ServerProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings({"removal"})
public class ClientProxy extends ServerProxy {
    @Override
    public void serverHandler() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    }

    @Override
    public void clientHandler() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        Raid.RaiderType.create("ILLUSIONER", EntityType.ILLUSIONER, new int[]{0, 0, 1, 2, 2, 3, 4, 5});
    }

    @Override
    public Player clientPlayer() {
        return Minecraft.getInstance().player;
    }

}
