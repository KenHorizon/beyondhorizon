package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.LightningParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class BoltShockAbility extends AbilityEntity {
    public BoltShockAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setDuration(20);
        this.setBaseDamage(2);
        this.setRadius(2.0F);
    }

    public BoltShockAbility(Level level) {
        super(BHEntity.BOLT_SHOCK.get(), level);
        this.setDuration(20);
        this.setRadius(2.0F);
    }

    public static void spawn(Level level, double x, double y, double z, float damage, LivingEntity entity) {
        BoltShockAbility ability = new BoltShockAbility(level);
        ability.setBaseDamage(damage);
        ability.setCaster(entity);
        ability.setPos(x, y, z);
        level.addFreshEntity(ability);
    }

    @Override
    public void clientSide() {
        if (this.hasEnded()) {
            int colorCode = Colors.combineRGB(0, 186, 255);
            for (int i = 0; i < 20; ++i) {
                double d0 = (random.nextFloat() - 1.5F) + random.nextFloat() * this.getRadius();
                double d1 = (random.nextFloat() - 1.5F) + random.nextFloat() * this.getRadius();
                double d2 = (random.nextFloat() - 1.5F) + random.nextFloat() * this.getRadius();
                double dist = random.nextFloat() * this.getRadius();
                double d3 = d0 * dist;
                double d4 = d1 * dist;
                double d5 = d2 * dist;
                this.level().addAlwaysVisibleParticle(new LightningParticleOptions(0, 186, 255), this.getX() + d0, this.getY() + 0.5D, this.getZ() + d2, d3, d4, d5);
            }
            float r = Colors.getFARGB(colorCode)[0];
            float g = Colors.getFARGB(colorCode)[1];
            float b = Colors.getFARGB(colorCode)[2];

            this.level().addAlwaysVisibleParticle(new LightningParticleOptions(0, 186, 255), this.getX(), this.getY() + 10.5D, this.getZ(), 0.01D, -5.0D, 0.01D);

            this.level().addParticle(new RingParticleOptions(0, (float) Math.PI / 2, 20, r, g, b, 1.0F, 32.0F, false, RingParticles.Behavior.GROW), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected void onEnd() {
        this.dealDamage();
    }

    public void dealDamage() {
        LivingEntity attacker = this.getCaster();
        List<Entity> cleaveRange = this.level().getEntities(this, this.getBoundingBox().inflate(this.getRadius()));
        for (Entity entityOnRange : cleaveRange) {
            if (entityOnRange instanceof LivingEntity targetOnRange) {
                if (attacker == null) {
                    if (targetOnRange.isAlive() && !targetOnRange.isInvulnerable()) {
                        targetOnRange.hurt(BHDamageTypes.applyDamage(damageTypes.MAGIC_DAMAGE, DamageTags.AREA_OF_EFFECTS, null, null), this.getBaseDamage());
                    }
                } else {
                    if (targetOnRange == attacker) continue;
                    if (attacker.isAlliedTo(targetOnRange)) continue;
                    if (targetOnRange.isAlive() && !targetOnRange.isInvulnerable()) {
                        targetOnRange.hurt(BHDamageTypes.applyDamage(damageTypes.MAGIC_DAMAGE, DamageTags.AREA_OF_EFFECTS,this, attacker), this.getBaseDamage());
                    }
                }
            }
        }
    }
}
