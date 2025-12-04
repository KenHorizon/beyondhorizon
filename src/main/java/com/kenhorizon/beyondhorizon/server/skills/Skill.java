package com.kenhorizon.beyondhorizon.server.skills;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.level.tooltips.AttributeTooltips;
import com.kenhorizon.beyondhorizon.client.level.tooltips.ColorCodedText;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.client.level.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.accessory.Accessories;
import com.kenhorizon.beyondhorizon.server.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IItemGeneric;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
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

import javax.annotation.Nullable;
import java.util.*;

public abstract class Skill {
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
    public enum Format {
        NEGATIVE(ChatFormatting.RED),
        NORMAL(ChatFormatting.YELLOW);

        private final ChatFormatting chatFormatting;

        private Format(ChatFormatting chatFormatting) {
            this.chatFormatting = chatFormatting;
        }

        public ChatFormatting getChatFormatting() {
            return this.chatFormatting;
        }
    }
    protected String MODID = Skills.REGISTRY.getRegistryName().getNamespace();
    protected boolean isSkill = false;
    protected boolean isMelee = false;
    protected boolean isRanged = false;
    protected boolean isThrowing = false;
    protected boolean isAccessory = false;
    protected Format format = Format.NORMAL;
    protected Type skillType = Type.PASSIVE;
    protected int cooldown = 0;
    protected int manaCost = 0;
    private final AttributeTooltips attributeTooltip = new AttributeTooltips();
    protected boolean tooltipEnable = true;
    protected boolean tooltipNameEnable = true;
    protected boolean tooltipDescriptionEnable = true;
    protected boolean attributeTooltipEnable = true;
    public static final String ATTRIBUTES_TAGS = "attribute_modifiers";
    @Nullable
    protected String descriptionId;
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();

    public Skill format(Format format) {
        this.format = format;
        return this;
    }

    public Skill type(Type type) {
        this.skillType = type;
        return this;
    }

    public Type getSkillType() {
        return this.skillType;
    }

    public Skill disableTooltipName() {
        this.tooltipDescriptionEnable = false;
        return this;
    }

    public Skill disableTooltip() {
        this.tooltipEnable = false;
        return this;
    }

    public boolean isTooltipDescriptionEnable() {
        return this.tooltipDescriptionEnable;
    }

