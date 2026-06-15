package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TitanicCrescentCleave extends CleaveConeAbility {

    public TitanicCrescentCleave(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public static void spawn(Level level, LivingEntity target, LivingEntity owner, float damage, boolean startWithTarget) {
        TitanicCrescentCleave ability = new TitanicCrescentCleave(BHEntity.CLEAVE_CONE_ABILITY.get(), level);
        ability.setBaseDamage(damage);
        if (startWithTarget) {
            ability.setPos(target.position().add(0, target.getEyeHeight() * .7, 0));
        } else {
            ability.setPos(owner.position().add(0, owner.getEyeHeight() * .7, 0));
        }
        ability.setConeAtTarget(startWithTarget);
        ability.setTarget(target);
        ability.setCaster(owner);
        level.addFreshEntity(ability);
    }


    @Override
    public void spawnParticles() {
        var owner = this.getTarget();
        if (!this.level().isClientSide() || owner == null) {
            return;
        }
        Vec3 rotation = owner.getLookAngle().normalize();
        var pos = owner.position().add(rotation.scale(1.6));

        double x = pos.x;
        double y = pos.y + owner.getEyeHeight() * .9f;
        double z = pos.z;

        double speed = random.nextDouble() * .35 + .35;
        for (int i = 0; i < 10; i++) {
            double offset = .15;
            double ox = Math.random() * 2 * offset - offset;
            double oy = Math.random() * 2 * offset - offset;
            double oz = Math.random() * 2 * offset - offset;

            double angularness = .5;
            Vec3 randomVec = new Vec3(Math.random() * 2 * angularness - angularness, Math.random() * 2 * angularness - angularness, Math.random() * 2 * angularness - angularness).normalize();
            Vec3 result = (rotation.scale(3).add(randomVec)).normalize().scale(speed);
            this.level().addParticle(ParticleTypes.FLAME, x + ox, y + oy, z + oz, result.x, result.y, result.z);
        }
    }
}
