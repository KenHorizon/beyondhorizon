package com.kenhorizon.beyondhorizon.server.accessory;

import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.beyondhorizon.client.level.tooltips.Tooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
        tooltip.add(this.spacing().append(Component.translatable(this.createId(), Maths.FORMAT.format(this.getMagnitude()), Mth.ceil(bonusHp)).withStyle(Tooltips.TOOLTIP[0])));
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
