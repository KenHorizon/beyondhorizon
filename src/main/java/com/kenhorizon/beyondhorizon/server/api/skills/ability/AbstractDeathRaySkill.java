package com.kenhorizon.beyondhorizon.server.api.skills.ability;

import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.skills.WeaponActiveSkills;
import com.kenhorizon.beyondhorizon.server.entity.ability.AbstractDeathRayAbility;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.libs.client.WeaponAnimations;
import net.minecraft.client.CameraType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public abstract class AbstractDeathRaySkill extends WeaponActiveSkills {

    protected float ADScale = 0.0F;
    protected float APScale = 0.0F;

    private CameraType cameraType;
    protected float baseDamage = 1.0F;
    protected boolean canIgnoreFrame = false;
    protected AbstractDeathRayAbility.BeamDamageTags tagTypes;
    protected DamageType types;
    private static final UUID SPEED_MODIFIER_SPRINTING_UUID = UUID.fromString("1a63ada7-7fcd-4695-b8db-0873ced4be94");
    private static final AttributeModifier SPEED_MODIFIER_SPRINTING = new AttributeModifier(SPEED_MODIFIER_SPRINTING_UUID, "Sprinting speed boost", (double)-0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    protected boolean canBurnTarget = false;

    public AbstractDeathRaySkill(float ADScale, float APScale, float baseDamage, boolean ignoreFrame, DamageType types, AbstractDeathRayAbility.BeamDamageTags tags) {
        this.baseDamage = baseDamage;
        this.tagTypes = tags;
        this.canIgnoreFrame =ignoreFrame;
        this.types = types;
        this.ADScale = ADScale;
        this.APScale = APScale;
    }

    @Override
    public WeaponAnimations getWeaponAnimations() {
        return WeaponAnimations.HOLDING;
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int chargedDuration) {
        if (entity instanceof Player player) {
            List<AbstractDeathRayAbility> list = player.level().getEntitiesOfClass(AbstractDeathRayAbility.class, player.getBoundingBox().inflate(2.0D));
            if (!list.isEmpty()) {
                for (AbstractDeathRayAbility laserBeam : list) {
                    if (laserBeam.caster != null && laserBeam.caster.getUUID() == player.getUUID()) {
                        laserBeam.setDuration(0);
                        laserBeam.discard();
                    }
                }
            }
            AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attributeInstance.getModifier(SPEED_MODIFIER_SPRINTING_UUID) != null) {
                attributeInstance.removeModifier(SPEED_MODIFIER_SPRINTING);
            }
        }
    }

    @Override
    public void onUsingTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseDuration) {
        if (entity instanceof Player player) {
            PlayerData playerData = PlayerData.getInstance(player);
            if (playerData.getMana() <= 0) {
                List<AbstractDeathRayAbility> list = player.level().getEntitiesOfClass(AbstractDeathRayAbility.class, player.getBoundingBox().inflate(2.0D));
                if (!list.isEmpty()) {
                    for (AbstractDeathRayAbility laserBeam : list) {
                        if (laserBeam.caster != null && laserBeam.caster.getUUID() == player.getUUID()) {
                            itemStack.finishUsingItem(level, player);
                            laserBeam.setDuration(0);
                            laserBeam.discard();
                        }
                    }
                }
            }
            List<AbstractDeathRayAbility> list = player.level().getEntitiesOfClass(AbstractDeathRayAbility.class, player.getBoundingBox().inflate(2.0D));
            if (player.getTicksUsingItem() > 20) {
                if (!player.level().isClientSide()) {
                    if (!(playerData.getMana() > 0)) {
                        itemStack.finishUsingItem(level, player);
                        player.stopUsingItem();
                    }
                    ItemStack getItemStack = player.getItemInHand(player.getUsedItemHand());
                    if (!player.getAbilities().instabuild && !list.isEmpty() && player.getTicksUsingItem() % 10 == 0) {
                        playerData.removeMana(this.getManaCost(), true);
                        getItemStack.hurtAndBreak(1, player, (user) -> {
                            user.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                        });
                    }
                    if (list.isEmpty()) {
                        this.summonLaserBeam(player, level, itemStack);
                    }
                    for (AbstractDeathRayAbility laserBeam : list) {
                        if (laserBeam.caster != null && laserBeam.caster.getUUID() == player.getUUID()) {
                            laserBeam.setDuration(player.getTicksUsingItem());
                        }
                    }
                }
            }
        }
    }

    public void summonLaserBeam(Player player, Level level, ItemStack itemStack) {
        AbstractDeathRayAbility deathLaserBeam = new AbstractDeathRayAbility(this.summonRayBeam(), level, player, player.getX(), player.getY() + 1.2f, player.getZ(), (float) ((player.yHeadRot + 90) * Math.PI / 180), (float) (-player.getXRot() * Math.PI / 180), player.getTicksUsingItem());
        deathLaserBeam.setHasPlayer(true);
        deathLaserBeam.setCanBurnTarget(this.canBurnTarget);
        deathLaserBeam.setBaseDamage(this.baseDamage);
        deathLaserBeam.setDamageType(this.types);
        deathLaserBeam.damageConfig(this.tagTypes, deathLaserBeam.getBaseDamage() + this.additionalDamage(player, itemStack));
        deathLaserBeam.setImmunityFrameIgnore(this.canIgnoreFrame);
        player.level().addFreshEntity(deathLaserBeam);
    }

    public EntityType<? extends AbstractDeathRayAbility> summonRayBeam() {
        return BHEntity.INFERNAL_RAY.get();
    }

    @Override
    public void finishedUsingItem(ItemStack itemStack, Level level, Player player) {
//        Minecraft minecraft = Minecraft.getInstance();
//        minecraft.options.setCameraType(this.cameraType);
        AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeInstance.getModifier(SPEED_MODIFIER_SPRINTING_UUID) != null) {
            attributeInstance.removeModifier(SPEED_MODIFIER_SPRINTING);
        }
//        itemStack.finishUsingItem(level, player);
    }

    @Override
    public void abilityUse(ItemStack itemStack, Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
//        Minecraft minecraft = Minecraft.getInstance();
//        this.cameraType = minecraft.options.getCameraType();
//        minecraft.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeInstance.getModifier(SPEED_MODIFIER_SPRINTING_UUID) != null) {
            attributeInstance.removeModifier(SPEED_MODIFIER_SPRINTING);
        }
        if (player.isUsingItem()) {
            attributeInstance.addTransientModifier(SPEED_MODIFIER_SPRINTING);
        }
    }
}
