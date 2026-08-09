package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveConeAbility;
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

public class CelestialStrikeSkill extends WeaponPassiveSkills {
    public CelestialStrikeSkill(float magnitude) {
        super(magnitude);
    }

    @Override
    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        list.add(Component.translatable(this.createId(), Maths.format(100.0F * this.getMagnitude()), Maths.format(100.0F * this.getMagnitude())));
        return list;
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target == null || attacker == null) return damageDealt;
        if (attacker instanceof Player player) {
            CleaveConeAbility.spawn(player.level(), target, attacker, damageDealt * this.getMagnitude(), false, DamageType.PHYSICAL_DAMAGE);
        }
        return damageDealt;
    }
}
