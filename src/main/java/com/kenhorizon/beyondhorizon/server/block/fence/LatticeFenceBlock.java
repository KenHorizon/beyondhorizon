package com.kenhorizon.beyondhorizon.server.block.fence;

import net.minecraft.world.level.block.state.BlockState;

public class LatticeFenceBlock extends AdvanceFenceBlock {

    public LatticeFenceBlock(Properties properties) {
        super(properties);
    }
    @Override
    protected boolean connectsTo(BlockState state, boolean solid) {
        return state.getBlock() instanceof LatticeFenceBlock || super.connectsTo(state, solid);
    }

    @Override
    protected boolean makeConenctionState(BlockState state) {
        return state.getBlock() instanceof LatticeFenceBlock || super.makeConenctionState(state);
    }
}
