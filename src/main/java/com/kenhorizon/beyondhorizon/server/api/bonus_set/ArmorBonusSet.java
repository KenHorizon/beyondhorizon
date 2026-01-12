package com.kenhorizon.beyondhorizon.server.api.bonus_set;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public abstract class ArmorBonusSet implements ArmorSet {
    public static final String PREFIX = "armor_bonus_set";
    protected final ItemStack head;
    protected final ItemStack chestplate;
    protected final ItemStack leggings;
    protected final ItemStack boots;
    protected final ResourceLocation id;
    protected final int tooltipLines;

    public static final Map<UUID, Set<ResourceLocation>> ACTIVE_SETS = new HashMap<>();

    public ArmorBonusSet(ResourceLocation id, int tooltipLines, ItemStack head, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this.id = id;
        this.tooltipLines = tooltipLines;
        this.head = head;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    @Override
    public boolean matches(Player player) {
        boolean var0 = player.getItemBySlot(EquipmentSlot.HEAD).is(this.head.getItem());
        boolean var1 = player.getItemBySlot(EquipmentSlot.CHEST).is(this.chestplate.getItem());
        boolean var2 = player.getItemBySlot(EquipmentSlot.LEGS).is(this.leggings.getItem());
        boolean var3 = player.getItemBySlot(EquipmentSlot.FEET).is(this.boots.getItem());
        return var0 && var1 && var2 && var3;
    }

    @Override
    public void applyBonus(Player player) {
        BeyondHorizon.LOGGER.error("applied!!");
    }

    @Override
    public void removeBonus(Player player) {
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public int tooltipLines() {
        return this.tooltipLines;
    }

    public void addTooltips(List<Component> tooltips, ItemStack itemStack, Player player) {
        tooltips.add(CommonComponents.space());
        tooltips.add(CommonComponents.space());
        tooltips.add(Component.translatable(Tooltips.TOOLTIP_BONUS_ARMOR_SET).withStyle(ChatFormatting.DARK_AQUA));
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
    public int countPieces(Player player) {
        int count = 0;
        if (player.getItemBySlot(EquipmentSlot.HEAD).is(this.head.getItem())) count++;
        if (player.getItemBySlot(EquipmentSlot.CHEST).is(this.chestplate.getItem())) count++;
        if (player.getItemBySlot(EquipmentSlot.LEGS).is(this.leggings.getItem())) count++;
        if (player.getItemBySlot(EquipmentSlot.FEET).is(this.boots.getItem())) count++;
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
    public ArmorBonusSet getInstance() {
        return this;
    }
}
