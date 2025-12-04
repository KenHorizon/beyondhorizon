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

    public static final MeleeBuilder<SwordBaseItem> SWORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.SWORD[0], Constant.SWORD[1], properties));
    public static final MeleeBuilder<SwordBaseItem> AXE = ((materials, properties) -> new SwordBaseItem(materials, Constant.AXE[0], Constant.AXE[1], properties));
    public static final MeleeBuilder<SwordBaseItem> PICKAXE = ((materials, properties) -> new SwordBaseItem(materials, Constant.PICKAXE[0], Constant.PICKAXE[1], properties));
    public static final MeleeBuilder<SwordBaseItem> SHOVEL = ((materials, properties) -> new SwordBaseItem(materials, Constant.SHOVEL[0], Constant.SHOVEL[1], properties));
    public static final MeleeBuilder<SwordBaseItem> HOE = ((materials, properties) -> new SwordBaseItem(materials, Constant.HOE[0], Constant.HOE[1], properties));

    public static final MeleeBuilder<SwordBaseItem> BLADE_OF_THE_ENDERLORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.BLADE_OF_THE_ENDERLORD[0], Constant.BLADE_OF_THE_ENDERLORD[1], properties, SkillBuilder.RUINED_BLADE));
    public static final MeleeBuilder<SwordBaseItem> ZENITH = ((materials, properties) -> new SwordBaseItem(materials, Constant.ZENITH[0], Constant.ZENITH[1], properties, SkillBuilder.BLADE_EDGE));
    public static final MeleeBuilder<SwordBaseItem> GIANT_SLAYER_SWORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.GIANT_SLAYER[0], Constant.GIANT_SLAYER[1], Constant.GIANT_SLAYER[2], properties, SkillBuilder.GIANT_SLAYER_SWORD));
}
