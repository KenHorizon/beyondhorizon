package com.kenhorizon.beyondhorizon.server.item.base;

import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.item.base.weapons.MagicWeaponBaseItem;
import com.kenhorizon.beyondhorizon.server.item.materials.MagicWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.item.Item;

public class MagicItemBuilder {
    @FunctionalInterface
    public interface Factory<T extends Item> {
        public abstract T create(MagicWeaponMaterials materials, Item.Properties properties);
    }

    public static final Factory<MagicWeaponBaseItem> WAND = ((materials, properties) -> new MagicWeaponBaseItem(materials, Constant.WAND, properties, SkillBuilder.WAND));
    public static final Factory<MagicWeaponBaseItem> NIGHT_RAY = ((materials, properties) -> new MagicWeaponBaseItem(materials, Constant.NIGHT_RAY, properties, SkillBuilder.NIGHT_RAY));
    public static final Factory<MagicWeaponBaseItem> STORMSURGE = ((materials, properties) -> new MagicWeaponBaseItem(materials, Constant.STORMSURGE, properties, SkillBuilder.STORMSURGE));
    public static final Factory<MagicWeaponBaseItem> THUNDER_ZAPPER = ((materials, properties) -> new MagicWeaponBaseItem(materials, Constant.THUNDERZAPPER, properties, SkillBuilder.THUNDERZAPPER));
    public static final Factory<MagicWeaponBaseItem> BlAZING_BEACON = ((materials, properties) -> new MagicWeaponBaseItem(materials, Constant.BLAZING_BEACON, properties, SkillBuilder.BLAZING_BEACON));
}
