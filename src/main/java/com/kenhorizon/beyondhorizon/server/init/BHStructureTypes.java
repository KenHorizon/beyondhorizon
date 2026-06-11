package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.level.world.structures.GenericJigsawStructure;
import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHStructureTypes {

    public static final RegistryObject<StructureType<GenericJigsawStructure>> GENERIC_JIGSAW_STRUCTURE = RegistryEntries.STRUCTURE_TYPE.register("generic_jigsaw", () -> () -> GenericJigsawStructure.CODEC);
    public static void register(IEventBus eventBus) {
        RegistryEntries.STRUCTURE_TYPE.register(eventBus);
        RegistryEntries.STRUCTURE_PIECE.register(eventBus);
    }
}
