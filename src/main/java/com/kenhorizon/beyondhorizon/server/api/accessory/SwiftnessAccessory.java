package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
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
            sLevel.sendParticles(new TrailParticleOptions(20, 0, 186, 255, 255, 1.0F,
                    TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(player.getX(), player.getY() + player.getBbHeight() / 2, player.getZ())
            ),player.getX(), player.getY() + player.getBbHeight() / 2, player.getZ(),10,0,0, 0, 0);
            sLevel.playSound(null, BlockPos.containing(player.position()), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS);
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
