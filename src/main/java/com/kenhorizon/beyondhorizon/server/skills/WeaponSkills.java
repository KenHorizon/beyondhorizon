package com.kenhorizon.beyondhorizon.server.skills;

import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IItemGeneric;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class WeaponSkills extends Skill implements IAttack, IItemGeneric {
    private float magnitude;
    private float level;

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        if (this.getMagnitude() > 0.0F && this.getLevel() > 0.0F) {
            return Component.translatable(this.createId(), Maths.format0(this.getMagnitude()), this.getLevel());
        } else {
            return Component.translatable(this.createId(), Maths.format0(this.getMagnitude()));
        }
    }

    @Override
    public Optional<IAttack> IAttackCallback() {
        return Optional.of(this);
    }

    @Override
    public Optional<IItemGeneric> IItemGeneric() {
        return Optional.of(this);
    }

    public float getMagnitude() {
        return magnitude;
    }

    public float getLevel() {
        return level;
    }
    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }
    public void setLevel(float level) {
        this.level = level;
    }
}
