package com.kenhorizon.beyondhorizon.server.api.skills.item_properties;


import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingRod;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityUtils;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.Main;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class GuardianSwordProperties extends ItemPropertiesSkill {

    @Override
    public void onLeftClick(ItemStack itemStack, Player player) {
        Level level = player.level();
        if (!level.isClientSide()) {
            BlazingRod projectile = new BlazingRod(level, player);
            projectile.setBaseDamage((float) EntityUtils.getAttackDamage(player));
            Vec3 vector3d = player.getViewVector(1.0F);
            Vec3 vec3 = player.getHandHoldingItemAngle(itemStack.getItem());
            double d0 = player.getX() + vec3.x();
            double d1 = player.getY() + vec3.y() + (player.getBbHeight() / 2) + 0.2D;
            double d2 = player.getZ() + vec3.z();
            projectile.shoot(vector3d.x(), vector3d.y(), vector3d.z(), 2.0F, 1.0F);
            projectile.setPosRaw(d0, d1, d2);
            level.addFreshEntity(projectile);
        }
        player.swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public boolean onLeftClickProperties(ItemStack itemStack, Player player) {
        return true;
    }

    protected void stab(LivingEntity user, float damage) {
        double range = 2.5D;
        Vec3 srcVec = user.getEyePosition();
        Vec3 lookVec = user.getViewVector(1.0F);
        Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
        float var9 = 1.0F;
        List<Entity> possibleList = user.level().getEntities(user, user.getBoundingBox().expandTowards(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range).inflate(var9, var9, var9));
        boolean flag = false;
        for (Entity entity : possibleList) {
            if (entity instanceof LivingEntity) {
                float borderSize = 0.5F;
                AABB collisionBB = entity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
                Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);
                if (collisionBB.contains(srcVec)) {
                    flag =true;
                } else if (interceptPos.isPresent()) {
                    flag =true;
                }

                if (flag) {
                    if (entity.hurt(BHDamageTypes.physicalDamage(user), damage)) {
                        entity.invulnerableTime = - 20;
                        int j = EnchantmentHelper.getFireAspect(user);
                        if (j > 0 && !entity.isOnFire()) {
                            entity.setSecondsOnFire(j * 4);
                        }
                    }
                }
            }
        }
    }
}
