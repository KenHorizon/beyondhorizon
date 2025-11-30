package com.kenhorizon.beyondhorizon.server.item.base;

import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.item.Item;

public class WeaponBuilder {
    @FunctionalInterface
    public interface MeleeBuilder<T extends Item> {
        public abstract T create(MeleeWeaponMaterials materials, Item.Properties properties);
    }

    public static final MeleeBuilder<SwordBaseItem> BLADE_OF_THE_ENDERLORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.BLADE_OF_THE_ENDERLORD[0], Constant.BLADE_OF_THE_ENDERLORD[1], properties, SkillBuilder.RUINED_BLADE));
    public static final MeleeBuilder<SwordBaseItem> ZENITH = ((materials, properties) -> new SwordBaseItem(materials, Constant.ZENITH[0], Constant.ZENITH[1], properties, SkillBuilder.BLADE_EDGE));
}
