package com.kenhorizon.libs.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.listeners.SpawnerBuilderListener;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReloadResourceEventHandler {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onUpdateTags(AddReloadListenerEvent event) {
        ICondition.IContext context = event.getConditionContext();
        List<IReloadable> reloadList = ReloadableHandler.getReloadList();
        BeyondHorizon.LOGGER.debug("Initaliasing reloadables for {} values", reloadList.size());
        long start = System.nanoTime();
        reloadList.forEach(IReloadable::reload);
        long end = System.nanoTime();
        double milliseconds = (end - start) / 1000000.0d;
        BeyondHorizon.LOGGER.info("Finished initialising! Took {}ms", milliseconds);
    }
}
