package com.kenhorizon.beyondhorizon.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.level.tooltips.IconAttributesTooltip;
import com.kenhorizon.beyondhorizon.server.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.accessory.IAccessoryEvent;
import com.kenhorizon.beyondhorizon.server.accessory.IAccessoryItems;
import com.kenhorizon.beyondhorizon.server.capability.*;
import com.kenhorizon.beyondhorizon.server.classes.IRoleClass;
import com.kenhorizon.beyondhorizon.server.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IItemGeneric;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.inventory.AccessoryContainer;
import com.kenhorizon.beyondhorizon.server.level.ICombatCore;
import com.kenhorizon.beyondhorizon.server.level.damagesource.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.skills.Skill;
import com.kenhorizon.beyondhorizon.server.accessory.IAccessoryItemHandler;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.client.level.tooltips.Tooltips;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class ServerEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRegisterTooltipGatherComponents(RenderTooltipEvent.GatherComponents event) {
        ItemStack itemStack = event.getItemStack();

        List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
        if (!itemStack.isEmpty()) {
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).left().isPresent()) {
                    FormattedText text = elements.get(i).left().get();
                    if (text instanceof MutableComponent component) {
                        if (this.getComponents(Tooltips.TOOLTIP_ACCESSORY, component, itemStack)) {
                            this.addIcons(BeyondHorizon.resource("textures/gui/sprites/icon/tooltip/accessory_icon.png"), false, elements, i, itemStack, text);
                        }
                    }
                }
            }
        }
    }

    private void addIcons(ResourceLocation texture, boolean removeText, List<Either<FormattedText, TooltipComponent>> elements, int index, ItemStack itemStack, FormattedText text) {
        FormattedCharSequence paddedTitle = FormattedCharSequence.fromList(List.of(FormattedCharSequence.forward("   ", Style.EMPTY),Language.getInstance().getVisualOrder(text), FormattedCharSequence.forward("  ", Style.EMPTY)));
        List<FormattedText> recomposedTitle = Tooltips.recompose(List.of(ClientTooltipComponent.create(paddedTitle)));
        if (!recomposedTitle.isEmpty()) {
            if (!removeText) {
                elements.set(index, Either.left(recomposedTitle.get(0)));
            }
            elements.add(index, Either.right(new IconAttributesTooltip(text, texture)));
        }
    }

    private boolean getComponents(String textFind, MutableComponent component, ItemStack itemStack) {
        for (Component vanillaAttribute : component.getSiblings()) {
            if (vanillaAttribute.getContents() instanceof TranslatableContents translatableContents) {
                return translatableContents.getKey().startsWith(textFind);
            }
        }
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            return translatableContents.getKey().startsWith(textFind);
        }
        return false;
    }

    @SubscribeEvent
    public void onLivingHealEvent(LivingHealEvent event) {
        float heal = event.getAmount();
        float bonus = (float) (heal * event.getEntity().getAttributeValue(BHAttributes.HEALING.get()));
        event.setAmount(heal + bonus);
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (AccessoryInventoryCap.canAttachTo(entity)) {
            event.addCapability(AccessoryInventoryCap.NAME, new AccessoryInventoryCap());
        }
        if (DamageInfoCap.canAttachTo(entity)) {
            event.addCapability(DamageInfoCap.NAME, new DamageInfoCap());
        }
        if (CombatCoreCap.canAttachTo(entity)) {
            event.addCapability(CombatCoreCap.NAME, new CombatCoreCap());
        }
        if (RoleClassCap.canAttachTo(entity)) {
            event.addCapability(RoleClassCap.NAME, new RoleClassCap((Player) entity));
        }
    }

    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IAccessoryItemHandler.class);
        event.register(ICombatCore.class);
        event.register(IDamageInfo.class);
        event.register(IRoleClass.class);
    }


    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            AccessoryContainer.dropContent(player);
        }
    }

    @SubscribeEvent
    public void playerStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            this.syncSlot(serverPlayer);
        }
        float healthRegen = (float) player.getAttributeValue(BHAttributes.HEALTH_REGENERATION.get());
        if (player.tickCount % 10 == 0) {
            player.heal(healthRegen);
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        Player oldPlayer = event.getOriginal();
        if (event.isWasDeath()) {
            oldPlayer.revive();
            oldPlayer.getCapability(BHCapabilties.ACCESSORY).ifPresent(oldData -> {
                player.getCapability(BHCapabilties.ACCESSORY).ifPresent(newData -> {
                    newData.deserializeNBT(oldData.serializeNBT());
                });
            });
            oldPlayer.invalidateCaps();
        }
    }

    private void syncSlot(ServerPlayer player) {
        player.getCapability(BHCapabilties.ACCESSORY).ifPresent(handler -> {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack itemStack = handler.getStackInSlot(i);
                BeyondHorizon.PROXY.syncAccessoryToPlayer(i, itemStack, player);
            }
        });
    }

    private void onPlayerTick(Player player) {
        player.getCapability(BHCapabilties.ACCESSORY).ifPresent(handler -> {
            for (int inv = 0; inv < player.getInventory().getContainerSize(); inv++) {
                ItemStack itemStacks = player.getInventory().getItem(inv);
                if (!itemStacks.isEmpty() && itemStacks.getItem() instanceof IAccessoryItems<?>) {
                    itemStacks.inventoryTick(player.level(), player, -1, false);
                }
            }
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack itemStacks = handler.getStackInSlot(i);
                if (!itemStacks.isEmpty()) {
                    itemStacks.inventoryTick(player.level(), player, -1, true);
                }
                if (itemStacks.getItem() instanceof IAccessoryItems<?> items) {
                    for (Accessory accessory : items.getAccessories()) {
                        Optional<IItemGeneric> optional = accessory.IItemGeneric();
                        optional.ifPresent(callback -> callback.onEntityUpdate(player, itemStacks));
                    }
                }
                if (!player.level().isClientSide()) {
                    ItemStack prevItemStack = handler.getPreviousItemStack(i);
                    if (!ItemStack.matches(itemStacks, prevItemStack)) {
                        if (!prevItemStack.isEmpty()) {
                            if (prevItemStack.getItem() instanceof IAccessoryItems<?> container) {
                                for (Accessory accessory : container.getAccessories()) {
                                    accessory.removeAttributeModifiers(player, player.getAttributes(), prevItemStack);
                                    Optional<IAccessoryEvent> optional = accessory.IAccessory();
                                    optional.ifPresent(callback -> callback.onChangePrevAccessorySlot(player, prevItemStack));
                                }
                            }
                        }
                        if (!itemStacks.isEmpty()) {
                            if (itemStacks.getItem() instanceof IAccessoryItems<?> container) {
                                for (Accessory accessory : container.getAccessories()) {
                                    accessory.addAttributeModifiers(player, player.getAttributes(), itemStacks);
                                    Optional<IAccessoryEvent> optional = accessory.IAccessory();
                                    optional.ifPresent(callback -> callback.onChangePostAccessorySlot(player, itemStacks));
                                }
                            }
                        }
                        handler.setPreviousItemStack(i, itemStacks.copy());
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack itemStack = entity.getMainHandItem();
        ICombatCore combatCore = CapabilityCaller.combat(entity);
        if (combatCore != null) {
            combatCore.tick();
            IDamageInfo damageInfo = CapabilityCaller.damageInfo(entity);
            if (damageInfo != null && !combatCore.OnCombat()) {
                damageInfo.setPostDamage(0.0F);
                damageInfo.setPreDamage(0.0F);
                damageInfo.setPreStoredDamage(0.0F);
                damageInfo.setPostStoredDamage(0.0F);
            }
        }
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ISkillItems<?> items) {
            for (Skill skill : items.getSkills()) {
                Optional<IItemGeneric> optional = skill.IItemGeneric();
                optional.ifPresent(callback -> callback.onEntityUpdate(entity, itemStack));
            }
        }
        if (entity instanceof Player player) {
            this.onPlayerTick(player);
            IRoleClass roleCallback = CapabilityCaller.roleClass(player);
            RoleClass roleClass = roleCallback.getInstance();
            roleClass.tick();
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack prevItemStacks = player.getInventory().getItem(i);
                if (!ItemStack.matches(itemStack, prevItemStacks)) {
                    if (!prevItemStacks.isEmpty()) {
                        if (prevItemStacks.getItem() instanceof ISkillItems<?> skillItems) {
                            for (Skill skill : skillItems.getSkills()) {
                                Optional<IItemGeneric> optional = skill.IItemGeneric();
                                skill.removeAttributeModifiers(player, player.getAttributes(), prevItemStacks);
                                optional.ifPresent(iItemGeneric -> iItemGeneric.onChangeEquipment(player, itemStack, true));
                            }
                        }
                    }
                    if (!itemStack.isEmpty()) {
                        if (itemStack.getItem() instanceof ISkillItems<?> skillItems) {
                            for (Skill skill : skillItems.getSkills()) {
                                Optional<IItemGeneric> optional = skill.IItemGeneric();
                                skill.addAttributeModifiers(player, player.getAttributes(), itemStack);
                                optional.ifPresent(iItemGeneric -> iItemGeneric.onChangeEquipment(player, itemStack, false));
                            }
                        }
                    }
                }
            }
        }
        if (entity instanceof Mob mobs) {
            LivingEntity target = mobs.getTarget();
            LivingEntity getLastHurtByMob = mobs.getLastHurtByMob();
            double followRange = mobs.getAttributeValue(Attributes.FOLLOW_RANGE);
            if (mobs.hasEffect(MobEffects.BLINDNESS)) {
                if (target != null) {
                    if (entity.distanceToSqr(target) > 5) {
                        mobs.setTarget(null);
                        mobs.setLastHurtByMob(null);
                    }
                }
            }
            if (getLastHurtByMob instanceof Player player) {
                double stealth = player.getAttributeValue(BHAttributes.STEALTH.get());
                if (stealth == 0.0D) return;
                if (entity.distanceToSqr(player) < (Math.max(followRange - (followRange * stealth), 5))) {
                    mobs.setLastHurtByMob(null);
                }
            }
            if (target instanceof Player player) {
                double stealth = player.getAttributeValue(BHAttributes.STEALTH.get());
                if (stealth == 0.0D) return;
                if (entity.distanceToSqr(player) < (Math.max(followRange - (followRange * stealth), 5))) {
                    mobs.setTarget(null);
                }
            }
        }
    }

    @SubscribeEvent
    public void onCriticalHit(CriticalHitEvent event) {
        Player player = event.getEntity();
        if (player != null) {
            ItemStack itemStack = player.getMainHandItem();
            double criticalStrike = player.getAttributeValue(BHAttributes.CRITICAL_STRIKE.get());
            double criticalDamage = player.getAttributeValue(BHAttributes.CRITICAL_DAMAGE.get());
            if (player.getRandom().nextDouble() <= criticalStrike) {
                event.setResult(Event.Result.ALLOW);
                event.setDamageModifier((float) criticalDamage);
            }
            event.setDamageModifier((float) criticalDamage);
        }
    }

    @SubscribeEvent
    public void onLivingDamageEvent(LivingDamageEvent event) {
        float damageDealt = event.getAmount();
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        ICombatCore targetCombatCore = CapabilityCaller.combat(target);
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
        if (target instanceof Player player) {
            IAccessoryItemHandler handler = CapabilityCaller.accessory(player);
            if (handler != null) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    final ItemStack itemStack = handler.getStackInSlot(i);
                    if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItems<?> accessoryItems) {
                        for (Accessory trait : accessoryItems.getAccessories()) {
                            Optional<IAttack> weaponCallback = trait.IAttackCallback();
                            if (weaponCallback.isPresent()) {
                                damageDealt = weaponCallback.get().damageTaken(damageDealt, source, target);
                            }
                        }
                    }
                }
            }
        }
        StatModifiers totalDamageTaken = new StatModifiers(1.0F, (float) target.getAttributeValue(BHAttributes.DAMAGE_TAKEN.get()), 0.0F, 0.0F);
        damageDealt = totalDamageTaken.applyTo(damageDealt);
        IDamageInfo damageInfo = CapabilityCaller.damageInfo(target);
        if (damageInfo != null) {
            damageInfo.setPostDamage(damageDealt);
            if (!source.is(BHDamageTypeTags.CANT_STORE_DAMAGE)) {
                damageInfo.setPostStoredDamage(damageInfo.postDamage() + damageDealt);
            }
        }
        targetCombatCore.activated();
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
                ICombatCore attackerCombatCore = CapabilityCaller.combat(attacker);
                ItemStack attackerStack = attacker.getMainHandItem();
                if (attacker instanceof Player) {
                    IRoleClass callback = CapabilityCaller.roleClass((Player) attacker);
                    RoleClass roleClass = callback.getInstance();
                    Optional<IAttack> attack = roleClass.IAttack();
                    if (attack.isPresent()) {
                        damageDealt = attack.get().preMigitationDamage(damageDealt, source, attacker, target);
                    }
                }
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
                attackerCombatCore.activated();
            }
        }
        if (target != null) {
            IDamageInfo damageInfo = CapabilityCaller.damageInfo(target);
            if (damageInfo != null) {
                damageInfo.setPreDamage(damageDealt);
                if (!source.is(BHDamageTypeTags.CANT_STORE_DAMAGE)) {
                    damageInfo.setPreStoredDamage(damageInfo.preDamage() + damageDealt);
                }
                if (source.getEntity() instanceof LivingEntity attacker) {
                    damageInfo.setLastAttacker(attacker);
                }
            }
        }
        event.setAmount(damageDealt);
    }
    @SubscribeEvent
    public void onMiningSpeedUpdate(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        double originalSpeed = event.getOriginalSpeed();
        if (player != null) {
            Level level = player.level();
            BlockPos blockPos = player.getOnPos();
            BlockState blockState = event.getState();
            double miningEfficiency;
            double miningSpeed = player.getAttributeValue(BHAttributes.MINING_SPEED.get());
            if (blockState.requiresCorrectToolForDrops()) {
                miningEfficiency = player.getAttributeValue(BHAttributes.MINING_EFFICIENCY.get());
            } else {
                miningEfficiency = 1.0D;
            }
            double bonusMiningSpeed = originalSpeed * miningSpeed * miningEfficiency;
            IAccessoryItemHandler handler = CapabilityCaller.accessory(player);
            if (handler != null) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    final ItemStack itemStack = handler.getStackInSlot(i);
                    if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItems<?> accessoryItems) {
                        for (Accessory trait : accessoryItems.getAccessories()) {
                            Optional<IItemGeneric> optional = trait.IItemGeneric();
                            if (optional.isPresent()) {
                                originalSpeed = optional.get().onModifyMiningSpeed(player, blockState, blockPos, originalSpeed);
                            }
                        }
                    }
                }
            }
            originalSpeed += bonusMiningSpeed;
        }
        event.setNewSpeed((float) originalSpeed);
    }
}
