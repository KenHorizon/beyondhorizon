package com.kenhorizon.beyondhorizon.server;

import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.Fonts;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.enchantment.*;
import com.kenhorizon.beyondhorizon.server.level.damagesource.AdvanceDamageSource;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageTags;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundPlayerLevelSystemPacket;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import com.kenhorizon.beyondhorizon.server.util.*;
import com.kenhorizon.libs.server.event.MobEffectModificationEvent;
import com.kenhorizon.beyondhorizon.client.particle.world.DamageIndicatorOptions;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.api.accessory.*;
import com.kenhorizon.beyondhorizon.server.api.armor_ability.ArmorAbility;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerDataHelper;
import com.kenhorizon.beyondhorizon.server.api.inventory.IStackHandler;
import com.kenhorizon.beyondhorizon.server.api.level_system.LevelSystem;
import com.kenhorizon.beyondhorizon.server.api.event.HarvestBlockEvent;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.IStackableInstance;
import com.kenhorizon.beyondhorizon.server.capability.*;
import com.kenhorizon.beyondhorizon.server.api.IAttack;
import com.kenhorizon.beyondhorizon.server.api.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.init.*;
import com.kenhorizon.beyondhorizon.server.item.ILeftClick;
import com.kenhorizon.beyondhorizon.server.item.QuiverItem;
import com.kenhorizon.beyondhorizon.server.api.level.ICombatData;
import com.kenhorizon.beyondhorizon.server.api.level.IDamageInfo;
import com.kenhorizon.beyondhorizon.server.listeners.SpawnerBuilderListener;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundAccessoryPacket;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundPlayerDataPacket;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundPlayerSwingArmPacket;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerEventHandler {

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new SpawnerBuilderListener(event.getConditionContext()));
    }

    @SubscribeEvent
    public void onHarvestBlockBreak(HarvestBlockEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemStackUse();
        BlockState blockState = event.getState();
        BlockPos blockPos = event.getPos();
        boolean flag = true;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(player.getItemBySlot(slot));
            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                if (entry.getKey() instanceof IAdditionalEnchantment additionalEnchantment) {
                    Optional<IAdditionalEnchantment> optional = additionalEnchantment.enchantmentCallback();
                    if (optional.isPresent()) {
                        event.setCanDropLoot(optional.get().onHarverstDrop(entry.getValue(), player, event.getLevel(), itemStack, blockPos, blockState, event.getItemDrops()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onParticleEffect(MobEffectModificationEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (player.isInvisible() && AccessoryHelper.getAccessory(player, Accessories.STALKER.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
        Entity getEntity = event.getEntity();
        var world = event.getLevel();
       if (!world.isClientSide()) {
           if (getEntity instanceof LivingEntity entity) {
               LevelSystem levelSystem = Capabilities.levelSystem(entity);
               if (levelSystem != null) {
                   levelSystem.sync();
               }
           }
       }
    }

    @SubscribeEvent
    public void onFinalizeSpawnEvent(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        MobSpawnType spawnType = event.getSpawnType();
        LevelSystem levelSystem = Capabilities.levelSystem(mob);
        if (!mob.level().isClientSide() && levelSystem != null) {
            if (!levelSystem.isPlayer() && BHConfigs.ENABLE_MOB_LEVELS) {
                int randomLevels = mob.getRandom().nextIntBetweenInclusive(5, BHConfigs.MOBS_MAX_LEVEL_CAP);
                levelSystem.setLevel(randomLevels);
                levelSystem.assignRandomPoints();
                if (spawnType != MobSpawnType.CONVERSION) {
                    mob.setHealth(mob.getMaxHealth());
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            PlayerData playerData = Capabilities.data(serverPlayer);
            AccessoryHelper.getInventory(player).ifPresent(handler -> {
                NetworkHandler.sendToPlayer(new ClientboundAccessoryPacket(handler.serializeNBT()), serverPlayer);
            });
            playerData.syncData();
            playerData.syncCooldowns();
        }
    }

    @SubscribeEvent
    public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            LevelSystem role = Capabilities.levelSystem(player);
            PlayerData playerData = Capabilities.data(player);
            AccessoryHelper.getInventory(serverPlayer).ifPresent(handler -> {
                NetworkHandler.sendToPlayer(new ClientboundAccessoryPacket(handler.serializeNBT()), serverPlayer);
            });
            NetworkHandler.sendToPlayer(new ClientboundPlayerLevelSystemPacket(role.saveNbt()), serverPlayer);
            NetworkHandler.sendToPlayer(new ClientboundPlayerDataPacket(playerData.saveNbt()), serverPlayer);
        }
    }


    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            LevelSystem role = Capabilities.levelSystem(player);
            PlayerData data = Capabilities.data(player);
            NetworkHandler.sendToPlayer(new ClientboundPlayerLevelSystemPacket(role.saveNbt()), serverPlayer);
            data.setMana(data.getMaxMana());
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        Player oldPlayer = event.getOriginal();
        oldPlayer.revive();
        LazyOptional<IAccessoryStackHandler> oldAccHandler = AccessoryHelper.getInventory(oldPlayer);
        LazyOptional<IAccessoryStackHandler> newAccHandler = AccessoryHelper.getInventory(player);
        oldAccHandler.ifPresent(oldAcc -> newAccHandler.ifPresent(newAcc -> newAcc.deserializeNBT(oldAcc.serializeNBT())));

        LazyOptional<PlayerData> oldPlayerDataHandler = PlayerDataHelper.getPlayerData(oldPlayer);
        LazyOptional<PlayerData> newPlayerDataHandler = PlayerDataHelper.getPlayerData(player);
        oldPlayerDataHandler.ifPresent(oldAcc -> newPlayerDataHandler.ifPresent(newAcc -> newAcc.loadNbt(oldAcc.saveNbt())));
    }



    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (event.level instanceof ServerLevel level) {
            level.players().stream().toList().forEach(player -> {
                if (player instanceof ServerPlayer) {
                    PlayerData playerData = Capabilities.data(player);
                    playerData.tick(level);
                }
            });
        }
    }
    @SubscribeEvent
    public void onLevelTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient() && event.phase == TickEvent.Phase.END && event.player == BeyondHorizon.PROXY.clientPlayer()) {
            Player player = event.player;
            Level level = player.level();
            PlayerData playerData = Capabilities.data(player);
            if (playerData == null) return;
            playerData.tick(level);
        }
    }
    //TODO
    @SubscribeEvent
    public void onLivingHealEvent(LivingHealEvent event) {
        float heal = event.getAmount();
        LivingEntity entity = event.getEntity();
        float bonus = (float) (heal * event.getEntity().getAttributeValue(BHAttributes.HEALING.get()));
        float finalHeal = heal + bonus;
        if (entity.level() instanceof ServerLevel level) {
            if (entity.getHealth() < entity.getMaxHealth() && finalHeal > 10.0F) {
                if (BHConfigs.DAMAGE_INDICATOR) {
                    damageIndicator(level, entity.getLastDamageSource(), true, finalHeal, entity);
                }
            }
        }
        event.setAmount(finalHeal);
    }


    @SubscribeEvent
    public void onFarmLandTrample(BlockEvent.FarmlandTrampleEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (entity != null) {
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, entity) > 0) {
                event.setCanceled(true);
            }
            if (entity instanceof Player player) {
                if (AccessoryHelper.getAccessory(player, Accessories.FEATHER_FEET.get())) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity getEntity = event.getObject();
        if (getEntity instanceof LivingEntity entity) {
            if (AccessoryInventoryCap.canAttachTo(entity) && !event.getCapabilities().containsKey(BHCapabilties.ID_ACCESSORY)) {
                event.addCapability(BHCapabilties.ID_ACCESSORY, new AccessoryInventoryCap((Player) entity));
            }
            if (DamageInfoCap.canAttachTo(entity) && !event.getCapabilities().containsKey(BHCapabilties.ID_DAMAGE_INFO)) {
                event.addCapability(BHCapabilties.ID_DAMAGE_INFO, new DamageInfoCap());
            }
            if (CombatDataCap.canAttachTo(entity) && !event.getCapabilities().containsKey(BHCapabilties.ID_COMBAT_INFO)) {
                event.addCapability(BHCapabilties.ID_COMBAT_INFO, new CombatDataCap());
            }
            if (LevelSystemsCap.canAttachTo(entity) && !event.getCapabilities().containsKey(BHCapabilties.ID_LEVEL_SYSTEM)) {
                event.addCapability(BHCapabilties.ID_LEVEL_SYSTEM, new LevelSystemsCap((LivingEntity) entity));
            }
            if (PlayerDataCap.canAttachTo(entity) && !event.getCapabilities().containsKey(BHCapabilties.ID_PLAYER_DATA)) {
                event.addCapability(BHCapabilties.ID_PLAYER_DATA, new PlayerDataCap((Player) entity));
            }
            if (StackableTagCap.canAttachTo(entity) && !event.getCapabilities().containsKey(BHCapabilties.ID_STACKABLE_TAGS)) {
                event.addCapability(BHCapabilties.ID_STACKABLE_TAGS, new StackableTagCap((LivingEntity) entity));
            }
        }
    }

    @SubscribeEvent
    public void onAttachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        Item item = stack.getItem();
        if (item instanceof IAccessoryItem accessoryItem && accessoryItem.hasCapability(stack)) {
            ItemizedAccessoryCap itemizedCapability = new ItemizedAccessoryCap(accessoryItem, stack);
            event.addCapability(BHCapabilties.ID_ITEM, AccessoryItemCap.createProvider(itemizedCapability));
        }
        if (item instanceof ISkillItems skillItems && skillItems.hasCapability(stack)) {
            ItemizedSkillsCap itemizedCapability = new ItemizedSkillsCap(skillItems, stack);
            event.addCapability(BHCapabilties.ID_SKILL_SLOTS, new SkillSlotsCap(skillItems));
            event.addCapability(BHCapabilties.ID_ITEM, SkillItemCap.createProvider(itemizedCapability));
        }
    }
    @SubscribeEvent
    public void playerStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            this.syncSlot(serverPlayer);
        }
    }

    private void syncSlot(ServerPlayer player) {
        player.getCapability(BHCapabilties.ACCESSORY).ifPresent(handler -> {
            for (int i = 0; i < handler.getStacks().getSlots(); i++) {
                ItemStack itemStack = handler.getStacks().getStackInSlot(i);
                BeyondHorizon.PROXY.syncAccessoryToPlayer(i, itemStack, player);
            }
        });
    }


    @SubscribeEvent
    public void onEquipmentChangeEvent(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();
        EquipmentSlot slot = event.getSlot();
        EnchantmentSlotContext slotContext = new EnchantmentSlotContext(AdvancedEnchantment.ENCHANTMENT_UUID, entity, slot);
        if (!from.isEmpty()) {
            if (!from.isEmpty()) {
                if (from.getItem() instanceof ISkillItems skillItems) {
                    for (Skill skill : skillItems.getSkills()) {
                        Optional<IEntityProperties> optional = skill.entityProperties();
                        skill.removeAttributeModifiers(entity, entity.getAttributes(), to);
                        optional.ifPresent(iItemGeneric -> iItemGeneric.onUnequipEquipment(entity, to));
                    }
                }
            }
            var fromStackEnchantment = EnchantmentHelper.getEnchantments(from);
            UUID uuid = ExtendedEnchantmentHelper.getSlotUuid(slotContext);
            for (var enchants : fromStackEnchantment.entrySet()) {
                if (enchants.getKey() instanceof IAttributeEnchantment instance) {
                    Multimap<Attribute, AttributeModifier> map = ExtendedEnchantmentHelper.getAttributeModifiers(uuid, from);
                    entity.getAttributes().removeAttributeModifiers(map);
                }
            }
        }
        if (!to.isEmpty()) {
            if (!to.isEmpty()) {
                if (to.getItem() instanceof ISkillItems skillItems) {
                    for (Skill skill : skillItems.getSkills()) {
                        Optional<IEntityProperties> optional = skill.entityProperties();
                        skill.addAttributeModifiers(entity, entity.getAttributes(), to);
                        optional.ifPresent(iItemGeneric -> iItemGeneric.onEquipEquipment(entity, to));
                    }
                }
            }
            var toStackEnchantment = EnchantmentHelper.getEnchantments(to);
            UUID uuid = ExtendedEnchantmentHelper.getSlotUuid(slotContext);
            for (var enchants : toStackEnchantment.entrySet()) {
                if (enchants.getKey() instanceof IAttributeEnchantment instance) {
                    Multimap<Attribute, AttributeModifier> map = ExtendedEnchantmentHelper.getAttributeModifiers(uuid, to);
                    entity.getAttributes().addTransientAttributeModifiers(map);
                }
            }
        }
    }


    // TODO: ACCESSORY LOGICS
    private void accessoryLogics(Player player) {
        AccessoryHelper.getInventory(player).ifPresent(handler -> {
            var stacks = handler.getStacks();
            for (int inv = 0; inv < player.getInventory().getContainerSize(); inv++) {
                ItemStack itemStacks = player.getInventory().getItem(inv);
                if (!itemStacks.isEmpty() && itemStacks.getItem() instanceof IAccessoryItem) {
                    itemStacks.inventoryTick(player.level(), player, inv, false);
                }
            }
            for (int i = 0; i < handler.getStacks().getSlots(); i++) {
                AccessorySlotContext slotContext = new AccessorySlotContext(Accessory.ACCESSORY_UUID, player, i);
                ItemStack itemStacks = handler.getStacks().getStackInSlot(i);
                if (!itemStacks.isEmpty()) {
                    itemStacks.inventoryTick(player.level(), player, -1, true);
                }
                if (itemStacks.getItem() instanceof IAccessoryItem item) {
                    for (Accessory accessory : item.getAccessories()) {
                        Optional<IEntityProperties> optional = accessory.entityProperties();
                        optional.ifPresent(callback -> callback.onEntityUpdate(player, itemStacks));
                    }
                }
                if (!player.level().isClientSide()) {
                    ItemStack prevItemStack = handler.getStacks().getPreviousItemStack(i);
                    if (!ItemStack.matches(itemStacks, prevItemStack)) {
                        UUID uuid = AccessoryHelper.getSlotUuid(slotContext);
                        if (!prevItemStack.isEmpty()) {
                            Multimap<Attribute, AttributeModifier> map = AccessoryHelper.getAttributeModifiers(uuid, prevItemStack);
                            player.getAttributes().removeAttributeModifiers(map);
                            if (prevItemStack.getItem() instanceof IAccessoryItem item) {
                                for (Accessory accessory : item.getAccessories()) {
                                    accessory.removeAttributeModifiers(player, map);
                                    Optional<IAccessoryEvent> optional = accessory.accessory();
                                    if (optional.isPresent()) {
                                        optional.get().onUnequip(player, prevItemStack, i);
                                    }
                                }
                            }
                        }
                        if (!itemStacks.isEmpty()) {
                            Multimap<Attribute, AttributeModifier> map = AccessoryHelper.getAttributeModifiers(uuid, itemStacks);
                            player.getAttributes().addTransientAttributeModifiers(map);
                            if (itemStacks.getItem() instanceof IAccessoryItem item) {
                                for (Accessory accessory : item.getAccessories()) {
                                    Optional<IAccessoryEvent> optional = accessory.accessory();
                                    if (optional.isPresent()) {
                                        optional.get().onEquip(player, itemStacks, i);
                                    }
                                }
                            }
                        }
                        stacks.setPreviousItemStack(i, itemStacks.copy());
                    }
                }
            }
        });
    }
    //TODO: DROP
    @SubscribeEvent
    public void onEntityDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        RandomSource random = entity.getRandom();
        int lootingLevel = event.getLootingLevel();
        DamageSource damageSource = event.getSource();
        boolean isPlayerKilled = damageSource.getDirectEntity() == damageSource.getEntity() && damageSource.getEntity() instanceof Player;
        float dropRateIncrease = 0.01F + (0.01F * lootingLevel);
        Collection<ItemEntity> entityDrops = event.getDrops();
        if (!entity.isSpectator()) {
            if (damageSource.getEntity() instanceof LivingEntity attacker) {
                if (attacker instanceof Player player) {
                    if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
                        IAccessoryStackHandler handler = AccessoryHelper.getInventory(player).resolve().get();
                        var stacks = handler.getStacks();
                        for (int i = 0; i < stacks.getSlots(); i++) {
                            ItemStack itemStacks = stacks.getStackInSlot(i);
                            if (itemStacks.getItem() instanceof IAccessoryItem items) {
                                for (Accessory accessory : items.getAccessories()) {
                                    Optional<IEntityProperties> properties = accessory.entityProperties();
                                    if (properties.isPresent()) {
                                        var newDrops = properties.get().modifyLootdrops(entity, player, entityDrops);
                                        if (!newDrops.isEmpty()) {
                                            entityDrops.addAll(newDrops);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (entity instanceof Hoglin) {
                if (random.nextDouble() <= 0.35F && isPlayerKilled) {
                    entityDrops.add(this.createItemDrops(entity, new ItemStack(BHItems.HOGLIN_TUSK.get())));
                }
            }
            if (entity instanceof EnderMan) {
                if (random.nextDouble() <= 0.35F && isPlayerKilled) {
                    entityDrops.add(this.createItemDrops(entity, new ItemStack(BHItems.DUSK_LEATHER.get(), this.getRandomizedDropCount(random, 2, 5, lootingLevel))));
                }
            }
            if (entity instanceof Evoker) {
                if (this.getRandomizedDrop(random, 0.25F, dropRateIncrease) && isPlayerKilled) {
                    entityDrops.add(createItemDrops(entity, new ItemStack(BHItems.AMPLIFLYING_TOME.get(),
                            this.getRandomizedDropCount(random,2,3, lootingLevel))));
                }
                if (random.nextDouble() <= 0.05D && isPlayerKilled) {
                    entityDrops.add(createItemDrops(entity, new ItemStack(BHItems.ANKH_ETERNITY.get())));
                }
            }
            if (entity instanceof Shulker) {
                if (this.getRandomizedDrop(random, 0.05F, dropRateIncrease) && isPlayerKilled) {
                    entityDrops.add(createItemDrops(entity, new ItemStack(BHItems.BROKEN_SHULKER_SHELL.get())));
                }
            }
            if (entity instanceof Player player) {
                AccessoryHelper.getInventory(player).ifPresent(handler -> {
                    Collection<ItemEntity> drops = new ArrayList<>();
                    var stacks = handler.getStacks();
                    boolean keepInventory = player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
                    boolean finalKeepInventory = keepInventory;
                    this.handleDrops(player, drops, stacks, finalKeepInventory);
                    entityDrops.addAll(drops);
                });
            }
        }
    }
    private int getRandomizedDropCount(RandomSource random, int min, int max, int bonus) {
        return random.nextIntBetweenInclusive(min, max) + bonus;
    }
    private boolean getRandomizedDrop(RandomSource random, float chance) {
        return getRandomizedDrop(random, chance, 0.0F);
    }
    private boolean getRandomizedDrop(RandomSource random, float chance, float bonus) {
        return random.nextDouble() <= Mth.clamp((chance + bonus), 0.0F, 1.0F);
    }

    private void handleDrops(Player player, Collection<ItemEntity> drops, IStackHandler stacks, boolean keepInv) {
        for (int i = 0; i < stacks.getSlots();i++) {
            ItemStack stack = stacks.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (keepInv) {
                    continue;
                }
                if (!EnchantmentHelper.hasVanishingCurse(stack)) {
                    drops.add(getDroppedItem(stack, player));
                }
                stacks.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    private ItemEntity getDroppedItem(ItemStack droppedItem, LivingEntity entity) {
        double d0 = entity.getY() - 0.30000001192092896D + entity.getEyeHeight();
        ItemEntity entityitem = new ItemEntity(entity.level(), entity.getX(), d0, entity.getZ(), droppedItem);
        entityitem.setPickUpDelay(40);
        float f = entity.level().random.nextFloat() * 0.5F;
        float f1 = entity.level().random.nextFloat() * ((float) Math.PI * 2F);
        entityitem.setDeltaMovement((-Mth.sin(f1) * f), 0.20000000298023224D, (Mth.cos(f1) * f));
        return entityitem;
    }

    private ItemEntity createItemDrops(LivingEntity entity, ItemStack itemStack) {
        return new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), itemStack);
    }
    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack itemStack = entity.getMainHandItem();
        if (entity instanceof Player player) {
            AccessoryHelper.getInventory(player).ifPresent(handler -> {
                var stacks = handler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack itemStacks = stacks.getStackInSlot(i);
                    if (itemStacks.getItem() instanceof IAccessoryItem items) {
                        for (Accessory accessory : items.getAccessories()) {
                            Optional<IEntityProperties> optional = accessory.entityProperties();
                            optional.ifPresent(callback -> callback.onEntityJump(player, itemStacks));
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public void onPotionSystemEvent(PotionColorCalculationEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (player.isInvisible() && AccessoryHelper.getAccessory(player, Accessories.STALKER.get())) {
                event.shouldHideParticles(true);
            }
        }
    }

    @SubscribeEvent
    public void onEndermanAnger(EnderManAngerEvent event) {
        Player player = event.getPlayer();
        boolean flag = false;
        if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
            IAccessoryStackHandler handler = AccessoryHelper.getInventory(player).resolve().get();
            var stacks = handler.getStacks();

            all:
            for (int i = 0; i < stacks.getSlots(); i++) {
                ItemStack itemStacks = stacks.getStackInSlot(i);
                if (itemStacks.getItem() instanceof IAccessoryItem items) {
                    flag = items.isEndermanMask(player, event.getEntity());
                    if (flag) {
                        event.setCanceled(true);
                        break all;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPotionModification(MobEffectModificationEvent event) {
        LivingEntity entity = event.getEntity();
        var instance = event.getEffectInstance();
        MobEffectInstance newInstance = null;
        if (entity instanceof Player player) {
            if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
                IAccessoryStackHandler handler = AccessoryHelper.getInventory(player).resolve().get();
                var stacks = handler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack itemStacks = stacks.getStackInSlot(i);
                    if (itemStacks.getItem() instanceof IAccessoryItem items) {
                        for (Accessory accessory : items.getAccessories()) {
                            Optional<IEntityProperties> optional = accessory.entityProperties();
                            if (optional.isPresent()) {
                                newInstance = optional.get().onMobEffectApplied(entity, instance);
                                if (newInstance != null) {
                                    instance = newInstance;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (newInstance == null) return;
//        BeyondHorizon.LOGGER.info("Event Debug | NEW={} | OLD={}", newInstance, instance);
        event.setEffectInstance(instance);
    }

    @SubscribeEvent
    public void onLivingEntitySpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mobs = event.getEntity();
        var spawnType = event.getSpawnType();
        if (spawnType == MobSpawnType.SPAWNER) {
            GlobalTags.setSpawner(mobs, true);
        }
    }

    //TODO: Living Tick Update
    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack itemStack = entity.getMainHandItem();
        ICombatData combatCore = Capabilities.combat(entity);
        IStackableInstance stackableTags = Capabilities.stackable(entity);
        var levelSystem = Capabilities.levelSystem(entity);
        if (levelSystem != null) {
            levelSystem.tick();
        }
        if (stackableTags != null) {
            stackableTags.tick(entity);
        }
        if (combatCore != null) {
            combatCore.tick();
            IDamageInfo damageInfo = Capabilities.damageInfo(entity);
            if (damageInfo != null && !combatCore.OnCombat()) {
                damageInfo.reset();
            }
        }
        if (entity.hasEffect(BHEffects.LETHAL_PROTECTION_COOLDOWN.get())) {
            entity.removeEffect(BHEffects.LETHAL_PROTECTION.get());
        }
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ISkillItems items) {
            for (Skill skill : items.getSkills()) {
                Optional<IEntityProperties> optional = skill.entityProperties();
                optional.ifPresent(callback -> callback.onEntityUpdate(entity, itemStack));
            }
        }

        Set<ResourceLocation> active = ArmorAbility.ACTIVE_SETS.computeIfAbsent(entity.getUUID(), id -> new HashSet<>());
        for (ArmorAbility set : BHRegistries.ARMOR_ABILITY_KEY.get()) {
            boolean matches = set.matches(entity);
            boolean applied = active.contains(set.getResourceId());
            if (matches && !applied) {
                set.applyBonus(entity);
                active.add(set.getResourceId());
            }
            if (!matches && applied) {
                set.removeBonus(entity);
                active.remove(set.getResourceId());
            }
        }

        if (entity instanceof Player player) {
            this.accessoryLogics(player);
            float healthRegen = (float) player.getAttributeValue(BHAttributes.HEALTH_REGENERATION.get());
            if (player.tickCount % 10 == 0) {
                player.heal(healthRegen);
            }
        }
        if (entity instanceof Mob mobs) {
            LivingEntity target = mobs.getTarget();
            LivingEntity getLastHurtByMob = mobs.getLastHurtByMob();
            double followRange = mobs.getAttributeValue(Attributes.FOLLOW_RANGE);
            if (mobs.hasEffect(MobEffects.BLINDNESS)) {
                if (target != null) {
                    if (entity.distanceTo(target) > 5) {
                        mobs.setTarget(null);
                        mobs.setLastHurtByMob(null);
                    }
                }
            }
            if (getLastHurtByMob instanceof Player player) {
                double stealth = player.getAttributeValue(BHAttributes.STEALTH.get());
                if (stealth == 0.0D) return;
                if (entity.distanceTo(player) > (Math.max(followRange * stealth, 5))) {
                    mobs.setLastHurtByMob(null);
                }
            }
            if (target instanceof Player player) {
                double stealth = player.getAttributeValue(BHAttributes.STEALTH.get());
                if (stealth == 0.0D) return;
                if (entity.distanceTo(player) > (Math.max(followRange * stealth, 5))) {
                    mobs.setTarget(null);
                }
            }
        }
    }
    @SubscribeEvent
    public void onVisibilityChanged(LivingEvent.LivingVisibilityEvent event) {
        LivingEntity entity = event.getEntity();
        Entity entityLookedAt = event.getLookingEntity();
        double multiplier = 1.0D;
        if (entity instanceof Player player) {
            multiplier = 1.0D - player.getAttributeValue(BHAttributes.STEALTH.get());
        }
        if (entityLookedAt instanceof LivingEntity targetEntity) {
            if (targetEntity.hasEffect(MobEffects.BLINDNESS)) {
                multiplier = 0.05D;
            }
        }
        event.modifyVisibility(multiplier);
    }
    //TODO: On-Hit Effects
    @SubscribeEvent
    public void onLivingAttackEvent(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        if (target instanceof Player player) {
            AccessoryHelper.getInventory(player).ifPresent(handler -> {
                var stacks = handler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    final ItemStack itemStack = stacks.getStackInSlot(i);
                    if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem container) {
                        for (Accessory trait : container.getAccessories()) {
                            Optional<IAttack> optional = trait.attack();
                            optional.ifPresent(callback -> event.setCanceled(callback.canEntiyReceiveDamage(player, target, source)));
                        }
                    }
                }
            });
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
        ItemStack targetMainHandItem = target.getMainHandItem();
        ICombatData targetCombatCore = Capabilities.combat(target);
        if (event.isCanceled() || source.is(DamageTypes.GENERIC_KILL) || !(target.level() instanceof ServerLevel level)) return;
        if (source.getEntity() instanceof LivingEntity attacker) {
            ItemStack attackerStack = attacker.getMainHandItem();
            for (ArmorAbility set : BHRegistries.ARMOR_ABILITY_KEY.get()) {
                boolean matches = set.matches(attacker);
                if (matches) {
                    var attack = set.attack();
                    if (attack.isPresent()) {
                        damageDealt = attack.get().postMigitationDamage(new DamageContext(damageDealt), source, attacker, target);
                        attack.get().onHitAttack(source, attackerStack, target, attacker, new DamageContext(damageDealt));
                    }
                }
            }
            this.enchantmentOnHitEffect(attacker, damageDealt, source, target);
            damageDealt = this.enchantmentPostMitigationDamage(attacker, damageDealt, source, target);
            if (!attackerStack.isEmpty() && attackerStack.getItem() instanceof ISkillItems container) {
                for (Skill skill : container.getSkills()) {
                    Optional<IAttack> attack = skill.attack();
                    if (attack.isPresent()) {
                        damageDealt = attack.get().postMigitationDamage(new DamageContext(damageDealt), source, attacker, target);
                        attack.get().onHitAttack(source, attackerStack, target, attacker, new DamageContext(damageDealt));
                    }
                }
            }
            if (attacker instanceof Player player) {
                if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
                    IAccessoryStackHandler handler = AccessoryHelper.getInventory(player).resolve().get();
                    var stacks = handler.getStacks();
                    for (int i = 0; i < stacks.getSlots(); i++) {
                        final ItemStack itemStack = stacks.getStackInSlot(i);
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem accessoryItems) {
                            for (Accessory trait : accessoryItems.getAccessories()) {
                                Optional<IAttack> attack = trait.attack();
                                if (attack.isPresent()) {
                                    damageDealt = attack.get().postMigitationDamage(new DamageContext(damageDealt), source, attacker, target);
                                    attack.get().onHitAttack(source, attackerStack, target, attacker, new DamageContext(damageDealt));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!targetMainHandItem.isEmpty() && target.getMainHandItem().getItem() instanceof ISkillItems container) {
            for (Skill trait : container.getSkills()) {
                Optional<IAttack> attack = trait.attack();
                if (attack.isPresent()) {
                    damageDealt = attack.get().damageTaken(new DamageContext(damageDealt), source, target);
                }
            }
        }

        this.enchantmentDamageTaken(damageDealt, source, target);
        for (ArmorAbility set : BHRegistries.ARMOR_ABILITY_KEY.get()) {
            boolean matches = set.matches(target);
            if (matches) {
                var weaponCallback = set.attack();
                if (weaponCallback.isPresent()) {
                    damageDealt = weaponCallback.get().damageTaken(new DamageContext(damageDealt), source, target);
                }
            }
        }
        if (target instanceof Player player) {
            if (source.getEntity() instanceof LivingEntity lEntity && lEntity instanceof EnderDragon) {
                player.addEffect(new MobEffectInstance(BHEffects.DRAGONIC_FLAME.get(), Maths.sec(3), 1));
            }
            if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
                IAccessoryStackHandler handler = AccessoryHelper.getInventory(player).resolve().get();
                var stacks = handler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    final ItemStack itemStack = stacks.getStackInSlot(i);
                    if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem accessoryItems) {
                        for (Accessory trait : accessoryItems.getAccessories()) {
                            Optional<IAttack> attack = trait.attack();
                            if (attack.isPresent()) {
                                if (trait.getTags().isUnique()) {
                                    damageDealt = attack.get().damageTaken(new DamageContext(damageDealt), source, target);
                                } else {
                                    if (attack.get().damageTaken(new DamageContext(damageDealt), source, target) != damageDealt) {
                                        damageDealt += attack.get().damageTaken(new DamageContext(damageDealt), source, target);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        damageDealt *= (float) target.getAttributeValue(BHAttributes.DAMAGE_TAKEN.get());
        IDamageInfo damageInfo = Capabilities.damageInfo(target);
        if (damageInfo != null) {
            damageInfo.setPostDamage(damageDealt);
            if (!source.is(BHDamageTypeTags.CANT_STORE_DAMAGE)) {
                damageInfo.setPostStoredDamage(damageInfo.postDamage() + damageDealt);
            }
        }
        targetCombatCore.activated();
        if (source.getEntity() instanceof LivingEntity attacker) {
            double omniVamp = attacker.getAttributeValue(BHAttributes.OMNIVAMP.get());
            double physicalVamp = attacker.getAttributeValue(BHAttributes.PHYSICALVAMP.get());
            double spellVamp = attacker.getAttributeValue(BHAttributes.SPELLVAMP.get());
            boolean isMagic = source.is(BHDamageTypeTags.MAGIC_DAMAGE);
            boolean isPhysical = source.is(BHDamageTypeTags.PHYSICAL_DAMAGE);
            boolean isTrueDamage = source.is(BHDamageTypeTags.TRUE_DAMAGE);
            float healingEffectiveness = 1.0F;
            if (source instanceof AdvanceDamageSource advanceDamageSource) {
                if (advanceDamageSource.getDamageTags() == DamageTags.AOT) {
                    healingEffectiveness = Constant.AOE_HEALING_EFFECTIVENESS;
                }
            }
            if (spellVamp > 0.0F && isMagic) {
                double healAmount = damageDealt * spellVamp;
                attacker.heal((float) ((damageDealt + healAmount) * healingEffectiveness));
            }
            if (physicalVamp > 0.0F && isPhysical) {
                double healAmount = damageDealt * spellVamp;
                attacker.heal((float) ((damageDealt + healAmount) * healingEffectiveness));
            }
            if (omniVamp > 0.0F && ((isTrueDamage || isPhysical || isMagic))) {
                double healAmount = damageDealt * spellVamp;
                attacker.heal((float) ((damageDealt + healAmount) * healingEffectiveness));
            }
        }
        if (BHConfigs.DAMAGE_INDICATOR) {
            damageIndicator(level, source, false, damageDealt, target);
        }
        event.setAmount(damageDealt);
    }

    // TODO: Pre Mitigation Damage Handler
    @SubscribeEvent
    public void onLivingHurtEvent(LivingHurtEvent event) {
        float damageDealt = event.getAmount();
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        boolean isCrit = false;
        if (source.getEntity() instanceof AbstractArrow) {
            target.hurtTime = 0;
            target.invulnerableTime = 0;
        }
        if (source.getEntity() instanceof LivingEntity attacker && damageDealt > 0.0F) {
            if (attacker instanceof Player player) {
                PlayerData playerData = Capabilities.data(player);
                if (playerData != null) {
                    double criticalStrike = player.getAttributeValue(BHAttributes.CRITICAL_CHANCE.get());
                    double criticalDamage = player.getAttributeValue(BHAttributes.CRITICAL_DAMAGE.get());
                    if (!playerData.isCantCrit() && (player.getRandom().nextDouble() <= criticalStrike || playerData.isDoCrit())) {
                        isCrit = true;
                        damageDealt = (float) (damageDealt * criticalDamage);
                        player.crit(target);
                        playerData.setDoCrit(false);
                    }
                    playerData.setCrit(isCrit);
                }
                double getAttackSpeed = player.getAttributeValue(Attributes.ATTACK_SPEED);
                player.getAttackStrengthScale((float) getAttackSpeed);
                target.invulnerableTime = target.invulnerableDuration - (int) getAttackSpeed;
            }
            ICombatData attackerCombatData = Capabilities.combat(attacker);
            ItemStack attackerStack = attacker.getMainHandItem();
            if (attacker instanceof Player player) {
                if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
                    IAccessoryStackHandler handler = AccessoryHelper.getInventory(player).resolve().get();
                    var stacks = handler.getStacks();
                    for (int i = 0; i < stacks.getSlots(); i++) {
                        final ItemStack itemStack = stacks.getStackInSlot(i);
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem container) {
                            for (Accessory trait : container.getAccessories()) {
                                Optional<IAttack> attack = trait.attack();
                                if (attack.isPresent()) {
                                    if (trait.getTags().isUnique()) {
                                        damageDealt = attack.get().preMigitationDamage(new DamageContext(damageDealt), source, attacker, target);
                                    } else {
                                        if (attack.get().preMigitationDamage(new DamageContext(damageDealt), source, attacker, target) != damageDealt) {
                                            damageDealt += attack.get().preMigitationDamage(new DamageContext(damageDealt), source, attacker, target);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!attackerStack.isEmpty() && attackerStack.getItem() instanceof ISkillItems container) {
                for (Skill trait : container.getSkills()) {
                    Optional<IAttack> meleeWeaponCallback = trait.attack();
                    if (meleeWeaponCallback.isPresent()) {
                        damageDealt = meleeWeaponCallback.get().preMigitationDamage(new DamageContext(damageDealt), source, attacker, target);
                    }
                }
            }
            damageDealt *= (float) attacker.getAttributeValue(BHAttributes.DAMAGE_DEALT.get());
            attackerCombatData.activated();
        }
        if (target != null) {
            IDamageInfo damageInfo = Capabilities.damageInfo(target);
            if (target instanceof Player player) {
                boolean validateIsCrit = isCrit;
                PlayerDataHelper.getPlayerData(player).ifPresent(handler -> {
                    handler.setCrit(validateIsCrit);
                });
            }
            if (damageInfo != null) {
                damageInfo.setDamageSource(source);
                damageInfo.setReceivedCritDamage();
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


    private static void damageIndicator(ServerLevel level, DamageSource source,  boolean healing, float value, LivingEntity entity) {
        boolean isCrit = false;
        if (source != null && source.getEntity() instanceof Player player) {
            PlayerData playerData = Capabilities.data(player);
            if (playerData != null) {
                isCrit = playerData.isCrit();
            }
        }
        float roundedAmount = Math.round(value * 10) / 10.0F;
        int intAmount = (int) roundedAmount;
        String text = roundedAmount % 1 == 0 ? String.valueOf(intAmount) : String.valueOf(roundedAmount);
        Vec3 pos = entity.getEyePosition();
        Style damageIndColor;
        if (healing) {
            damageIndColor = BHChatformatting.HEAL;
        } else {
            if (BHConfigs.DAMAGE_INDICATOR_COLOR_FORMAT) {
                if (source == null) {
                    damageIndColor = BHChatformatting.PHYSICAL_DAMAGE;
                } else {
                    if (source.is(BHDamageTypeTags.PHYSICAL_DAMAGE)) {
                        damageIndColor = BHChatformatting.PHYSICAL_DAMAGE;
                    } else if (source.is(BHDamageTypeTags.MAGIC_DAMAGE)) {
                        damageIndColor = BHChatformatting.MAGIC_DAMAGE;
                    } else if (source.is(BHDamageTypeTags.TRUE_DAMAGE)) {
                        damageIndColor = BHChatformatting.TRUE_DAMAGE;
                    } else {
                        damageIndColor = BHChatformatting.RAW_DAMAGE;
                    }
                }
            } else {
                damageIndColor = BHChatformatting.PHYSICAL_DAMAGE;
            }
            if (isCrit) {
                damageIndColor = BHChatformatting.CRITICAL_DAMAGE;
            }
        }
        MutableComponent component = Component.literal(text).withStyle(damageIndColor);
        if (BHConfigs.DAMAGE_INDICATOR_TEXT_BOLD) {
            component.withStyle(ChatFormatting.BOLD);
        }
        if (!BHConfigs.DAMAGE_INDICATOR_VANILLA_FONT) {
            component.withStyle(Fonts.DAMAGE_INDICATOR);
        }
        level.sendParticles(new DamageIndicatorOptions(component, isCrit), pos.x, pos.y, pos.z, 1, 0.1D, 0.1D, 0.1D, 0);
    }

    private int enchantmentModifiyExpDrop(LivingEntity attacker, int experienceDrop, LivingEntity target) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(attacker.getItemBySlot(slot));
            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                if (entry.getKey() instanceof IAdditionalEnchantment additionalEnchantment) {
                    Optional<IAdditionalEnchantment> optional = additionalEnchantment.enchantmentCallback();
                    if (optional.isPresent()) {
                        experienceDrop = optional.get().modifyExprienceDrop(entry.getValue(), experienceDrop, target, (Player) attacker);
                    }
                }
            }
        }
        return experienceDrop;
    }
    //TODO: Item Use Event
    @SubscribeEvent
    public void onItemUse(LivingEntityUseItemEvent event) {
        LivingEntity entity = event.getEntity();
        Item item = event.getItem().getItem();
        ItemStack itemStack = item.getDefaultInstance();
        int duration = event.getDuration();
        if (event.isCancelable() && (entity.hasEffect(BHEffects.CURSED.get()) || entity.hasEffect(BHEffects.PARALYZE.get()) || entity.hasEffect(BHEffects.STUN.get()))) {
            event.setCanceled(true);
        }
        AtomicInteger itemDuration = new AtomicInteger();
        if (EnchantmentHelper.getEnchantmentLevel(BHEnchantments.DRAW_SPEED.get(), entity) > 0) {
            itemDuration.set(AdvancedEnchantment.getDrawSpeed(entity, duration));
        }
        if (entity instanceof Player player) {
            AccessoryHelper.getInventory(player).ifPresent(handler -> {
                var stacks = handler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    final ItemStack itemStacks = stacks.getStackInSlot(i);
                    if (!itemStacks.isEmpty() && itemStacks.getItem() instanceof IAccessoryItem container) {
                        for (Accessory trait : container.getAccessories()) {
                            Optional<IAttack> rangedWeaponCallback = trait.attack();
                            if (rangedWeaponCallback.isPresent()) {
                                itemDuration.set(rangedWeaponCallback.get().onItemUseItem(itemStack, duration));
                                if (itemDuration.get() < 0) {
                                    event.setCanceled(true);
                                }
                            }
                        }
                    }
                }
            });
        }
        if (itemDuration.get() > 0) {
            event.setDuration(event.getDuration() - itemDuration.get());
        }
    }

    //TODO: Enchantment Post Mitigation Damage
    private float enchantmentPostMitigationDamage(LivingEntity attacker, float damageDealt, DamageSource source, LivingEntity target) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(attacker.getItemBySlot(slot));
            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                if (entry.getKey() instanceof IAdditionalEnchantment additionalEnchantment) {
                    Optional<IAdditionalEnchantment> optional = additionalEnchantment.enchantmentCallback();
                    if (optional.isPresent()) {
                        damageDealt = optional.get().postMigitationDamage(entry.getValue(), new DamageContext(damageDealt), source, attacker, target);
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
                    optional.ifPresent(callback -> callback.onHitAttack(entry.getValue(), source, attacker.getItemBySlot(slot), target, attacker, new DamageContext(damageDealt)));
                }
            }
        }
    }
    //TODO: Enchantment Damage Taken
    private float enchantmentDamageTaken(float damageDealt, DamageSource source, LivingEntity target) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(target.getItemBySlot(slot));
            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                if (entry.getKey() instanceof IAdditionalEnchantment additionalEnchantment) {
                    Optional<IAdditionalEnchantment> optional = additionalEnchantment.enchantmentCallback();
                    if (optional.isPresent()) {
                        damageDealt = optional.get().damageTaken(entry.getValue(), new DamageContext(damageDealt), source, target);
                    }
                }
            }
        }
        return damageDealt;
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
        modifiyDropExperience = this.enchantmentModifiyExpDrop(player, droppedExperience, target);
        LevelSystem levelSystem = Capabilities.levelSystem(target);
        if (levelSystem != null) {
            if (!target.level().isClientSide()) {
                if (!levelSystem.isPlayer() && BHConfigs.ENABLE_MOB_LEVELS) {
                    modifiyDropExperience += (int) (droppedExperience * (1.0F + levelSystem.getLevel()));
                }
            }
        }
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ISkillItems skillItems) {
            for (Skill skill : skillItems.getSkills()) {
                Optional<IEntityProperties> callback = skill.entityProperties();
                if (callback.isPresent()) {
                    modifiyDropExperience = callback.get().modifyExprienceDrop(droppedExperience, target, player);
                }
            }
        }
        if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
            IAccessoryStackHandler handler = AccessoryHelper.getInventory(player).resolve().get();
            var stacks = handler.getStacks();
            for (int i = 0; i < stacks.getSlots(); i++) {
                final ItemStack stackInSlot = stacks.getStackInSlot(i);
                if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IAccessoryItem container) {
                    for (Accessory trait : container.getAccessories()) {
                        Optional<IEntityProperties> callback = trait.entityProperties();
                        if (callback.isPresent()) {
                            modifiyDropExperience += callback.get().modifyExprienceDrop(droppedExperience, target, player);
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
//        boolean cantDie = false;
        if (target instanceof Player player) {
            if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
                var handler = AccessoryHelper.getInventory(player).resolve().get();
                var stacks = handler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    final ItemStack itemStack = stacks.getStackInSlot(i);
                    if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem accessoryItem) {
                        for (Accessory trait : accessoryItem.getAccessories()) {
                            Optional<IAttack> meleeWeaponCallback = trait.attack();
                            if (meleeWeaponCallback.isPresent()) {
                                event.setCanceled(meleeWeaponCallback.get().onEntityDeath(player, itemStack));
                            }
                        }
                    }
                }
            }
        }


        if (target.hasEffect(BHEffects.LETHAL_PROTECTION.get())) {
            target.setHealth(1.0F);
            target.addEffect(new MobEffectInstance(BHEffects.LETHAL_PROTECTION_COOLDOWN.get()));
            event.setCanceled(true);
        }
        if (source.getEntity() instanceof LivingEntity attacker) {
            ICombatData attackerCombatCore = Capabilities.combat(attacker);
            ItemStack attackerStack = attacker.getMainHandItem();
            if (attacker instanceof Player player) {
                AccessoryHelper.getInventory(player).ifPresent(handler -> {
                    var stacks = handler.getStacks();
                    for (int i = 0; i < stacks.getSlots(); i++) {
                        final ItemStack itemStack = stacks.getStackInSlot(i);
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem accessoryItem) {
                            for (Accessory trait : accessoryItem.getAccessories()) {
                                Optional<IAttack> meleeWeaponCallback = trait.attack();
                                if (meleeWeaponCallback.isPresent()) {
                                    meleeWeaponCallback.get().onEntityKilled(source, player, target);
                                }
                            }
                        }
                    }
                });
                ServerLevel level = (ServerLevel) player.level();
            }
            if (!attackerStack.isEmpty() && attackerStack.getItem() instanceof ISkillItems skillItems) {
                for (Skill trait : skillItems.getSkills()) {
                    Optional<IAttack> meleeWeaponCallback = trait.attack();
                    meleeWeaponCallback.ifPresent(callback -> callback.onEntityKilled(source, attacker, target));
                }
            }
        }
//        event.setCanceled(cantDie);
    }


    private boolean inStructures(ServerLevel level, Player player, ResourceKey<Structure> structure) {
        var isAt = LocationPredicate.inStructure(structure);
        return isAt.matches(level, player.getX(), player.getY(), player.getZ());
    }

    @SubscribeEvent
    public void onMiningSpeedUpdate(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        float originalSpeed = event.getOriginalSpeed();
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
            float bonusMiningSpeed = (float) (originalSpeed * miningSpeed * miningEfficiency);
            if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
                var handler = AccessoryHelper.getInventory(player).resolve().get();
                var stacks = handler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    final ItemStack itemStack = stacks.getStackInSlot(i);
                    if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem accessoryItems) {
                        for (Accessory accessory : accessoryItems.getAccessories()) {
                            Optional<IEntityProperties> optional = accessory.entityProperties();
                            if (optional.isPresent()) {
                                if (accessory.getTags().isUnique()) {
                                    originalSpeed = (float) optional.get().onModifyMiningSpeed(player, blockState, blockPos, originalSpeed);
                                } else {

                                    originalSpeed += (float) optional.get().onModifyMiningSpeed(player, blockState, blockPos, originalSpeed);
                                }
                            }
                        }
                    }
                }
            }

            originalSpeed *= bonusMiningSpeed;
        }
        event.setNewSpeed(originalSpeed);
    }
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        if (event.isCancelable() && player.hasEffect(BHEffects.STUN.get())) {
            event.setCanceled(true);
        }
        boolean flag = false;
        ItemStack leftItem = player.getOffhandItem();
        ItemStack rightItem = player.getMainHandItem();
        if (!player.hasEffect(BHEffects.STUN.get())) {
            if (leftItem.getItem() instanceof ILeftClick) {
                ((ILeftClick) leftItem.getItem()).onLeftClick(leftItem, player);
                flag = true;
            }
            if (rightItem.getItem() instanceof ILeftClick) {
                ((ILeftClick) rightItem.getItem()).onLeftClick(rightItem, player);
                flag = true;
            }
            if (level.isClientSide() && flag) {
                NetworkHandler.sendToServer(new ServerboundPlayerSwingArmPacket());
            }
        }
    }
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
        if (event.isCancelable() && event.getEntity().hasEffect(BHEffects.STUN.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.isCancelable() && event.getEntity().hasEffect(BHEffects.STUN.get())) {
            event.setCanceled(true);
        }
        ItemStack itemInHand = event.getEntity().getItemInHand(InteractionHand.MAIN_HAND);
        if (itemInHand.getItem() instanceof ILeftClick leftClick) {
            event.setCanceled(leftClick.preventClickOthers(itemInHand, event.getEntity()));
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && event.getEntity().hasEffect(BHEffects.STUN.get())) {
            event.setCanceled(true);
        }
        ItemStack itemInHand = event.getEntity().getItemInHand(InteractionHand.MAIN_HAND);
        if (itemInHand.getItem() instanceof ILeftClick leftClick) {
            event.setCanceled(leftClick.preventClickOthers(itemInHand, event.getEntity()));
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.isCancelable() && event.getEntity().hasEffect(BHEffects.STUN.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onGetProjectiles(LivingGetProjectileEvent event) {
        if (event.getProjectileItemStack().isEmpty() && event.getEntity() instanceof Player player) {
            ItemStack quiver = QuiverHelper.getQuiverStacks(player);
            if (!quiver.isEmpty()) {
                QuiverItemStackHandler handler = (QuiverItemStackHandler) quiver.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().orElseThrow();
//                Predicate<ItemStack> predicate = ((ProjectileWeaponItem) event.getProjectileWeaponItemStack().getItem()).getSupportedHeldProjectiles();
//                ItemStack projectile = IntStream.range(0, handler.getSlots()).mapToObj(handler::getStackInSlot).filter(predicate).findFirst().orElse(ItemStack.EMPTY);
                ItemStack projectile = ItemStack.EMPTY;
                for (int i = 0; i < handler.getSlots(); ++i) {
                    projectile = handler.getStackInSlot(handler.getSelectedSlot());
                }
                if (!projectile.isEmpty()) {
                    event.setProjectileItemStack(projectile);
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemPickEvent(EntityItemPickupEvent event) {
        ItemStack pickedUpStack = event.getItem().getItem().copy();
        int beforeCount = pickedUpStack.getCount();
        int afterCount = beforeCount;
        Player player = event.getEntity();
        Level level = player.level();
        List<ItemStack> quivers = QuiverHelper.findValidQuivers(player);
        if (!quivers.isEmpty()) {
            for (ItemStack quiver : quivers) {
                if (!pickedUpStack.isEmpty() && !quiver.isEmpty() && ((QuiverItem) quiver.getItem()).isAmmoValid(pickedUpStack, quiver)) {
                    QuiverItemStackHandler quiverHandler = (QuiverItemStackHandler) quiver.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().orElseThrow();
                    for (int i = 0; i < quiverHandler.getSlots(); i++) {
                        pickedUpStack = quiverHandler.insertItem(i, pickedUpStack, false);
                    }
                }
                if (pickedUpStack.isEmpty()) break;
            }
            afterCount = pickedUpStack.getCount();
            if (afterCount < beforeCount) {
                player.take(event.getItem(), beforeCount - afterCount);
                event.getItem().getItem().setCount(afterCount);
                level.playSound((Player) null, event.getItem().getX(), event.getItem().getY(), event.getItem().getZ(), SoundEvents.ITEM_PICKUP, player.getSoundSource(), 0.2F, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 0.0F);
            }
        }
    }
}
