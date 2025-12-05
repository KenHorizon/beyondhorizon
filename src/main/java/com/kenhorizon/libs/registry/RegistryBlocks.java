package com.kenhorizon.libs.registry;

import com.google.common.collect.ImmutableList;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.datagen.BHBlockStateProvider;
import com.kenhorizon.beyondhorizon.server.datagen.BHBlockTagsProvider;
import com.kenhorizon.beyondhorizon.server.datagen.BHLootTableProvider;
import com.kenhorizon.beyondhorizon.server.item.BasicBlockItem;
import com.kenhorizon.libs.server.ModifiedNonNullFunction;
import com.kenhorizon.libs.server.ModifiedNonNullUnaryOperator;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class RegistryBlocks<T extends Block> {
    public enum Mineable {
        PICKAXE,
        AXE,
        SHOVEL,
        HOE;
    }
    public enum ToolTiers {
        STONE,
        IRON,
        DIAMOND;
    }
    private String name;
    private final Builder<T> builder;

    public RegistryBlocks(Builder<T> builder) {
        this.builder = builder;
        this.name = builder.name;
    }

    public static Builder<Block> register(String name, NonNullFunction<BlockBehaviour.Properties, Block> factory) {
        return new Builder<>(name, factory);
    }

    private String builderName(String name) {
        String[] array = name.split("_");
        StringBuilder builderName = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            builderName.append(i == 0 ? array[i] : " " + array[i]);
        }
        return Utils.capitalize(builderName.toString());
    }


    public RegistryObject<T> build() {
        this.buildInternal(this.builder);
        return builder.registryObject;
    }

    protected void buildInternal(Builder<T> builder) {
        String lang;
        if (builder.hasCustomName) {
            lang = builder.customName;
        } else {
            lang = builderName(this.name);
        }

        this.registerBlockItems(builder.name, builder.registryObject);
        if (!builder.disalbeLang) {
            RegistryLanguage.ADD_BLOCK_TRANSLATION.put(builder.registryObject, lang);
        }
        switch (builder.toolTiers) {
            case STONE:
                BHBlockTagsProvider.ADD_MINEABLE_FOR_STONE.add(builder.registryObject);
                break;
            case IRON:
                BHBlockTagsProvider.ADD_MINEABLE_FOR_IRON.add(builder.registryObject);
                break;
            case DIAMOND:
                BHBlockTagsProvider.ADD_MINEABLE_FOR_DIAMOND.add(builder.registryObject);
                break;
            default:
                break;
        }
        switch (builder.mineable) {
            case PICKAXE:
                BHBlockTagsProvider.ADD_MINEABLE_FOR_PICKAXE.add(builder.registryObject);
                break;
            case AXE:
                BHBlockTagsProvider.ADD_MINEABLE_FOR_AXE.add(builder.registryObject);
                break;
            case HOE:
                BHBlockTagsProvider.ADD_MINEABLE_FOR_HOE.add(builder.registryObject);
                break;
            case SHOVEL:
                BHBlockTagsProvider.ADD_MINEABLE_FOR_SHOVEL.add(builder.registryObject);
                break;
            default:
                break;
        }
        if (builder.tags != null) {
            BHBlockTagsProvider.TAGS.put(builder.registryObject, (TagKey<Block>) builder.tags);
        }
        if (builder.dropSelf) {
            BHLootTableProvider.Blocks.ADD_DROP_SELF.add(builder.registryObject);
        }
        RegistryTabs.register(builder.registryObject, RegistryTabs.Category.BLOCKS);
    }

    private void registerBlockItems(String name, Supplier<T> blockItem) {
        RegistryEntries.ITEMS.register(name, () -> new BasicBlockItem(blockItem.get(), new Item.Properties()));
    }

    public static class Builder<T extends Block> {
        private String name;
        private T entry;
        private RegistryObject<T> registryObject;
        private TagKey<T> tags = null;
        private final NonNullFunction<BlockBehaviour.Properties, T> factory;
        private NonNullSupplier<BlockBehaviour.Properties> initialProperties = BlockBehaviour.Properties::of;
        private ModifiedNonNullFunction<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesCallback = ModifiedNonNullUnaryOperator.identity();
        private String customName = "";
        private boolean hasCustomName = false;
        private boolean disalbeLang = false;
        private boolean dontCreateItemBlocks = false;
        private boolean removeFromTabs = false;
        private boolean noLootTable = false;
        private boolean dropSelf = false;
        private RegistryBlocks.Mineable mineable;
        private RegistryBlocks.ToolTiers toolTiers;
        private List<RegistryTabs.Category> creativeTabs = ImmutableList.of();

        public Builder(String name, NonNullFunction<BlockBehaviour.Properties, T> factory) {
            this.factory = factory;
            this.name = name;
        }

        public Builder<T> properties(ModifiedNonNullUnaryOperator<BlockBehaviour.Properties> function) {
            this.propertiesCallback = this.propertiesCallback.andThen(function);
            return this;
        }

        public Builder<T> inialProperties(NonNullSupplier<BlockBehaviour.Properties> properties) {
            this.initialProperties = properties;
            return this;
        }

        public Builder<T> tag(TagKey<T> tags) {
            this.tags = tags;
            return this;
        }

        public Builder<T> itemName(String name) {
            this.hasCustomName = true;
            this.customName = name;
            return this;
        }

        public Builder<T> disableLang() {
            this.disalbeLang = true;
            return this;
        }

        public Builder<T> drop(Supplier<? extends Block> blocks) {
            BHLootTableProvider.Blocks.ADD_DROP.add(blocks);
            return this;
        }
        public Builder<T> dropSelf() {
            this.dropSelf = true;
            return this;
        }

        public Builder<T> tier(RegistryBlocks.ToolTiers toolTiers) {
            this.toolTiers = toolTiers;
            return this;
        }

        public Builder<T> mineable(RegistryBlocks.Mineable mineable) {
            this.mineable = mineable;
            return this;
        }

        public Builder<T> dontCreateItemBlocks() {
            this.dontCreateItemBlocks = true;
            this.removeFromTabs = true;
            return this;
        }

        public Builder<T> removeFromTabs() {
            this.removeFromTabs = true;
            return this;
        }

        private T createEntry() {
            BlockBehaviour.Properties properties = this.initialProperties.get();
            properties = this.propertiesCallback.apply(properties);
            return this.factory.apply(properties);
        }

        public RegistryBlocks<T> register() {
            this.registryObject = RegistryEntries.BLOCKS.register(this.name, this::createEntry);
            return new RegistryBlocks<>(this);
        }
    }
}