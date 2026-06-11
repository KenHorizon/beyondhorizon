package com.kenhorizon.beyondhorizon.server.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.level.BlockEvent;

import java.util.List;

public class DamageTiltEvent extends LivingEvent {
    public DamageTiltEvent(LivingEntity entity) {
        super(entity);
    }

    public boolean isCancelDamageTilt() {
        return this.getEntity().hurtDuration <= 0;
    }
}
