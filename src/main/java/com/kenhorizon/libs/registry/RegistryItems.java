package com.kenhorizon.libs.registry;

import com.google.common.collect.ImmutableList;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.datagen.BHItemTagsProvider;
import com.kenhorizon.libs.client.model.item.ItemModels;
import com.kenhorizon.libs.server.ModifiedNonNullFunction;
import com.kenhorizon.libs.server.ModifiedNonNullUnaryOperator;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class RegistryItems<T extends Item> {
    private RegistryObject<T> registryObject;
    private String name;
    private final Builder<T> builder;

    public RegistryItems(Builder<T> builder) {
        this.builder = builder;
        this.name = builder.name;
        this.registryObject = builder.registryObject;
    }

    public RegistryObject<T> build() {
        this.buildInternal(this.builder);
        return this.registryObject;
    }

    public static Builder<Item> register(String name, NonNullFunction<Item.Properties, Item> factory) {
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

    protected void buildInternal(Builder<T> builder) {
        String lang;
        if (builder.hasCustomName) {
            lang = builder.customName;
        } else {
            lang = builderName(this.name);
        }
        if (!builder.disalbeLang) {
            RegistryLanguage.ADD_ITEM_TRANSLATION.put(this.registryObject, lang);
        }
        if (builder.tags != null) {
            BHItemTagsProvider.TAGS.put(this.registryObject, (TagKey<Item>) builder.tags);
        }
        RegistryItemModels.register(this.registryObject, builder.model);
        RegistryTabs.register(this.registryObject, builder.categories);
    }

    public static class Builder<T extends Item> {
        private String name;
        private T entry;
        private RegistryObject<T> registryObject;
        private TagKey<T> tags = null;
        private final NonNullFunction<Item.Properties, T> factory;
        private NonNullSupplier<Item.Properties> initialProperties = Item.Properties::new;
        private ModifiedNonNullFunction<Item.Properties, Item.Properties> propertiesCallback = ModifiedNonNullUnaryOperator.identity();
        private String customName = "";
        private boolean hasCustomName = false;
        private boolean disalbeLang = false;
        private ItemModels model;
        private List<RegistryTabs.Category> creativeTabs = ImmutableList.of();
        private RegistryTabs.Category[] categories;

        public Builder(String name, NonNullFunction<Item.Properties, T> factory) {
            this.name = name;
            this.factory = factory;
        }

        public Builder<T> properties(ModifiedNonNullUnaryOperator<Item.Properties> function) {
            this.propertiesCallback = this.propertiesCallback.andThen(function);
            return this;
        }

        public Builder<T> inialProperties(NonNullSupplier<Item.Properties> properties) {
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

        public Builder<T> tab(RegistryTabs.Category... creativeTabs) {
//            RegistryTabs.register(this.registryObject, creativeTabs);
            this.categories = creativeTabs;
            return this;
        }

        public Builder<T> model(ItemModels types) {
            this.model = types;
            return this;
        }

        private T createEntry() {
            Item.Properties properties = this.initialProperties.get();
            properties = this.propertiesCallback.apply(properties);
            return this.factory.apply(properties);
        }

        public RegistryItems<T> register() {
            this.registryObject = RegistryEntries.ITEMS.register(this.name, this::createEntry);
            return new RegistryItems<>(this);
        }
    }
}