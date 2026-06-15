package com.kenhorizon.beyondhorizon.server.entity.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.entity.PartEntity;

public class ConePart extends PartEntity<AbstractConeAbility> {
    public final AbstractConeAbility parentEntity;
    public final String name;
    private final EntityDimensions size;

    public ConePart(AbstractConeAbility parent, String name, float scaleX, float scaleY) {
        super(parent);
        this.size = EntityDimensions.scalable(scaleX, scaleY);
        this.parentEntity = parent;
        this.name = name;

    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.parentEntity == entity;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}
