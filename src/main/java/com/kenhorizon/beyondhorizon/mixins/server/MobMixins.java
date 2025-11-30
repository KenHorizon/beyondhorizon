package com.kenhorizon.beyondhorizon.mixins.server;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixins extends LivingEntityMixins {
	@Shadow
	@Final
	public GoalSelector goalSelector;

	@Shadow
	public void setItemSlot(EquipmentSlot slotIn, ItemStack item) {
		throw new IllegalStateException("Mixin failed to shadow the \"Zombie.setItemSlot(...)\" method!");
	}
}
