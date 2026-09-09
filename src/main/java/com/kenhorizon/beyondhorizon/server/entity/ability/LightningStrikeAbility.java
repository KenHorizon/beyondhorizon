package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.LightningParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
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
        this.setRadius(3.0F);
        this.seed = this.random.nextLong();
    }

    public LightningStrikeAbility(Level level) {
        super(BHEntity.LIGHTNING_STRIKE.get(), level);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == 4) {
            int colorCode = Colors.combineRGB(0, 186, 255);
            for (int i = 0; i < 3; ++i) {
                float r = Colors.getFARGB(colorCode)[0];
                float g = Colors.getFARGB(colorCode)[1];
                float b = Colors.getFARGB(colorCode)[2];

                this.level().addParticle(new RingParticleOptions(0, (float) Math.PI / 2, 20, r, g, b, 1.0F, 32.0F,
                        false, RingParticles.Behavior.GROW), this.getX(), this.getY() + (20 * i), this.getZ(), 0, 0, 0);
            }
        }
    }

    @Override
    protected void onEnd() {
        super.onEnd();
        if (!this.sentEventSpike) {
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.sentEventSpike = true;
        }
        this.dealDamage();
    }

    public boolean hasStriked(int initialStart) {
        return this.getLifeTime() > this.getDuration() - initialStart;
    }
    public boolean hasStriked() {
        return hasStriked(1);
    }
    public void dealDamage() {
        boolean isOnWater = this.level().isFluidAtPosition(this.blockPosition(), fluidState -> fluidState.is(FluidTags.WATER));
        double range = this.getRadius();
        LivingEntity attacker = this.getCaster();
        for (Entity entityOnRange : this.level().getEntities(this, this.getBoundingBox().inflate(range))) {
            if (entityOnRange instanceof LivingEntity nearby) {
                if (attacker == null) {
                    if (nearby.isAlive() && !nearby.isInvulnerable()) {
                        nearby.hurt(BHDamageTypes.magicDamage(this), this.getBaseDamage());
                    }
                } else {
                    if (nearby == attacker) continue;
                    if (attacker.isAlliedTo(nearby)) continue;
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
