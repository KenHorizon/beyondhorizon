package com.kenhorizon.beyondhorizon.server.api.entity.player;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundPlayerDataSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayerData {
    public static String NBT_MANA = "Mana";
    public static String NBT_CRIT = "Crit";
    public static String NBT_COMBAT_STANCE = "CombatStance";
    protected boolean combatStance;
    protected boolean crit;
    protected double mana;
    public Player player;
    public int tick;

    public PlayerData(Player player) {
        this.player = player;
    }

    public void addMana(int amount) {
        this.mana += amount;
    }

    public void removeMana(int amount) {
        this.mana = Math.max(0, amount);
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
            this.addMana((int) value);
        } else {
            this.setMana(this.getMaxMana());
        }
    }

    public boolean doRegenMana(Level level) {
        return level.getServer().getTickCount() % 20 == 0;
    }

    public void tick(Level level) {
        if (this.player instanceof ServerPlayer serverPlayer) {
            if (this.doRegenMana(level)) {
                this.regenMana(serverPlayer);
            }
            NetworkHandler.sendToPlayer(new ClientboundPlayerDataSyncPacket(this.saveNbt()), serverPlayer);
        }
    }

    public CompoundTag saveNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble(NBT_MANA, this.getMana());
        nbt.putBoolean(NBT_CRIT, this.isCrit());
        return nbt;
    }

    public void loadNbt(CompoundTag nbt) {
        this.setMana(nbt.getDouble(NBT_MANA));
        this.setCrit(nbt.getBoolean(NBT_CRIT));
    }
}
