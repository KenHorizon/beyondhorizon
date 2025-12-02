package com.kenhorizon.beyondhorizon.server.accessory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.level.tooltips.AttributeTooltips;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IItemGeneric;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.util.Tooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

public class Accessory {
    private final AttributeTooltips attributeTooltip = new AttributeTooltips();
    private float magnitude = 0.0F;
    private int level = 1;
    protected boolean tooltipEnable = true;
    protected boolean tooltipEnableName = true;
    protected boolean tooltipDescriptionEnable = true;
    protected boolean attributeTooltipEnable = true;
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
    protected String MODID = Accessories.REGISTRY.getRegistryName().getNamespace();
    public static final String ATTRIBUTES_TAGS = "attribute_modifiers";
    @Nullable
    private String descriptionId;

    public Accessory(float magnitude, int level) {
        this.magnitude = magnitude;
        this.level = level;
    }

    public Accessory() {
        this(0, 0);
    }

    public Accessory disableTooltip() {
        this.tooltipEnable = false;
        return this;
    }
    public Accessory disableTooltipName() {
        this.tooltipEnableName = false;
        return this;
    }
    public Accessory disableAttributeTooltip() {
        this.attributeTooltipEnable = false;
        return this;
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

    public String getName() {
        return Accessories.SUPPLIER_KEY.get().getKey(this).getPath();
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public String getId() {
        return Accessories.SUPPLIER_KEY.get().getKey(this).getNamespace();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = String.format("skills.%s.%s", this.getId(), this.getName());
        }
        return this.descriptionId;

    }

    public final String getSkillId() {
        if (this.MODID == null) {
            var id = Objects.requireNonNull(BeyondHorizon.resource(this.getName()));
            this.MODID = id.toString().intern();
        }
        return this.MODID;
    }

    public Optional<IAttack> IAttackCallback() {
        return Optional.empty();
    }

    public Optional<IItemGeneric> IItemGeneric() {
        return Optional.empty();
    }

    public Optional<IAccessoryEvent> IAccessory() {
        return Optional.empty();
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
        this.tooltipEnableName = tooltipEnableName;
    }

    public Accessory addAttributeModifier(Attribute attribute, String uuid, double amount, AttributeModifier.Operation operation) {
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

    public double getAttributeModifierValue(AttributeModifier modifier) {
        return modifier.getAmount();
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.attributeModifiers;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers() {
        return ImmutableMultimap.of();
    }

    public boolean isAttributeTooltipEnable() {
        return this.attributeTooltipEnable;
    }

    public boolean isEnalbeTooltipName() {
        return this.tooltipEnableName;
    }

    public void addTooltip(ItemStack itemStack, List<Component> tooltip, boolean isShiftPressed) {
        if (!this.isTooltipEnable()) return;
        if (this.isEnalbeTooltipName()) {
            this.addTooltipTitle(itemStack, tooltip);
        }
        if (!this.isTooltipDescriptionEnable()) return;
        if (this.isAttributeTooltipEnable()) {
            this.attributeTooltip.makeAttributeTooltip(itemStack, tooltip, this.getAttributeModifierByTags(itemStack));
        }
        if (isShiftPressed && I18n.exists(this.createId())) {
            this.addTooltipDescription(itemStack, tooltip);
            this.appendDescription(itemStack, tooltip);
        }
    }
    protected void appendDescription(ItemStack itemStack, List<Component> tooltip) {

    }

    protected void addTooltipTitle(ItemStack itemStack, List<Component> tooltip) {
        this.addTooltipTitle(itemStack, tooltip, false);
    }

    protected void addTooltipTitle(ItemStack itemStack, List<Component> tooltip, boolean firstType) {
        Component text;
        if (firstType) {
            text = CommonComponents.space()
                    .append(CommonComponents.space()
                            .append(Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.GOLD)));
        } else {
            text = CommonComponents.space().append(Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.GOLD));
        }
        tooltip.add(text);
    }

    protected void prefixTooltipDesc(ItemStack itemStack, List<Component> tooltip) {

    }
    protected void suffixTooltipDesc(ItemStack itemStack, List<Component> tooltip) {

    }

    public MutableComponent spacing() {
        return Component.literal("     ");
    }

    protected void addTooltipDescription(ItemStack itemStack, List<Component> tooltip) {
        tooltip.add(this.spacing().append(Component.translatable(this.createId()).withStyle(Tooltips.TOOLTIP[0])));
        this.suffixTooltipDesc(itemStack, tooltip);
    }

    protected String createId(int lines) {
        return lines == 0 ? String.format("%s.desc", this.getDescriptionId()) :
                String.format("%s.desc.%s", this.getDescriptionId(), lines);
    }

    protected String createId() {
        return createId(0);
    }

    protected List<Attribute> randomAttributes() {
        List<Attribute> attributes = new ArrayList<>(new HashSet<>());
        attributes.add(Attributes.ATTACK_DAMAGE);
        attributes.add(Attributes.ATTACK_SPEED);
        attributes.add(Attributes.ATTACK_KNOCKBACK);
        attributes.add(Attributes.ARMOR);
        attributes.add(Attributes.MOVEMENT_SPEED);
        attributes.add(Attributes.MAX_HEALTH);
        attributes.add(BHAttributes.ABILITY_POWER.get());
        attributes.add(BHAttributes.CRITICAL_STRIKE.get());
        attributes.add(BHAttributes.CRITICAL_DAMAGE.get());
        attributes.add(BHAttributes.DAMAGE_DEALT.get());
        attributes.add(BHAttributes.DAMAGE_TAKEN.get());
        attributes.add(BHAttributes.MANA_REGENERATION.get());
        return attributes;
    }

    public boolean isTooltipEnable() {
        return this.tooltipEnable;
    }

    public boolean isTooltipDescriptionEnable() {
        return this.tooltipDescriptionEnable;
    }
}
