package com.kenhorizon.beyondhorizon.server.api.accessory.ability.active;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryActiveSkill;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SwiftnessAccessory extends AccessoryActiveSkill {

    @Override
    public void onActiveAbility(Player player, ItemStack itemStack) {
        if (player.level() instanceof ServerLevel sLevel) {
            for (int i = 0; i < 12; i++) {
                sLevel.sendParticles(new TrailParticleOptions(20, 0, 186, 255, 255, 1.0F,
                        TrailParticles.Behavior.FADE, new Vec3(player.getRandomX(0.50D), player.getY() + player.getBbHeight() / 2, player.getRandomZ(0.50D))
                ),player.getX(), player.getY() + player.getBbHeight() / 2, player.getZ(),1,0,0, 0, 0);
            }
            sLevel.sendParticles(new RingParticleOptions(0, (float) Math.PI / 2, 20, Colors.GREEN, 32.0F, false, RingParticles.Behavior.GROW), player.getX(), player.getY() + player.getBbHeight() / 2, player.getZ(),1, 0,0,0, 0);
        }
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Maths.sec(5), 1, true, true));
    }

    @Override
    protected int getCooldown() {
        return Maths.sec(5);
    }

    @Override
    protected int getManaCost() {
        return 10;
    }
}
