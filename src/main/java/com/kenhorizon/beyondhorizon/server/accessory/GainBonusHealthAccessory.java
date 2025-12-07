package com.kenhorizon.beyondhorizon.server.accessory;

import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class GainBonusHealthAccessory extends AccessorySkill {
    public enum Type {
        BASE,
        TOTAL
    }
    public Type gainType;
    public GainBonusHealthAccessory(float magnitude, Type gainType) {
        this.setMagnitude(magnitude);
        this.gainType = gainType;
        switch (gainType) {
            case BASE -> {
                this.addAttributeModifier(Attributes.MAX_HEALTH, "dbb7fedf-a109-4a53-af6f-72ce8847f0b6", magnitude, AttributeModifier.Operation.MULTIPLY_BASE);
            }
            case TOTAL -> {
                this.addAttributeModifier(Attributes.MAX_HEALTH, "dbb7fedf-a109-4a53-af6f-72ce8847f0b6", magnitude, AttributeModifier.Operation.MULTIPLY_TOTAL);
            }
        }
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        double bonusHp = 0.0D;
        String tagName = String.format("bonus_hp_%s", this.getName());
        if (itemStack.hasTag() && itemStack.getTag().contains(tagName)) {
            bonusHp = itemStack.getTag().getDouble(tagName);
        }
        return Component.translatable(this.createId(), Maths.format0(this.getMagnitude()), Mth.ceil(bonusHp));
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (entity instanceof ServerPlayer serverSide) {
            updateHpBonus(this.getName(), serverSide, itemStack);
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
