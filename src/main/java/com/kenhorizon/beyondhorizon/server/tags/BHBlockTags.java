package com.kenhorizon.beyondhorizon.server.tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BHBlockTags {
    public static final TagKey<Block> MINEABLE_WITH_MULTITOOLS = create("mineable_with_multitools");
    public static final TagKey<Block> UNMOVEABLE = create("unmoveable");

    public static TagKey<Block> create(String name) {
        return BlockTags.create(BeyondHorizon.resource(name));
    }
}