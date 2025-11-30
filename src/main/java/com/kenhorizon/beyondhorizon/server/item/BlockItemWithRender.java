package com.kenhorizon.beyondhorizon.server.item;

import net.minecraft.world.level.block.Block;

public class BlockItemWithRender extends BasicBlockItem {
    public BlockItemWithRender(Block block, Properties properties) {
        super(block, properties);
    }

//    @Override
//    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
//        consumer.accept(new IClientItemExtensions() {
//            static final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(() -> new BHBlockItemWithRenders(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));
//
//
//            @Override
//            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
//                return renderer.get();
//            }
//        });
//    }
}
