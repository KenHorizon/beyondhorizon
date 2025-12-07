package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.accessory.IAccessoryItemHandler;
import com.kenhorizon.beyondhorizon.server.classes.IRoleClass;
import com.kenhorizon.beyondhorizon.server.level.ICombatCore;
import com.kenhorizon.beyondhorizon.server.level.damagesource.IDamageInfo;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class BHCapabilties {
    public static final Capability<IAccessoryItemHandler> ACCESSORY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IDamageInfo> DAMAGE_INFOS = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ICombatCore> COMBAT_CORE = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IRoleClass> ROLE_CLASS = CapabilityManager.get(new CapabilityToken<>() {});
}
