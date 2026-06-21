package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

public abstract class AbstractImmolateAccessory extends AccessoryPassiveSkill {
    public AbstractImmolateAccessory(float magnitude) {
        super(magnitude, 1);
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        var cap = Capabilities.combat(entity);
        if (cap != null) {
            if (cap.OnCombat()) {
                Level level = entity.level();
                if (entity.tickCount % this.immolatePerSecond() == 0) {
                    if (level instanceof ServerLevel sLevel) {
                        float r = ColorUtil.getFARGB(colorRing())[0];
                        float g = ColorUtil.getFARGB(colorRing())[1];
                        float b = ColorUtil.getFARGB(colorRing())[2];
                        sLevel.sendParticles(new RingParticleOptions(0, (float) -Math.PI / 2, 10, r, g, b, 1.0F, 32.0F, false, RingParticles.Behavior.GROW), entity.getX(), entity.getY(), entity.getZ(), 2, 0,0 ,0 ,0);
                    }
                    for (LivingEntity affected : entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(6.0D, 2.0F, 6.0D))) {
                        if (affected.isDamageSourceBlocked(this.getSource(affected))) continue;
                        if (affected != entity && !(affected.isInvulnerable() || affected.isAlliedTo(entity))) {
                            affected.hurtTime = 0;
                            affected.hurt(this.getSource(affected), this.getImmolateDamage(affected, entity));
                        }
                    }
                }
            }
        }
    }

    public int colorRing() {
        return 0xFFFFFF;
    }
    public long immolatePerSecond() {
        return 20L;
    }

    public DamageSource getSource(LivingEntity affected) {
        return BHDamageTypes.magicDamage(affected, true);
    }

    public abstract float getImmolateDamage(LivingEntity affected, LivingEntity source);
}
