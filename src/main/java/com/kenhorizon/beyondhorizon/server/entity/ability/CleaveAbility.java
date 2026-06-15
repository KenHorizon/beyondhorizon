package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CleaveAbility extends AbilityEntity {
    public static final int CLEAVE_DURATION = 15;
    public enum Type {
        CIRCLE,
        CONE
    }

    public CleaveAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setDuration(2);
        this.setRadius(2.5F);
    }

    public static void spawn(Level level, LivingEntity target, LivingEntity owner, float damage, float range) {
        CleaveAbility ability = new CleaveAbility(BHEntity.CLEAVE_ABILITY.get(), level);
        ability.setBaseDamage(damage);
        ability.setRadius(range);
        ability.setPos(target.position().add(0, target.getBbHeight() * 0.05D, 0));
        ability.setCaster(owner);
        level.addFreshEntity(ability);
    }


    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {

            float r = ColorUtil.getFARGB(0xFF6500)[0];
            float g = ColorUtil.getFARGB(0xFF6500)[1];
            float b = ColorUtil.getFARGB(0xFF6500)[2];
            this.level().addParticle(new RingParticleOptions(0, (float) Math.PI / 2, CLEAVE_DURATION, r, g, b, 1.0F, 32.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(), this.getZ(), 0, 0, 0);

        }
    }

    @Override
    protected void onEnd() {
        LivingEntity user = this.getCaster();
        if (!this.sentEventSpike) {
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.sentEventSpike = true;
        }
        this.cleaveAttack();
    }

    public void cleaveAttack() {
        LivingEntity attacker = this.getCaster();
        List<Entity> cleaveRange = this.level().getEntities(this, this.getBoundingBox().inflate(this.getRadius()));
        for (Entity entityOnRange : cleaveRange) {
            if (entityOnRange instanceof LivingEntity targetOnRange) {
                if (targetOnRange == attacker || targetOnRange == this.getTarget()) continue;
                if (targetOnRange.isAlive() && !targetOnRange.isInvulnerable()) {
                    targetOnRange.hurt(BHDamageTypes.physicalDamage(this), this.getBaseDamage());
                }
            }
        }
    }
}
