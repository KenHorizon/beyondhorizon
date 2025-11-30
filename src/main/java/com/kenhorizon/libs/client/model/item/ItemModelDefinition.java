package com.kenhorizon.libs.client.model.item;

import com.kenhorizon.libs.registry.RegistryItemModels;
import net.minecraft.resources.ResourceLocation;

public record ItemModelDefinition(ResourceLocation baseModel, ResourceLocation secondModel, RegistryItemModels.Type itemType, String suffix) {
    public static class Builder {
        private final ResourceLocation baseModel;
        private ResourceLocation secondModel;
        private RegistryItemModels.Type itemType;
        private String suffix = "";

        public static Builder create(ResourceLocation baseModel) {
            return new Builder(baseModel);
        }

        public Builder(ResourceLocation baseModel) {
            this.baseModel = baseModel;
        }

        public Builder secondModel(ResourceLocation secondModel) {
            this.secondModel = secondModel;
            return this;
        }

        public Builder itemType(RegistryItemModels.Type itemType) {
            this.itemType = itemType;
            return this;
        }

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public ItemModelDefinition build() {
            return new ItemModelDefinition(this.baseModel, this.secondModel, this.itemType, this.suffix);
        }
    }
}