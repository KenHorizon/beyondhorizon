package com.kenhorizon.beyondhorizon.server.accessory;

import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Tooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

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
    protected void addTooltipDescription(ItemStack itemStack, List<Component> tooltip) {
        double bonusHp = 0;
        if (itemStack.hasTag() && itemStack.getTag().contains("bonus_hp")) {
            bonusHp = itemStack.getTag().getDouble("bonus_hp");

        }
        tooltip.add(Component.translatable(this.createId(), this.getMagnitude() * 100.0F, bonusHp).withStyle(Tooltips.TOOLTIP[1]));

    }

    @Override
    public void onEntityUpdate(Player player, ItemStack itemStack) {
        if (player instanceof ServerPlayer serverSide) {
            updateHpBonus(serverSide, itemStack);
        }
    }

    public static void updateHpBonus(ServerPlayer player, ItemStack stack) {
        double maxHp = player.getMaxHealth();
        double percent = Constant.OVERGROWTH_BONUS_HEALTH;
        double bonus = maxHp * percent;

        stack.getOrCreateTag().putDouble("bonus_hp", bonus);
    }
}
