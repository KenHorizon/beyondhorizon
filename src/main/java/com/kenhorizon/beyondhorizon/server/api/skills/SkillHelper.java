package com.kenhorizon.beyondhorizon.server.api.skills;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
