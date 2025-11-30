package com.kenhorizon.beyondhorizon.server.tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BHBlockTags {
    public static final TagKey<Block> MINEABLE_WITH_PAXEL = create("mineable_with_paxel");
    public static final TagKey<Block> REGENERATION_BLOCKS = create("regeneration_blocks");
    public static final TagKey<Block> TRENCH_GENERATION_IGNORES = create("trench_generation_ignores");
    public static final TagKey<Block> UNMOVEABLE = create("unmoveable");
    public static final TagKey<Block> WOODEN_TABLES = create("wooden_tables");
    public static final TagKey<Block> TABLES = create("tables");
    public static final TagKey<Block> WOODEN_PICKET_FENCE = create("wooden_picket_fence");
    public static final TagKey<Block> PICKET_FENCE = create("picket_fence");
    public static final TagKey<Block> PILLAR_WALLS = create("pillar_walls");
    public static final TagKey<Block> WOODEN_RAILING_FENCE = create("wooden_railing_fence");
    public static final TagKey<Block> RAILING_FENCE = create("railing_fence");
    public static final TagKey<Block> SHELF = create("shelf");
    public static final TagKey<Block> WOODEN_WALLS = create("wooden_walls");

    public static TagKey<Block> create(String name) {
        return BlockTags.create(BeyondHorizon.resource(name));
    }
}