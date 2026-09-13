package com.kenhorizon.beyondhorizon.server.api.skills.ability.active;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponActiveSkills;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.MagicBolt;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.libs.client.WeaponAnimations;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class MagicMissileSkill extends WeaponActiveSkills {
    protected float scaleDamage;
    public MagicMissileSkill(float scaleDamage) {
        this.scaleDamage = scaleDamage;
        this.setCooldown(0);
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
            if (level.addFreshEntity(projectile)) {
                this.addCooldownManaCost(player);
            }
        }
    }

    @Override
    protected float additionalDamage(Player player, ItemStack itemStack) {
        double bonusAp = this.getScaleBonus(player, BHAttributes.ABILITY_POWER.get(), this.scaleDamage);
        return (float) bonusAp;
    }
}
