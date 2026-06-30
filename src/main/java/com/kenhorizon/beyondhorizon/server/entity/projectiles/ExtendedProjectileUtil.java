package com.kenhorizon.beyondhorizon.server.entity.projectiles;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Copied from ProjectileUtil
//
public class ExtendedProjectileUtil {
    public static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(Entity holder, double range, Predicate<Entity> matching,
                                                                                          ClipContext.Block blockClipType) {
        Vec3 lookVec = holder.getViewVector(1.0F);
        Vec3 eyePosition = holder.getEyePosition();
        Vec3 from = eyePosition.add(lookVec.scale(2.0F));
        double ms = holder.getDeltaMovement().dot(lookVec);
        Vec3 to = eyePosition.add(lookVec.scale(range * Math.max(0.0F, ms)));
        return getHitEntitiesAlong(lookVec, from, eyePosition, to, holder, matching, range, blockClipType);
    }

    public static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(Vec3 lookVec, Vec3 from, Vec3 origin, Vec3 to,
                                                                                          Entity holder, Predicate<Entity> matching, double range,
                                                                                          ClipContext.Block blockClipType) {
        Level level = holder.level();
        BlockHitResult blockHitResult = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, holder));
        if (blockHitResult.getType() != HitResult.Type.MISS) {
            to = blockHitResult.getLocation();
            if (origin.distanceToSqr(to) < origin.distanceToSqr(from)) {
                return Either.left(blockHitResult);
            }
        }
        List<Entity> possibleList = holder.level().getEntities(holder, holder.getBoundingBox().expandTowards(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range).inflate(1.0F));
        Collection<EntityHitResult> allAffectedResult = new ArrayList<>();
        for (var entity : possibleList) {
            allAffectedResult.add(new EntityHitResult(entity));
        }
        return !possibleList.isEmpty() ? Either.right(allAffectedResult) : Either.left(blockHitResult);
    }

    public static HitResult getHitResultOnMoveVector(Entity projectile, double range, Predicate<Entity> filter) {
        Vec3 vec3 = projectile.getDeltaMovement();
        Level level = projectile.level();
        Vec3 vec31 = projectile.position();
        return getHitResult(range, vec31, projectile, filter, vec3, level);
    }

    public static HitResult getHitResultOnMoveVector(Entity projectile, Predicate<Entity> filter) {
        Vec3 vec3 = projectile.getDeltaMovement();
        Level level = projectile.level();
        Vec3 vec31 = projectile.position();
        return getHitResult(1.0D, vec31, projectile, filter, vec3, level);
    }

    public static HitResult getHitResultOnViewVector(Entity projectile, Predicate<Entity> filter, double scale, double range) {
        Vec3 vec3 = projectile.getViewVector(0.0F).scale(scale);
        Level level = projectile.level();
        Vec3 vec31 = projectile.getEyePosition();
        return getHitResult(range, vec31, projectile, filter, vec3, level);
    }

    private static HitResult getHitResult(double range, Vec3 start, Entity projectile, Predicate<Entity> filter, Vec3 end, Level level) {
        Vec3 vec3 = start.add(end);
        HitResult hitresult = level.clip(new ClipContext(start, vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, projectile));
        if (hitresult.getType() != HitResult.Type.MISS) {
            vec3 = hitresult.getLocation();
        }

        HitResult hitresult1 = getEntityHitResult(level, projectile, start, vec3, projectile.getBoundingBox().expandTowards(end).inflate(range), filter);
        if (hitresult1 != null) {
            hitresult = hitresult1;
        }

        return hitresult;
    }

    /**
     * Gets the EntityRayTraceResult representing the entity hit
     */
    @Nullable
    public static EntityHitResult getEntityHitResult(Entity shooter, Vec3 start, Vec3 end, AABB box, Predicate<Entity> filter, double distance) {
        Level level = shooter.level();
        double d0 = distance;
        Entity entity = null;
        Vec3 vec3 = null;

        for(Entity entity1 : level.getEntities(shooter, box, filter)) {
            AABB aabb = entity1.getBoundingBox().inflate((double)entity1.getPickRadius());
            Optional<Vec3> optional = aabb.clip(start, end);
            if (aabb.contains(start)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    vec3 = optional.orElse(start);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = start.distanceToSqr(vec31);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getRootVehicle() == shooter.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            vec3 = vec31;
                        }
                    } else {
                        entity = entity1;
                        vec3 = vec31;
                        d0 = d1;
                    }
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity, vec3);
    }

    /**
     * Gets the EntityHitResult representing the entity hit
     */
    @Nullable
    public static EntityHitResult getEntityHitResult(Level level, Entity projectile, Vec3 start, Vec3 end, AABB box, Predicate<Entity> filter) {
        return getEntityHitResult(level, projectile, start, end, box, filter, 0.3F);
    }

    /**
     * Gets the EntityHitResult representing the entity hit
     */
    @Nullable
    public static EntityHitResult getEntityHitResult(Level level, Entity projectile, Vec3 start, Vec3 end,
                                              AABB box, Predicate<Entity> filter, float radius) {
        double d0 = Double.MAX_VALUE;
        Entity entity = null;

        for(Entity entity1 : level.getEntities(projectile, box, filter)) {
            AABB aabb = entity1.getBoundingBox().inflate((double)radius);
            Optional<Vec3> optional = aabb.clip(start, end);
            if (optional.isPresent()) {
                double d1 = start.distanceToSqr(optional.get());
                if (d1 < d0) {
                    entity = entity1;
                    d0 = d1;
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity);
    }
}
