package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AlwaysCrtiAttackSkill extends WeaponPassiveSkills {
    public AlwaysCrtiAttackSkill(float magnitude) {
        super(magnitude);
    }

    @Override
    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        list.add(Component.translatable(this.createId(), Maths.format(100.0F * this.getMagnitude())));
        return list;
    }

    @Override
    public float preMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target == null || attacker == null) return context.damage();
        if (attacker instanceof Player player) {
            PlayerData playerData = Capabilities.data(player);
            if (playerData != null) {
                playerData.setDoCrit(true);
            }
        } else {
            return context.multiply(this.getMagnitude());
        }
        return context.damage();
    }
}
