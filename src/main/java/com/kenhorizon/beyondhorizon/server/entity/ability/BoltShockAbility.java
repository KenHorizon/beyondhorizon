package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.LightningParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.init.BHParticle;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class BoltShockAbility extends AbilityEntity {
    public BoltShockAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setDuration(2);
        this.setBaseDamage(2);
        this.setRadius(2.0F);
    }

    public static void spawn(Level level, double x, double y, double z, float damage, float radius, int duration, LivingEntity entity) {
        BoltShockAbility ability = new BoltShockAbility(BHEntity.BOLT_SHOCK.get(), level);
        ability.setBaseDamage(damage);
        ability.setCaster(entity);
        ability.setCasterID(entity.getUUID());
        ability.setRadius(radius);
        ability.setDuration(duration);
        ability.setPos(x, y, z);
        level.addFreshEntity(ability);
    }

    @Override
    public void onEndEvent() {
        super.onEndEvent();
        for (int i = 0; i < 20; ++i) {
            double d0 = (random.nextFloat() - 0.5F) + this.getDeltaMovement().x * this.getRadius();
            double d1 = (random.nextFloat() - 0.5F) + this.getDeltaMovement().y * this.getRadius();
            double d2 = (random.nextFloat() - 0.5F) + this.getDeltaMovement().z * this.getRadius();
            double dist = random.nextFloat() * (5.0F * this.getRadius());
            double d3 = d0 * dist;
            double d4 = d1 * dist;
            double d5 = d2 * dist;
            this.level().addParticle(new LightningParticleOptions(0, 186, 255), this.getX() + d0, this.getY() + 0.5D, this.getZ() + d2, d3, d4, d5);
        }
        float r = ColorUtil.getFARGB(0xFFFFFF)[0];
        float g = ColorUtil.getFARGB(0xFFFFFF)[1];
        float b = ColorUtil.getFARGB(0xFFFFFF)[2];
        this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) Math.PI / 2, 15, r, g, b, 1.0F, 4.0F, false, RingParticles.Behavior.SHRINK), this.getX(), this.getY(), this.getZ(), 0, -10, 0);
        this.level().addAlwaysVisibleParticle(new RingParticleOptions(0, (float) -Math.PI / 2, 15, r, g, b, 1.0F, 4.0F, false, RingParticles.Behavior.SHRINK), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F, false);
        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F, false);

    }

    @Override
    protected void onEnd() {
        super.onEnd();
        this.dealDamage();
    }

    public void dealDamage() {
        LivingEntity attacker = this.getCaster();
        List<Entity> cleaveRange = this.level().getEntities(this, this.getBoundingBox().inflate(this.getRadius()));
        for (Entity entityOnRange : cleaveRange) {
            if (entityOnRange instanceof LivingEntity targetOnRange) {
                if (targetOnRange == attacker || targetOnRange == this.getTarget()) continue;
                if (targetOnRange.isAlive() && !targetOnRange.isInvulnerable()) {
                    targetOnRange.hurt(BHDamageTypes.magicDamage(this), this.getBaseDamage());
                }
            }
        }
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return super.shouldRender(pX, pY, pZ);
    }
}
