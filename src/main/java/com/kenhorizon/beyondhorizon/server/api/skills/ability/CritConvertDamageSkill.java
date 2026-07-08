package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CritConvertDamageSkill extends WeaponPassiveSkills {
    public float scale;
    public float min;
    public float max;
    public CritConvertDamageSkill(float min, float max, float scale) {
        super();
        this.scale = scale;
        this.min = min;
        this.max = max;
    }

    @Override
    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        list.add(Component.translatable(this.createId(), Maths.format0(this.getMagnitude())));
        return list;
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (entity instanceof Player player) {
            PlayerData playerData = Capabilities.data(player);
            if (playerData != null) {
                playerData.setCantCrit(true);
            }
        }
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        if (target == null || attacker == null) return;
        target.invulnerableTime = 0;
        var instance = attacker.getAttributes();
        if (instance.hasAttribute(BHAttributes.CRITICAL_CHANCE.get())) {
            float crit = (float) attacker.getAttributeValue(BHAttributes.CRITICAL_CHANCE.get());
            if (crit >= 1.0F) {
                crit = 1.0F;
            }
            float damage = (float) Maths.perValue(crit, 0.01D, this.scale);
            DamageType.MAGIC_DAMAGE.dealDamage(target, attacker, damage);
        }
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target == null || attacker == null) return damageDealt;
        if (attacker instanceof Player player) {
            PlayerData playerData = Capabilities.data(player);
            if (playerData != null) {
                playerData.setDoCrit(true);
            }
        } else {
            return damageDealt * this.getMagnitude();
        }
        return damageDealt;
    }
}
