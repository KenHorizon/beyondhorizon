package com.kenhorizon.beyondhorizon.server.datagen;

import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import com.kenhorizon.libs.client.data.BlockStateBuilder;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BHBlockStateProvider extends BlockStateBuilder {
    public BHBlockStateProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, existingFileHelper);
    }
    @Override
    protected void registerStatesAndModels() {
        this.blockWithItem(BHBlocks.ADAMANTITE_ORE);
        this.blockWithItem(BHBlocks.COBALT_ORE);
        this.blockWithItem(BHBlocks.DEEPSLATE_COBALT_ORE);
        this.blockWithItem(BHBlocks.RUBY_ORE);
        this.blockWithItem(BHBlocks.DEEPSLATE_RUBY_ORE);
        this.blockWithItem(BHBlocks.SILVER_ORE);
        this.blockWithItem(BHBlocks.DEEPSLATE_SILVER_ORE);
        this.blockWithItem(BHBlocks.TITANIUM_ORE);
        this.blockWithItem(BHBlocks.HELLSTONE_ORE);
        this.blockWithItem(BHBlocks.NETHERRACK_HELLSTONE_ORE);
        this.blockWithItem(BHBlocks.HALITE_ORE);
        this.blockWithItem(BHBlocks.SULFUR_ORE);
        this.blockWithItem(BHBlocks.PALLADIUM_ORE);
        this.fullFaceBlock(BHBlocks.WORKBENCH);
    }
}
