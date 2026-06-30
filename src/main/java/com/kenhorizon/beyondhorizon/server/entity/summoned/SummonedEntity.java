package com.kenhorizon.beyondhorizon.server.entity.summoned;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public interface SummonedEntity {

    LivingEntity getSummoner();

    void onUnSummon();


    default boolean isAlliedHelper(Entity entity) {
        if (this.getSummoner() == null) return false;
        boolean isFellowSummon = entity == this.getSummoner() || entity.isAlliedTo(this.getSummoner());
        boolean hasCommonOwner = entity instanceof OwnableEntity ownableEntity && ownableEntity.getOwner() == this.getSummoner();
        return isFellowSummon || hasCommonOwner;
    }

    default void onDeathHelper() {
        if (this instanceof LivingEntity entity) {
            Level level = entity.level();
            var deathMessage = entity.getCombatTracker().getDeathMessage();
            if (!level.isClientSide() && level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && getSummoner() instanceof ServerPlayer player) {
                player.sendSystemMessage(deathMessage);
            }
        }
    }
}
