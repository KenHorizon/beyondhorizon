package com.kenhorizon.beyondhorizon.server.api;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.ColorCodedText;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.api.data.IItemProperties;
import com.kenhorizon.beyondhorizon.server.item.ItemAbilityType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Abstact Ability Components handle all the basic logic such as tooltips and properties event and systems
 * */
public abstract class AbstractAbilityComponents {
    protected ItemAbilityType type = ItemAbilityType.PASSIVE;
    protected boolean tooltipEnable = true;
    protected boolean tooltipNameEnable = true;
    protected boolean tooltipDescriptionEnable = true;
    protected boolean attributeTooltipEnable = true;
    public static final String ATTRIBUTES_TAGS = "AttributeModifiers";
    @Nullable
    protected String descriptionId;

    public AbstractAbilityComponents disableTooltipDescription() {
        this.tooltipDescriptionEnable = false;
        return this;
    }

    public AbstractAbilityComponents disableTooltipName() {
        this.tooltipNameEnable = false;
        return this;
    }

    public AbstractAbilityComponents disableTooltip() {
        this.tooltipEnable = false;
        return this;
    }


    public abstract String getName();

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    protected abstract String getOrCreateDescriptionId();

    public abstract String getId();

    public boolean isPassive() {
        return this.type == ItemAbilityType.PASSIVE;
    }

    public boolean isActive() {
        return this.type == ItemAbilityType.ACTIVE;
    }

    public ItemAbilityType getType() {
        return this.type;
    }

    /**
     * Add Tooltips Title
     * */
    protected MutableComponent addTooltipTitle() {
        MutableComponent passive = this.spacing().append(Component.literal("[P]").withStyle(Tooltips.TOOLTIP[1]).append(this.spacing()));
        MutableComponent active = this.spacing().append(Component.literal("[A]").withStyle(Tooltips.TOOLTIP[1]).append(this.spacing()));
        if (this.isPassive()) {
            return passive.append(Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.GOLD));
        } else {
            return active.append(Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.GOLD));
        }
    }

    /**
     * <p>Handle adding tooltips to the items
     * <pre>{@code
     *      ///Item's Tooltip Format///
     *      - Attribute Tooltips
     *      - Passive Ability
     *      - Active Ability
     *      - Item Limitations
     * }</pre>
     * @param itemStack The Item being used
     * @param tooltip The Item's Tooltip
     * @param size The Tooltip line
     * @param isShiftPressed Is Shift Key is pressed
     * @param first Determine if on the first added of ability's tooltips passive or either active type
     * */
    public void addTooltip(ItemStack itemStack, List<Component> tooltip, int size, boolean isShiftPressed, boolean first) {
        if (!this.isTooltipEnable()) return;
        if (this.isTooltipNameEnable()) {
            tooltip.add(this.addTooltipTitle());
        }
        if (!this.isTooltipDescriptionEnable()) return;
        boolean flag = size == 1;
        if (BHConfigs.ADVANCED_TOOLTIP && I18n.exists(this.createId())) {
            tooltip.addAll(this.addTooltipDescription(itemStack));
        } else if ((flag || isShiftPressed) && I18n.exists(this.createId())) {
            tooltip.addAll(this.addTooltipDescription(itemStack));
        }
    }

    /**
     * <p>Add Tooltips Header for Tooltip Description Section
     * */
    protected void addTooltipDescriptionHeader(ItemStack itemStack, List<Component> tooltip) {

    }

    /**
     * <p>Add Tooltips for Tooltip Description Section
     * */
    public List<Component> addTooltipDescription(ItemStack itemStack) {
        List<Component> list = new ArrayList<>();
        this.addTooltipDescriptionHeader(itemStack, list);
        for (var createTooltips : this.makeTooltips(itemStack)) {
            list.add(ColorCodedText.applyFormat(createTooltips, Tooltips.TOOLTIP[0].getColor()));
        }
        return list;
    }

    /**
     * <p>Handle adding mutliple tooltip's descriptions
     * <p>Use {@code makeTooltip(ItemStack itemstack)} if making single descriptions
     * */
    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        list.add(this.makeTooltip(itemStack));
        return list;
    }

    /**
     * <p>Handle adding single tooltip's descriptions
     * <p>Use {@code makeTooltips(ItemStack itemstack)} if making multiple descriptions
     * */
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId());
    }

    /**
     * <p>Handle adding attrbiutes of the items
     * <p>Note:This adds on the top of the tooltips
     * */
    public void addTooltipAttributes(ItemStack itemStack, List<Component> tooltip) {

    }

    /**
     * <p>Handle adding attrbiutes of the items using Attributes Maps
     * <p>Note:This adds on the top of the tooltips
     * */
    public void addTooltipAttributes(ItemStack itemStack, List<Component> tooltip, Multimap<Attribute, AttributeModifier> map) {

    }

    /**
     * <p>Handle adding item's tooltip descriptions
     * @param lines How many desc is will be added
     * */
    protected String createId(int lines) {
        return lines == 0 ? String.format("%s.desc", this.getDescriptionId()) : String.format("%s.desc.%s", this.getDescriptionId(), lines);
    }

    /**
     * <p>Handle adding item's tooltip descriptions
     * */
    protected String createId() {
        return createId(0);
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers() {
        return ImmutableMultimap.of();
    }

    /**
     * <p> Add simple Spacing Append
     * */
    public MutableComponent spacing() {
        return Component.literal(" ");
    }

    /**
     * <p> Add if the ability can render an icon when being used or in the slots
     * */
    public boolean registerIcons() {
        return false;
    }

    /**
     * <p> Enable to put ability tooltips
     * */
    public boolean isTooltipEnable() {
        return this.tooltipEnable;
    }

    /**
     * <p> Enable to put ability attributes
     * */
    public boolean isAttributeTooltipEnable() {
        return this.attributeTooltipEnable;
    }


    /**
     * <p> Enable to put ability name
     * */
    public boolean isTooltipNameEnable() {
        return this.tooltipNameEnable;
    }

    /**
     * <p> Enable to put ability descriptions
     * */
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

    /**
     * <p> Handle all the Damage-System Logic of the ability
     * */
    public Optional<IAttack> attack() {
        return Optional.empty();
    }

    /**
     * <p> Handle all for update/modification a properties for target or the holder
     * */
    public Optional<IEntityProperties> entityProperties() {
        return Optional.empty();
    }

    /**
     * <p> Handle all the modification for items
     * */
    public Optional<IItemProperties> itemProperties() {
        return Optional.empty();
    }

    /**
     * <p> Check if the items is allowed to have this enchantments
     * <p> {@code This is only used for items that can be enchantment with}
     * */
    public boolean isEnchantmentCompatible(Enchantment enchant) {
        return false;
    }

    /**
     * <p> Check if the items is not allowed to have this enchantments
     * <p> {@code This is only used for items that can be enchantment with}
     * */
    public boolean isEnchantmentIncompatible(Enchantment enchant) {
        return false;
    }
}
