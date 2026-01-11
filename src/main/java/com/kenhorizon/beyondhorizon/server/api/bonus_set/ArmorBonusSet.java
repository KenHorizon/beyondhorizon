package com.kenhorizon.beyondhorizon.server.api.bonus_set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class ArmorBonusSet implements ArmorSet{
    protected final ItemStack head;
    protected final ItemStack chestplate;
    protected final ItemStack leggings;
    protected final ItemStack boots;
    protected final ResourceLocation id;

    private final Consumer<Player> apply;
    private final Consumer<Player> remove;

    public static final Map<UUID, Set<ResourceLocation>> ACTIVE_SETS = new HashMap<>();

    public ArmorBonusSet(ResourceLocation id, ItemStack head, ItemStack chestplate, ItemStack leggings, ItemStack boots, Consumer<Player> apply, Consumer<Player> remove) {
        this.id = id;
        this.head = head;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.apply = apply;
        this.remove = remove;
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
        this.apply.accept(player);
    }

    @Override
    public void removeBonus(Player player) {
        this.remove.accept(player);
    }

    @Override
    public ResourceLocation getId() {
        return null;
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
}
