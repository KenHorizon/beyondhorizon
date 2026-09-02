package com.kenhorizon.beyondhorizon.server.api.skills.ability.active;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.api.skills.ability.AbstractDeathRaySkill;
import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.InfernalRayAbility;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class InfernalRaySkill extends AbstractDeathRaySkill {

    public InfernalRaySkill(float ADScale, float APScale, float baseDamage, boolean ignoreFrame, DamageType types, AbstractDeathRayAbility.BeamDamageTags tags) {
        super(ADScale, APScale, baseDamage, ignoreFrame, types, tags);
    }

    @Override
    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        double bonusAp = this.getScaleBonus(player, BHAttributes.ABILITY_POWER.get(), this.APScale);
        double bonusAd = this.getScaleBonus(player, Attributes.ATTACK_DAMAGE, this.ADScale);
        list.add(Component.translatable(createId(0), Maths.format(bonusAd + bonusAp), Utils.formattedWords(this.types.name())));
        return list;
    }

    @Override
    public void summonLaserBeam(Player player, Level level, ItemStack itemStack) {
        InfernalRayAbility deathLaserBeam = new InfernalRayAbility(level, player, player.getX(), player.getY() + 1.2f, player.getZ(), (float) ((player.yHeadRot + 90) * Math.PI / 180), (float) (-player.getXRot() * Math.PI / 180), player.getTicksUsingItem());
        deathLaserBeam.setHasPlayer(true);
        deathLaserBeam.setCanBurnTarget(this.canBurnTarget);
        deathLaserBeam.setBaseDamage(this.baseDamage);
        deathLaserBeam.setDamageType(this.types);
        deathLaserBeam.damageConfig(this.tagTypes, this.additionalDamage(player, itemStack));
        deathLaserBeam.setImmunityFrameIgnore(this.canIgnoreFrame);
        player.level().addFreshEntity(deathLaserBeam);
    }

    @Override
    public void onUsingTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseDuration) {
        super.onUsingTick(level, entity, itemStack, remainingUseDuration);
    }

    @Override
    protected float additionalDamage(Player player, ItemStack itemStack) {
        double bonusAp = this.getScaleBonus(player, BHAttributes.ABILITY_POWER.get(), this.APScale);
        double bonusAd = this.getScaleBonus(player, Attributes.ATTACK_DAMAGE, this.ADScale);
        return (float) (bonusAd + bonusAp);
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public double getManaCost() {
        return 10;
    }
}
