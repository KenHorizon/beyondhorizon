package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

public class StringBowAccessory extends AccessorySkill {
    public enum Type {
        LIGHT,
        HEAVY
    }
    protected final Type type;

    public StringBowAccessory(Type type) {
        this.type = type;
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        if (this.type == Type.HEAVY) {
            return Component.translatable(this.createId(), MathUtils.format0(Constant.HEAVY_STRING_DAMAGE * 10.0F), MathUtils.format0(Constant.HEAVY_STRING_KNOCKBACK * 10.0F));
        } else {
            return super.tooltipDescription(itemStack);
        }
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        if (attacker == null || target == null) return;
        if (damageSource.is(DamageTypes.ARROW) && this.getType() == Type.HEAVY) {
            double d0 = attacker.getX() - target.getX();
            double d1;
            for(d1 = attacker.getZ() - target.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                d0 = (Math.random() - Math.random()) * 0.01D;
            }
            target.knockback(0.4D * (1.0D + Constant.HEAVY_STRING_KNOCKBACK), d0, d1);
            float damageMult = Constant.HEAVY_STRING_DAMAGE;
            target.invulnerableTime = 0;
            target.hurt(BHDamageTypes.physicalDamage(target, attacker), damageDealt * damageMult);
        }
    }

    @Override
    public int modifyRangedWeaponUseTime(ItemStack itemStack, int duration) {
        if ((itemStack.is(BHItemTags.BOWS) || itemStack.getItem() instanceof BowItem) && this.getType() == Type.LIGHT) {
            return (duration % (4) == 0 ? 1 : 0);
        }
        return 0;
    }

    public Type getType() {
        return type;
    }
}
