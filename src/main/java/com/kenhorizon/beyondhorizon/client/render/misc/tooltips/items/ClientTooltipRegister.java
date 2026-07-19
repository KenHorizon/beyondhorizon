package com.kenhorizon.beyondhorizon.client.render.misc.tooltips.items;

public class ClientTooltipRegister {

    public static void register() {
        ClientQuiverTooltip.registerFactory();
        ClientVoidBagTooltip.registerFactory();
        ClientSkillTooltip.registerFactory();
    }
    
}
