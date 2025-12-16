package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ExperienceAccessory extends AccessorySkill {
    public ExperienceAccessory(double modifier) {
        this.setMagnitude((float) modifier);
        this.setLevel(1);
    }

    @Override
    public int modifyExprienceDrop(int dropExperience, LivingEntity target, Player player) {
        int newDropExperience = dropExperience + (int) (dropExperience * (this.getMagnitude() * this.getLevel()));
        BeyondHorizon.LOGGER.info("Exp Drop {} Modified Exp Drop {}", dropExperience, newDropExperience);
        return newDropExperience;
    }
}
