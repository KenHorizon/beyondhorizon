package com.kenhorizon.beyondhorizon.server.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BHItemTagsProvider extends ItemTagsProvider {
    public static Map<RegistryObject<? extends Item>, TagKey<Item>> TAGS = new HashMap<>();
    public BHItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, BeyondHorizon.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        TAGS.forEach((object, tags) -> {
            this.tag(tags).add(object.get());
        });
    }

//    private void getAccessory() {
//        for (Item item : ForgeRegistries.ITEMS) {
//            if (item instanceof AccessoryItem) {
//                this.tag(BHItemTags.ONLY_ACCESSORY).add(item);
//            }
//        }
//    }
    @Override
    public @NotNull String getName() {
        return "Item Tags";
    }
}
