package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveAbility;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingRod;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CleaveEffectSkill extends WeaponSkills {
    private float cleaveRange;
    protected CleaveAbility.Type type = CleaveAbility.Type.CIRCLE;
    public CleaveEffectSkill(float magnitude, float range, CleaveAbility.Type type) {
        this.setMagnitude(magnitude);
        this.cleaveRange = range;
        this.type = type;
    }

    public void setCleaveRange(float cleaveRange) {
        this.cleaveRange = cleaveRange;
    }

    public float getCleaveRange() {
        return cleaveRange;
    }

    public CleaveAbility.Type getCleaveType() {
        return this.type;
    }

    public void setType(CleaveAbility.Type type) {
        this.type = type;
    }

    @Override
    protected MutableComponent tooltipDescription(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format(this.getMagnitude() * 100.0F), Maths.format(this.getCleaveRange() * 100.0F));
    }

    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
//        CleaveAbility.spawn(attacker.level(), target, attacker, 999.99F, this.getCleaveRange(), this.getCleaveType());
        CleaveAbility.spawn(attacker.level(), target , attacker, damageDealt * this.getMagnitude(), this.getCleaveRange(), this.getCleaveType());
    }

    private void shootBlazingCleave(ItemStack itemStack, Player player) {
        BeyondHorizon.LOGGER.debug("Using Blazing Cleave");
        Level level = player.level();
        Vec3 vector3d = player.getViewVector(1.0F);
        Vec3 vec3 = player.getHandHoldingItemAngle(itemStack.getItem());
        BlazingRod projectile = new BlazingRod(player.level(), vec3.x, vec3.y, vec3.z, player);
        projectile.setBaseDamage((float) (player.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5F));
        double d0 = player.getX() + vec3.x();
        double d1 = player.getY() + vec3.y() + (player.getBbHeight() / 2) + 0.2D;
        double d2 = player.getZ() + vec3.z();
        projectile.shoot(vector3d.x(), vector3d.y(), vector3d.z(), 2.0F, 1.0F);
        projectile.setPosRaw(d0, d1, d2);
        level.addFreshEntity(projectile);
        if (!level.isClientSide()) {
            level.addFreshEntity(projectile);
        }
        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
    }
}
