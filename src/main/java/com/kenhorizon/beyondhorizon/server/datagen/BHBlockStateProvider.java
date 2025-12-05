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
        this.blockWithItem(BHBlocks.COBALT_ORE);
        this.fullFaceBlock(BHBlocks.WORKBENCH);
    }
}
