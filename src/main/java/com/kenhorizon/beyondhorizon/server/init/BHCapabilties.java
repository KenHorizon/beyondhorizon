package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillSlots;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryStackHandler;
import com.kenhorizon.beyondhorizon.server.api.level_system.LevelSystem;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkill;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.IStackableInstance;
import com.kenhorizon.beyondhorizon.server.api.level.ICombatData;
import com.kenhorizon.beyondhorizon.server.api.level.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class BHCapabilties {
    public static final Capability<ISkillSlots> SKILL_SLOTS = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ISkill> SKILL_ITEM = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IAccessory> ACCESSORY_ITEM = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IStackableInstance> STACK_TAGS = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IAccessoryStackHandler> ACCESSORY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IDamageInfo> DAMAGE_INFOS = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ICombatData> COMBAT_DATA = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<LevelSystem> ROLE_CLASS = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<PlayerData> PLAYER_DATA = CapabilityManager.get(new CapabilityToken<>() {});

    public static final ResourceLocation ID_ACCESSORY = BeyondHorizon.resource("accessory");
    public static final ResourceLocation ID_DAMAGE_INFO = BeyondHorizon.resource("damage_info");
    public static final ResourceLocation ID_COMBAT_INFO = BeyondHorizon.resource("combat_date");
    public static final ResourceLocation ID_LEVEL_SYSTEM = BeyondHorizon.resource("level_system");
    public static final ResourceLocation ID_PLAYER_DATA = BeyondHorizon.resource("player_data");
    public static final ResourceLocation ID_STACKABLE_TAGS = BeyondHorizon.resource("stackable_tags");
    public static final ResourceLocation ID_SKILL_SLOTS = BeyondHorizon.resource("skill_slots");
    public static final ResourceLocation ID_ITEM = BeyondHorizon.resource("item");
}
