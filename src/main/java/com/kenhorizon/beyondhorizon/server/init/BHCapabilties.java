package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItemHandler;
import com.kenhorizon.beyondhorizon.server.api.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.level.ICombatCore;
import com.kenhorizon.beyondhorizon.server.level.damagesource.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.skills.ActiveSkill;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class BHCapabilties {
    public static final Capability<IAccessoryItemHandler> ACCESSORY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IDamageInfo> DAMAGE_INFOS = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ICombatCore> COMBAT_CORE = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<RoleClass> ROLE_CLASS = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<PlayerData> PLAYER_DATA = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ActiveSkill> ACTIVE_SKILL = CapabilityManager.get(new CapabilityToken<>() {});
}
