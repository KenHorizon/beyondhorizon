package com.kenhorizon.beyondhorizon.server.block.entity;

import com.kenhorizon.beyondhorizon.server.block.GateBlocks;
import com.kenhorizon.beyondhorizon.server.init.BHBlockEntity;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Optional;
import java.util.UUID;

public class ChainPulleyBlockEntity extends BHBlockEntityBase<ChainPulleyBlockEntity> {
    private UUID chainedTo;
    private UUID chainedId;
    public ChainPulleyBlockEntity(BlockPos baseBlockPos, BlockState blockState) {
        super(BHBlockEntity.CHAIN_PULLEY.get(), baseBlockPos, blockState);
        this.chainedId = UUID.randomUUID();
    }

    public void setChainedId(UUID chainedId) {
        this.chainedId = chainedId;
    }

    public void setChainedTo(UUID chainedTo) {
        this.chainedTo = chainedTo;
    }

    public UUID getChainedId() {
        return chainedId;
    }

    public UUID getChainedTo() {
        return chainedTo;
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState, ChainPulleyBlockEntity entity) {
        this.tickCount++;

    }

    private void particle(Level level, BlockPos blockPos, int amount) {
        for (int i = 0; i < amount; i++) {
            level.addParticle(ParticleTypes.CLOUD, blockPos.getX() + 0.5F, blockPos.getY(), blockPos.getZ() + 0.5F, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.SMOKE, blockPos.getX() + 0.5F, blockPos.getY(), blockPos.getZ() + 0.5F, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public AABB getRenderBoundingBox() {
        AABB bounds = super.getRenderBoundingBox();
        bounds = bounds.expandTowards(0, 5, 0);
        return bounds;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (this.chainedId != null) {
            this.chainedId = compound.getUUID("chained_id");
        }
        if (compound.hasUUID("chained_to")) {
            this.chainedTo = compound.getUUID("chained_to");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putUUID("chained_id", this.chainedId);
        if (this.chainedTo != null) {
            compound.putUUID("chained_to", this.chainedTo);
        }
    }
}
