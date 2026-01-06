package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.tags.BHBlockTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class BHBlockTagsProvider extends BlockTagsProvider {
    public static List<Supplier<? extends Block>> ADD_MINEABLE_FOR_STONE = new ArrayList<>();
    public static List<Supplier<? extends Block>> ADD_MINEABLE_FOR_IRON = new ArrayList<>();
    public static List<Supplier<? extends Block>> ADD_MINEABLE_FOR_DIAMOND = new ArrayList<>();
    public static List<Supplier<? extends Block>> ADD_MINEABLE_FOR_AXE = new ArrayList<>();
    public static List<Supplier<? extends Block>> ADD_MINEABLE_FOR_PICKAXE = new ArrayList<>();
    public static List<Supplier<? extends Block>> ADD_MINEABLE_FOR_SHOVEL = new ArrayList<>();
    public static List<Supplier<? extends Block>> ADD_MINEABLE_FOR_HOE = new ArrayList<>();
    public static Map<RegistryObject<? extends Block>, TagKey<Block>> TAGS = new HashMap<>();

    public BHBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, BeyondHorizon.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        TAGS.forEach((object, tags) -> {
            this.tag(tags).add(object.get());
        });
        ADD_MINEABLE_FOR_PICKAXE.forEach((blocks) -> {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blocks.get());
        });
        ADD_MINEABLE_FOR_AXE.forEach((blocks) -> {
            this.tag(BlockTags.MINEABLE_WITH_AXE).add(blocks.get())
                    .addTag(BHBlockTags.WOODEN_TABLES)
                    .addTag(BHBlockTags.PICKET_FENCE)
                    .addTag(BHBlockTags.RAILING_FENCE)
                    .addTag(BHBlockTags.SHELF)
                    .addTag(BHBlockTags.WOODEN_WALLS);
        });
        ADD_MINEABLE_FOR_HOE.forEach((blocks) -> {
            this.tag(BlockTags.MINEABLE_WITH_HOE).add(blocks.get());
        });
        ADD_MINEABLE_FOR_SHOVEL.forEach((blocks) -> {
            this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(blocks.get())
                    .addTag(BHBlockTags.PILLAR_WALLS);
        });
        this.tag(BHBlockTags.MINEABLE_WITH_PAXEL).addTag(BlockTags.MINEABLE_WITH_AXE).addTag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(BlockTags.MINEABLE_WITH_SHOVEL).addTag(BlockTags.MINEABLE_WITH_HOE);
        this.tag(BHBlockTags.UNMOVEABLE).addTag(BlockTags.WITHER_IMMUNE);
        BHBlockTagsProvider.ADD_MINEABLE_FOR_STONE.forEach(blocksTags -> this.tag(BlockTags.NEEDS_STONE_TOOL).add(blocksTags.get()));
        BHBlockTagsProvider.ADD_MINEABLE_FOR_IRON.forEach(blocksTags -> this.tag(BlockTags.NEEDS_IRON_TOOL).add(blocksTags.get()));
        BHBlockTagsProvider.ADD_MINEABLE_FOR_DIAMOND.forEach(blocksTags -> this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(blocksTags.get()));
    }

    @Override
    public @NotNull String getName() {
        return "Block Tags";
    }
}