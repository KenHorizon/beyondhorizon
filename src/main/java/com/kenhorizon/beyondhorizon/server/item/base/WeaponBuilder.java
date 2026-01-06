package com.kenhorizon.beyondhorizon.server.item.base;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
    public static final MeleeBuilder<SwordBaseItem> ELUCIDATOR = ((materials, properties) -> new SwordBaseItem(materials, Constant.ELUCIDATOR[0], Constant.ELUCIDATOR[1], Constant.ELUCIDATOR[2], properties, SkillBuilder.ELUDICATOR).addAttribues(BHAttributes.CRITICAL_CHANCE.get(), "73ddecd3-7e85-4743-a4ec-8c81a5d8ce3e", Constant.CRITICAL_STRIKE_0, AttributeModifier.Operation.ADDITION));
    public static final MeleeBuilder<SwordBaseItem> DARK_REPULSER = ((materials, properties) -> new SwordBaseItem(materials, Constant.DARK_REPULSER[0], Constant.DARK_REPULSER[1], Constant.DARK_REPULSER[2], properties, SkillBuilder.DARK_REPULSOR).addAttribues(BHAttributes.CRITICAL_CHANCE.get(), "a4339059-a960-462c-bd5c-c2ec7ddc570b", Constant.CRITICAL_STRIKE_0, AttributeModifier.Operation.ADDITION));
    public static final MeleeBuilder<SwordBaseItem> ANCIENT_BLAZING_SWORD = ((materials, properties) -> new SwordBaseItem(materials, Constant.ANCIENT_BLAZING_SWORD[0], Constant.ANCIENT_BLAZING_SWORD[1], Constant.ANCIENT_BLAZING_SWORD[2], properties, SkillBuilder.ANCIENT_BLAZING_SWORD).addAttribues(BHAttributes.CRITICAL_CHANCE.get(), "a4339059-a960-462c-bd5c-c2ec7ddc570b", Constant.CRITICAL_STRIKE_0, AttributeModifier.Operation.ADDITION));
}
