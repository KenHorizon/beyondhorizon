package com.kenhorizon.beyondhorizon.server.api.accessory.ability.active;

import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryActiveSkill;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class StalkerAccessory extends AccessoryActiveSkill {
    private static final UUID STEALTH_UUID = UUID.fromString("20c13c52-4226-4724-bf7a-b0ce3dbcf00a");
    private static final AttributeModifier STEALTH = new AttributeModifier(STEALTH_UUID, "Bonus stealth", 1.0D, AttributeModifier.Operation.ADDITION);

    public StalkerAccessory() {
        super(ManaCostType.PER_SECOND);
    }

    @Override
    public void onDurationAbility(Player player, ItemStack itemStack, boolean active) {
        if (this.manaNotEnough(player)) {
            player.setInvisible(false);
            var attr = player.getAttribute(BHAttributes.STEALTH.get());
            if (attr != null) {
                if (attr.getModifier(STEALTH_UUID) != null) {
                    attr.removeModifier(STEALTH_UUID);
                }
            }
        } else {
            player.setInvisible(active);
        }
    }

    @Override
    public void onActiveAbility(Player player, ItemStack itemStack) {
        if (player.level() instanceof ServerLevel sLevel) {
            for(int i = 0; i < 12; i++) {
                sLevel.sendParticles(new TrailParticleOptions(20, 0, 0, 0, 255, 1.0F,
                        TrailParticles.Behavior.FADE, new Vec3(player.getRandomX(0.50D), player.getY() + player.getBbHeight() / 2, player.getRandomZ(0.50D))
                ),player.getX(), player.getY() + player.getBbHeight() / 2, player.getZ(),1,0,0, 0, 0);
            }
            sLevel.playSound(null, BlockPos.containing(player.position()), SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS);
        }
        var attr = player.getAttribute(BHAttributes.STEALTH.get());
        if (attr != null) {
            if (attr.getModifier(STEALTH_UUID) == null) {
                attr.addTransientModifier(STEALTH);
            }
        }
    }

    @Override
    protected int getCooldown() {
        return 1;
    }

    @Override
    protected double getManaCost() {
        return 2;
    }
}