    public String getName() {
        return Skills.SUPPLIER_KEY.get().getKey(this).getPath();
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = String.format("skills.%s.%s", this.getId(), this.getName());
        }
        return this.descriptionId;
    }

    public String getId() {
        return Skills.SUPPLIER_KEY.get().getKey(this).getNamespace();
    }

    public boolean isSkill() {
        return this.skillType != Type.ACTIVE;
    }

    public Skill melee() {
        this.isMelee = true;
        return this;
    }

    public Skill ranged() {
        this.isRanged = true;
        return this;
    }

    public Skill universal() {
        this.isThrowing = true;
        this.isRanged = true;
        this.isMelee = true;
        return this;
    }

    public Skill throwing() {
        this.isThrowing = true;
        return this;
    }
    public Skill accessory() {
        this.isAccessory = true;
        return this;
    }
    public final boolean isMeleeAbility() {
        return this.isMelee;
    }

    public final boolean isRangedAbility() {
        return this.isRanged;
    }

    public final boolean isThrowingAbility() {
        return this.isThrowing;
    }
    public final boolean isAccessory() {
        return this.isAccessory;
    }

    public final boolean isUniversal() {
        return this.isMelee && this.isRanged && this.isThrowing;
    }
    public String errorNotMatch(Skill skill) {
        if (skill.isMeleeAbility()) {
            return String.format("Skill is not match with weapon type[melee]: %s", skill.getName());
        }
        if (skill.isRangedAbility()) {
            return String.format("Skill is not match with weapon type[ranged]: %s", skill.getName());
        }
        if (skill.isThrowingAbility()) {
            return String.format("Skill is not match with weapon type[throwable]: %s", skill.getName());
        }
        return String.format("Skill is not match with skillType %s", skill.getName());
    }

    public String errorInCompatibleMessages(Skill other0, Skill other1) {
        return String.format("%s: other skill are not compatible each other %s and %s", other0.getName(), other1.getName());
    }

    public String errorMessages(String causing) {
        return String.format("%s", causing);
    }

    public boolean isEnchantmentCompatible(Enchantment enchantIn) {
        return false;
    }

    public boolean isEnchantmentIncompatible(Enchantment enchantIn) {
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

    @Override
    public String toString() {
        return String.format("Skill:{Type: %s:%s, Type: %s, Settings:{Tooltip:%s, TooltipName:%s, TooltipDescription:%s}}", this.MODID, this.getName(), this.getSkillType(), this.isTooltipEnable(), this.isTooltipNameEnable(), this.isTooltipDescriptionEnable());
    }

    public final String getSkillId() {
        if (this.MODID == null) {
            var id = Objects.requireNonNull(BeyondHorizon.resource(this.getName()));
            this.MODID = id.toString().intern();
        }
        return this.MODID;
    }

    protected void addTooltipTitle(ItemStack itemStack, List<Component> tooltip, boolean firstType) {
        Component abilityTrait;
        if (firstType) {
            abilityTrait = CommonComponents.space()
                    .append(Component.translatable(Tooltips.SKILL_TYPE,
                            this.getSkillType().getName())).withStyle(Tooltips.TOOLTIP[1])
                    .append(CommonComponents.space()
                            .append(Component.translatable(this.getDescriptionId()).withStyle(this.format.getChatFormatting())));
        } else {
            abilityTrait = CommonComponents.space().append(Component.translatable(this.getDescriptionId()).withStyle(this.format.getChatFormatting()));
        }
        tooltip.add(abilityTrait);
    }
    public void addTooltip(ItemStack itemStack, List<Component> tooltip, boolean isShiftPressed) {
        if (!this.isTooltipEnable()) return;
        if (this.isTooltipNameEnable()) {
            this.addTooltipTitle(itemStack, tooltip, true);
        }
        if (!this.isTooltipDescriptionEnable()) return;
        if (this.isAttributeTooltipEnable()) {
            this.attributeTooltip.makeAttributeTooltip(itemStack, tooltip, this.getAttributeModifierByTags(itemStack));
        }
        if (I18n.exists(this.createId())) {
            this.addTooltipDescription(itemStack, tooltip);
        }
    }
    protected void addTooltipDescription(ItemStack itemStack, List<Component> tooltip) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        int screenWidth = minecraft.getWindow().getScreenWidth();
        int maxWidth;
        if (screenWidth < 860) {
            maxWidth = Constant.SMALL_TOOLTIP_MAX_TEXT_WITDH;
        } else if (screenWidth > 860 && screenWidth < 1280) {
            maxWidth = Constant.MEDUIM_TOOLTIP_MAX_TEXT_WITDH;
        } else {
            maxWidth = Constant.TOOLTIP_MAX_TEXT_WITDH;
        }
        List<FormattedCharSequence> wrappedText = font.split(this.tooltipDescription(itemStack), maxWidth);
        for (FormattedCharSequence format : wrappedText) {
            List<FormattedText> texts = Tooltips.recompose(List.of(ClientTooltipComponent.create(format)));
            Component text = Component.literal(texts.get(0).getString());
            tooltip.add(this.spacing().append(ColorCodedText.applyFormat(text)).withStyle(Tooltips.TOOLTIP[0]).append(this.spacing()));
        }
    }

    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        return Component.translatable(this.createId());
    }

    protected String createId(int lines) {
        return lines == 0 ? String.format("%s.desc", this.getDescriptionId()) : String.format("%s.desc.%s", this.getDescriptionId(), lines);
    }

    public MutableComponent spacing() {
        return Component.literal("   ");
    }

    protected String createId() {
        return createId(0);
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

    public Optional<IItemGeneric> IItemGeneric() {
        return Optional.empty();
    }
}
