package com.kenhorizon.libs.client.data;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.libs.client.model.item.ItemModelDefinition;
import com.kenhorizon.libs.registry.RegistryItemModels;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ItemModelGenerator {
    protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    protected ItemModelProvider itemModelProvider;
    public ItemModelGenerator(ItemModelProvider itemModelProvider) {
        this.itemModelProvider = itemModelProvider;
    }

    public ItemModelProvider getItemModelProvider() {
        return itemModelProvider;
    }

    public void createItemModels(Supplier<? extends Item> item, ItemModelDefinition itemModelDefinition) {
        if (itemModelDefinition.itemType() == RegistryItemModels.Type.THROWING) {
            if (itemModelDefinition.secondModel() == null) {
                throw new RuntimeException("Second Model coudn't find during Data Json Creation!!");
            }
            createThrowingWeaponItemModel(item, itemModelDefinition.baseModel(), itemModelDefinition.secondModel());
        } else if (itemModelDefinition.itemType() == RegistryItemModels.Type.HANDHELD) {
            createWeaponItemModel(item, itemModelDefinition.baseModel(), itemModelDefinition.suffix());
        } else if (itemModelDefinition.itemType() == RegistryItemModels.Type.BOW_HOLD) {
            createBowItem(item, itemModelDefinition.baseModel(), itemModelDefinition.suffix());
        } else if (itemModelDefinition.itemType() == RegistryItemModels.Type.CROSSBOW_HOLD) {
            createCrossbowItem(item, itemModelDefinition.baseModel(), itemModelDefinition.suffix());
        } else if (itemModelDefinition.itemType() == RegistryItemModels.Type.SPAWN_EGG) {
            spawnEgg(item);
        } else {
            createGenerated(item, itemModelDefinition.baseModel(), itemModelDefinition.suffix(), itemModelDefinition.itemType() == RegistryItemModels.Type.BUILTIN_ENTITY);
        }
    }


    private ResourceLocation createWeaponItemModel(Supplier<? extends Item> item, String suffix, ResourceLocation baseModels) {
        String itemPath = key(item.get()).getPath();
        String texturePath = getItemTexture(suffix, itemPath);
        return createSimpleItem(item, suffix, baseModels);
    }

    private ResourceLocation customItemRenderer(Supplier<? extends Item> item, String itemPath, ResourceLocation model) {
        String name = key(item.get()).getPath();
        return this.itemModelProvider.withExistingParent(itemPath, model)
                .customLoader(SeparateTransformsModelBuilder::begin).base(this.itemModelProvider.nested().parent(this.itemModelProvider.getExistingFile(BeyondHorizon.resource(String.format("item/%s_model", name)))))
                .perspective(ItemDisplayContext.GUI, this.itemModelProvider.nested().parent(this.itemModelProvider.getExistingFile(BeyondHorizon.resource(String.format("item/%s_icon", name)))))
                .perspective(ItemDisplayContext.FIXED, this.itemModelProvider.nested().parent(this.itemModelProvider.getExistingFile(BeyondHorizon.resource(String.format("item/%s_icon", name)))))
                .perspective(ItemDisplayContext.GROUND, this.itemModelProvider.nested().parent(this.itemModelProvider.getExistingFile(BeyondHorizon.resource(String.format("item/%s_icon", name)))))
                .end().getLocation();
    }

    private ResourceLocation createWeaponItemModel(Supplier<? extends Item> item, ResourceLocation baseModels, String suffix) {
        return createWeaponItemModel(item, suffix, baseModels);
    }

    private ResourceLocation createWeaponItemModel(Supplier<? extends Item> item, ResourceLocation baseModels) {
        return createWeaponItemModel(item, "", baseModels);
    }

    public ResourceLocation createThrowingWeaponItemModel(Supplier<? extends Item> item, ResourceLocation baseModels, ResourceLocation throwingModel) {
        String itemPath = key(item.get()).getPath();
        ResourceLocation throwing = this.itemModelProvider.withExistingParent(itemPath + "_throwing", throwingModel).
                texture("layer0", "item/" + itemPath).
                getLocation();
        return this.itemModelProvider.withExistingParent(itemPath, baseModels).texture("layer0",
                        BeyondHorizon.resource("item/" + itemPath))
                .override()
                .predicate(ModelOverrides.THROWING, 1.0F)
                .model(new ModelFile.ExistingModelFile(throwing, this.itemModelProvider.existingFileHelper)).end().getLocation();
    }

    private ResourceLocation createBuiltInEntity(Supplier<? extends Item> item, String suffix, ResourceLocation baseModel) {
        String itemPath = key(item.get()).getPath();
        return this.itemModelProvider.getBuilder(key(item.get()).toString())
                .guiLight(BlockModel.GuiLight.FRONT)
                .getLocation();
    }

    private ResourceLocation createSimpleItem(Supplier<? extends Item> item, String suffix, ResourceLocation baseModel) {
        String itemPath = key(item.get()).getPath();
        String itemTexture = getItemTexture(suffix, itemPath);
        String itemTextureOverlay = getItemTexture(suffix, itemPath) + "_overlay";
        ResourceLocation overlayLocation = BeyondHorizon.resource(itemTextureOverlay);
        if (this.itemModelProvider.existingFileHelper.exists(overlayLocation, TEXTURE)) {
            return this.itemModelProvider
                    .withExistingParent(itemPath, baseModel)
                    .texture("layer0", BeyondHorizon.resource(itemTexture))
                    .texture("layer1", BeyondHorizon.resource(itemTextureOverlay))
                    .getLocation();
        }
        return this.itemModelProvider
                .withExistingParent(itemPath, baseModel)
                .texture("layer0", BeyondHorizon.resource(itemTexture))
                .getLocation();
    }

    public ResourceLocation spawnEgg(Supplier<? extends Item> item) {
        String itemPath = key(item.get()).getPath();
        return this.itemModelProvider.withExistingParent(itemPath, this.itemModelProvider.mcLoc("item/template_spawn_egg")).getLocation();
    }

    public ResourceLocation createBowItem(Supplier<? extends Item> item, ResourceLocation baseModel, String suffix) {
        String itemPath = key(item.get()).getPath();
        String itemTexture = getItemTexture(suffix, itemPath);
        ResourceLocation pull0 = this.itemModelProvider.withExistingParent(itemPath + "_pulling_0", baseModel).texture("layer0", itemTexture + "_pulling_0").getLocation();
        ResourceLocation pull1 = this.itemModelProvider.withExistingParent(itemPath + "_pulling_1", baseModel).texture("layer0", itemTexture + "_pulling_1").getLocation();
        ResourceLocation pull2 = this.itemModelProvider.withExistingParent(itemPath + "_pulling_2", baseModel).texture("layer0", itemTexture + "_pulling_2").getLocation();
        return this.itemModelProvider.withExistingParent(itemPath, baseModel).texture("layer0", BeyondHorizon.resource(itemTexture)).
                override().predicate(ModelOverrides.PULLING, 1.0F).model(new ModelFile.ExistingModelFile(pull0, this.itemModelProvider.existingFileHelper)).end().
                override().predicate(ModelOverrides.PULLING, 1.0F).predicate(ModelOverrides.PULL, 0.65F).model(new ModelFile.ExistingModelFile(pull1, this.itemModelProvider.existingFileHelper)).end().
                override().predicate(ModelOverrides.PULLING, 1.0F).predicate(ModelOverrides.PULL, 0.9F).model(new ModelFile.ExistingModelFile(pull2, this.itemModelProvider.existingFileHelper)).end().getLocation();
    }

    public ResourceLocation createCrossbowItem(Supplier<? extends Item> item, ResourceLocation baseModel, String suffix) {
        String itemPath = key(item.get()).getPath();
        String itemTexture = getItemTexture(suffix, itemPath);
        ResourceLocation pull0 = this.itemModelProvider.withExistingParent(itemPath + "_pulling_0", baseModel).texture("layer0", itemTexture + "_pulling_0").getLocation();
        ResourceLocation pull1 = this.itemModelProvider.withExistingParent(itemPath + "_pulling_1", baseModel).texture("layer0", itemTexture + "_pulling_1").getLocation();
        ResourceLocation pull2 = this.itemModelProvider.withExistingParent(itemPath + "_pulling_2", baseModel).texture("layer0", itemTexture + "_pulling_2").getLocation();
        ResourceLocation charged = this.itemModelProvider.withExistingParent(itemPath + "_arrow", baseModel).texture("layer0", itemTexture + "_arrow").getLocation();
        ResourceLocation chargedFireworks = this.itemModelProvider.withExistingParent(itemPath + "_firework", baseModel).texture("layer0", itemTexture + "_firework").getLocation();
        return this.itemModelProvider.withExistingParent(itemPath, baseModel).texture("layer0", BeyondHorizon.resource(itemTexture)).
                override().predicate(ModelOverrides.PULLING, 1.0F).model(new ModelFile.ExistingModelFile(pull0, this.itemModelProvider.existingFileHelper)).end().
                override().predicate(ModelOverrides.PULLING, 1.0F).predicate(ModelOverrides.PULL, 0.65F).model(new ModelFile.ExistingModelFile(pull1, this.itemModelProvider.existingFileHelper)).end().
                override().predicate(ModelOverrides.PULLING, 1.0F).predicate(ModelOverrides.PULL, 0.9F).model(new ModelFile.ExistingModelFile(pull2, this.itemModelProvider.existingFileHelper)).end().
                override().predicate(ModelOverrides.CHARGED, 1.0F).model(new ModelFile.ExistingModelFile(charged, this.itemModelProvider.existingFileHelper)).end().
                override().predicate(ModelOverrides.CHARGED, 1.0F).predicate(ModelOverrides.FIREWORK, 1.0F).model(new ModelFile.ExistingModelFile(chargedFireworks, this.itemModelProvider.existingFileHelper))
                .end().getLocation();
    }
    private @NotNull String getItemTexture(String suffix, String itemPath) {
        String itemTexture;
        if (isEmpty(suffix)) {
            itemTexture = String.format("item/%s", itemPath);
        } else {
            itemTexture = String.format("item/%s/%s", suffix, itemPath);
        }
        return itemTexture;
    }

    public void createGenerated(Supplier<? extends Item> item, ResourceLocation model, String suffix, boolean builtInEntity) {
        if (builtInEntity) {
            createBuiltInEntity(item, suffix, model);
        } else {
            createSimpleItem(item, suffix, model);
        }
    }


    private ResourceLocation key(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
    private boolean isEmpty(String suffix) {
        return suffix == "" || suffix.isEmpty();
    }
}
