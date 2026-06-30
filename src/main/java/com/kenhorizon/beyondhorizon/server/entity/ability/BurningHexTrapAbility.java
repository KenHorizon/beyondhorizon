package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BurningHexTrapAbility extends AbilityEntity {
    public BurningHexTrapAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setDuration(100);
        this.setRadius(3.5F);
    }

    public static void spawn(Level level, double x, double y, double z, float damage, LivingEntity entity) {
        BurningHexTrapAbility ability = new BurningHexTrapAbility(BHEntity.BURNING_HEX_TRAP.get(), level);
        ability.setBaseDamage(damage);
        ability.setCasterID(entity.getUUID());
        ability.setPos(x, y, z);
        level.addFreshEntity(ability);
    }

    @Override
    protected void onDuration() {
        if (this.level().isClientSide()) {
            if (this.tickCount % 5 == 0) {
                float[] colors = ColorUtil.getFARGB(ColorUtil.RED);
                float[] colors1 = ColorUtil.getFARGB(ColorUtil.YELLOW);
                this.level().addParticle(new TrailParticleOptions(this.getDuration(), colors[0], colors[1], colors[2], colors[3], 1.0F,
                        TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D))),
                        this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
                this.level().addParticle(new TrailParticleOptions(this.getDuration(), colors1[0], colors1[1], colors1[2], colors1[3], 1.0F,
                                TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D))),
                        this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        } else {
            if (this.tickCount % 10 == 0) {
                this.checkEntityHit();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        var afflicted = hitResult.getEntity();
        if (afflicted instanceof LivingEntity entity) {
            if (this.getDamageType().dealDamage(entity, this.getCaster(), this.getBaseDamage(), true)) {
                entity.addEffect(new MobEffectInstance(BHEffects.BURNING_HEX.get(), Maths.sec(5)));
            }
        }
    }
}
