package com.kenhorizon.beyondhorizon.client;

import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItem;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("deprecated")
public class TooltipsEventHandler {
    @SubscribeEvent
    public void onRegisterTooltipGatherComponents(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof ISkillItems<?> || stack.getItem() instanceof IAccessoryItem) {
            event.setMaxWidth(580);
        }
    }
}
