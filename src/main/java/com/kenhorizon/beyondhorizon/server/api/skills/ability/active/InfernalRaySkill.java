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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InfernalRaySkill extends AbstractDeathRaySkill {

    public InfernalRaySkill(float ADScale, float APScale, float baseDamage, boolean ignoreFrame, DamageType types, AbstractDeathRayAbility.BeamDamageTags tags) {
        super(ADScale, APScale, baseDamage, ignoreFrame, types, tags);
    }

    @Override
    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        list.add(Component.translatable(createId(0), Maths.format(this.ADScale) + Maths.format(this.APScale), Utils.capitalize(Utils.builderName(this.types.name().toLowerCase(Locale.ROOT)))));
        return list;
    }

    @Override
    public void summonLaserBeam(Player player, Level level, ItemStack itemStack) {
        InfernalRayAbility deathLaserBeam = new InfernalRayAbility(level, player, player.getX(), player.getY() + 1.2f, player.getZ(), (float) ((player.yHeadRot + 90) * Math.PI / 180), (float) (-player.getXRot() * Math.PI / 180), player.getTicksUsingItem());
        deathLaserBeam.setHasPlayer(true);
        deathLaserBeam.setDamageType(DamageType.PHYSICAL_DAMAGE);
        deathLaserBeam.setCanBurnTarget(this.canBurnTarget);
        deathLaserBeam.setBaseDamage(this.baseDamage);
        deathLaserBeam.setDamageType(this.types);
        deathLaserBeam.damageConfig(this.tagTypes, deathLaserBeam.getBaseDamage() + this.additionalDamage(player, itemStack));
        deathLaserBeam.setImmunityFrameIgnore(this.canIgnoreFrame);
        player.level().addFreshEntity(deathLaserBeam);
    }

    @Override
    protected float additionalDamage(Player player, ItemStack itemStack) {
        return (float) (this.getScaleBonusAttribute(player, Attributes.ATTACK_DAMAGE, this.ADScale) + this.getScaleBonusAttribute(player, BHAttributes.ABILITY_POWER.get(), this.APScale));
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
