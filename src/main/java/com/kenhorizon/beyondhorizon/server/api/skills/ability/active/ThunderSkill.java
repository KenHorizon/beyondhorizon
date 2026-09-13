package com.kenhorizon.beyondhorizon.server.api.skills.ability.active;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponActiveSkills;
import com.kenhorizon.beyondhorizon.server.entity.ability.BoltShockAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.LightningStrikeAbility;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.libs.client.WeaponAnimations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class ThunderSkill extends WeaponActiveSkills {
    protected float scaleDamage;
    public ThunderSkill(float scaleDamage) {
        this.scaleDamage = scaleDamage;
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
        list.add(Component.translatable(createId(0)));
        list.add(Component.translatable(createId(1), Maths.format(this.additionalDamage(player, itemStack))));
        return list;
    }

    @Override
    public void onLeftClick(ItemStack itemStack, Player player) {
        double headY = player.getY() + 1.0D;
        int standingOnY = Mth.floor(player.getY()) - 2;
        float yawRadians = (float) (Math.toRadians(90 + player.getYRot()));
        for (int l = 0; l < 10; l++) {
            double d2 = 2.25D * (double) (l + 1);
            if (this.spawnBoltStrike(player.getX() + (double) Mth.cos(yawRadians) * d2, player.getZ() + (double) Mth.sin(yawRadians) * d2, standingOnY, headY, this.additionalDamage(player, itemStack), player.level(), player)) {

            }
        }
    }

    @Override
    public void abilityUse(ItemStack itemStack, Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        double range = 128.0D;
        HitResult realHitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::canBeHitByProjectile, range);
        if(realHitResult.getType() == HitResult.Type.MISS){
            realHitResult = ProjectileUtil.getHitResultOnViewVector(player, Entity::canBeHitByProjectile, 42);
        }
        BlockPos mutableSkyPos = new BlockPos.MutableBlockPos(realHitResult.getLocation().x, realHitResult.getLocation().y + 0.5D, realHitResult.getLocation().z);
        Vec3 vec3 = mutableSkyPos.getCenter();
        LightningStrikeAbility projectile = new LightningStrikeAbility(level, this.additionalDamage(player, itemStack));
        projectile.setCaster(player);
        projectile.setRadius(4.0F);
        projectile.setPos(vec3);
        if (level.addFreshEntity(projectile)) {
            this.addCooldownManaCost(player);
        }
    }

    @Override
    protected float additionalDamage(Player player, ItemStack itemStack) {
        double bonusAp = this.getScaleBonus(player, BHAttributes.ABILITY_POWER.get(), this.scaleDamage);
        return  Constant.THUNDER_DAMAGE + (float) bonusAp;
    }

    private boolean spawnBoltStrike(double x, double z, double minY, double maxY, float damage, Level world, LivingEntity player) {
        BlockPos blockpos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = world.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(world, blockpos1, Direction.UP)) {
                if (!world.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = world.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(world, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while (blockpos.getY() >= minY);

        if (flag) {
            BoltShockAbility.spawn(world, x, (double) blockpos.getY() + d0, z, damage, player);
            return true;
        }
        return false;
    }
}
