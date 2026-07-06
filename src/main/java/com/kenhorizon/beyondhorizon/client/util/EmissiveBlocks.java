package com.kenhorizon.beyondhorizon.client.util;

import com.google.common.collect.ImmutableList;
import com.kenhorizon.beyondhorizon.server.block.BHBlockProperties;
import com.kenhorizon.beyondhorizon.server.block.basin.FireBasinBlock;
import com.kenhorizon.beyondhorizon.server.block.spawner.BaseSpawnerBlock;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.BHBaseSpawner;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.SpawnerState;
import com.kenhorizon.beyondhorizon.server.init.BHBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EmissiveBlocks {
//    private static final List<String> FULLBRIGHTS =
//            ImmutableList.of(
//                    "alexscaves:ambersol#",
//                    "alexscaves:radrock_uranium_ore#",
//                    "alexscaves:acidic_radrock#",
//                    "alexscaves:uranium_rod#axis=x",
//                    "alexscaves:uranium_rod#axis=y",
//                    "alexscaves:uranium_rod#axis=z",
//                    "alexscaves:block_of_uranium#",
//                    "alexscaves:abyssal_altar#active=true",
//                    "alexscaves:abyssmarine_",
//                    "alexscaves:peering_coprolith#",
//                    "alexscaves:forsaken_idol#",
//                    "alexscaves:magnetic_light#",
//                    "alexscaves:tremorzilla_egg#"
//            );
    private static final List<String> FULLBRIGHTS = new ArrayList<>();

    public static <T extends Comparable<T>> void register(Supplier<? extends Block> block, String properties) {
        var rl = ForgeRegistries.BLOCKS.getKey(block.get()).toString();
        FULLBRIGHTS.add(String.format("%s#%s", rl, properties));
    }

    public static <T extends Comparable<T>> String with(Property<T> prop, T value) {
        String propName = prop.getName();
        String valueProp = value.toString();
        return String.format("%s=%s",propName, valueProp);
    }

    public static void register(Supplier<? extends Block> block) {
        var rl = ForgeRegistries.BLOCKS.getKey(block.get()).toString();
        FULLBRIGHTS.add(String.format("%s#", rl));
    }

    public static List<String> registered() {
        return FULLBRIGHTS;
    }

    public static void init() {
        register(BHBlocks.FIRE_BASIN, with(FireBasinBlock.LIT, true));
        register(BHBlocks.WALL_FIRE_BASIN, with(FireBasinBlock.LIT, true));
        register(BHBlocks.RADIANCE_CRYSTRAL);
        register(BHBlocks.RAW_STARITE_BLOCK);
        register(BHBlocks.STARITE_ORE);
        register(BHBlocks.STARITE_BLOCK);
        register(BHBlocks.HELLSTONE_COBBLESTONE);
        register(BHBlocks.SPAWNER, with(BHBlockProperties.SPAWNER_STATE, SpawnerState.ACTIVE));
    }
}
