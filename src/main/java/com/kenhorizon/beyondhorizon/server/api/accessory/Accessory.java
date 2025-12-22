package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.configs.client.ModClientConfig;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

public abstract class Accessory extends Skill {
    private float magnitude;
    private int level = 1;
    public static final String ATTRIBUTES_TAGS = "AttributeModifiers";

    public Accessory(float magnitude, int level) {
        this.format = Format.NORMAL;
        this.category = Category.ITEMS;
        this.magnitude = magnitude;
        this.level = level;
    }

    public Accessory() {
        this(0, 1);
    }
    @Override
    public String getName() {
        return BHRegistries.ACCESSORY_KEY.get().getKey(this).getPath();
    }

    @Override
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    @Override
    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = String.format("accessory.%s.%s", this.getId(), this.getName());
        }
        return this.descriptionId;
    }

    @Override
    public String getId() {
        return BHRegistries.ACCESSORY_KEY.get().getKey(this).getNamespace();
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public float getMagnitude() {
        return magnitude;
    }

    @Override
    public Accessory innate(RegistryObject<Skill> skill) {
        this.innateSkills.add(skill);
        return this;
    }

    public Accessory disableTooltipName() {
        this.tooltipNameEnable = false;
        return this;
    }

    public Accessory disableAttributeTooltip() {
        this.attributeTooltipEnable = false;
        return this;
    }

    public Accessory disableTooltip() {
        this.tooltipEnable = false;
        return this;
    }

    @Override
    public void addTooltip(ItemStack itemStack, List<Component> tooltip, int size, boolean isShiftPressed, boolean first) {
        if (!this.isTooltipEnable()) return;
        if (this.isTooltipNameEnable()) {
            this.addTooltipTitle(itemStack, tooltip, first);
        }
        if (!this.isTooltipDescriptionEnable()) return;
        boolean flag = size == 1;
        boolean alwayShow = (ModClientConfig.ADVANCED_TOOLTIP.get() || ModClientConfig.ADVANCED_TOOLTIP_ACCESSORY.get()) && flag;
        if ((alwayShow || isShiftPressed) && I18n.exists(this.createId())) {
            this.addTooltipDescription(itemStack, tooltip);
        }
    }

    public void addTooltipAttributes(ItemStack itemStack, List<Component> tooltip) {
        if (this.isAttributeTooltipEnable()) {
            this.attributeTooltip.makeAttributeTooltip(itemStack, tooltip, this.getAttributeModifierByTags(itemStack));
        }
    }

    public Accessory addAttributes(Attribute attribute, String uuid, double amount, AttributeModifier.Operation operation) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(uuid), "Attribute Modifier", amount, operation);
        this.attributeModifiers.put(attribute, attributemodifier);
        return this;
    }

    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, ItemStack itemStack) {
        if (this.getAttributeModifierByTags(itemStack).isEmpty()) return;
        for (Map.Entry<Attribute, AttributeModifier> entry : this.getAttributeModifierByTags(itemStack).entries()) {
            AttributeInstance attributeinstance = attributeMap.getInstance(entry.getKey());
            if (attributeinstance != null) {
                attributeinstance.removeModifier(entry.getValue());
            }
        }
    }

    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, ItemStack itemStack) {
        if (this.getAttributeModifierByTags(itemStack).isEmpty()) return;
        for (Map.Entry<Attribute, AttributeModifier> entry : this.getAttributeModifierByTags(itemStack).entries()) {
            AttributeInstance attributeinstance = attributeMap.getInstance(entry.getKey());
            if (attributeinstance != null) {
                AttributeModifier attributemodifier = entry.getValue();
                attributeinstance.removeModifier(attributemodifier);
                attributeinstance.addPermanentModifier(new AttributeModifier(attributemodifier.getId(), "Attribute Modifier", getAttributeModifierValue(attributemodifier), attributemodifier.getOperation()));
            }
        }
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifierByTags(ItemStack itemStack) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        Multimap<Attribute, AttributeModifier> multimap;
        if ((!itemStack.isEmpty() && !nbt.isEmpty()) && nbt.contains(ATTRIBUTES_TAGS, 9)) {
            multimap = HashMultimap.create();
            ListTag nbtList = nbt.getList(ATTRIBUTES_TAGS, 10);
            for (int i = 0; i < nbtList.size(); ++i) {
                CompoundTag tags = nbtList.getCompound(i);
                Optional<Attribute> optional = Optional.ofNullable(ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(tags.getString("attribute_name"))));
                if (optional.isPresent()) {
                    AttributeModifier attributeModifier = AttributeModifier.load(tags);
                    if (attributeModifier != null && attributeModifier.getId().getLeastSignificantBits() != 0L && attributeModifier.getId().getMostSignificantBits() != 0L) {
                        multimap.put(optional.get(), attributeModifier);
                    }
                }
            }
        } else {
            multimap = this.attributeModifiers.isEmpty() ? this.getDefaultAttributeModifiers() : this.attributeModifiers;
        }
        return multimap;
    }

    public Optional<IAccessoryEvent> IAccessory() {
        return Optional.empty();
    }
}
