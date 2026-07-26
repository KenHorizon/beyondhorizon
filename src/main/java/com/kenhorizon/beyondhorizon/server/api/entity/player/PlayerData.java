package com.kenhorizon.beyondhorizon.server.api.entity.player;

import com.google.common.collect.Maps;
import com.kenhorizon.beyondhorizon.server.api.event.ManaDataEvent;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.level.utils.AttributeUtils;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundAbilityCooldownPacket;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundAbilityCooldownsPacket;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundManaSyncPacket;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundPlayerDataPacket;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.libs.server.world.CooldownInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PlayerData {
    public static final double MANA_DEDUCTION = 0.5D;
    public static final String NBT_MANA = "mana";
    public static final String NBT_CRIT = "crit";
    public static final String NBT_DO_CRIT = "do_crit"; // Force player to perform critical strike
    public static final String NBT_CANT_CRIT = "cant_crit"; // Ability to disable player critical strike
    public static final String NBT_ENTRY = "ability_cooldown";
    public static final String NBT_SLOT = "slot";
    public static final String NBT_ID = "id";
    public static final String NBT_COOLDOWN = "cooldown";
    public static final String NBT_CDR = "cdr";
    private final Map<String, CooldownInstance> skillManager;
    protected boolean cantCrit = false;
    protected boolean doCrit = true;
    protected boolean crit;
    protected boolean doDecut;
    protected double mana;
    public int tickManaDeduct = 0;
    private int tickBuffer = 0;
    protected final double manaDeduction = MANA_DEDUCTION;
    public Player player;
    public int tick;

    public PlayerData(Player player) {
        this.player = player;
        this.skillManager = Maps.newHashMap();
    }

    public void setTickBuffer(int tickBuffer) {
        this.tickBuffer = tickBuffer;
    }

    public void addMana(double amount) {
        this.setMana(this.getMana() + amount);
    }

    public void removeMana(double amount) {
        this.removeMana(amount, false);
    }

    public void removeMana(double amount, boolean doDecut) {
        this.doDecut = doDecut;
        amount *= this.getManaCostReduction();
        this.setMana(Math.max(0, this.getMana() - amount));
    }

    public void setMana(double mana) {
        if (this.player instanceof ServerPlayer splayer) {
            ManaDataEvent event = new ManaDataEvent(this.player, mana);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled()) return;
            this.mana = Mth.clamp(event.getAmount(), 0, this.getMaxMana());
            NetworkHandler.sendToPlayer(new ClientboundManaSyncPacket(this.getMana()), splayer);
        }
    }

    public float getManaCostReduction() {
        return (float) (1.0F - AttributeUtils.getTotal(this.player, BHAttributes.MANA_COST.get()));
    }

    public void setSyncMana(double mana) {
        this.mana = mana;
    }

    public double getMana() {
        return this.mana;
    }

    public void setCrit(boolean crit) {
        this.crit = crit;
    }

    public boolean isCrit() {
        return this.crit;
    }

    public void setDoCrit(boolean crit) {
        this.doCrit = crit;
    }

    public void setCantCrit(boolean cantCrit) {
        this.cantCrit = cantCrit;
    }

    public boolean isCantCrit() {
        return this.cantCrit;
    }

    public boolean isDoCrit() {
        return this.doCrit;
    }

    public double getMaxMana() {
        return this.player.getAttributeValue(BHAttributes.MAX_MANA.get());
    }

    public void regenMana(ServerPlayer player) {
        if (this.getMana() < this.getMaxMana()) {
            float value = (float) player.getAttributeValue(BHAttributes.MANA_REGENERATION.get());
            value *= (float) (this.doDecut ? this.manaDeduction : 1.0D);
            this.addMana(value);
        } else {
            this.setMana(this.getMaxMana());
        }
    }

    public boolean doRegenMana(Level level) {
        return level.getServer().getTickCount() % 10 == 0;
    }

    public void tick(Level level) {
        var spells = this.skillManager.entrySet().stream()
                .filter(x -> decrementCooldown(x.getValue(), 1)).toList();
        spells.forEach(spell -> {
            this.skillManager.remove(spell.getKey());
            this.onCooldownEnded(spell.getKey());
        });
        if (this.doDecut) {
            this.tickManaDeduct++;
            if (this.tickManaDeduct >= Maths.sec(3)) {
                this.doDecut = false;
                this.tickManaDeduct = 0;
            }
        }
        if (this.player instanceof ServerPlayer serverPlayer) {
            if (this.doRegenMana(level)) {
                this.regenMana(serverPlayer);
            }
        }
    }

    // Cooldown Mechanics
    public boolean isOnCooldown(String id) {
        return this.skillManager.containsKey(id);
    }

    public void addCooldown(String id, int cooldown) {
        int newCooldown = (int) (cooldown - (cooldown * this.player.getAttributeValue(BHAttributes.COOLDOWN.get())));
        this.skillManager.put(id, new CooldownInstance(newCooldown));
        this.onCooldownStarted(id, newCooldown);
    }

    public void addCooldown(String id, int cooldown, int cooldownReamining) {
        int newCooldown = (int) (cooldown - (cooldown * this.player.getAttributeValue(BHAttributes.COOLDOWN.get())));
        this.skillManager.put(id, new CooldownInstance(newCooldown, cooldownReamining));
        this.onCooldownStarted(id, newCooldown);
    }

    public float getCooldownPercent(String id) {
        return this.skillManager.getOrDefault(id, new CooldownInstance(0)).getCooldownPercent();
    }

    public int getCooldown(String id) {
        return this.skillManager.getOrDefault(id, new CooldownInstance(0)).getCooldown();
    }

    public void removeCooldown(String id) {
        this.skillManager.remove(id);
        this.syncCooldowns();
    }

    public boolean decrementCooldown(CooldownInstance instance, int amount) {
        instance.decrementBy(amount);
        return instance.getCooldownRemaining() <= this.tickBuffer;
    }

    public Map<String, CooldownInstance> getAllCooldowns() {
        return this.skillManager;
    }

    public void clearCooldowns() {
        this.skillManager.clear();
        this.syncCooldowns();
    }

    protected void onCooldownStarted(String id, int amount) {
        this.syncCooldown(id, amount);
    }

    protected void onCooldownEnded(String id) {
        this.syncCooldowns();
    }

    public void syncCooldown(String id, int duration) {
        if (this.player instanceof ServerPlayer sPlayer) {
            NetworkHandler.sendToPlayer(new ClientboundAbilityCooldownPacket(id, duration), sPlayer);
        }
    }

    public void syncCooldowns() {
        if (this.player instanceof ServerPlayer sPlayer) {
            NetworkHandler.sendToPlayer(new ClientboundAbilityCooldownsPacket(this.skillManager), sPlayer);
        }
    }

    public void syncData() {
        if (player instanceof ServerPlayer splayer) {
            NetworkHandler.sendToPlayer(new ClientboundPlayerDataPacket(this.saveNbt()), splayer);
        }
    }

    public CompoundTag saveNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble(NBT_MANA, this.getMana());
        nbt.putBoolean(NBT_CRIT, this.isCrit());
        nbt.putBoolean(NBT_DO_CRIT, this.isDoCrit());
        nbt.putBoolean(NBT_CANT_CRIT, this.isCantCrit());
        nbt.put(NBT_ENTRY, this.cooldownListTags());
        return nbt;
    }

    public void loadNbt(CompoundTag nbt) {
        this.setSyncMana(nbt.getDouble(NBT_MANA));
        this.setCrit(nbt.getBoolean(NBT_CRIT));
        this.setDoCrit(nbt.getBoolean(NBT_DO_CRIT));
        this.setCantCrit(nbt.getBoolean(NBT_CANT_CRIT));
        ListTag listTag = nbt.getList(NBT_ENTRY, 10);
        for (int k = 0; k < nbt.size(); ++k) {
            CompoundTag nbtData = listTag.getCompound(k);
            int slot = nbtData.getByte(NBT_SLOT) & 255;
            if (slot < skillManager.size()) {
                String id = nbtData.getString(NBT_ID);
                int abilityCooldown = nbtData.getInt(NBT_COOLDOWN);
                int cooldown = nbtData.getInt(NBT_CDR);
                this.skillManager.put(id, new CooldownInstance(abilityCooldown, cooldown));
            }
        }
    }

    private @NotNull ListTag cooldownListTags() {
        ListTag listTag = new ListTag();
        for (int i = 0; i < skillManager.size(); ++i) {
            for (Map.Entry<String, CooldownInstance> entry : skillManager.entrySet()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte(NBT_SLOT, (byte) i);
                tag.putString(NBT_ID, entry.getKey());
                tag.putInt(NBT_COOLDOWN, entry.getValue().getCooldown());
                tag.putInt(NBT_CDR, entry.getValue().getCooldownRemaining());
                listTag.add(tag);
            }
        }
        return listTag;
    }

    /**
     * Check if player holding item in mainhand or offhand
     * it the mainhand if empty will go in offhand, vice versa in offhand
     * if both hand are empty the return is empty
     * */
    public static ItemStack getHeldingItem(Player player) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack heldStacks;
            if (slot == EquipmentSlot.MAINHAND && player.getOffhandItem().isEmpty()) {
                heldStacks = player.getItemBySlot(slot);
                return heldStacks;
            }
            if (slot == EquipmentSlot.OFFHAND && player.getMainHandItem().isEmpty()) {
                heldStacks = player.getItemBySlot(slot);
                return heldStacks;
            }
        }
        return ItemStack.EMPTY;
    }
}
