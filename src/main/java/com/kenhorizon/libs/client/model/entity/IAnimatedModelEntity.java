package com.kenhorizon.libs.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public interface IAnimatedModelEntity {

    Optional<ModelPart> getAnyDescendantWithName(String name);

}
