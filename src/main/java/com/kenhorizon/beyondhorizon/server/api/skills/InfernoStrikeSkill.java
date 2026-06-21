package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.RingParticleOptions;
import com.kenhorizon.beyondhorizon.client.render.util.ColorUtil;
import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.level.utils.AttributeUtils;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
    protected List<MutableComponent> tooltipDescriptions(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        list.add(Component.translatable(createId(0), this.maxSlow));
        list.add(Component.translatable(createId(1), this.abilityDamageDealt(player, Attributes.ATTACK_DAMAGE, this.scaleDamage)));
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
                    double damage = Mth.lerp(durationFactor, this.abilityDamageDealt(player, Attributes.ATTACK_DAMAGE, 0.05F), this.abilityDamageDealt(player, Attributes.ATTACK_DAMAGE, this.scaleDamage) * durationFactor);

                    if (level instanceof ServerLevel sLevel) {
                        float r = ColorUtil.getFARGB(0xFFFFFF)[0];
                        float g = ColorUtil.getFARGB(0xFFFFFF)[1];
                        float b = ColorUtil.getFARGB(0xFFFFFF)[2];
                        sLevel.sendParticles(new RingParticleOptions(0, (float) Math.PI / 2f, 33,
                                        r, g, b, 1.0F, 110F,
                                        false, RingParticles.Behavior.GROW),
                                entity.getX(), entity.getY(), entity.getZ(), 1, 0,0, 0, 0);
                    }

                    CameraShake.spawn(level, player.position(), 8.0F, 0.02F, 20, 20);
                    this.stab(player, 6.0, (float) damage, DamageTypes.PHYISCAL_DAMAGE);
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
    public void onUsingTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseDuration) {
        if (level.isClientSide()) {
           if (entity.tickCount % 8L == 0) {
                float r = ColorUtil.getFARGB(0xFF6500)[0];
                float g = ColorUtil.getFARGB(0xFF6500)[1];
                float b = ColorUtil.getFARGB(0xFF6500)[2];
                level.addParticle(new RingParticleOptions(0, (float)Math.PI/2f, 33, r, g, b, 1.0F, 32, false, RingParticles.Behavior.GROW), entity.getX(), entity.getY() + 0.5D, entity.getZ(), 0, 0, 0);
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
