package com.kenhorizon.beyondhorizon.server.block.the_forge;

import com.kenhorizon.beyondhorizon.server.block.BHBlockProperties;
import com.kenhorizon.beyondhorizon.server.block.BasicBlock;
import com.kenhorizon.beyondhorizon.server.inventory.menu.ForgeAnvilMenu;
import com.kenhorizon.beyondhorizon.server.inventory.menu.ForgeCraftingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class ForgeBlock extends BasicBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<ForgeTypeStation> FORGE_TYPE_STATION = BHBlockProperties.FORGE_TYPE_STATION;
    private static Component CONTAINER_TITLE = Component.empty();

    public ForgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FORGE_TYPE_STATION, ForgeTypeStation.CRAFTING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FORGE_TYPE_STATION);
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(blockState.getMenuProvider(level, blockPos));
            switch (blockState.getValue(FORGE_TYPE_STATION)) {
                case CRAFTING -> {
                    player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                }
                default ->  {
                    player.awardStat(Stats.INTERACT_WITH_ANVIL);
                }
            }
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        return new SimpleMenuProvider((id, inventory, player) -> {
            switch (blockState.getValue(FORGE_TYPE_STATION)) {
                case CRAFTING -> {
                    CONTAINER_TITLE = Component.translatable("container.crafting");
                    return new ForgeCraftingMenu(id, inventory, ContainerLevelAccess.create(level, blockPos));
                }
                default ->  {
                    CONTAINER_TITLE = Component.translatable("container.repair");
                    return new ForgeAnvilMenu(id, inventory, ContainerLevelAccess.create(level, blockPos));
                }
            }
        }, CONTAINER_TITLE);
    }


    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }
}
