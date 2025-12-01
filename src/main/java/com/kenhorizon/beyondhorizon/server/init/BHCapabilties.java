package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.skills.accessory.IAccessoryItemHandler;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class BHCapabilties {
    public static final Capability<IAccessoryItemHandler> ACCESSORY = CapabilityManager.get(new CapabilityToken<>() {});
}
