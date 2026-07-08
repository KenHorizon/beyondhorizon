package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ExecuteAbilityAccessory extends AccessoryPassiveSkill {
    private float healthThereshold;

    public ExecuteAbilityAccessory(float healthThereshold) {
        this.healthThereshold = healthThereshold;
    }

    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format0(this.healthThereshold));
    }

    public float getHealthExecute() {
        return this.healthThereshold;
    }


    @Override
    public float postMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        if (target.getHealth() <= (target.getMaxHealth() * this.getHealthExecute())) {
            attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), BHSounds.ENTITY_EXECUTED.get(), SoundSource.MASTER, 1.0F, 1.0F);
            return damageDealt * target.getMaxHealth();
        }
        return damageDealt;
    }
}
