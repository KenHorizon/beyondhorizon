package com.kenhorizon.beyondhorizon.server.block;

import com.kenhorizon.beyondhorizon.server.block.spawner.data.SpawnerState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BlockProperties {
    public static final EnumProperty<SpawnerState> SPAWNER_STATE = EnumProperty.create("base_spawner_state", SpawnerState.class);

}
