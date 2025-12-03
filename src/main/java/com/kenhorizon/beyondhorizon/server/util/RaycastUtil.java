package com.kenhorizon.beyondhorizon.server.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class RaycastUtil {
    public static Entity getEntityLookedAt(Entity entity) {
        return getEntityLookedAt(entity, 32);
    }

    public static Entity getEntityLookedAt(Entity entity, double radius) {
        Entity foundEntity = null;
        HitResult pos = raycast(entity, radius);
        Vec3 positionVector = entity.getEyePosition();
        double distance = pos.getLocation().distanceTo(positionVector);
        Vec3 lookVector = entity.getLookAngle();
        Vec3 reachVector = positionVector.add(lookVector.x * radius, lookVector.y * radius, lookVector.z * radius);
        List<Entity> entitiesInBoundingBox = entity.level().getEntities(entity, entity.getBoundingBox().inflate(lookVector.x * radius, lookVector.y * radius, lookVector.z * radius).expandTowards(1F, 1F, 1F));
        double minDistance = distance;

        for (Entity entityInBoundingBox : entitiesInBoundingBox) {
            Entity lookedEntity = null;
            if (entityInBoundingBox.isPickable()) {
                AABB collisionBox = entityInBoundingBox.getBoundingBoxForCulling();
                Optional<Vec3> interceptPosition = collisionBox.clip(positionVector, reachVector);
                if (collisionBox.contains(positionVector)) {
                    if (0.0D < minDistance || minDistance == 0.0D) {
                        lookedEntity = entityInBoundingBox;
                        minDistance = 0.0D;
                    }
                } else if (interceptPosition.isPresent()) {
                    double distanceToEntity = positionVector.distanceTo(interceptPosition.get());
                    if (distanceToEntity < minDistance || minDistance == 0.0D) {
                        lookedEntity = entityInBoundingBox;
                        minDistance = distanceToEntity;
                    }
                }
            }
            if (lookedEntity != null && minDistance < distance) {
                foundEntity = lookedEntity;
            }
        }

        return foundEntity;
    }

    private static HitResult raycast(Entity entity, double length) {
        Vec3 origin = entity.getEyePosition();
        Vec3 raycast = entity.getLookAngle();
        Vec3 next = origin.add(raycast.normalize().scale(length));
        return entity.level().clip(new ClipContext(origin, next, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
    }
}
