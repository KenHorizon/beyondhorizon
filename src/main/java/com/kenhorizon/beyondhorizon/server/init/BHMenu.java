package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.inventory.AccessoryMenu;
import com.kenhorizon.beyondhorizon.server.inventory.WorkbenchMenu;
import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.RegistryObject;

public class BHMenu {
    public static final RegistryObject<MenuType<AccessoryMenu>> ACCESSORY_MENU = registerMenuType(AccessoryMenu::new, "accessory_menu");
    public static final RegistryObject<MenuType<WorkbenchMenu>> WORKBENCH_MENU = registerMenuType(WorkbenchMenu::new, "workbench_menu");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return RegistryEntries.MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        RegistryEntries.MENUS.register(eventBus);
    }
}
