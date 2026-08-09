package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GainBonusHealthAccessory extends AccessoryPassiveSkill {
    public enum Type {
        BASE,
        TOTAL
    }

    public Type gainType;
    public GainBonusHealthAccessory(float magnitude, Type gainType) {
        this.setMagnitude(magnitude);
        this.gainType = gainType;

    }

    @Override
    public void onUnequip(Player player, ItemStack itemStack, int slot) {
        this.removeAttributeModifiers(player, this.getAttributeModifiers());
    }

    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        double bonusHp = 0.0D;
        String tagName = String.format("bonus_hp_%s", this.getName());
        if (itemStack.hasTag() && itemStack.getTag().contains(tagName)) {
            bonusHp = itemStack.getTag().getDouble(tagName);
        }
        return Component.translatable(this.createId(), Maths.format(100.0F * this.getMagnitude()), Mth.ceil(bonusHp));
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack)
    {
        if (entity instanceof ServerPlayer serverSide) {
            updateHpBonus(this.getName(), serverSide, itemStack);
            switch (this.gainType) {
                case BASE -> {
                    this.addAttributes(Attributes.MAX_HEALTH,  this.getMagnitude(), AttributeModifier.Operation.MULTIPLY_BASE);
                }
                case TOTAL -> {
                    this.addAttributes(Attributes.MAX_HEALTH,  this.getMagnitude(), AttributeModifier.Operation.MULTIPLY_TOTAL);
                }
            }
        }
    }
    public static void updateHpBonus(String name, ServerPlayer player, ItemStack stack) {
        double maxHp = player.getMaxHealth();
        double percent = Constant.OVERGROWTH_BONUS_HEALTH;
        double bonus = maxHp * percent;
        String tagName = String.format("bonus_hp_%s", name);
        stack.getOrCreateTag().putDouble(tagName, bonus);
    }
}
