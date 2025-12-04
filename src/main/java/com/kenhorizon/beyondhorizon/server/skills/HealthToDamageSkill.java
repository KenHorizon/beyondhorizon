package com.kenhorizon.beyondhorizon.server.skills;

import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class HealthToDamageSkill extends WeaponSkills {
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("503acee5-96e0-400d-85ae-5549f60ad64c");

    public HealthToDamageSkill(float magnitude) {
        this.setMagnitude(magnitude);
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        double bonusAttackDamage = 0.0D;
        if (itemStack.hasTag() && itemStack.getTag().contains("bonus_damage_tranny")) {
            bonusAttackDamage = itemStack.getTag().getDouble("bonus_damage_tranny");
        }
        return Component.translatable(this.createId(), Maths.format(this.getMagnitude()), Mth.ceil(bonusAttackDamage));
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (entity instanceof ServerPlayer serverPlayer) {
            updateBonusAttackDamage(this.getMagnitude(), serverPlayer, itemStack);
        }
        double maxHp = entity.getMaxHealth();
        double bonusAttackDamage = maxHp * (double) this.getMagnitude();
        AttributeInstance attribute = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        addModifiers((float) bonusAttackDamage, attribute);
    }

    @Override
    public void onChangeEquipment(LivingEntity entity, ItemStack itemStack, boolean hasChanged) {
        AttributeInstance attribute = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (hasChanged) {
            attribute.removeModifier(ATTACK_DAMAGE_UUID);
        }
    }

    public static void updateBonusAttackDamage(float magnitude, ServerPlayer player, ItemStack stack) {
        double maxHp = player.getMaxHealth();
        double bonus = maxHp * (double) magnitude;
        stack.getOrCreateTag().putDouble("bonus_damage_tranny", bonus);
    }

    private void addModifiers(float damage, AttributeInstance instance) {
        AttributeModifier modifier = new AttributeModifier(ATTACK_DAMAGE_UUID, "Bonus Attack Damage", damage, AttributeModifier.Operation.ADDITION);
        AttributeModifier prevModifier = instance.getModifier(ATTACK_DAMAGE_UUID);
        if (prevModifier == null) {
            instance.addTransientModifier(modifier);
        } else if (prevModifier.getAmount() != damage) {
            instance.removeModifier(ATTACK_DAMAGE_UUID);
            instance.addTransientModifier(modifier);
        }
    }
}
