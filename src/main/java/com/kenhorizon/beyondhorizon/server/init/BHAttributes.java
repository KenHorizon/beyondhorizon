package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.libs.registry.RegistryEntry;
import com.kenhorizon.libs.registry.RegistryHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHAttributes {
    private static final double MAX_VALUE = Double.MAX_VALUE;
    public static final RegistryObject<Attribute> ARMOR_PENETRATION = registerAttributes("armor_penetration", 0.0D, 0.0D, 1.0D, true);
    public static final RegistryObject<Attribute> COOLDOWN = registerAttributes("cooldown", 0.0D, -MAX_VALUE, 1.0D, true);
    public static final RegistryObject<Attribute> CAST_TIME = registerAttributes("cast_time", 0.0D, 0.0D, 1.0D, true);
    public static final RegistryObject<Attribute> CRITICAL_DAMAGE = registerAttributes("critical_damage", 1.5D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> CRITICAL_STRIKE = registerAttributes("critical_strike", 0.0D, -MAX_VALUE, MAX_VALUE, true);
    public static final RegistryObject<Attribute> DAMAGE_DEALT = registerAttributes("damage_dealt", 1.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> DAMAGE_TAKEN = registerAttributes("damage_taken", 1.0D, -MAX_VALUE, 1.0D, true);
    public static final RegistryObject<Attribute> EVADE = registerAttributes("evade", 0.0D, 0.0D, 100.0D, true);
    public static final RegistryObject<Attribute> HEALING = registerAttributes("healing", 1.0D, -MAX_VALUE, MAX_VALUE, true);
    public static final RegistryObject<Attribute> HEALTH_REGENERATION = registerAttributes("health_regeneration", 0.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> LETHALITY = registerAttributes("lethality", 0.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> ABILITY_POWER = registerAttributes("ability_power", 0.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> MAGIC_PENETRATION = registerAttributes("magic_penetration", 0.0D, 0.0D, 1.0D, true);
    public static final RegistryObject<Attribute> MAGIC_RESISTANCE = registerAttributes("magic_resistance", 0.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> MANA_COST = registerAttributes("mana_cost", 0.0D, -1.0D, 1.0D, true);
    public static final RegistryObject<Attribute> MANA_REGENERATION = registerAttributes("mana_regeneration", 1.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> MAX_MANA = registerAttributes("max_mana", 100.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> MINING_SPEED = registerAttributes("mining_speed", 1.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> OMNIVAMP = registerAttributes("omnivamp", 0.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> PHYSICALVAMP = registerAttributes("physical_vamp", 0.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> RANGED_DAMAGE = registerAttributes("ranged_damage", 0.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> SHIELDING = registerAttributes("shielding", 1.0D, -MAX_VALUE, MAX_VALUE, true);
    public static final RegistryObject<Attribute> SPELLVAMP = registerAttributes("spell_vamp", 0.0D, 0.0D, MAX_VALUE, true);
    public static final RegistryObject<Attribute> STEALTH = registerAttributes("stealth", 0.0D, 0.0D, 1.0D, true);

    static RegistryObject<Attribute> registerAttributes(String name, double defaultValue, double min, double max) {
        String attributesName = String.format("attribute.%s.%s", BeyondHorizon.ID, name);
        RegistryObject<Attribute> register = RegistryEntry.ATTRIBUTES.register(name, () -> new RangedAttribute(attributesName, defaultValue, min, max).setSyncable(false));
        RegistryHelper.registerAttribiuteLang(name, register);
        return register;
    }

    static RegistryObject<Attribute> registerAttributes(String name, double defaultValue, double min, double max, boolean syncable) {
        String attributesName = String.format("attribute.%s.%s", BeyondHorizon.ID, name);
        RegistryObject<Attribute> register = RegistryEntry.ATTRIBUTES.register(name, () -> new RangedAttribute(attributesName, defaultValue, min, max).setSyncable(syncable));
        RegistryHelper.registerAttribiuteLang(name, register);
        return register;
    }

    public static void register(IEventBus eventBus) {
        RegistryEntry.ATTRIBUTES.register(eventBus);
    }
}
