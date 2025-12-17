package com.kenhorizon.libs.registry;

import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.datagen.BHEntityTypeTagsProvider;
import com.kenhorizon.beyondhorizon.server.datagen.BHItemTagsProvider;
import com.kenhorizon.libs.server.ModifiedNonNullFunction;
import com.kenhorizon.libs.server.ModifiedNonNullUnaryOperator;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class RegistryEntity<T extends Entity> {
    protected String name;
    protected Builder<T> builder;

    public RegistryEntity(Builder<T> builder) {
        this.builder = builder;
        this.name = builder.name;
    }

    public static <M extends Entity> Builder<M> register(String name,EntityType.EntityFactory<M> factory) {
        return new Builder<M>(name, factory);
    }

    public RegistryObject<EntityType<T>> build() {
        this.buildInternal(this.builder, this.builder.registryObject);
        return this.builder.registryObject;
    }

    private String builderName(String name) {
        String[] array = name.split("_");
        StringBuilder builderName = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            builderName.append(i == 0 ? array[i] : " " + array[i]);
        }
        return Utils.capitalize(builderName.toString());
    }

    protected <M extends EntityType<?>> void buildInternal(Builder<T> builder, RegistryObject<M> entry) {
        String lang;
        if (builder.langName.isEmpty()) {
            lang = builderName(builder.name);
        } else {
            lang = builder.langName;
        }
        RegistryLanguage.ADD_ENTITY_TRANSLATION.put((RegistryObject<EntityType<?>>) entry, lang);
        if (builder.tags != null) {
            BHEntityTypeTagsProvider.TAGS.put(builder.registryObject, builder.tags);
        }
    }


    private static <T extends EntityType<?>> void reigsterLang(String name, RegistryObject<T> entity) {
        String[] array = name.split("_");
        StringBuilder splitName = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            splitName.append(i == 0 ? array[i] : " " + array[i]);
        }
        RegistryLanguage.ADD_ENTITY_TRANSLATION.put((RegistryObject<EntityType<?>>) entity, Utils.capitalize(splitName.toString()));
    }

    public static class Builder<T extends Entity>  {
        private String name;
        private String langName = "";
        private EntityType.EntityFactory<T> factory;
        private RegistryObject<EntityType<T>> registryObject;
        private ModifiedNonNullFunction<EntityType.Builder<T>, EntityType.Builder<T>> propertiesCallback = ModifiedNonNullUnaryOperator.identity();
        private EntityType<T> entry;
        private TagKey<EntityType<?>> tags = null;
        private MobCategory mobCategory;

        public Builder(String name, EntityType.EntityFactory<T> factory) {
            this.name = name;
            this.factory = factory;
        }

        public Builder<T> mobCategory(MobCategory mobCategory) {
            this.mobCategory = mobCategory;
            return this;
        }

        public RegistryEntity.Builder<T> properties(ModifiedNonNullUnaryOperator<EntityType.Builder<T>> factory) {
            this.propertiesCallback = this.propertiesCallback.andThen(factory);
            return this;
        }

        public RegistryEntity.Builder<T> tag(TagKey<EntityType<?>> tag) {
            this.tags = tag;
            return this;
        }

        public RegistryEntity.Builder<T> lang(String name) {
            this.langName = name;
            return this;
        }

        private EntityType<T> createEntry() {
            EntityType.Builder<T> properties = EntityType.Builder.of(this.factory, this.mobCategory);
            properties = this.propertiesCallback.apply(properties);
            return properties.build(this.name);
        }

        public RegistryObject<EntityType<T>> register() {
            this.registryObject = RegistryEntries.ENTITY_TYPES.register(this.name, this::createEntry);
            if (this.tags != null) {
                BHEntityTypeTagsProvider.TAGS.put(this.registryObject, this.tags);
            }
            return new RegistryEntity<>(this).build();
        }
    }
}