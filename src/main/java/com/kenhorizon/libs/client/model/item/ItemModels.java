package com.kenhorizon.libs.client.model.item;

import net.minecraftforge.common.IExtensibleEnum;

public enum ItemModels implements IExtensibleEnum {
    NONE(null),
    RUNE(ItemModelType.RUNES),
    ACCESSORY(ItemModelType.ACCESSORY),
    BIG_SCYTHE(ItemModelType.BIG_SCYTHE),
    BIG_SPEAR(ItemModelType.BIG_SPEAR),
    SCYTHE(ItemModelType.SCYTHE),
    CLAYMORE(ItemModelType.CLAYMORE),
    SPEAR(ItemModelType.SPEAR),
    KATANA(ItemModelType.KATANA),
    DAGGER(ItemModelType.DAGGER),
    BIG_HANDHELD(ItemModelType.BIG_HANDHELD),
    BIG32_HANDHELD(ItemModelType.BIG32_HANDHELD),
    REFINED_SWORD(ItemModelType.REFINED_SWORD),
    X16_BOW(ItemModelType.X16_BOW),
    // VANILLA
    BUILTIN_ENTITY(ItemModelType.BUILTIN_ENTITY),
    GENERATED(ItemModelType.GENERATED),
    SPAWN_EGG(ItemModelType.SPAWN_EGG),
    BOW(ItemModelType.BOW),
    CROSSBOW(ItemModelType.CROSSBOW),
    HANDHELD(ItemModelType.HANDHELD);

    private final ItemModelDefinition itemModelDefinition;

    ItemModels(ItemModelDefinition itemModelDefinition) {
        this.itemModelDefinition = itemModelDefinition;
    }

    public ItemModelDefinition getItemModel() {
        return itemModelDefinition;
    }

    public static ItemModels create(String name, ItemModelDefinition itemModelDefinition) {
        throw new IllegalStateException("Enum not extended");
    }
}
