package com.kenhorizon.beyondhorizon.server.skills;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IItemGeneric;
import com.kenhorizon.beyondhorizon.server.util.Tooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
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
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;

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
    public static final String ATTRIBUTES_TAGS = "attribute_modifiers";

    protected boolean isSkill = false;
    protected boolean tooltipEnable = true;
    protected boolean tooltipDescriptionEnable = true;
    protected boolean isMelee = false;
    protected boolean isRanged = false;
    protected boolean isThrowing = false;
    protected Format format = Format.NORMAL;
    protected Type skillType = Type.PASSIVE;
    protected int cooldown = 0;
    protected int manaCost = 0;
    @Nullable
    private String descriptionId;
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

    public String getName() {
        return Skills.SUPPLIER_KEY.get().getKey(this).getPath();
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = String.format("skills.%s.%s", Skills.SUPPLIER_KEY.get().getKey(this).getNamespace(), Skills.SUPPLIER_KEY.get().getKey(this).getPath());
        }
        return this.descriptionId;

    }

    public String getID() {
        return Skills.SUPPLIER_KEY.get().getKey(this).toString();
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

    public Skill setUniversal() {
        this.isThrowing = true;
        this.isRanged = true;
        this.isMelee = true;
        return this;
    }

    public Skill isThrowing() {
        this.isThrowing = true;
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

    public final boolean isUniversal() {
        return this.isMelee && this.isRanged && this.isThrowing;
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

    public Optional<IAttack> IAttackCallback() {
        return Optional.empty();
    }

    public Optional<IItemGeneric> IItemGeneric() {
        return Optional.empty();
    }

    public Skill addAttributeModifier(Attribute attribute, String uuid, double amount, AttributeModifier.Operation operation) {
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

    public String toString() {
        return String.format("Skill:{Type: %s:%s, Type: %s}", this.MODID, this.getName(), this.getSkillType());
    }

    public final String getSkillId() {
        if (this.MODID == null) {
            var id = Objects.requireNonNull(BeyondHorizon.resource(this.getName()));
            this.MODID = id.toString().intern();
        }
        return this.MODID;
    }

    public void addTooltip(ItemStack itemStack, List<Component> tooltip, boolean isShiftPressed) {
        this.addTooltip(itemStack, tooltip, isShiftPressed, true);
    }

    public void addTooltip(ItemStack itemStack, List<Component> tooltip, boolean isShiftPressed, boolean firstType) {
        if (!this.isTooltipEnable()) return;
        this.addTooltipTitle(itemStack, tooltip, firstType);
        if (!this.isTooltipDescriptionEnable()) return;
        if (isShiftPressed && I18n.exists(this.createId())) {
            this.addTooltipDescription(itemStack, tooltip);
        }
    }

    protected void addTooltipTitle(ItemStack itemStack, List<Component> tooltip) {
        this.addTooltipTitle(itemStack, tooltip, false);
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

    protected void prefixTooltipDesc(ItemStack itemStack, List<Component> tooltip) {

    }
    protected void suffixTooltipDesc(ItemStack itemStack, List<Component> tooltip) {

    }
    protected void addTooltipDescription(ItemStack itemStack, List<Component> tooltip) {
        tooltip.add(Component.literal("     ").append(Component.translatable(this.createId()).withStyle(Tooltips.TOOLTIP[0])));
        this.prefixTooltipDesc(itemStack, tooltip);
        if (!this.addBulletDescription(itemStack).isEmpty()) {
            for (Component components : this.addBulletDescription(itemStack)) {
                List<Component> text = this.wrapperText(components, 150);
                tooltip.addAll(text);
            }
        }
        this.suffixTooltipDesc(itemStack, tooltip);
    }

    private List<Component> wrapperText(Component text, int maxWidth) {
        Font font = Minecraft.getInstance().font;
        List<Component> result = new ArrayList<>();

        List<String> lines = font.split(text, maxWidth)
                .stream()
                .map(sequence ->  text.getString())
                .toList();

        for (String line : lines) {
            // Optional: Add indent to first line
            if (result.isEmpty()) line = "     " + line;
            result.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
        }

        return result;
    }

    protected List<Component> addDescription(ItemStack itemStack) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal("     ")
                .append(Component.translatable(this.createId()).withStyle(Tooltips.TOOLTIP[0])));
        return tooltip;
    }
    protected List<Component> addBulletDescription(ItemStack itemStack) {
        return new ArrayList<>();
    }
    protected String createId(int lines) {
        return lines == 0 ? String.format("%s.desc", this.getDescriptionId()) :
                String.format("%s.desc.%s", this.getDescriptionId(), lines);
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

//    public Component addKeyBinds() {
//        return CommonComponents.space()
//                .append(Component.translatable(TooltipUtil.TOOLTIP_KEYBIND, KeyBindings.ABILITY_TRAIT_SKILLS.getKey().getDisplayName()).withStyle(ChatFormatting.GOLD));
//    }

    public boolean isTooltipDescriptionEnable() {
        return this.tooltipDescriptionEnable;
    }
}
