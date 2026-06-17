package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SwiftnessAccessory extends AccessoryActiveSkill {

    public SwiftnessAccessory() {
        this.manaCost = 5;
    }

    @Override
    public void onKeypress(Player player, ItemStack itemStack, int slot) {
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, MathUtils.sec(5), 1, true, true));
    }
}
