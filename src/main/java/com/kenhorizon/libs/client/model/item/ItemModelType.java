package com.kenhorizon.libs.client.model.item;

import com.kenhorizon.libs.client.data.BaseModels;
import com.kenhorizon.libs.registry.RegistryItemModels;

public class ItemModelType {
    public static ItemModelDefinition BUILTIN_ENTITY = ItemModelDefinition.Builder.create(BaseModels.BUILTIN_ENTITY)
            .itemType(RegistryItemModels.Type.BUILTIN_ENTITY).build();
    public static ItemModelDefinition GENERATED = ItemModelDefinition.Builder.create(BaseModels.GENERATED)
            .itemType(RegistryItemModels.Type.HOLD).build();
    public static ItemModelDefinition SPAWN_EGG = ItemModelDefinition.Builder.create(BaseModels.SPAWN_EGG)
            .itemType(RegistryItemModels.Type.SPAWN_EGG).build();
    public static ItemModelDefinition BOW = ItemModelDefinition.Builder.create(BaseModels.BOW)
            .itemType(RegistryItemModels.Type.BOW_HOLD).suffix("bow").build();
    public static ItemModelDefinition CROSSBOW = ItemModelDefinition.Builder.create(BaseModels.CROSSBOW)
            .itemType(RegistryItemModels.Type.CROSSBOW_HOLD).suffix("crossbow").build();
    public static ItemModelDefinition HANDHELD = ItemModelDefinition.Builder.create(BaseModels.HANDHELD)
            .itemType(RegistryItemModels.Type.HANDHELD).build();

    public static ItemModelDefinition REFINED_SWORD = ItemModelDefinition.Builder.create(BaseModels.REFINED_SWORD)
            .itemType(RegistryItemModels.Type.HANDHELD).suffix("refined").build();
    public static ItemModelDefinition BIG_SCYTHE = ItemModelDefinition.Builder.create(BaseModels.X32_SCYTHE)
            .itemType(RegistryItemModels.Type.HANDHELD).build();
    public static ItemModelDefinition BIG_SPEAR = ItemModelDefinition.Builder.create(BaseModels.X32_SPEAR)
            .itemType(RegistryItemModels.Type.HANDHELD).build();
    public static ItemModelDefinition SCYTHE = ItemModelDefinition.Builder.create(BaseModels.SCYTHE)
            .itemType(RegistryItemModels.Type.HANDHELD).build();
    public static ItemModelDefinition CLAYMORE = ItemModelDefinition.Builder.create(BaseModels.CLAYMORE)
            .itemType(RegistryItemModels.Type.HANDHELD).build();
    public static ItemModelDefinition SPEAR = ItemModelDefinition.Builder.create(BaseModels.SPEAR)
            .itemType(RegistryItemModels.Type.THROWING).secondModel(BaseModels.SPEAR_THORWING).build();
    public static ItemModelDefinition DAGGER = ItemModelDefinition.Builder.create(BaseModels.DAGGER)
            .itemType(RegistryItemModels.Type.HANDHELD).build();
    public static ItemModelDefinition KATANA = ItemModelDefinition.Builder.create(BaseModels.KATANA)
            .itemType(RegistryItemModels.Type.HANDHELD).build();
    public static ItemModelDefinition X16_BOW = ItemModelDefinition.Builder.create(BaseModels.X16_BOW)
            .itemType(RegistryItemModels.Type.BOW_HOLD).suffix("bow").build();
    public static ItemModelDefinition BIG_HANDHELD = ItemModelDefinition.Builder.create(BaseModels.X32_HANDHELD)
            .itemType(RegistryItemModels.Type.HANDHELD).build();
    public static ItemModelDefinition BIG32_HANDHELD = ItemModelDefinition.Builder.create(BaseModels.X64_HANDHELD)
            .itemType(RegistryItemModels.Type.HANDHELD).build();
    public static ItemModelDefinition ACCESSORY = ItemModelDefinition.Builder.create(BaseModels.ACCESSORY)
            .itemType(RegistryItemModels.Type.HOLD).suffix("accessory").build();
    public static ItemModelDefinition RUNES = ItemModelDefinition.Builder.create(BaseModels.ACCESSORY)
            .itemType(RegistryItemModels.Type.HOLD).suffix("rune").build();
}