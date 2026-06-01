package com.kenhorizon.beyondhorizon.server.item.debug_items;

import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.BlazingInferno;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.item.BasicItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DebugBlazingInfernoItems extends BasicItem {
    public DebugBlazingInfernoItems(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            BlockPos blockPos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockState = level.getBlockState(blockPos);
            BlockPos spawnPos;
            if (blockState.getCollisionShape(level, blockPos).isEmpty()) {
                spawnPos = blockPos;
            } else {
                spawnPos = blockPos.relative(direction);
            }
            BlazingInferno boss = new BlazingInferno(BHEntity.BLAZING_INFERNO.get(), level);
            boss.setXRot(-context.getPlayer().getXRot());
            boss.setIsPowered(false);
            boss.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            level.addFreshEntity(boss);
            return InteractionResult.CONSUME;
        }
    }
}
