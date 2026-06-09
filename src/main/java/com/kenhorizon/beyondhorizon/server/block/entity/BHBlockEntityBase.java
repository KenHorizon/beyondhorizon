package com.kenhorizon.beyondhorizon.server.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.PacketDistributor;

public abstract class BHBlockEntityBase<T extends BlockEntity> extends BlockEntity {
    public int tickCount = 0;
    protected RandomSource random = RandomSource.create();

    public BHBlockEntityBase(BlockEntityType<T> entityType, BlockPos blockPos, BlockState blockState) {
        super(entityType, blockPos, blockState);
    }

    public abstract void tick(Level level, BlockPos blockPos, BlockState blockState, T entity);


    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @SuppressWarnings("deprecation")
    public HolderGetter<Block> blockHolderGetter() {
        return (HolderGetter<Block>) (level != null ? level.holderLookup(Registries.BLOCK)
                : BuiltInRegistries.BLOCK.asLookup());
    }
}
