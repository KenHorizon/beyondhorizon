package com.kenhorizon.beyondhorizon.server.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public record SimpleBlockHelper(Supplier<? extends Block> block, ResourceLocation nTexture, ResourceLocation sTexture, ResourceLocation eTexture, ResourceLocation wTexture, ResourceLocation uTexture, ResourceLocation dTexture) {
    public static class Builder {
        private final Supplier<? extends Block> block;
        private ResourceLocation nTexture, sTexture, eTexture, wTexture, uTexture, dTexture;
        public Builder(Supplier<? extends Block> block) {
            this.block = block;
        }

        public static Builder create(Supplier<? extends Block> block) {
            return new Builder(block);
        }

        public void north(ResourceLocation location) {
            this.nTexture = location;
        }

        public void south(ResourceLocation location) {
            this.sTexture = location;
        }

        public void east(ResourceLocation location) {
            this.eTexture = location;
        }

        public void west(ResourceLocation location) {
            this.wTexture = location;
        }

        public void up(ResourceLocation location) {
            this.uTexture = location;
        }

        public void down(ResourceLocation location) {
            this.dTexture = location;
        }

        public SimpleBlockHelper build() {
            return new SimpleBlockHelper(this.block, this.nTexture, this.sTexture, this.eTexture, this.wTexture, this.uTexture, this.dTexture);
        }
    }
}
