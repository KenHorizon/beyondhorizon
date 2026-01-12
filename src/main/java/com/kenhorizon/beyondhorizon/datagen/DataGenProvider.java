package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = BeyondHorizon.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenProvider {
    @SubscribeEvent
    public static void GatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        //Data Gen.

        DatapackEntryProvider datapackEntryProvider = new DatapackEntryProvider(packOutput, lookupProvider);
        BlockTagsProvider blockTagsProvider = new BHBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(event.includeServer(), new BHDamageTypesTagProvider(packOutput, datapackEntryProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new BHItemTagsProvider(packOutput, datapackEntryProvider.getRegistryProvider(), blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), datapackEntryProvider);
        generator.addProvider(event.includeServer(), BHLootTableProvider.create(packOutput));
        generator.addProvider(event.includeServer(), new BHMobEffectTagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new BHSoundProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new BHEntityTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new BHRecipeProvider(packOutput));
        generator.addProvider(event.includeClient(), new BHParticleProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new BHLangProvider(packOutput));
        generator.addProvider(event.includeClient(), new BHBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new BHItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ForgeAdvancementProvider(packOutput, lookupProvider, existingFileHelper, List.of()));
        generator.addProvider(true, new PackMetadataGenerator(packOutput).add(PackMetadataSection.TYPE, new PackMetadataSection(
                Component.literal("Resources for Beyond Horizon"),
                DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
                Arrays.stream(PackType.values()).collect(Collectors.toMap(Function.identity(), DetectedVersion.BUILT_IN::getPackVersion)))));
    }
}
