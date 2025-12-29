package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class FlameStrikeAbility extends AbstractAbilityEntity {
    public FlameStrikeAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setDuration(40);
        this.setRadius(2.5F);
    }

    public static void spawn(Level level, double x, double y, double z, float damage, float radius, int duration, LivingEntity entity) {
        FlameStrikeAbility ability = new FlameStrikeAbility(BHEntity.FLAME_STRIKE.get(), level);
        ability.setBaseDamage(damage);
        ability.setCasterID(entity.getUUID());
        ability.setRadius(radius);
        ability.setDuration(duration);
        ability.setPos(x, y, z);
        level.addFreshEntity(ability);
    }
    public static void spawn(Level level, double x, double y, double z, float damage, LivingEntity entity) {
        FlameStrikeAbility ability = new FlameStrikeAbility(BHEntity.FLAME_STRIKE.get(), level);
        ability.setBaseDamage(damage);
        ability.setCasterID(entity.getUUID());
        ability.setPos(x, y, z);
        level.addFreshEntity(ability);
    }

    @Override
    protected void onDuration() {
        if (this.level().isClientSide()) {
            if (this.tickCount % 5 == 0) {
                this.level().addParticle(ParticleTypes.FLAME, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onEnd() {
        if (this.level().isClientSide()) {
            this.circleParticle(5);
        }
        LivingEntity attacker = this.getCaster();
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.getRadius()));
        for (LivingEntity entityOnRange : entities) {
            if (entityOnRange == attacker) continue;
            if (entityOnRange.isAlliedTo(attacker)) continue;
            if (entityOnRange.isAlive() && !entityOnRange.isInvulnerable()) {
                boolean flag = entityOnRange.hurt(BHDamageTypes.magicDamage(this, attacker), this.getBaseDamage());
                if (flag) {
                    entityOnRange.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Maths.sec(1), 2));
                }
            }
        }
    }

    private void circleParticle(int amount) {
        for (int i = 0; i < amount; i++) {
            final float velocity = 0.1F;
            float yaw = (float) (i * (2 * StrictMath.PI / amount));
            float vy = random.nextFloat() * 0.08F;
            float vx = velocity * Mth.cos(yaw);
            float vz = velocity * Mth.sin(yaw);
            level().addParticle(ParticleTypes.FLAME, getX(), getY() + 0.1, getZ(), vx, vy, vz);
        }
        for (int i = 0; i < amount / 2; i++) {
            level().addParticle(ParticleTypes.LAVA, getX(), getY() + 0.1, getZ(), 0, 0, 0);
        }
    }
}
