package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.LightningParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.entity.util.ShockwaveUtils;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LightningStrikeAbility extends AbilityEntity {
    public long seed;
    public LightningStrikeAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setDuration(40);
        this.setBaseDamage(2);
        this.setRadius(4.0F);
        this.seed = this.random.nextLong();
    }

    public LightningStrikeAbility(Level level, float damage) {
        super(BHEntity.LIGHTNING_STRIKE.get(), level);
        this.setDuration(40);
        this.setBaseDamage(damage);
        this.setRadius(4.0F);
        this.seed = this.random.nextLong();
    }


    @Override
    public void clientSide() {
        int colorCode = Colors.combineRGB(0, 186, 255);
        float r = Colors.getFARGB(colorCode)[0];
        float g = Colors.getFARGB(colorCode)[1];
        float b = Colors.getFARGB(colorCode)[2];
        if (this.getLifeTime() == 0) {
            for (int i = 0; i < 60; ++i) {
                this.level().addParticle(new TrailParticleOptions((this.getDuration() / 2) + 10, r, g, b, 1.0F, 0.70F
                                , TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(this.getX(), this.getY(), this.getZ()))
                        , this.getRandomX(8), this.getY() + random.nextInt(4), this.getRandomZ(8), 0, 0, 0);
            }
        }
        if (this.hasStriked(5)) {
            this.seed = this.random.nextLong();
        }
       if (this.hasStriked(2)) {
           for (int i = 0; i < 20; ++i) {
               this.level().addParticle(new TrailParticleOptions(120, r, g, b, 1.0F, 0.70F
                               , TrailParticles.Behavior.DEFAULT, new Vec3(this.getRandomX(8), this.getY() + random.nextInt(4), this.getRandomZ(8)))
                       , this.getX(), this.getY(), this.getZ(), 0, 0, 0);
           }
           for (int i = 0; i < 20; ++i) {
               RandomSource randoms = RandomSource.create(this.random.nextLong());
               double d0 = (random.nextFloat() - 1.5F) + randoms.nextFloat() * 2.0F;
               double d1 = (random.nextFloat() - 1.5F) + randoms.nextFloat() * 2.0F;
               double d2 = (random.nextFloat() - 1.5F) + randoms.nextFloat() * 2.0F;
               double dist = random.nextFloat() * this.getRadius();
               double d3 = d0 * dist;
               double d4 = d1 * dist;
               double d5 = d2 * dist;
               this.level().addAlwaysVisibleParticle(new LightningParticleOptions(0, 186, 255), this.getX() + d0, this.getY() + 0.5D, this.getZ() + d2, d3, d4, d5);
           }
           this.level().addParticle(new RingParticleOptions(0, (float) Math.PI / 2, 20, r, g, b, 1.0F, 32.0F,
                   false, RingParticles.Behavior.GROW), this.getX(), this.getY(0.01D), this.getZ(), 0, 0, 0);
       }
        if (this.hasStriked()) {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F, false);
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F, false);
        }
    }
    @Override
    protected void onEnd() {
        this.dealDamage();
        ShockwaveUtils.doRingShockwave(this, this.position(), 2, -0.101F, 20);
    }

    public boolean hasStriked(int initialStart) {
        return this.getLifeTime() > this.getDuration() - initialStart;
    }
    public boolean hasStriked() {
        return hasStriked(1);
    }

    public void dealDamage() {
        LivingEntity attacker = this.getCaster();
        for (Entity entityOnRange : this.level().getEntities(this, this.getBoundingBox().inflate(this.getRadius()))) {
            if (entityOnRange instanceof LivingEntity nearby) {
                if (attacker == null) {
                    if (nearby.isAlive() && !nearby.isInvulnerable()) {
                        nearby.hurt(BHDamageTypes.applyDamage(damageTypes.MAGIC_DAMAGE, DamageTags.AREA_OF_EFFECTS, this, null), this.getBaseDamage());
                    }
                } else {
                    if (nearby == attacker || attacker.isAlliedTo(nearby)) continue;
                    if (nearby.isAlive() && !nearby.isInvulnerable()) {
                        nearby.hurt(BHDamageTypes.applyDamage(damageTypes.MAGIC_DAMAGE, DamageTags.AREA_OF_EFFECTS, this, attacker), this.getBaseDamage());
                    }
                }
            }
        }
    }
    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        double d0 = 64.0D * getViewScale();
        return pDistance < d0 * d0;
    }
}
