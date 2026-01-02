package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.client.level.tooltips.AttributeTooltips;
import com.kenhorizon.beyondhorizon.client.level.tooltips.ColorCodedText;
import com.kenhorizon.beyondhorizon.client.level.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.configs.client.ModClientConfig;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public abstract class Accessory {
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
    protected int cooldown = 0;
    protected int manaCost = 0;
    protected final AttributeTooltips attributeTooltip = new AttributeTooltips();
    protected boolean tooltipEnable = true;
    protected boolean tooltipNameEnable = true;
    protected boolean tooltipDescriptionEnable = true;
    protected boolean attributeTooltipEnable = true;
    private float magnitude;
    private int level = 1;
    public static final String ATTRIBUTES_TAGS = "AttributeModifiers";
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
    public Skill.Category category;
    protected boolean isInnate = false;
    protected List<RegistryObject<? extends Accessory>> innateSkills = new ArrayList<>();
    @Nullable
    protected String descriptionId;

    public Accessory(float magnitude, int level) {
        this.magnitude = magnitude;
        this.level = level;
    }

    public Accessory() {
        this(0, 1);
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

    public Accessory innate(RegistryObject<Accessory> skill) {
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

    public void addTooltip(ItemStack itemStack, List<Component> tooltip, int size, boolean isShiftPressed, boolean first) {
        if (!this.isTooltipEnable()) return;
        if (this.isTooltipNameEnable()) {
            this.addTooltipTitle(itemStack, tooltip, first);
        }
        if (!this.isTooltipDescriptionEnable()) return;
        boolean flag = size == 1;
        boolean alwayShow = (BHConfigs.ADVANCED_TOOLTIP || BHConfigs.ADVANCED_TOOLTIP_ACCESSORY) && flag;
        if ((alwayShow || isShiftPressed) && I18n.exists(this.createId())) {
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

    protected String createId() {
        return createId(0);
    }

    protected void addTooltipTitle(ItemStack itemStack, List<Component> tooltip, boolean firstType) {
        Component text;
        text = this.spacing().append(Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.GOLD));
        tooltip.add(text);
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
                attributeinstance.addPermanentModifier(new AttributeModifier(attributemodifier.getId(), "Attribute Modifier", attributemodifier.getAmount(), attributemodifier.getOperation()));
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

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.attributeModifiers;
    }
    
    public Optional<IAccessoryEvent> IAccessory() {
        return Optional.empty();
    }
    public
    Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers() {
        return ImmutableMultimap.of();
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
}
