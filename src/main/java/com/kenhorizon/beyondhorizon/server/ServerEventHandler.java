package com.kenhorizon.beyondhorizon.server;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.world.DamageIndicatorOptions;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryEvent;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItems;
import com.kenhorizon.beyondhorizon.server.capability.*;
import com.kenhorizon.beyondhorizon.server.api.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.enchantment.IAdditionalEnchantment;
import com.kenhorizon.beyondhorizon.server.enchantment.IAttributeEnchantment;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.inventory.AccessoryContainer;
import com.kenhorizon.beyondhorizon.server.level.ICombatCore;
import com.kenhorizon.beyondhorizon.server.level.damagesource.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundPlayerDataSyncPacket;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundRoleClassSyncPacket;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.api.skills.ActiveSkill;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItemHandler;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ServerEventHandler {
    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof Player player) {
                RoleClass role = CapabilityCaller.roleClass(player);
                PlayerData data = CapabilityCaller.data(player);
                NetworkHandler.sendToPlayer(new ClientboundRoleClassSyncPacket(role.saveNbt()), (ServerPlayer) player);
                NetworkHandler.sendToPlayer(new ClientboundPlayerDataSyncPacket(data.saveNbt()), (ServerPlayer) player);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        RoleClass role = CapabilityCaller.roleClass(event.getEntity());
        PlayerData data = CapabilityCaller.data(event.getEntity());
        NetworkHandler.sendToPlayer(new ClientboundRoleClassSyncPacket(role.saveNbt()), (ServerPlayer) event.getEntity());
        NetworkHandler.sendToPlayer(new ClientboundPlayerDataSyncPacket(data.saveNbt()), (ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        RoleClass role = CapabilityCaller.roleClass(event.getEntity());
        PlayerData data = CapabilityCaller.data(event.getEntity());
        NetworkHandler.sendToPlayer(new ClientboundRoleClassSyncPacket(role.saveNbt()), (ServerPlayer) event.getEntity());
        NetworkHandler.sendToPlayer(new ClientboundPlayerDataSyncPacket(data.saveNbt()), (ServerPlayer) event.getEntity());
    }

    @SubscribeEvent
    public void onLivingHealEvent(LivingHealEvent event) {
        float heal = event.getAmount();
        LivingEntity entity = event.getEntity();
        float bonus = (float) (heal * event.getEntity().getAttributeValue(BHAttributes.HEALING.get()));
        if (entity.level() instanceof ServerLevel level) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                float roundedAmount = Math.round(heal * 10) / 10f;
                int intAmount = (int) roundedAmount;
                String text = roundedAmount % 1 == 0 ? String.valueOf(intAmount) : String.valueOf(roundedAmount);
                Vec3 pos = entity.getEyePosition();
                MutableComponent component = Component.literal(text).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD);
                level.sendParticles(new DamageIndicatorOptions(component, false), pos.x, pos.y, pos.z, 1, 0.1D, 0.1D, 0.1D, 0);

            }
        }
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
            event.addCapability(RoleClassCap.NAME, new RoleClassCap());
        }
        if (PlayerDataCap.canAttachTo(entity)) {
            event.addCapability(PlayerDataCap.NAME, new PlayerDataCap((Player) entity));
        }
        if (ActiveSkillCap.canAttachTo(entity)) {
            event.addCapability(ActiveSkillCap.NAME, new ActiveSkillCap());
        }
    }

    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IAccessoryItemHandler.class);
        event.register(ICombatCore.class);
        event.register(IDamageInfo.class);
        event.register(RoleClass.class);
        event.register(PlayerData.class);
        event.register(ActiveSkill.class);
    }


    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            AccessoryContainer.dropContent(player);
        }
    }
    @SubscribeEvent
    public void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        PlayerData playerData = CapabilityCaller.data(player);
        if (!event.isEndConquered()) {
            playerData.setDefaults();
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
            oldPlayer.getCapability(BHCapabilties.PLAYER_DATA).ifPresent(oldData -> {
                player.getCapability(BHCapabilties.PLAYER_DATA).ifPresent(newData -> {
                    newData.loadNbt(oldData.saveNbt());
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
                        Optional<IEntityProperties> optional = accessory.IEntityProperties();
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
    public void onEntityDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        RandomSource random = entity.getRandom();
        int lootingLevel = event.getLootingLevel();
        DamageSource damageSource = event.getSource();
        boolean isPlayerKilled = damageSource.getDirectEntity() == damageSource.getEntity() && damageSource.getEntity() instanceof Player;
        float dropRateIncrease = 0.01F + (0.01F * lootingLevel);
        Collection<ItemEntity> itemEntities = event.getDrops();
        if (entity instanceof Sheep sheep) {
            if (sheep.getColor() == DyeColor.WHITE && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.WHITE_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.ORANGE && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.ORANGE_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.MAGENTA && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.MAGENTA_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.LIGHT_BLUE && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.LIGHT_BLUE_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.YELLOW && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.YELLOW_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.LIME && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.LIME_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.PINK && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.PINK_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.GRAY && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.GRAY_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.LIGHT_GRAY && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.LIGHT_GRAY_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.CYAN && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.CYAN_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.PURPLE && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.PURPLE_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.BLUE && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.BLUE_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.BROWN && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.BROWN_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.GREEN && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.GREEN_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.RED && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.RED_WOOL_FUR.get())));
            }
            if (sheep.getColor() == DyeColor.BLACK && random.nextDouble() <= 1.0F) {
                itemEntities.add(this.createItemDrops(entity, new ItemStack(BHItems.BLACK_WOOL_FUR.get())));
            }
        }
    }

    private ItemEntity createItemDrops(LivingEntity entity, ItemStack itemStack) {
        return new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), itemStack);
    }

    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level instanceof ServerLevel level) {
            level.players().stream().toList().forEach(player -> {
                if (player instanceof ServerPlayer) {
                    PlayerData playerData = CapabilityCaller.data(player);
                    playerData.tick(level);
                }
            });
        }
    }

    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack itemStack = entity.getMainHandItem();
        if (entity instanceof Player player) {
            player.getCapability(BHCapabilties.ACCESSORY).ifPresent(handler -> {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack itemStacks = handler.getStackInSlot(i);
                    if (itemStacks.getItem() instanceof IAccessoryItems<?> items) {
                        for (Accessory accessory : items.getAccessories()) {
                            Optional<IEntityProperties> optional = accessory.IEntityProperties();
                            optional.ifPresent(callback -> callback.onEntityJump(player, itemStacks));
                        }
                    }
                }
            });
        }
    }
    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack itemStack = entity.getMainHandItem();
        ICombatCore combatCore = CapabilityCaller.combat(entity);
        this.onEnchantmentTick(entity);
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
                Optional<IEntityProperties> optional = skill.IEntityProperties();
                optional.ifPresent(callback -> callback.onEntityUpdate(entity, itemStack));
            }
        }
        if (entity instanceof Player player) {
            this.onPlayerTick(player);
            RoleClass roleClass = CapabilityCaller.roleClass(player);
            if (roleClass != null) {
                Optional<IEntityProperties> optional = roleClass.IEntityProperties();
                optional.ifPresent(callback -> callback.onEntityUpdate(entity, itemStack));
            }
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack prevItemStacks = player.getInventory().getItem(i);
                if (!ItemStack.matches(itemStack, prevItemStacks)) {
                    if (!prevItemStacks.isEmpty()) {
                        if (prevItemStacks.getItem() instanceof ISkillItems<?> skillItems) {
                            for (Skill skill : skillItems.getSkills()) {
                                Optional<IEntityProperties> optional = skill.IEntityProperties();
                                skill.removeAttributeModifiers(player, player.getAttributes(), prevItemStacks);
                                optional.ifPresent(iItemGeneric -> iItemGeneric.onChangeEquipment(player, itemStack, true));
                            }
                        }
                    }
                    if (!itemStack.isEmpty()) {
                        if (itemStack.getItem() instanceof ISkillItems<?> skillItems) {
                            for (Skill skill : skillItems.getSkills()) {
                                Optional<IEntityProperties> optional = skill.IEntityProperties();
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
    //TODO: On-Hit Effects
    @SubscribeEvent
    public void onLivingAttackEvent(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        float damage = event.getAmount();
        if (target instanceof Player player) {
            IAccessoryItemHandler handler = CapabilityCaller.accessory(player);
            if (handler != null) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    final ItemStack itemStack = handler.getStackInSlot(i);
                    if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItems<?> container) {
                        for (Accessory trait : container.getAccessories()) {
                            Optional<IAttack> optional = trait.IAttackCallback();
                            optional.ifPresent(callback -> event.setCanceled(callback.canEntiyReceiveDamage(player, target, source)));
                        }
                    }
                }
            }
        }
        if (source.getEntity() instanceof LivingEntity attacker) {
            this.enchantmentOnHitEffect(attacker, damage, source, target);
        }
        AttributeInstance evade = target.getAttribute(BHAttributes.EVADE.get());
        double dodgeChance = 0;
        if (evade != null) {
            dodgeChance = evade.getValue();
        }
        boolean doDodge = target.getRandom().nextDouble() <= dodgeChance;
        if (doDodge && !target.isInvulnerableTo(source)) {
            event.setCanceled(true);
        }
    }
    // TODO: Post Mitigation Damage Handler
    @SubscribeEvent
    public void onLivingDamageEvent(LivingDamageEvent event) {
        float damageDealt = event.getAmount();
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        ICombatCore targetCombatCore = CapabilityCaller.combat(target);
        if (event.isCanceled() || source.is(DamageTypes.GENERIC_KILL) || !(target.level() instanceof ServerLevel level)) return;
        boolean isCrit = false;
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
        if (source.getEntity() instanceof LivingEntity attacker) {
            ItemStack attackerStack = attacker.getMainHandItem();
            if (attacker instanceof Player player) {
                double criticalStrike = player.getAttributeValue(BHAttributes.CRITICAL_CHANCE.get());
                double criticalDamage = player.getAttributeValue(BHAttributes.CRITICAL_DAMAGE.get());
                if (player.getRandom().nextDouble() <= criticalStrike) {
                    isCrit = true;
                    damageDealt = (float) (damageDealt * criticalDamage);
                    player.crit(target);
                }
            }
            damageDealt = this.enchantmentPostMitigationDamage(attacker, damageDealt, source, target);
            if (!attackerStack.isEmpty() && attackerStack.getItem() instanceof ISkillItems<?> container) {
                for (Skill trait : container.getSkills()) {
                    Optional<IAttack> meleeWeaponCallback = trait.IAttackCallback();
                    if (meleeWeaponCallback.isPresent()) {
                        damageDealt = meleeWeaponCallback.get().postMigitationDamage(damageDealt, source, attacker, target);
                        meleeWeaponCallback.get().onHitAttack(source, attackerStack, target, attacker, damageDealt);
                    }
                }
            }
            if (attacker instanceof Player player) {
                RoleClass roleClass = CapabilityCaller.roleClass(player);
                Optional<IAttack> attack = roleClass.IAttack();
                if (attack.isPresent()) {
                    attack.get().onHitAttack(source, attackerStack, target, attacker, damageDealt);
                }
                IAccessoryItemHandler handler = CapabilityCaller.accessory(player);
                if (handler != null) {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        final ItemStack itemStack = handler.getStackInSlot(i);
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItems<?> accessoryItems) {
                            for (Accessory trait : accessoryItems.getAccessories()) {
                                Optional<IAttack> meleeWeaponCallback = trait.IAttackCallback();
                                if (meleeWeaponCallback.isPresent()) {
                                    damageDealt = meleeWeaponCallback.get().postMigitationDamage(damageDealt, source, attacker, target);
                                    meleeWeaponCallback.get().onHitAttack(source, attackerStack, target, attacker, damageDealt);
                                }
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
        if (target instanceof Player player) {
            CapabilityCaller.data(player).setCrit(isCrit);
        }
        float roundedAmount = Math.round(damageDealt * 10) / 10f;
        int intAmount = (int) roundedAmount;
        String text = roundedAmount % 1 == 0 ? String.valueOf(intAmount) : String.valueOf(roundedAmount);
        Vec3 pos = target.getEyePosition();
        MutableComponent component = Component.literal(text).withStyle(isCrit ? ChatFormatting.DARK_RED : ChatFormatting.GOLD, ChatFormatting.BOLD);
        level.sendParticles(new DamageIndicatorOptions(component, isCrit), pos.x, pos.y, pos.z, 1, 0.1D, 0.1D, 0.1D, 0);
        event.setAmount(damageDealt);
    }
    //TODO: Enchantment Post Mitigation Damage
    private float enchantmentPostMitigationDamage(LivingEntity attacker, float damageDealt, DamageSource source, LivingEntity target) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(attacker.getItemBySlot(slot));
            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                if (entry.getKey() instanceof IAdditionalEnchantment additionalEnchantment) {
                    Optional<IAdditionalEnchantment> optional = additionalEnchantment.enchantmentCallback();
                    if (optional.isPresent()) {
                        damageDealt = optional.get().postMigitationDamage(entry.getValue(), damageDealt, source, attacker, target);
                    }
                }
            }
        }
        return damageDealt;
    }
    //TODO: Enchantment Post Mitigation Damage
    private void enchantmentOnHitEffect(LivingEntity attacker, float damageDealt, DamageSource source, LivingEntity target) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(attacker.getItemBySlot(slot));
            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                if (entry.getKey() instanceof IAdditionalEnchantment additionalEnchantment) {
                    Optional<IAdditionalEnchantment> optional = additionalEnchantment.enchantmentCallback();
                    if (optional.isPresent()) {
                        optional.get().onHitAttack(entry.getValue(), source, attacker.getItemBySlot(slot), attacker, target, damageDealt);
                    }
                }
            }
        }
    }
    public void onEnchantmentTick(LivingEntity entity) {
        if (entity == null) return;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS) {
                int enchantmentLevels = EnchantmentHelper.getEnchantmentLevel(enchantment, entity);
                if (enchantmentLevels > 0) {
                    ((IAttributeEnchantment) enchantment).addAttributeModifiers(entity, entity.getAttributes(), enchantmentLevels, getArmorAttributeMultiplier(slot, entity));
                } else {
                    ((IAttributeEnchantment) enchantment).removeAttributeModifiers(entity, entity.getAttributes());
                }
            }
        }
    }
    // TODO: Check if each armor slot have items if you have items will add one if not minus one
    private int getArmorAttributeMultiplier(EquipmentSlot slot, LivingEntity entity) {
        int armorAttributeMultiplier = 0;
        if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            armorAttributeMultiplier += checkArmorAttributeMultiplier(EquipmentSlot.HEAD, entity);
            armorAttributeMultiplier += checkArmorAttributeMultiplier(EquipmentSlot.CHEST, entity);
            armorAttributeMultiplier += checkArmorAttributeMultiplier(EquipmentSlot.LEGS, entity);
            armorAttributeMultiplier += checkArmorAttributeMultiplier(EquipmentSlot.FEET, entity);
        }
        return Mth.clamp(armorAttributeMultiplier, 0, 4);
    }

    private int checkArmorAttributeMultiplier(EquipmentSlot slot, LivingEntity entity) {
        ItemStack checkStacks = entity.getItemBySlot(slot);
        if (checkStacks.isEmpty() && checkStacks.isEnchanted()) {
            return -1;
        } else {
            return 1;
        }
    }

    // TODO: Pre Mitigation Damage Handler
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
                if (attacker instanceof Player player) {
                    RoleClass roleClass = CapabilityCaller.roleClass(player);
                    Optional<IAttack> attack = roleClass.IAttack();
                    if (attack.isPresent()) {
                        damageDealt = attack.get().preMigitationDamage(damageDealt, source, attacker, target);
                    }
                    IAccessoryItemHandler handler = CapabilityCaller.accessory(player);
                    if (handler != null) {
                        for (int i = 0; i < handler.getSlots(); i++) {
                            final ItemStack itemStack = handler.getStackInSlot(i);
                            if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItems<?> container) {
                                for (Accessory trait : container.getAccessories()) {
                                    Optional<IAttack> meleeWeaponCallback = trait.IAttackCallback();
                                    if (meleeWeaponCallback.isPresent()) {
                                        damageDealt = meleeWeaponCallback.get().preMigitationDamage(damageDealt, source, attacker, target);
                                    }
                                }
                            }
                        }
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
    public void onExperienceDropEvent(LivingExperienceDropEvent event) {
        LivingEntity target = event.getEntity();
        Player player = event.getAttackingPlayer();
        if (player == null || target == null) return;
        int droppedExperience = event.getDroppedExperience();
        int originalExperience = event.getOriginalExperience();
        int modifiyDropExperience = 0;
        ItemStack itemStack = player.getMainHandItem();
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ISkillItems<?> skillItems) {
            for (Skill skill : skillItems.getSkills()) {
                Optional<IEntityProperties> callback = skill.IEntityProperties();
                if (callback.isPresent()) {
                    modifiyDropExperience = callback.get().modifyExprienceDrop(droppedExperience, target, player);
                }
            }
        }
        IAccessoryItemHandler handler = CapabilityCaller.accessory(player);
        if (handler != null) {
            for (int i = 0; i < handler.getSlots(); i++) {
                final ItemStack stackInSlot = handler.getStackInSlot(i);
                if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IAccessoryItems<?> container) {
                    for (Accessory trait : container.getAccessories()) {
                        Optional<IEntityProperties> callback = trait.IEntityProperties();
                        if (callback.isPresent()) {
                            modifiyDropExperience = callback.get().modifyExprienceDrop(droppedExperience, target, player);
                        }
                    }
                }
            }
        }
        event.setDroppedExperience(modifiyDropExperience);
    }

    @SubscribeEvent
    public void onKilledEntiy(LivingDeathEvent event) {
        LivingEntity target = event.getEntity();
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof LivingEntity attacker) {
            ICombatCore attackerCombatCore = CapabilityCaller.combat(attacker);
            ItemStack attackerStack = attacker.getMainHandItem();
            if (attacker instanceof Player player) {
                RoleClass roleClass = CapabilityCaller.roleClass(player);
                Optional<IAttack> attack = roleClass.IAttack();
                attack.ifPresent(iAttack -> iAttack.onEntityKilled(source, attacker, target));
            }
            if (!attackerStack.isEmpty() && attackerStack.getItem() instanceof ISkillItems<?> container) {
                for (Skill trait : container.getSkills()) {
                    Optional<IAttack> meleeWeaponCallback = trait.IAttackCallback();
                    meleeWeaponCallback.ifPresent(iAttack -> iAttack.onEntityKilled(source, attacker, target));
                }
            }
        }
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
                            Optional<IEntityProperties> optional = trait.IEntityProperties();
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
