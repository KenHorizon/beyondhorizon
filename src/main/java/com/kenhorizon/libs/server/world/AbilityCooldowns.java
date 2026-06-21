package com.kenhorizon.libs.server.world;

import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AbilityCooldowns implements INBTSerializable<CompoundTag> {
    public static final String NBT_ENTRY = "ability";
    public static final String NBT_SLOT = "slot";
    public static final String NBT_ID = "id";
    public static final String NBT_COOLDOWN = "cooldown";
    public static final String NBT_CDR = "cdr";
    private final Map<String, CooldownInstance> skillManager;

    public AbilityCooldowns() {
        this.skillManager = Maps.newHashMap();
    }

    public boolean isOnCooldown(String id) {
        return this.skillManager.containsKey(id);
    }

    public void addCooldown(String id, int cooldown) {
        this.skillManager.put(id, new CooldownInstance(cooldown));
        this.onCooldownStarted(id);
    }

    public void addCooldown(String id, int cooldown, int cooldownReamining) {
        this.skillManager.put(id, new CooldownInstance(cooldown, cooldownReamining));
    }

    public float getCooldownPercent(String id) {
        return this.skillManager.getOrDefault(id, new CooldownInstance(0)).getCooldownPercent();
    }

    public int getCooldown(String id) {
        return this.skillManager.getOrDefault(id, new CooldownInstance(0)).getCooldownRemaining();
    }

    public void removeCooldown(String id) {
        this.skillManager.remove(id);
    }

    public void tick() {
        var spells = this.skillManager.entrySet().stream()
                .filter(x -> decrementCooldown(x.getValue(), 1)).toList();
        spells.forEach(spell -> {
            this.skillManager.remove(spell.getKey());
            this.onCooldownEnded(spell.getKey());
        });
    }

    public boolean decrementCooldown(CooldownInstance instance, int amount) {
        instance.decrementBy(amount);
        return instance.getCooldownRemaining() <= 0;
    }

    public Map<String, CooldownInstance> getSkillManager() {
        return this.skillManager;
    }

    public void clearCooldown() {
        this.skillManager.clear();
    }

    protected void onCooldownStarted(String id) {
    }

    protected void onCooldownEnded(String id) {
    }

    public void syncToPlayer(ServerPlayer player) {
//        NetworkHandler.sendToPlayer(new ClientboundSkillCooldown(this.skillManager), player);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag listTag = getTags();
        nbt.put(NBT_ENTRY, listTag);
        return nbt;
    }

    private @NotNull ListTag getTags() {
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

    @Override
    public void deserializeNBT(CompoundTag nbt) {
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
}
