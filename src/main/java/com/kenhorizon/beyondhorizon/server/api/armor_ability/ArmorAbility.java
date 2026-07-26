package com.kenhorizon.beyondhorizon.server.api.armor_ability;

import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.api.IAttack;
import com.kenhorizon.beyondhorizon.server.api.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

public abstract class ArmorAbility implements IArmorAbility {
    protected ItemStack head;
    protected ItemStack chestplate;
    protected ItemStack leggings;
    protected ItemStack boots;
    @Nullable
    protected String descriptionId;

    public static final Map<UUID, Set<ResourceLocation>> ACTIVE_SETS = new HashMap<>();

    public ArmorAbility itemHead(ItemStack head) {
        this.head = head;
        return this;
    }
    public ArmorAbility itemBody(ItemStack chestplate) {
        this.chestplate = chestplate;
        return this;
    }
    public ArmorAbility itemLeggings(ItemStack leggings) {
        this.leggings = leggings;
        return this;
    }
    public ArmorAbility itemBoots(ItemStack boots) {
        this.boots = boots;
        return this;
    }

    public String getName() {
        return BHRegistries.ARMOR_ABILITY_KEY.get().getKey(this).getPath();
    }

    public String getId() {
        return BHRegistries.ARMOR_ABILITY_KEY.get().getKey(this).getNamespace();
    }

    public ResourceLocation getResourceId() {
        return BHRegistries.ARMOR_ABILITY_KEY.get().getKey(this);
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = String.format("armor_ability.%s.%s", this.getId(), this.getName());
        }
        return this.descriptionId;
    }

    @Override
    public boolean matches(LivingEntity entity) {
        boolean var0 = entity.getItemBySlot(EquipmentSlot.HEAD).is(this.head.getItem());
        boolean var1 = entity.getItemBySlot(EquipmentSlot.CHEST).is(this.chestplate.getItem());
        boolean var2 = entity.getItemBySlot(EquipmentSlot.LEGS).is(this.leggings.getItem());
        boolean var3 = entity.getItemBySlot(EquipmentSlot.FEET).is(this.boots.getItem());
        return var0 && var1 && var2 && var3;
    }

    @Override
    public void applyBonus(LivingEntity entity) {
    }

    @Override
    public void removeBonus(LivingEntity entity) {
    }

    public void addTooltips(List<Component> tooltips, ItemStack itemStack, Player player) {
        tooltips.add(CommonComponents.space());
        tooltips.add(Component.translatable(Tooltips.FULL_BONUS_ARMOR_SET).append(CommonComponents.space()).append(Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.DARK_AQUA)));
        this.addTooltipPerPiece(tooltips, itemStack, player);
        this.addTooltipFullBonusSet(tooltips, itemStack, player);
    }
    public void addTooltipPerPiece(List<Component> tooltips, ItemStack itemStack, Player player) {

    }

    public void addTooltipFullBonusSet(List<Component> tooltips, ItemStack itemStack, Player player) {

    }

    protected String createId(int lines) {
        return lines == 0 ? String.format("%s.desc", this.getDescriptionId()) : String.format("%s.desc.%s", this.getDescriptionId(), lines);
    }

    protected String createId() {
        return createId(0);
    }

    protected MutableComponent space() {
        return Component.literal("  ");
    }

    public boolean contains(ItemStack itemStack) {
        return this.getHead().getItem() == itemStack.getItem() ||
                this.getChestplate().getItem() == itemStack.getItem() ||
                this.getLeggings().getItem() == itemStack.getItem() ||
                this.getBoots().getItem() == itemStack.getItem();
    }

    @Override
    public int countPieces(LivingEntity entity) {
        int count = 0;
        if (entity.getItemBySlot(EquipmentSlot.HEAD).is(this.head.getItem())) count++;
        if (entity.getItemBySlot(EquipmentSlot.CHEST).is(this.chestplate.getItem())) count++;
        if (entity.getItemBySlot(EquipmentSlot.LEGS).is(this.leggings.getItem())) count++;
        if (entity.getItemBySlot(EquipmentSlot.FEET).is(this.boots.getItem())) count++;
        return count;
    }

    public ItemStack getHead() {
        return head;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public Optional<IAttack> attack() {
        return Optional.empty();
    }

    public Optional<IEntityProperties> entityProperties() {
        return Optional.empty();
    }

    @Override
    public ArmorAbility getInstance() {
        return this;
    }
}
