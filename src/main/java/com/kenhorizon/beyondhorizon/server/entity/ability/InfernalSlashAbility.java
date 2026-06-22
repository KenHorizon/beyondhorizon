package com.kenhorizon.beyondhorizon.server.entity.ability;

import com.kenhorizon.beyondhorizon.client.particle.world.SlashParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponActiveSkills;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTypeTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class InfernalSlashAbility extends AbstractConeAbility {
    public InfernalSlashAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setDuration(2);
        this.setDelay(20);
        this.setRadius(1.5F);
    }

    public static void spawn(Level level, LivingEntity owner, float damage, DamageType damageType) {
        InfernalSlashAbility ability = new InfernalSlashAbility(BHEntity.INFERNAL_SLASH_ABILITY.get(), level);
        ability.setBaseDamage(damage);
        ability.setPos(owner.position().add(0, owner.getEyeHeight() * .7, 0));
        ability.setCaster(owner);
        ability.setDamageType(damageType);
        level.addFreshEntity(ability);
    }
    @Override
    protected Vec3 rayTrace(Entity owner) {
        float f = owner.getXRot();
        float f1 = owner.getYRot();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        return new Vec3(f6, 0.0F, f7);
    }
    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        var entity = entityHitResult.getEntity();
        this.getDamageType().dealDamage((LivingEntity) entity, this.getCaster(), this.getBaseDamage());
    }

    @Override
    public void spawnParticles() {
        var owner = this.getCaster();
        if (!this.level().isClientSide() || owner == null) {
            return;
        }
        Vec3 rotation = owner.getLookAngle().normalize();
        var pos = owner.position().add(rotation.scale(1.6));

        double x = pos.x;
        double y = pos.y + owner.getEyeHeight() * 0.9F;
        double z = pos.z;

        double speed = random.nextDouble() * .35 + .35;
        for (int i = 0; i < 10; i++) {
            double offset = .15;
            double ox = Math.random() * 2 * offset - offset;
            double oy = Math.random() * 2 * offset - offset;
            double oz = Math.random() * 2 * offset - offset;

            double angularness = .5;
            Vec3 randomVec = new Vec3(Math.random() * 2 * angularness - angularness, 0, Math.random() * 2 * angularness - angularness).normalize();
            Vec3 result = (rotation.scale(3).add(randomVec)).normalize().scale(speed);
            this.level().addParticle(ParticleTypes.FLAME, x + ox, y + oy, z + oz, result.x, result.y, result.z);
        }
    }

    @Override
    protected void onEnd() {
        LivingEntity user = this.getCaster();
        if (!this.sentEventSpike) {
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.sentEventSpike = true;
        }
    }
}
