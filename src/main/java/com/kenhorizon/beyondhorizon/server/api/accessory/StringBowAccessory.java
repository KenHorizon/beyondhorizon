package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class StringBowAccessory extends AccessorySkill {
    public enum Type {
        LIGHT,
        HEAVY
    }
    protected Type type;

    public StringBowAccessory(Type type) {
        this.type = type;
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        if (this.type == Type.HEAVY) {
            return Component.translatable(this.createId(), MathUtils.format0(Constant.HEAVY_STRING_DAMAGE * 100.0F));
        } else {
            return super.tooltipDescription(itemStack);
        }
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (attacker == null || target == null) return damageDealt;
        if (this.type == Type.HEAVY) {
            target.knockback(1.0D + Constant.HEAVY_STRING_DAMAGE, target.getX(), target.getZ());
            float damageMult = Constant.HEAVY_STRING_DAMAGE;
            return damageDealt + (damageDealt * damageMult);
        }
        return damageDealt;
    }

    @Override
    public int modifyRangedWeaponUseTime(ItemStack itemStack, int duration) {
        if (itemStack.is(BHItemTags.BOWS) && this.type == Type.LIGHT) {
            return (duration % (4) == 0 ? 1 : 0);
        }
        return 0;
    }
}
