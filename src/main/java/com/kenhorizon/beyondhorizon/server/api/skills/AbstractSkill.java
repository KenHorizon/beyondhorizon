package com.kenhorizon.beyondhorizon.server.api.skills;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.client.level.tooltips.AttributeTooltips;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public abstract class AbstractSkill<T> {
    public enum Category {
        EQUIPPED,
        ITEMS,
        ENTITY
    }
    public enum Type implements StringRepresentable {
        PASSIVE,
        ACTIVE;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public String getName() {
            return Utils.capitalize(this.name().toLowerCase(Locale.ROOT));
        }
    }

    public static final Logger LOGGER = LogUtils.getLogger();
    protected ChatFormatting format = ChatFormatting.GOLD;
    protected Type type = Type.PASSIVE;
    protected int cooldown = 0;
    protected int manaCost = 0;
    protected final AttributeTooltips attributeTooltip = new AttributeTooltips();
    protected boolean tooltipEnable = true;
    protected boolean tooltipNameEnable = true;
    protected boolean tooltipDescriptionEnable = true;
    protected boolean attributeTooltipEnable = true;
    public static final String ATTRIBUTES_TAGS = "attribute_modifiers";
    @Nullable
    protected String descriptionId;
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
    protected List<RegistryObject<? extends T>> innateSkills = new ArrayList<>();
    public Category category;
    protected boolean isInnate = false;


    public Category getCategory() {
        return category;
    }

    public Type getType() {
        return this.type;
    }

    public AbstractSkill<T> format(ChatFormatting format) {
        this.format = format;
        return this;
    }

    public AbstractSkill<T> category(Category category) {
        this.category = category;
        return this;
    }

    public AbstractSkill<T> type(Type type) {
        this.type = type;
        return this;
    }

    public AbstractSkill<T> disableTooltipName() {
        this.tooltipDescriptionEnable = false;
        return this;
    }

    public AbstractSkill<T> disableTooltip() {
        this.tooltipEnable = false;
        return this;
    }

    public boolean isTooltipDescriptionEnable() {
        return this.tooltipDescriptionEnable;
    }

    public AbstractSkill<T> isInnate() {
        this.isInnate = true;
        return this;
    }

    public AbstractSkill<T> innate(RegistryObject<T> skill) {
        this.innateSkills.add(skill);
        return this;
    }

    public List<RegistryObject<? extends T>> innateSkill() {
        return this.innateSkills;
    }

    public boolean isSkillCompatible(AbstractSkill skill) {
        return false;
    }

    public boolean isSkillIncompatible(AbstractSkill skill) {
        return false;
    }

    public boolean isEnchantmentCompatible(Enchantment enchant) {
        return false;
    }

    public boolean isEnchantmentIncompatible(Enchantment enchant) {
        return false;
    }

    public boolean canPerformToolAction(ItemStack stack, ToolAction action) {
        return false;
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

    public double getAttributeModifierValue(AttributeModifier modifier) {
        return modifier.getAmount();
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.attributeModifiers;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers() {
        return ImmutableMultimap.of();
    }

    protected abstract void addTooltipTitle(ItemStack itemStack, List<Component> tooltip, boolean firstType);

    public abstract void addTooltip(ItemStack itemStack, List<Component> tooltip, int size, boolean isShiftPressed, boolean first);

    protected abstract void addTooltipDescription(ItemStack itemStack, List<Component> tooltip);

    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        return Component.empty();
    }

    public MutableComponent spacing() {
        return Component.literal("   ");
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
}
