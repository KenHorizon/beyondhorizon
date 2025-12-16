package com.kenhorizon.beyondhorizon.server.api.skills;

import com.google.common.collect.Maps;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundActiveSkillSyncPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.checkerframework.checker.units.qual.C;

import java.util.Map;

public class ActiveSkill {
    public static final String COOLDOWN = "cooldown";
    public static final String CDR = "cdr";
    private final Map<ResourceLocation, CooldownInstance> cooldownManager;

    public ActiveSkill() {
        this.cooldownManager = Maps.newHashMap();
    }

    public boolean isOnCooldown(ResourceLocation id) {
        return this.cooldownManager.containsKey(id);
    }

    public void addCooldown(ResourceLocation id, int cooldown, int cooldownRemaining) {
        this.cooldownManager.put(id, new CooldownInstance(cooldown, cooldownRemaining));
    }

    public float getCooldownPercent(ResourceLocation id) {
        return this.cooldownManager.getOrDefault(id, new CooldownInstance(0)).getCooldownPercent();
    }

    public int getCooldown(ResourceLocation id) {
        return this.cooldownManager.getOrDefault(id, new CooldownInstance(0)).getCooldownRemaining();
    }

    public void removeCooldown(ResourceLocation id) {
        this.cooldownManager.remove(id);
    }

    public void addCooldown(ResourceLocation id, int cooldown) {
        this.cooldownManager.put(id, new CooldownInstance(cooldown));
    }

    public void tick(Player player) {
        var activeSpells = this.cooldownManager.entrySet().stream().filter(entry -> {
            return this.decrementCooldown(entry.getValue(), 1);
        }).toList();
        activeSpells.forEach(entry -> {
            this.cooldownManager.remove(entry.getKey());
        });
        if (player instanceof ServerPlayer serverPlayer) {
            this.syncToPlayer(serverPlayer);
        }
    }

    public boolean decrementCooldown(CooldownInstance instance, int amount) {
        instance.decrementBy(amount);
        return instance.getCooldownRemaining() <= 0;
    }

    public Map<ResourceLocation, CooldownInstance> getCooldownManager() {
        return this.cooldownManager;
    }

    public void clearCooldown(ResourceLocation id) {
        this.cooldownManager.remove(id);
    }

    public void syncToPlayer(ServerPlayer player) {
        NetworkHandler.sendToPlayer(new ClientboundActiveSkillSyncPacket(this.saveNbt()), player);
    }

    public CompoundTag saveNbt() {
        CompoundTag nbt = new CompoundTag();
//        nbt.putInt(COOLDOWN, );
//        nbt.putInt(CDR, );
        return nbt;
    }

    public void loadNbt(CompoundTag nbt) {

    }


    public static class CooldownInstance {
        private int cooldownRemaining;
        private final int cooldown;

        public CooldownInstance(int cooldown) {
            this.cooldown = cooldown;
            this.cooldownRemaining = cooldown;
        }

        public CooldownInstance(int cooldown, int cooldownRemaining) {
            this.cooldown = cooldown;
            this.cooldownRemaining = cooldownRemaining;
        }

        public void decrement() {
            cooldownRemaining--;
        }

        public void decrementBy(int amount) {
            cooldownRemaining -= amount;
        }

        public int getCooldownRemaining() {
            return cooldownRemaining;
        }

        public int getCooldown() {
            return cooldown;
        }

        public float getCooldownPercent() {
            if (cooldownRemaining == 0) {
                return 0;
            }
            return cooldownRemaining / (float) cooldown;
        }
    }
}
