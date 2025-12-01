package com.kenhorizon.beyondhorizon.server;

import com.kenhorizon.beyondhorizon.server.capability.AccessoryInventoryCap;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.inventory.AccessoryContainer;
import com.kenhorizon.beyondhorizon.server.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.skills.Skill;
import com.kenhorizon.beyondhorizon.server.skills.accessory.IAccessoryItemHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class ServerEventHandler {

    @SubscribeEvent
    public void onLivingHealEvent(LivingHealEvent event) {
        float heal = event.getAmount();
        float bonus = (float) (heal * event.getEntity().getAttributeValue(BHAttributes.HEALING.get()));
        event.setAmount(heal + bonus);
    }

    @SubscribeEvent
    public void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (AccessoryInventoryCap.canAttachTo(entity)) {
            event.addCapability(AccessoryInventoryCap.NAME, new AccessoryInventoryCap());
        }
    }

    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IAccessoryItemHandler.class);
    }


    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            AccessoryContainer.dropContent(player);
        }
    }

    @SubscribeEvent
    public void onLivingDamageEvent(LivingDamageEvent event) {
        float damageDealt = event.getAmount();
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        if (source.getEntity() instanceof LivingEntity attacker) {
            ItemStack attackerStack = attacker.getMainHandItem();
            if (!attackerStack.isEmpty() && attackerStack.getItem() instanceof ISkillItems<?> container) {
                for (Skill trait : container.getSkills()) {
                    Optional<IAttack> meleeWeaponCallback = trait.IAttackCallback();
                    if (meleeWeaponCallback.isPresent()) {
                        damageDealt = meleeWeaponCallback.get().postMigitationDamage(damageDealt, source, attacker, target);
                    }
                }
            }
        }

        if (!target.getMainHandItem().isEmpty() && target.getMainHandItem().getItem() instanceof ISkillItems<?> container) {
            for (Skill trait : container.getSkills()) {
                Optional<IAttack> weaponCallback = trait.IAttackCallback();
                if (weaponCallback.isPresent()) {
                    damageDealt = weaponCallback.get().damageTaken(damageDealt, source, target);
                }
            }
        }
        StatModifiers totalDamageTaken = new StatModifiers(1.0F, (float) target.getAttributeValue(BHAttributes.DAMAGE_TAKEN.get()), 0.0F, 0.0F);
        damageDealt = totalDamageTaken.applyTo(damageDealt);
        event.setAmount(damageDealt);
    }

    @SubscribeEvent
    public void onLivingHurtEvent(LivingHurtEvent event) {
        float damageDealt = event.getAmount();
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        if (source.getDirectEntity() == source.getEntity() && source.getEntity() instanceof LivingEntity && target != null) {
            if (source.getEntity() instanceof Player attacker) {
                double getAttackSpeed = attacker.getAttributeValue(Attributes.ATTACK_SPEED);
                attacker.getAttackStrengthScale((float) getAttackSpeed);
                target.invulnerableTime = target.invulnerableDuration - (int) getAttackSpeed;
            }
            if (source.getEntity() instanceof LivingEntity attacker) {
                ItemStack attackerStack = attacker.getMainHandItem();
                if (!attackerStack.isEmpty() && attackerStack.getItem() instanceof ISkillItems<?> container) {
                    for (Skill trait : container.getSkills()) {
                        Optional<IAttack> meleeWeaponCallback = trait.IAttackCallback();
                        if (meleeWeaponCallback.isPresent()) {
                            damageDealt = meleeWeaponCallback.get().preMigitationDamage(damageDealt, source, attacker, target);
                        }
                    }
                }
                StatModifiers totalDamageDealt = new StatModifiers(1.0F, (float) attacker.getAttributeValue(BHAttributes.DAMAGE_DEALT.get()), 0.0F, 0.0F);
                damageDealt = totalDamageDealt.applyTo(damageDealt);
            }
        }
        event.setAmount(damageDealt);
    }
}
