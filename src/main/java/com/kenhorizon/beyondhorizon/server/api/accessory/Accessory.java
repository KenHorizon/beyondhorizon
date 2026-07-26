package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.keybinds.Keybinds;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.AttributeTooltips;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.ColorCodedText;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.api.AbstractAbilityComponents;
import com.kenhorizon.beyondhorizon.server.api.IAttack;
import com.kenhorizon.beyondhorizon.server.api.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.item.ItemAbilityType;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public abstract class Accessory extends AbstractAbilityComponents {
    public enum Tags {
        NONE, // Bonuses can stack each others
        UNIQUE; // Bonuses do not stack each others

        public boolean isUnique() {
            return this != NONE;
        }
    }
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String ACCESSORY_ATTRIBUTES_TAGS = "accessory_attribute_modifiers";
    protected int cooldown = 0;
    protected int manaCost = 0;
    protected final AttributeTooltips attributeTooltip = new AttributeTooltips();
    private float magnitude;
    private int level = 1;
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
    protected final Multimap<Attribute, AttributeModifier> indetifierModifiers = HashMultimap.create();
    protected boolean isInnate = false;
    protected List<RegistryObject<? extends Accessory>> innateSkills = new ArrayList<>();
    protected Tags tags = Tags.NONE;

    public Accessory(ItemAbilityType type, float magnitude, int level) {
        this.magnitude = magnitude;
        this.level = level;
        this.type = type;
    }

    public Accessory() {
        this(ItemAbilityType.PASSIVE,0, 1);
    }

    public void setType(ItemAbilityType type) {
        this.type = type;
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

    public Tags getTags() {
        return tags;
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

    public List<RegistryObject<? extends Accessory>> innateSkill() {
        return this.innateSkills;
    }

    public Accessory innate(RegistryObject<Accessory> skill) {
        this.innateSkills.add(skill);
        return this;
    }

    public Accessory tagUnique() {
        this.tags = Tags.UNIQUE;
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

    public Multimap<Attribute, AttributeModifier> registerAttributes(UUID uuid, ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        for (var entryMap : this.getIndetifierModifiers().entries()) {
            var staticModifier = entryMap.getValue();
            var attribute = entryMap.getKey();
            if (staticModifier != null) {
                map.put(attribute, new AttributeModifier(uuid, staticModifier.getName(), staticModifier.getAmount(), staticModifier.getOperation()));
            }
        }
        this.attributeModifiers.putAll(map);
        return map;
    }

    protected String createId(int lines) {
        return lines == 0 ? String.format("%s.desc", this.getDescriptionId()) : String.format("%s.desc.%s", this.getDescriptionId(), lines);
    }

    protected String createId() {
        return createId(0);
    }

    @Override
    public void addTooltipAttributes(ItemStack itemStack, List<Component> tooltip, Multimap<Attribute, AttributeModifier> map) {
        if (this.isAttributeTooltipEnable()) {
            this.attributeTooltip.makeAttributeTooltip(itemStack, tooltip, map);
        }
    }


    public Accessory addAttributes(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.randomUUID(), "Attribute Modifier", amount, operation);
        this.indetifierModifiers.put(attribute, attributemodifier);
        return this;
    }

    public void removeAttributeModifiers(LivingEntity entity, Multimap<Attribute, AttributeModifier> modifier) {
        AttributeMap attributeMap = entity.getAttributes();
        attributeMap.removeAttributeModifiers(modifier);
    }

    public void addAttributeModifiers(LivingEntity entity, Multimap<Attribute, AttributeModifier> modifier) {
        AttributeMap attributeMap = entity.getAttributes();
        attributeMap.addTransientAttributeModifiers(modifier);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.attributeModifiers;
    }

    public Multimap<Attribute, AttributeModifier> getIndetifierModifiers() {
        return indetifierModifiers;
    }

    public Optional<IAccessoryEvent> accessory() {
        return Optional.empty();
    }

    public MutableComponent addKeyBinds(int slot) {
        return Component.translatable(Tooltips.KEYBINDS, Keybinds.ACCESSORY_SLOTS.getKey().getDisplayName(), slot + 1).withStyle(ChatFormatting.GOLD);
    }

    public MutableComponent addKeyBindDestinated() {
        return Component.translatable(Tooltips.KEYBINDS, Keybinds.ACCESSORY_SLOTS.getKey().getDisplayName(), "Destinated Slot").withStyle(ChatFormatting.GOLD);
    }
}
