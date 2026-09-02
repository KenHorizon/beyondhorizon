package com.kenhorizon.beyondhorizon.server.item.base;

import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.item.base.tools.*;
import com.kenhorizon.beyondhorizon.server.item.base.weapons.MagicWeaponBaseItem;
import com.kenhorizon.beyondhorizon.server.item.base.weapons.SwordBaseItem;
import com.kenhorizon.beyondhorizon.server.item.materials.MagicWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public class MagicItemBuilder {
    @FunctionalInterface
    public interface Factory<T extends Item> {
        public abstract T create(MagicWeaponMaterials materials, Item.Properties properties);
    }

    public static final Factory<MagicWeaponBaseItem> BlAZING_BEACON = ((materials, properties) -> new MagicWeaponBaseItem(materials, Constant.BLAZING_BEACON, properties, SkillBuilder.BLAZING_BEACON));
}
