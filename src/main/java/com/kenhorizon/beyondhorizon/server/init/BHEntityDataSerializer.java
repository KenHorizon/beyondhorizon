package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.entity.boss.pyrolliger.Pyrolliger;
import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHEntityDataSerializer {


    private static final EntityDataSerializer<Pyrolliger.Mode> REG_PYROLLIGER_MODE = EntityDataSerializer.simpleEnum(Pyrolliger.Mode.class);
    public static final RegistryObject<EntityDataSerializer<Pyrolliger.Mode>> PYROLLIGER_MODE =
            RegistryEntries.ENTITY_DATA_SERIALIZER.register("pyrolliger_mode", () -> REG_PYROLLIGER_MODE);


    public static void register(IEventBus eventBus) {
        RegistryEntries.ENTITY_DATA_SERIALIZER.register(eventBus);
    }
}
