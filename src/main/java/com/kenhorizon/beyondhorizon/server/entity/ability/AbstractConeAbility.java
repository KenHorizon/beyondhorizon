package com.kenhorizon.beyondhorizon.server.entity.ability;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractConeAbility extends AbilityEntity {
    protected static final int FAIL_SAFE_EXPIRE_TIME = 20 * 20;
    protected final ConePart[] subParts;
    protected boolean coneAtTarget;

    public AbstractConeAbility(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setDuration(40);
        this.noPhysics = true;
        this.blocksBuilding = false;
        this.subParts = new ConePart[] {
                new ConePart(this, "part1", 1.0F, 1.0F),
                new ConePart(this, "part2", 2.5F, 1.5F),
                new ConePart(this, "part3", 3.5F, 2.0F),
                new ConePart(this, "part4", 4.5F, 3.0F)
        };
    }


    @Override
    public boolean isOnFire() {
        return false;
    }

    public void setConeAtTarget(boolean coneAtTarget) {
        this.coneAtTarget = coneAtTarget;
    }

    public boolean isConeAtTarget() {
        return coneAtTarget;
    }

    protected abstract void onHitEntity(EntityHitResult entityHitResult);

    public abstract void spawnParticles();

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public PartEntity<?>[] getParts() {
        return this.subParts;
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        for (int i = 0; i < this.subParts.length; i++) // Forge: Fix MC-158205: Set part ids to successors of parent mob id
            this.subParts[i].setId(id + i + 1);
    }

    protected Vec3 rayTrace(Entity owner) {
        float f = owner.getXRot();
        float f1 = owner.getYRot();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        return new Vec3(f6, f5, f7);
    }

    @Override
    protected void onDuration() {
        super.onDuration();
        //TODO: try this instead of the ray trace
        /*
        So. This is what vectors are for.
        The player has a vector that is their "front" called "LookVec" (Search the EntityPlayer class).
        Take that vector, multiply by 0.5 (or 0.2 or whatever), add their current position, and voila. You have the spot a half-block in front of them.
        */
        var owner = this.getCaster();
        var target = this.getTarget();
        if (this.isConeAtTarget()) {
            this.coneWorks(owner, target);
        } else {
            this.coneWorks(owner);
        }

        /* Hit Detection */
        if (!level().isClientSide()) {
            for (Entity entity : this.getSubEntityCollisions()) {
                this.onHitEntity(new EntityHitResult(entity));
            }
        } else {
            this.spawnParticles();
        }
    }

    private void coneWorks(LivingEntity owner, LivingEntity target) {
        if (owner != null && target != null) {
            var rayTraceVector = rayTrace(owner);
            var ownerEyePos = target.getEyePosition(1.0f).subtract(0, 0.8, 0);
            this.setPos(ownerEyePos);
            this.setXRot(owner.getXRot());
            this.setYRot(owner.getYRot());
            this.yRotO = getYRot();
            this.xRotO = getXRot();
            //setDeltaMovement(ownerEyePos);
            double scale = 1;
            for (int i = 0; i < subParts.length; i++) {
                var subEntity = subParts[i];

                double distance = 1 + (i * scale * subEntity.getDimensions(null).width / 2);
                Vec3 newVector = ownerEyePos.add(rayTraceVector.multiply(distance, distance, distance));
                subEntity.setPos(newVector);
                subEntity.setDeltaMovement(newVector);
                var vec3 = new Vec3(subEntity.getX(), subEntity.getY(), subEntity.getZ());
                subEntity.xo = vec3.x;
                subEntity.yo = vec3.y;
                subEntity.zo = vec3.z;
                subEntity.xOld = vec3.x;
                subEntity.yOld = vec3.y;
                subEntity.zOld = vec3.z;
            }
        }
    }
    private void coneWorks(LivingEntity owner) {
        if (owner != null) {
            var rayTraceVector = rayTrace(owner);
            var ownerEyePos = owner.getEyePosition(1.0f).subtract(0, 0.8, 0);
            this.setPos(ownerEyePos);
            this.setXRot(owner.getXRot());
            this.setYRot(owner.getYRot());
            this.yRotO = getYRot();
            this.xRotO = getXRot();
            //setDeltaMovement(ownerEyePos);
            double scale = 1;
            for (int i = 0; i < subParts.length; i++) {
                var subEntity = subParts[i];

                double distance = 1 + (i * scale * subEntity.getDimensions(null).width / 2);
                Vec3 newVector = ownerEyePos.add(rayTraceVector.multiply(distance, distance, distance));
                subEntity.setPos(newVector);
                subEntity.setDeltaMovement(newVector);
                var vec3 = new Vec3(subEntity.getX(), subEntity.getY(), subEntity.getZ());
                subEntity.xo = vec3.x;
                subEntity.yo = vec3.y;
                subEntity.zo = vec3.z;
                subEntity.xOld = vec3.x;
                subEntity.yOld = vec3.y;
                subEntity.zOld = vec3.z;
            }
        }
    }

    protected Set<Entity> getSubEntityCollisions() {
        List<Entity> collisions = new ArrayList<>();
        for (Entity conepart : this.subParts) {
            collisions.addAll(this.level().getEntities(conepart, conepart.getBoundingBox().inflate(this.getRadius())));
        }

        return collisions.stream().filter(target ->
                target instanceof LivingEntity && target != getCaster()
        ).collect(Collectors.toSet());
    }

    protected static boolean hasLineOfSight(Entity start, Entity target) {
        Vec3 vec3 = new Vec3(start.getX(), start.getEyeY(), start.getZ());
        Vec3 vec31 = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        return start.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, start)).getType() == HitResult.Type.MISS;
    }
}
