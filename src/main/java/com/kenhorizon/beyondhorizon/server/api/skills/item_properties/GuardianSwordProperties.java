package com.kenhorizon.beyondhorizon.server.api.skills.item_properties;


import com.kenhorizon.beyondhorizon.server.api.skills.WeaponItemProperties;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingRod;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GuardianSwordProperties extends WeaponItemProperties {

    @Override
    public void onLeftClick(ItemStack itemStack, Player player) {
        Level level = player.level();
        if (!level.isClientSide()) {
            BlazingRod projectile = new BlazingRod(level, player);
            projectile.setBaseDamage((float) EntityUtils.getAttackDamage(player, 0.75F));
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
}
