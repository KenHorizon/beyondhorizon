package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.LightningParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RoarParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.api.skills.ability.active.ThunderSkill;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;

import java.util.List;
import java.util.Optional;

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
            for (int i = 0; i < 120; ++i) {
                this.level().addParticle(new TrailParticleOptions((this.getDuration() / 2) + 10, r, g, b, 1.0F, 0.70F
                                , TrailParticles.Behavior.FADE_N_SHRINK, new Vec3(this.getX(), this.getY(), this.getZ()))
                        , this.getRandomX(8), this.getY() + random.nextInt(4), this.getRandomZ(8), 0, 0, 0);
            }
        }
       if (this.hasStriked(2)) {
           for (int i = 0; i < 20; ++i) {
               this.level().addParticle(new TrailParticleOptions(120, r, g, b, 1.0F, 0.70F
                               , TrailParticles.Behavior.DEFAULT, new Vec3(this.getRandomX(8), this.getY() + random.nextInt(4), this.getRandomZ(8)))
                       , this.getX(), this.getY(), this.getZ(), 0, 0, 0);
           }
           this.level().addParticle(new RingParticleOptions(0, (float) Math.PI / 2, 20, r, g, b, 1.0F, 32.0F,
                   false, RingParticles.Behavior.GROW), this.getX(), this.getY(0.01D), this.getZ(), 0, 0, 0);
           this.level().addParticle(new RoarParticleOptions(22, (int) r * 255, (int) g * 255, (int) b * 255,  255, 0, 1, 4), this.getX(), this.getY(0.01D), this.getZ(), 0, 0, 0);
       }
    }

    @Override
    protected void onEnd() {
        super.onEnd();
        this.dealDamage();
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
                        nearby.hurt(BHDamageTypes.magicDamage(this), this.getBaseDamage());
                    }
                } else {
                    if (nearby == attacker || attacker.isAlliedTo(nearby)) continue;
                    if (nearby.isAlive() && !nearby.isInvulnerable()) {
                        nearby.hurt(BHDamageTypes.magicDamage(this, attacker), this.getBaseDamage());
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
