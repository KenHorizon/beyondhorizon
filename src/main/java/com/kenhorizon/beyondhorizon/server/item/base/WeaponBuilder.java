package com.kenhorizon.beyondhorizon.server.item.base;

import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.item.Item;

public interface WeaponBuilder {
    @FunctionalInterface
    public interface MeleeBuilder<T extends Item> {
        public abstract T create(MeleeWeaponMaterials materials, Item.Properties properties);
    }

    public static final MeleeBuilder<SwordBaseItem> BLADE_OF_THE_ENDERLORD = ((materials, properties) -> {
        return new SwordBaseItem(materials, Constant.BLADE_OF_THE_ENDERLORD[0], Constant.BLADE_OF_THE_ENDERLORD[1], properties, SkillBuilder.RUINED_BLADE);
    });
}
