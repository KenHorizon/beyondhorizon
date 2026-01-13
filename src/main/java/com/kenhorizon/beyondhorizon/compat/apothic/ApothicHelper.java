package com.kenhorizon.beyondhorizon.compat.apothic;

import com.kenhorizon.beyondhorizon.compat.ModCompats;
import com.kenhorizon.beyondhorizon.compat.ModLists;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Hashtable;

public class ApothicHelper {
    public static final String ATTRIBUTES_ID = "attributeslib";
    public static final ResourceLocation CRIT_CHANCE = mod("crit_chance");
    public static final ResourceLocation ARROW_VELOCITY =mod("arrow_velocity");
    public static final ResourceLocation ARROW_DAMAGE = mod("arrow_damage");
    public static final ResourceLocation DODGE_CHANCE = mod("dodge_chance");
    public static final ResourceLocation MINING_SPEED = mod("mining_speed");
    public static final ResourceLocation ARMOR_PIERCE = mod("armor_pierce");

    public static boolean isAttributesLoaded() {
        return ModCompats.getMod(ATTRIBUTES_ID).isModLoaded();
    }

    public static ResourceLocation mod(String rl) {
        return ModCompats.getMod(ATTRIBUTES_ID).getRL(rl);
    }

    public static void preset(Hashtable<Attribute, Attribute> map) {
        if (isAttributesLoaded()) {
            map.put(BHAttributes.CRITICAL_CHANCE.get(), ForgeRegistries.ATTRIBUTES.getValue(ApothicHelper.CRIT_CHANCE));
            map.put(BHAttributes.RANGED_DAMAGE.get(), ForgeRegistries.ATTRIBUTES.getValue(ApothicHelper.ARROW_DAMAGE));
            map.put(BHAttributes.EVADE.get(), ForgeRegistries.ATTRIBUTES.getValue(ApothicHelper.DODGE_CHANCE));
            map.put(BHAttributes.MINING_SPEED.get(), ForgeRegistries.ATTRIBUTES.getValue(ApothicHelper.MINING_SPEED));
            map.put(BHAttributes.ARMOR_PENETRATION.get(), ForgeRegistries.ATTRIBUTES.getValue(ApothicHelper.ARMOR_PIERCE));
        }
    }
}
