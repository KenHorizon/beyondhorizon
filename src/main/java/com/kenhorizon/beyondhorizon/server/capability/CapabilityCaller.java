package com.kenhorizon.beyondhorizon.server.capability;

import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.accessory.IAccessoryItemHandler;
import com.kenhorizon.beyondhorizon.server.level.ICombatCore;
import com.kenhorizon.beyondhorizon.server.level.damagesource.IDamageInfo;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class CapabilityCaller {
    public static IAccessoryItemHandler accessory(Player player) {
        return getCapability(player, BHCapabilties.ACCESSORY);
    }
    public static IDamageInfo damageInfo(LivingEntity entity) {
        return getCapability(entity, BHCapabilties.DAMAGE_INFOS);
    }
    public static ICombatCore combat(LivingEntity entity) {
        return getCapability(entity, BHCapabilties.COMBAT_CORE);
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
