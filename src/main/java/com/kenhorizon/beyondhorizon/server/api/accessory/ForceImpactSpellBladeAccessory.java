package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundAcessoryKeyPacket;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ForceImpactSpellBladeAccessory extends BaseSpellbladeAccessory {

    public ForceImpactSpellBladeAccessory(int attackInterval, float attackScale) {
        super(attackInterval, attackScale, DamageType.PHYSICAL);
    }

    @Override
    protected float spellBladeDamage(LivingEntity attacker, float damage, float damageScale) {
        return (float) (attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * damageScale);
    }

    @Override
    protected String spellBladeTag() {
        return "force_impact_spellblade";
    }
}
