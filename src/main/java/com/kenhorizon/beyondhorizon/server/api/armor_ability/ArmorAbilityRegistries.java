package com.kenhorizon.beyondhorizon.server.api.armor_ability;

import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ArmorAbilityRegistries {
    private static final String WILDFIRE_ARMOR_ABILITY = "wildfire_armor_ability";

    public static final RegistryObject<ArmorAbility> WILDFIRE = registerArmor(WILDFIRE_ARMOR_ABILITY, () ->
            new WildfireArmorAbility(DefaultArmorBonus.WILDFIRE_SHOCKWAVE_DAMAGE, DefaultArmorBonus.WILDFIRE_INCREASED_DAMAGE)
                    .itemHead(new ItemStack(BHItems.WILDFIRE_HELMET.get()))
                    .itemBody(new ItemStack(BHItems.WILDFIRE_CHESTPLATE.get()))
                    .itemLeggings(new ItemStack(BHItems.WILDFIRE_LEGGINGS.get()))
                    .itemBoots(new ItemStack(BHItems.WILDFIRE_BOOTS.get()))
    );

    public static RegistryObject<ArmorAbility> registerArmor(String name, Supplier<ArmorAbility> properties) {
        return BHRegistries.DEF_ARMOR_ABILITY.register(name, properties);
    }

    public static void register(IEventBus eventBus) {
        BHRegistries.DEF_ARMOR_ABILITY.register(eventBus);
    }
}
