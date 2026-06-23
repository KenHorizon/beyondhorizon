package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.particle.world.TrailParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.entity.ability.InfernalSlashAbility;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityUtils;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTypeTags;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.beyondhorizon.server.util.RaycastUtil;
import com.kenhorizon.libs.client.WeaponAnimations;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
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

public class InfernoStrikeSkill extends WeaponActiveSkills {
    private static final UUID SPEED_MODIFIER_SPRINTING_UUID = UUID.fromString("1a63ada7-7fcd-4695-b8db-0873ced4be94");
    private static final AttributeModifier SPEED_MODIFIER_SPRINTING = new AttributeModifier(SPEED_MODIFIER_SPRINTING_UUID, "Reduce speed boost", (double)-0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL);

    protected float scaleDamage;
    protected float maxSlow;
    public InfernoStrikeSkill(float maxSlow, float scaleDamage) {
        this.maxSlow = maxSlow;
        this.scaleDamage = scaleDamage;
    }

    @Override
    public WeaponAnimations getWeaponAnimations() {
        return WeaponAnimations.GUARDIAN_SWORD;
    }

    @Override
    protected List<MutableComponent> tooltipDescriptions(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        list.add(Component.translatable(createId(0), Maths.format0(this.maxSlow)));
        list.add(Component.translatable(createId(1), Maths.format0(this.scaleDamage)));
        return list;
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int chargedDuration) {
        if (entity instanceof Player player) {
            int durations = this.getUseDuration(itemStack) - chargedDuration;
            if (durations <= 0) return;
            float durationFactor = (float) Mth.lerp((float) durations / Maths.sec(3), 0.0D, 1.0D);
            if (!((double) durationFactor < 0.1D)) {
                if (!level.isClientSide()) {
                    this.addCooldownManaCost(player);
                    double damage = Mth.lerp(durationFactor, this.getScaleBonusAttribute(player, Attributes.ATTACK_DAMAGE, 0.05F), this.getScaleBonusAttribute(player, Attributes.ATTACK_DAMAGE, this.scaleDamage) * durationFactor);
                    var color = ColorUtil.RED;
                    if (level instanceof ServerLevel sLevel) {
                        float r = ColorUtil.getFARGB(color)[0];
                        float g = ColorUtil.getFARGB(color)[1];
                        float b = ColorUtil.getFARGB(color)[2];
                        sLevel.sendParticles(new RingParticleOptions(0, (float) Math.PI / 2f, 33,
                                        r, g, b, 1.0F, 110F,
                                        false, RingParticles.Behavior.GROW),
                                entity.getX(), entity.getY(), entity.getZ(), 1, 0,0, 0, 0);
                    }
                    CameraShake.spawn(level, player.position(), 8.0F, 0.02F, 20, 20);

                    Vec3 rotation = player.getLookAngle().normalize();
                    var pos = player.position().add(rotation.scale(1.6));
                    double dx = pos.x - player.getX();
                    double dz = pos.z - player.getZ();
                    InfernalSlashAbility.spawn(level, player, (float) damage, DamageType.PHYSICAL_DAMAGE, dx, 0, dz);
                    AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                    if (attributeInstance.getModifier(SPEED_MODIFIER_SPRINTING_UUID) != null) {
                        attributeInstance.removeModifier(SPEED_MODIFIER_SPRINTING);
                    }
                }
            }
        }
    }

    @Override
    public void finishedUsingItem(ItemStack itemStack, Level level, Player player) {
        AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeInstance.getModifier(SPEED_MODIFIER_SPRINTING_UUID) != null) {
            attributeInstance.removeModifier(SPEED_MODIFIER_SPRINTING);
        }
    }

    @Override
    public void onLeftClick(ItemStack itemStack, Player player) {

    }

    @Override
    public void onUsingTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseDuration) {
        if (level.isClientSide()) {
            int durations = this.getUseDuration(itemStack) - remainingUseDuration;
            if (durations <= 0) return;
            float durationFactor = (float) Mth.lerp((float) durations / Maths.sec(3), 0.0D, 1.0D);
            if (!((double) durationFactor < 0.1D)) {
                var color = ColorUtil.lerpG(durationFactor, ColorUtil.RED, ColorUtil.YELLOW);
                if (entity.tickCount % 8L == 0) {
                    float r = ColorUtil.getFARGB(color)[0];
                    float g = ColorUtil.getFARGB(color)[1];
                    float b = ColorUtil.getFARGB(color)[2];
                    level.addParticle(new RingParticleOptions(0, (float)Math.PI / 2f, 33, r, g, b, 1.0F, 32, false, RingParticles.Behavior.GROW), entity.getX(), entity.getY() + 0.5D, entity.getZ(), 0, 0, 0);
                    int particleCount = 16;
                    while (particleCount --> 0) {
                        double radius = 16.0F;
                        float yaw = (float) (entity.getRandom().nextFloat() * 2 * Math.PI);
                        float pitch = (float) (entity.getRandom().nextFloat() * 2 * Math.PI);
                        double ox = (float) (radius * Math.sin(yaw) * Math.sin(pitch));
                        double oy = (float) (radius * Math.cos(pitch));
                        double oz = (float) (radius * Math.cos(yaw) * Math.sin(pitch));
                        TrailParticleOptions.add(entity.level(), TrailParticles.Behavior.FADE_N_SHRINK,
                                entity.getX() + ox, entity.getY() + oy + 0.1, entity.getZ() + oz, 1.50F,
                                1, r, g, b,
                                40, new Vec3(entity.getX(), entity.getY() + entity.getY() + 1.25F, entity.getZ()));
                    }
                }
            }
        }

    }

    @Override
    public void abilityUse(ItemStack itemStack, Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeInstance.getModifier(SPEED_MODIFIER_SPRINTING_UUID) != null) {
            attributeInstance.removeModifier(SPEED_MODIFIER_SPRINTING);
        }
        if (player.isUsingItem()) {
            attributeInstance.addTransientModifier(SPEED_MODIFIER_SPRINTING);
        }
    }
    @Override
    public int getUseDuration(ItemStack itemStack) {
        return super.getUseDuration(itemStack);
    }

    @Override
    public int getCooldown() {
        return Maths.sec(6);
    }

    @Override
    public int getManaCost() {
        return 20;
    }

}
