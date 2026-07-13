package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.keybinds.Keybinds;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.AttributeTooltips;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.ColorCodedText;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.api.IAttack;
import com.kenhorizon.beyondhorizon.server.api.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
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

public abstract class Accessory {
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
    protected boolean tooltipEnable = true;
    protected boolean tooltipNameEnable = true;
    protected boolean tooltipDescriptionEnable = true;
    protected boolean attributeTooltipEnable = true;
    private float magnitude;
    private int level = 1;
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
    protected final Multimap<Attribute, AttributeModifier> indetifierModifiers = HashMultimap.create();
    protected boolean isInnate = false;
    protected List<RegistryObject<? extends Accessory>> innateSkills = new ArrayList<>();
    @Nullable
    protected String descriptionId;
    protected ItemAbilityType type;
    protected Tags tags = Tags.NONE;
    public Accessory(ItemAbilityType type, float magnitude, int level) {
        this.magnitude = magnitude;
        this.level = level;
        this.type = type;
    }

    public Accessory() {
        this(ItemAbilityType.PASSIVE,0, 1);
    }

    public ItemAbilityType getType() {
        return type;
    }

    public void setType(ItemAbilityType type) {
        this.type = type;
    }

    public String getName() {
        return BHRegistries.ACCESSORY_KEY.get().getKey(this).getPath();
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = String.format("accessory.%s.%s", this.getId(), this.getName());
        }
        return this.descriptionId;
    }

    public Tags getTags() {
        return tags;
    }

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

    public void addTooltip(ItemStack itemStack, List<Component> tooltip, int size, boolean isShiftPressed, boolean first) {
        if (!this.isTooltipEnable()) return;
        if (this.isTooltipNameEnable()) {
            this.addTooltipTitle(itemStack, tooltip, first);
        }
        if (!this.isTooltipDescriptionEnable()) return;
        boolean flag = size == 1;
        if (BHConfigs.ADVANCED_TOOLTIP && I18n.exists(this.createId())) {
            this.addTooltipDescription(itemStack, tooltip);
        } else if (BHConfigs.ADVANCED_TOOLTIP_ACCESSORY && I18n.exists(this.createId())) {
            this.addTooltipDescription(itemStack, tooltip);
        } else if ((flag || isShiftPressed) && I18n.exists(this.createId())) {
            this.addTooltipDescription(itemStack, tooltip);
        }
    }

    protected void addTooltipDescriptionHeader(ItemStack itemStack, List<Component> tooltip) {

    }

    protected void addTooltipDescription(ItemStack itemStack, List<Component> tooltip) {
        Minecraft mc = Minecraft.getInstance();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        this.addTooltipDescriptionHeader(itemStack, tooltip);
        for (var createTooltips : this.makeTooltips(itemStack)) {
            tooltip.add(ColorCodedText.applyFormat(createTooltips, Tooltips.TOOLTIP[0].getColor()));
        }
    }

    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        list.add(this.makeTooltip(itemStack));
        return list;
    }

    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId()).withStyle(Tooltips.TOOLTIP[0]);
    }

    protected String createId(int lines) {
        return lines == 0 ? String.format("%s.desc", this.getDescriptionId()) : String.format("%s.desc.%s", this.getDescriptionId(), lines);
    }

    protected String createId() {
        return createId(0);
    }

    protected void addTooltipTitle(ItemStack itemStack, List<Component> tooltip, boolean firstType) {
        Component text;
        text = this.spacing().append(Component.literal(Utils.capitalize(this.getType().getName().toLowerCase(Locale.ROOT))).withStyle(Tooltips.TOOLTIP[1]).append(this.spacing()).append(this.spacing().append(Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.GOLD))));
        tooltip.add(text);
    }

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

    public Optional<IAccessoryEvent> IAccessory() {
        return Optional.empty();
    }

    public MutableComponent spacing() {
        return Component.literal(" ");
    }

    public boolean registerIcons() {
        return false;
    }

    public boolean isTooltipEnable() {
        return this.tooltipEnable;
    }

    public boolean isAttributeTooltipEnable() {
        return this.attributeTooltipEnable;
    }

    public boolean isTooltipNameEnable() {
        return this.tooltipNameEnable;
    }

    public boolean isTooltipDescriptionEnable() {
        return this.tooltipDescriptionEnable;
    }

    public void setTooltipEnable(boolean tooltipEnable) {
        this.tooltipEnable = tooltipEnable;
    }

    public void setAttributeTooltipEnable(boolean attributeTooltipEnable) {
        this.attributeTooltipEnable = attributeTooltipEnable;
    }

    public void setTooltipDescriptionEnable(boolean tooltipDescriptionEnable) {
        this.tooltipDescriptionEnable = tooltipDescriptionEnable;
    }

    public void setTooltipEnableName(boolean tooltipEnableName) {
        this.tooltipNameEnable = tooltipEnableName;
    }

    public Optional<IAttack> IAttackCallback() {
        return Optional.empty();
    }

    public Optional<IEntityProperties> IEntityProperties() {
        return Optional.empty();
    }

    public MutableComponent addKeyBinds(int slot) {
        return Component.translatable(Tooltips.TOOLTIP_KEYBIND, Keybinds.ACCESSORY_SLOTS.getKey().getDisplayName(), slot + 1).withStyle(ChatFormatting.GOLD);
    }
    public MutableComponent addKeyBindDestinated() {
        return Component.translatable(Tooltips.TOOLTIP_KEYBIND, Keybinds.ACCESSORY_SLOTS.getKey().getDisplayName(), "Destinated Slot").withStyle(ChatFormatting.GOLD);
    }
}
