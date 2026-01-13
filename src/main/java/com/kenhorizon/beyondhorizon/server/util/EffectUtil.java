package com.kenhorizon.beyondhorizon.server.util;

import com.kenhorizon.beyondhorizon.server.tags.BHEffectTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectUtil {
    public static boolean is(TagKey<MobEffect> mobEffectTagKey) {
        for (MobEffect mobEffects : ForgeRegistries.MOB_EFFECTS) {
            return ForgeRegistries.MOB_EFFECTS.getHolder(mobEffects).map(holder -> holder.is(mobEffectTagKey)).orElse(false);
        }
        return false;
    }
    public static boolean is(MobEffect effect, TagKey<MobEffect> mobEffectTagKey) {
        return ForgeRegistries.MOB_EFFECTS.getHolder(effect).map(holder -> holder.is(mobEffectTagKey)).orElse(false);
    }
}
