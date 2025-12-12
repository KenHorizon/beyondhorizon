package com.kenhorizon.beyondhorizon.server.util.attributes;

import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.mixins.server.accessor.AttributeAccessor;
import com.kenhorizon.beyondhorizon.mixins.server.accessor.AttributeRangeAccessor;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.HashMap;
import java.util.Map;

public class AttributeModify {
    @Expose
    private Map<String, Entry> attributes = new HashMap<>();

    public void applyChanges(IAttributeRegistryHelper<Attribute> registry) {
        for (Map.Entry<String, Entry> configEntry : attributes.entrySet()) {
            final ResourceLocation attributeId = ResourceLocation.tryParse(configEntry.getKey());
            if (attributeId != null && registry.exists(attributeId)) {
                final Attribute attribute = registry.get(attributeId);
                if (attribute instanceof RangedAttribute ranged) {
                    final double minValue = configEntry.getValue().min.value;
                    final double maxValue = configEntry.getValue().max.value;
                    if (minValue > maxValue) {
                        BeyondHorizon.LOGGER.error("Attribute {} was configured to have a minimum value higher than it's maximum. This is not permitted!", attributeId);
                        continue;
                    }
                    final AttributeAccessor attributeAccessor = (AttributeAccessor) attribute;
                    final AttributeRangeAccessor rangeAccessor = (AttributeRangeAccessor) attribute;
                    attributeAccessor.setSyncable(true);
                    if (minValue != ranged.getMinValue()) {
                        BeyondHorizon.LOGGER.debug("Modifying minimum value for {} from {} to {}.", attributeId, Maths.format0(ranged.getMinValue()), Maths.format0(minValue));
                        rangeAccessor.setMinValue(minValue);
                    }
                    if (maxValue != ranged.getMaxValue()) {
                        BeyondHorizon.LOGGER.debug("Modifying maximum value for {} from {} to {}.", attributeId, Maths.format0(ranged.getMaxValue()), Maths.format0(maxValue));
                        rangeAccessor.setMaxValue(maxValue);
                    }
                }
            }
        }
    }
    public void applySyncable(IAttributeRegistryHelper<Attribute> registry) {
        for (Map.Entry<String, Entry> configEntry : attributes.entrySet()) {
            final ResourceLocation attributeId = ResourceLocation.tryParse(configEntry.getKey());
            if (attributeId != null && registry.exists(attributeId)) {
                final Attribute attribute = registry.get(attributeId);
                if (attribute instanceof RangedAttribute ranged) {
                    final boolean setSyncable = configEntry.getValue().syncToClient;
                    final AttributeAccessor attributeAccessor = (AttributeAccessor) attribute;
                    attributeAccessor.setSyncable(setSyncable);
                }
            }
        }
    }
    //
    public static AttributeModify load(IAttributeRegistryHelper<Attribute> registry) {
        final AttributeModify config = new AttributeModify();
        for (Attribute attribute : registry.getValues()) {
            if (attribute instanceof RangedAttribute ranged) {
                final ResourceLocation id = registry.getId(attribute);
                config.attributes.put(id.toString(), new Entry(id, ranged));
            }
        }
        return config;
    }
    //
    private static final Map<Attribute, Double> NEW_DEFAULT_VALUES = ImmutableMap.of(
            Attributes.MAX_HEALTH, 1_000_000D,
            Attributes.ARMOR, 1_000_000D,
            Attributes.ARMOR_TOUGHNESS, 1_000_000D,
            Attributes.ATTACK_DAMAGE, 1_000_000D,
            Attributes.ATTACK_KNOCKBACK, 1_000_000D
    );
    private static final Map<Attribute, Boolean> SYNCABLE = ImmutableMap.of(
            Attributes.MAX_HEALTH, Boolean.TRUE,
            Attributes.ARMOR, Boolean.TRUE,
            Attributes.ARMOR_TOUGHNESS, Boolean.TRUE,
            Attributes.ATTACK_DAMAGE, Boolean.TRUE,
            Attributes.ATTACK_KNOCKBACK, Boolean.TRUE,
            Attributes.KNOCKBACK_RESISTANCE, Boolean.TRUE
    );

    public static class Entry {

        @Expose
        private boolean syncToClient;

        @Expose
        private boolean enabled;

        @Expose
        private DoubleValue min;

        @Expose
        private DoubleValue max;

        public Entry(ResourceLocation id, RangedAttribute attribute) {
            this.enabled = "minecraft".equals(id.getNamespace());
            this.syncToClient = SYNCABLE.getOrDefault(attribute, true);
            this.min = new DoubleValue(attribute.getMinValue(), attribute.getMinValue());
            this.max = new DoubleValue(attribute.getMaxValue(), NEW_DEFAULT_VALUES.getOrDefault(attribute, attribute.getMaxValue()));
        }

        public boolean isEnabled() {

            return this.isEnabled();
        }
    }

    public static class DoubleValue {

        @Expose
        @SerializedName("default")
        private double defaultValue;

        @Expose
        private double value;

        public DoubleValue(double defaultValue, double value) {

            this.defaultValue = defaultValue;
            this.value = value;
        }
    }
}
