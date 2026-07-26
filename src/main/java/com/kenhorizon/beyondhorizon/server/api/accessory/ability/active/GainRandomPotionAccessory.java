package com.kenhorizon.beyondhorizon.server.api.accessory.ability.active;

import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryActiveSkill;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GainRandomPotionAccessory extends AccessoryActiveSkill {

    private List<MobEffect> effectList = new ArrayList<>();
    private int potionLevel = 0;
    public GainRandomPotionAccessory(List<MobEffect> effectList, int potionLevel) {
        this.potionLevel = potionLevel;
        this.effectList = effectList;
        this.setCooldown(Maths.sec(30));
    }

    public GainRandomPotionAccessory(int potionLevel) {
        this(List.of(MobEffects.DAMAGE_BOOST, MobEffects.MOVEMENT_SPEED, MobEffects.JUMP, MobEffects.ABSORPTION), potionLevel);
    }

    @Override
    public void onActiveAbility(Player player, ItemStack itemStack) {
        MobEffect effect = this.effectList.get(player.getRandom().nextInt(this.effectList.size()));
        player.addEffect(new MobEffectInstance(effect, Maths.sec(30), this.potionLevel, true, true, true));
    }
}
