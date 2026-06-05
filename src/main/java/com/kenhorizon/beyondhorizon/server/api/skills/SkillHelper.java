package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItemHandler;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItems;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SkillHelper {
    public static boolean getWeaponWithSkill(Player player, Skill skill) {
        return getAllSkill(player).contains(skill);
    }

    private static List<Skill> getAllSkill(Player player) {
        List<Skill> result = new ArrayList<>();
        if (!player.isAlive()) return result;
        ItemStack itemStack = player.getMainHandItem();
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ISkillItems<?> caller) {
            result.addAll(caller.getSkills());
        }
        return result;
    }
}
