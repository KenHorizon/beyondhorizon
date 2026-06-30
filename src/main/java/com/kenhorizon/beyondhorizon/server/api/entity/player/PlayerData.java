package com.kenhorizon.beyondhorizon.server.api.entity.player;

import com.google.common.collect.Maps;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundAbilityCooldownPacket;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundPlayerDataSyncPacket;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.libs.server.world.CooldownInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PlayerData {
    public static double MANA_DEDUCTION = 0.5D;
    public static String NBT_MANA = "mana";
    public static String NBT_CRIT = "crit";
//
    public static final String NBT_ENTRY = "ability_cooldown";
    public static final String NBT_SLOT = "slot";
    public static final String NBT_ID = "id";
    public static final String NBT_COOLDOWN = "cooldown";
    public static final String NBT_CDR = "cdr";
    //
    public static final String NBT_ABILITIES = "ability_skill";
    public static final String NBT_ITEM_TEMP_ID = "temp_id";
    private final Map<String, CooldownInstance> skillManager;

    protected boolean crit;
    protected boolean doDecut;
    protected double mana;
    public int tickManaDeduct = 0;
    protected final double manaDeduction = MANA_DEDUCTION;
    public Player player;
    public int tick;

    public PlayerData(Player player) {
        this.player = player;
        this.skillManager = Maps.newHashMap();
    }

    public void addMana(int amount) {
        this.mana += amount;
    }

    public void syncPlayerData(Player player) {
        if (player instanceof ServerPlayer sPlayer) {
            NetworkHandler.sendToPlayer(new ClientboundPlayerDataSyncPacket(this.saveNbt()), sPlayer);
        }
    }

    public void removeMana(int amount) {
        this.removeMana(amount, false);
    }

    public void removeMana(int amount, boolean doDecut) {
        this.doDecut = doDecut;
        this.mana = Math.max(0, this.mana - amount);
    }

    public void setMana(double mana) {
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

    public void setDefaults() {
        this.mana = this.player.getAttributeBaseValue(BHAttributes.MAX_MANA.get());
    }

    public double getMaxMana() {
        return this.player.getAttributeValue(BHAttributes.MAX_MANA.get());
    }

    public void regenMana(ServerPlayer player) {
        if (this.getMana() < this.getMaxMana()) {
            float value = (float) player.getAttributeValue(BHAttributes.MANA_REGENERATION.get());
            value *= (float) (this.doDecut ? this.manaDeduction : 1.0D);
            this.addMana((int) value);
        } else {
            this.setMana(this.getMaxMana());
        }
    }

    public boolean doRegenMana(Level level) {
        return level.getServer().getTickCount() % 10 == 0;
    }

    // Cooldown Mechanics
    public boolean isOnCooldown(String id) {
        return this.skillManager.containsKey(id);
    }

    public void addCooldown(String id, int cooldown) {
        int newCooldown = (int) (cooldown - (cooldown * this.player.getAttributeValue(BHAttributes.COOLDOWN.get())));
        this.skillManager.put(id, new CooldownInstance(newCooldown));
        this.onCooldownStarted(id);
    }

    public void addCooldown(String id, int cooldown, int cooldownReamining) {
        int newCooldown = (int) (cooldown - (cooldown * this.player.getAttributeValue(BHAttributes.COOLDOWN.get())));
        this.skillManager.put(id, new CooldownInstance(newCooldown, cooldownReamining));
    }

    public float getCooldownPercent(String id) {
        return this.skillManager.getOrDefault(id, new CooldownInstance(0)).getCooldownPercent();
    }

    public int getCooldown(String id) {
        return this.skillManager.getOrDefault(id, new CooldownInstance(0)).getCooldownRemaining();
    }

    public void removeCooldown(String id) {
        this.skillManager.remove(id);
        this.syncCooldown();
    }

    public boolean decrementCooldown(CooldownInstance instance, int amount) {
        instance.decrementBy(amount);
        return instance.getCooldownRemaining() <= 0;
    }

    public Map<String, CooldownInstance> getAllCooldowns() {
        return this.skillManager;
    }

    public void clearCooldowns() {
        this.skillManager.clear();
        this.syncCooldown();
    }

    protected void onCooldownStarted(String id) {
        this.syncCooldown();
    }

    protected void onCooldownEnded(String id) {
        this.syncCooldown();
    }

    public void syncCooldown() {
        if (this.player instanceof ServerPlayer sPlayer) {
            NetworkHandler.sendToPlayer(new ClientboundAbilityCooldownPacket(this.skillManager), sPlayer);
        }
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
        this.syncPlayerData(player);
    }

    public CompoundTag saveNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble(NBT_MANA, this.getMana());
        nbt.putBoolean(NBT_CRIT, this.isCrit());
        nbt.put(NBT_ENTRY, this.cooldownListTags());
        return nbt;
    }

    public void loadNbt(CompoundTag nbt) {
        this.setMana(nbt.getDouble(NBT_MANA));
        this.setCrit(nbt.getBoolean(NBT_CRIT));
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
    public static PlayerData getInstance(Player player) {
        return Capabilities.data(player);
    }
}
