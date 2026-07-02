package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ExperienceAccessory extends AccessoryPassiveSkill {
    public ExperienceAccessory(double modifier) {
        this.setMagnitude((float) modifier);
        this.setLevel(1);
    }

    @Override
    public int modifyExprienceDrop(int dropExperience, LivingEntity target, Player player) {
        int newDropExperience = dropExperience + (int) (dropExperience * (this.getMagnitude() * this.getLevel()));
//        BeyondHorizon.LOGGER.info("Exp Drop {} | Modified Exp Drop {}", dropExperience, newDropExperience);
        return newDropExperience;
    }
}
