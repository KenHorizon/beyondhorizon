package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class CleaveConeAbility extends AbstractConeAbility {
    public CleaveConeAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setDuration(2);
        this.setRadius(2.5F);
    }

    public static void spawn(Level level, LivingEntity target, LivingEntity owner, float damage, boolean startWithTarget, DamageType damageType) {
        CleaveConeAbility ability = new CleaveConeAbility(BHEntity.CLEAVE_CONE_ABILITY.get(), level);
        ability.setBaseDamage(damage);
        if (startWithTarget) {
            ability.setPos(target.position().add(0, target.getEyeHeight() * .7, 0));
        } else {
            ability.setPos(owner.position().add(0, owner.getEyeHeight() * .7, 0));
        }
        ability.setConeAtTarget(startWithTarget);
        ability.setTarget(target);
        ability.setCaster(owner);
        ability.setDamageType(damageType);
        level.addFreshEntity(ability);
    }


    @Override
    protected void onEnd() {
        LivingEntity user = this.getCaster();
        if (!this.sentEventSpike) {
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.sentEventSpike = true;
        }
    }


    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        var entity = entityHitResult.getEntity();
        entity.hurt(BHDamageTypes.physicalDamage(this), this.getBaseDamage());
    }

    @Override
    public void spawnParticles() {

    }
}
