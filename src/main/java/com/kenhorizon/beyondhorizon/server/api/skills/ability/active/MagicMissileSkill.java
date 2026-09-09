package com.kenhorizon.beyondhorizon.server.api.skills.ability.active;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.Colors;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponActiveSkills;
import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.InfernalSpear;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.MagicBolt;
import com.kenhorizon.beyondhorizon.server.entity.util.ShockwaveUtils;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.item.base.weapons.MagicWeaponBaseItem;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.libs.client.WeaponAnimations;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MagicMissileSkill extends WeaponActiveSkills {
    protected float scaleDamage;
    public MagicMissileSkill(float scaleDamage) {
        this.scaleDamage = scaleDamage;
        this.setCooldown(Maths.sec(1));
        this.setManaCost(20);
    }

    @Override
    public WeaponAnimations getWeaponAnimations(Player player, ItemStack itemStack) {
        return WeaponAnimations.GUARDIAN_SWORD;
    }

    @Override
    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        list.add(Component.translatable(createId(0), Maths.format(this.additionalDamage(player, itemStack))));
        return list;
    }

    @Override
    public void abilityUse(ItemStack itemStack, Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        if (!level.isClientSide()) {
            MagicBolt projectile = new MagicBolt(level, player);
            projectile.setBaseDamage(this.additionalDamage(player, itemStack));
            Vec3 vector3d = player.getViewVector(1.0F);
            Vec3 vec3 = player.getHandHoldingItemAngle(itemStack.getItem());
            double d0 = player.getX() + vec3.x();
            double d1 = player.getY() + vec3.y() + (player.getBbHeight() / 2) + 0.25D;
            double d2 = player.getZ() + vec3.z();
            projectile.shoot(vector3d.x(), vector3d.y(), vector3d.z(), 2.0F, 1.0F);
            projectile.setPosRaw(d0, d1, d2);
            level.addFreshEntity(projectile);
        }
    }

    @Override
    protected float additionalDamage(Player player, ItemStack itemStack) {
        double bonusAp = this.getScaleBonus(player, BHAttributes.ABILITY_POWER.get(), this.scaleDamage);
        return (float) bonusAp;
    }
}
