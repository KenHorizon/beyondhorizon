package com.kenhorizon.beyondhorizon.server.block;

import com.kenhorizon.beyondhorizon.server.api.block.AdvanceFenceBlock;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.SpawnerState;
import com.kenhorizon.beyondhorizon.server.block.wire.WireLaneMode;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BHBlockProperties {
    public static final BooleanProperty CHAINED = BooleanProperty.create("chained");

    public static final EnumProperty<SpawnerState> SPAWNER_STATE = EnumProperty.create("base_spawner_state", SpawnerState.class);
    public static final EnumProperty<WireLaneMode> WIRE_LANE_MODE = EnumProperty.create("wire_lane_mode", WireLaneMode.class);
    public static final IntegerProperty REDSTONE_LANE_POWER = IntegerProperty.create("redstone_lane_power", 0, 150);

    public static final EnumProperty<AdvanceFenceBlock.PostState> POST_FENCE = EnumProperty.create("post", AdvanceFenceBlock.PostState.class);
    public static final EnumProperty<AdvanceFenceBlock.FenceSide> EAST_FENCE = EnumProperty.create("east_fence", AdvanceFenceBlock.FenceSide.class);
    public static final EnumProperty<AdvanceFenceBlock.FenceSide> NORTH_FENCE = EnumProperty.create("north_fence", AdvanceFenceBlock.FenceSide.class);
    public static final EnumProperty<AdvanceFenceBlock.FenceSide> SOUTH_FENCE = EnumProperty.create("south_fence", AdvanceFenceBlock.FenceSide.class);
    public static final EnumProperty<AdvanceFenceBlock.FenceSide> WEST_FENCE = EnumProperty.create("west_fence", AdvanceFenceBlock.FenceSide.class);

}
