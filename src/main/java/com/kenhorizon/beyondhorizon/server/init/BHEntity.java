package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.entity.boss.BlazingInferno;
import com.kenhorizon.libs.registry.RegistryEntity;
import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHEntity {

    public static final RegistryObject<EntityType<BlazingInferno>> BLAZING_INFERNO = RegistryEntity
            .register("blazing_inferno", BlazingInferno::new)
            .lang("Blazing Inferno")
            .properties(p -> p.sized(0.85F, 2.75F))
            .properties(EntityType.Builder::fireImmune)
            .tag(Tags.EntityTypes.BOSSES)
            .register();

    public static void register(IEventBus eventBus) {
        RegistryEntries.ENTITY_TYPES.register(eventBus);
    }
}
