package com.kenhorizon.beyondhorizon.server.block.entity;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.block.BlockProperties;
import com.kenhorizon.beyondhorizon.server.block.spawner.BaseSpawnerBlock;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.BHBaseSpawner;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.BaseSpawnerData;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.SpawnerBuilder;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.SpawnerState;
import com.kenhorizon.beyondhorizon.server.init.BHBlockEntity;
import com.kenhorizon.beyondhorizon.server.util.PlayerDetector;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BaseSpawnerOmniousBlockEntity extends BlockEntity implements BHBaseSpawner.StateAccessor {
    private BHBaseSpawner spawner;

    public BaseSpawnerOmniousBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BHBlockEntity.BASE_SPAWNER_OMINOUS.get(), blockPos, blockState);
        this.spawner = new BHBaseSpawner(SpawnerBuilder.OMNIOUS, new BaseSpawnerData(),this, PlayerDetector.NO_CREATIVE_PLAYERS, PlayerDetector.EntitySelector.SELECT_FROM_LEVEL);
    }
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.spawner.codec().parse(NbtOps.INSTANCE, nbt).resultOrPartial(BeyondHorizon.LOGGER::error).ifPresent(baseSpawner -> this.spawner = baseSpawner);
        if (this.level != null) {
            this.markUpdated();
        }
    }
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        this.spawner.codec().encodeStart(NbtOps.INSTANCE, this.spawner).get().ifLeft(tag -> compoundTag.merge((CompoundTag)tag)).ifRight(param0x -> BeyondHorizon.LOGGER.warn("Failed to encode Base Spawner {}", param0x.message()));
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.spawner.getData().getUpdateTag(this.getBlockState().getValue(BaseSpawnerBlock.SPAWNER_STATE));
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public void setEntityId(EntityType<?> entityType, RandomSource randomSource) {
        this.spawner.getData().setEntityId(this.spawner, randomSource, entityType);
        this.setChanged();
    }

    public BHBaseSpawner getVoidSpawner() {
        return this.spawner;
    }

    @Override
    public SpawnerState getState() {
        return !this.getBlockState().hasProperty(BlockProperties.SPAWNER_STATE)
                ? SpawnerState.INACTIVE
                : this.getBlockState().getValue(BlockProperties.SPAWNER_STATE);
    }

    @Override
    public void setState(Level level, SpawnerState spawnerState) {
        this.setChanged();
        level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(BlockProperties.SPAWNER_STATE, spawnerState));
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) {
            this.load(pkt.getTag());
        }
        super.onDataPacket(net, pkt);
    }

    @Override
    public void markUpdated() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }
}
