package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.server.api.level_system.LevelSystem;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.IStackableInstance;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryStackHandler;
import com.kenhorizon.beyondhorizon.server.api.level.ICombatData;
import com.kenhorizon.beyondhorizon.server.api.level.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class Capabilities {

    public static IStackableInstance stackable(LivingEntity entity) {
        return getCapability(entity, BHCapabilties.STACK_TAGS);
    }
    public static IAccessoryStackHandler accessory(Player player) {
        return getCapability(player, BHCapabilties.ACCESSORY);
    }

    public static IDamageInfo damageInfo(LivingEntity entity) {
        return getCapability(entity, BHCapabilties.DAMAGE_INFOS);
    }

    public static ICombatData combat(LivingEntity entity) {
        return getCapability(entity, BHCapabilties.COMBAT_DATA);
    }

    public static LevelSystem levelSystem(Player player) {
        return getCapability(player, BHCapabilties.ROLE_CLASS);
    }

    public static PlayerData data(Player player) {
        return getCapability(player, BHCapabilties.PLAYER_DATA);
    }

    @Nullable
    private static <T> T getCapability(ItemStack itemStack, Capability<T> capability) {
        if (itemStack == null) return null;
        if (itemStack.isEmpty()) return null;
        return itemStack.getCapability(capability).isPresent() ? itemStack.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")) : null;
    }

    @Nullable
    private static <T> T getCapability(Entity entity, Capability<T> capability) {
        if (entity == null) return null;
        if (entity.isRemoved()) return null;
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")) : null;
    }
}
