package com.kenhorizon.beyondhorizon.server.item.base;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.item.base.tools.*;
import com.kenhorizon.beyondhorizon.server.item.base.weapons.SwordBaseItem;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public class MeleeItemBuilder {
    @FunctionalInterface
    public interface Factory<T extends Item> {
        public abstract T create(MeleeWeaponMaterials materials, Item.Properties properties);
    }


    public static final Factory<SwordBaseItem> CLAYMORE = ((materials, properties) -> new SwordBaseItem(materials, Constant.CLAYMORE, properties, SkillBuilder.CLAYMORE));
    public static final Factory<SwordBaseItem> SWORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.SWORD, properties));
    public static final Factory<SwordBaseItem> KNIFE = ((materials, properties) -> new SwordBaseItem(materials, Constant.KNIFE, properties));
    public static final Factory<SwordBaseItem> LIGHT_SWORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.SWORD, properties));
    public static final Factory<SwordBaseItem> HEAVY_SWORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.SWORD, properties));
    public static final Factory<SwordBaseItem> SPEAR = ((materials, properties) -> new SwordBaseItem(materials, Constant.SPEAR, properties));
    public static final Factory<DiggerBaseItem> AXE = ((materials, properties) -> new AxeBaseItem(materials, Constant.AXE, properties));
    public static final Factory<DiggerBaseItem> PICKAXE = ((materials, properties) -> new PickaxeBaseItem(materials, Constant.PICKAXE, properties));
    public static final Factory<DiggerBaseItem> SHOVEL = ((materials, properties) -> new ShovelBaseItem(materials, Constant.SHOVEL, properties));
    public static final Factory<DiggerBaseItem> HOE = ((materials, properties) -> new HoeBaseItem(materials, Constant.HOE, properties));

    public static final Factory<SwordBaseItem> ENERGIZED_MACE = ((materials, properties) -> new SwordBaseItem(materials, Constant.MACE, properties, SkillBuilder.MACE));
    public static final Factory<SwordBaseItem> BLADE_OF_THE_ENDERLORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.BLADE_OF_THE_ENDERLORD, properties, SkillBuilder.RUINED_BLADE));
    public static final Factory<SwordBaseItem> ZENITH = ((materials, properties) -> new SwordBaseItem(materials, Constant.ZENITH, properties, SkillBuilder.BLADE_EDGE));
    public static final Factory<SwordBaseItem> GIANT_SLAYER_SWORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.GIANT_SLAYER, properties, SkillBuilder.GIANT_SLAYER_SWORD));
    public static final Factory<SwordBaseItem> ELUCIDATOR = ((materials, properties) -> new SwordBaseItem(materials, Constant.ELUCIDATOR, properties, SkillBuilder.ELUDICATOR).addAttribues(BHAttributes.CRITICAL_CHANCE.get(), "73ddecd3-7e85-4743-a4ec-8c81a5d8ce3e", Constant.CRITICAL_STRIKE_0, AttributeModifier.Operation.ADDITION));
    public static final Factory<SwordBaseItem> DARK_REPULSER = ((materials, properties) -> new SwordBaseItem(materials, Constant.DARK_REPULSER, properties, SkillBuilder.DARK_REPULSOR).addAttribues(BHAttributes.CRITICAL_CHANCE.get(), "a4339059-a960-462c-bd5c-c2ec7ddc570b", Constant.CRITICAL_STRIKE_0, AttributeModifier.Operation.ADDITION));
    public static final Factory<SwordBaseItem> GUARDIAN_SWORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.GUARDIAN_SWORD, properties, SkillBuilder.GUARDIAN).addAttribues(BHAttributes.CRITICAL_CHANCE.get(), "a4339059-a960-462c-bd5c-c2ec7ddc570b", Constant.CRITICAL_STRIKE_0, AttributeModifier.Operation.ADDITION));
    public static final Factory<SwordBaseItem> RADIANT = ((materials, properties) -> new SwordBaseItem(materials, Constant.RADIANT_SWORD, properties, SkillBuilder.RADIANT));
    public static final Factory<SwordBaseItem> HARVESTER = ((materials, properties) -> new SwordBaseItem(materials, Constant.HARVESTER, properties, SkillBuilder.HARVESTER));
    public static final Factory<SwordBaseItem> SOLARFLARE = ((materials, properties) -> new SwordBaseItem(materials, Constant.SOLARFLARE, properties, SkillBuilder.SOLARFLARE));
    public static final Factory<SwordBaseItem> STELLAR_AXE = ((materials, properties) -> new SwordBaseItem(materials, Constant.STELLAR_AXE, properties, SkillBuilder.STELLAR_AXE));
    public static final Factory<SwordBaseItem> HEAVENLY_EDGE_DARK_SWORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.HEAVENLY_EDGE_DARK_SWORD, properties, SkillBuilder.STELLAR_AXE));
    public static final Factory<SwordBaseItem> HEAVENLY_EDGE_LIGHT_SWORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.HEAVENLY_EDGE_LIGHT_SWORD, properties, SkillBuilder.STELLAR_AXE));
}
