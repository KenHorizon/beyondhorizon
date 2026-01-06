package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.tags.BHEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BHEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public static Map<RegistryObject<?>, TagKey<EntityType<?>>> TAGS = new HashMap<>();
    public BHEntityTypeTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> holder, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, holder, BeyondHorizon.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        TAGS.forEach((object, tags) -> {
            this.tag(tags).add((EntityType<?>) object.get());
        });
        super.addTags(provider);
    }

    @Override
    public String getName() {
        return BeyondHorizon.ID + "Entity Tags";
    }
}
