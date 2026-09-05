package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponPassiveSkills;
import com.kenhorizon.beyondhorizon.server.level.utils.AttributeUtils;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HeavyHitterSkill extends WeaponPassiveSkills {
    protected static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID BASE_ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    protected float attackBonus;
    public HeavyHitterSkill(float attackBonus, float magnitude) {
        super(magnitude);
        this.setMagnitude(magnitude);
        this.attackBonus = attackBonus;
    }

    @Override
    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        Player player = BeyondHorizon.PROXY.clientPlayer();
        var instance = player.getAttribute(Attributes.ATTACK_SPEED);
        double damageMultiplier = 0.0D;
        if (instance != null && instance.getModifier(BASE_ATTACK_SPEED_UUID) != null) {
            double stackAttributeAmount = instance.getModifier(BASE_ATTACK_SPEED_UUID).getAmount();
            double atkSpdBonuses = AttributeUtils.getBonus(player, Attributes.ATTACK_SPEED) + stackAttributeAmount;
            damageMultiplier = Maths.perValue(atkSpdBonuses, this.attackBonus, this.getMagnitude());
        }
        List<MutableComponent> list = new ArrayList<>();
        list.add(Component.translatable(this.createId(), Maths.format(100.0F * this.attackBonus), Maths.format(100.0F * this.getMagnitude())));
        list.add(Component.translatable(this.createId(1), Maths.format(100.0F * damageMultiplier)).withStyle(ChatFormatting.GOLD, ChatFormatting.UNDERLINE));
        return list;
    }

    @Override
    public float preMigitationDamage(float damageDealt, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target == null || attacker == null) return damageDealt;
        var instance = attacker.getAttribute(Attributes.ATTACK_SPEED);
        if (instance != null && instance.getModifier(BASE_ATTACK_SPEED_UUID) != null) {
            double stackAttributeAmount = instance.getModifier(BASE_ATTACK_SPEED_UUID).getAmount();
            double atkSpdBonuses = AttributeUtils.getBonus(attacker, Attributes.ATTACK_SPEED) + stackAttributeAmount;
            double damageMultipler = Maths.perValue(atkSpdBonuses, this.attackBonus, this.getMagnitude());
            float outputDamage = (float) (damageDealt + (damageDealt * damageMultipler));
            return outputDamage;
        }
        return damageDealt;
    }

}
