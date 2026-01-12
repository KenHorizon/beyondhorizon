package com.kenhorizon.beyondhorizon.server.block.spawner;


import com.kenhorizon.beyondhorizon.server.block.BlockProperties;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.SpawnerState;
import com.kenhorizon.beyondhorizon.server.block.entity.BaseSpawnerBlockEntity;
import com.kenhorizon.beyondhorizon.server.init.BHBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class BaseSpawnerBlock extends BaseEntityBlock {

    public static final EnumProperty<SpawnerState> SPAWNER_STATE = BlockProperties.SPAWNER_STATE;

    public BaseSpawnerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SPAWNER_STATE, SpawnerState.INACTIVE));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (pLevel instanceof ServerLevel) {
            if (blockEntity instanceof BaseSpawnerBlockEntity spawnerBlockEntity) {
                ItemStack itemstack = pPlayer.getItemInHand(pHand);
                if (itemstack.getItem() instanceof SpawnEggItem eggItem) {
                    EntityType<?> entitytype1 = eggItem.getType(itemstack.getTag());
                    spawnerBlockEntity.setEntityId(entitytype1, pLevel.getRandom());
                    spawnerBlockEntity.setChanged();
                    pLevel.sendBlockUpdated(pPos, pState, pState, 3);
                    pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pPos);
                    itemstack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SPAWNER_STATE);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BaseSpawnerBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel
                ? createTickerHelper(type, BHBlockEntity.BASE_SPAWNER.get(), (level1, blockPos, state, block) -> block.getVoidSpawner().tickServer(serverLevel, blockPos))
                : createTickerHelper(type, BHBlockEntity.BASE_SPAWNER.get(), (level1, blockPos, state, block) -> block.getVoidSpawner().tickClient(level1, blockPos));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @javax.annotation.Nullable BlockGetter blockGetter, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(itemStack, blockGetter, components, flag);
        Optional<Component> optional = this.getSpawnEntityDisplayName(itemStack);
        if (optional.isPresent()) {
            components.add(optional.get());
        } else {

            components.add(CommonComponents.EMPTY);
            components.add(Component.translatable("block.minecraft.spawner.desc1").withStyle(ChatFormatting.GRAY));
            components.add(CommonComponents.space().append(Component.translatable("block.minecraft.spawner.desc2").withStyle(ChatFormatting.BLUE)));
        }
    }
    private Optional<Component> getSpawnEntityDisplayName(ItemStack itemStack) {
        CompoundTag nbts = BlockItem.getBlockEntityData(itemStack);
        if (nbts != null && nbts.contains("SpawnData", 10)) {
            String spawnData = nbts.getCompound("SpawnData").getCompound("entity").getString("id");
            ResourceLocation resourcelocation = ResourceLocation.tryParse(spawnData);
            if (resourcelocation != null) {
                return BuiltInRegistries.ENTITY_TYPE.getOptional(resourcelocation).map((p_255782_) -> {
                    return Component.translatable(p_255782_.getDescriptionId()).withStyle(ChatFormatting.GRAY);
                });
            }
        }

        return Optional.empty();
    }
}
