package com.kenhorizon.beyondhorizon;

import com.kenhorizon.beyondhorizon.client.ClientEventHandler;
import com.kenhorizon.beyondhorizon.client.ModResouces;
import com.kenhorizon.beyondhorizon.client.TooltipsEventHandler;
import com.kenhorizon.beyondhorizon.client.keybinds.Keybinds;
import com.kenhorizon.beyondhorizon.client.render.entity.misc.HealingOrbRenderer;
import com.kenhorizon.beyondhorizon.client.render.blockentity.BaseSpawnerRenderer;
import com.kenhorizon.beyondhorizon.client.render.blockentity.GateDoorRenderer;
import com.kenhorizon.beyondhorizon.client.render.entity.ability.*;
import com.kenhorizon.beyondhorizon.client.render.entity.projectiles.*;
import com.kenhorizon.beyondhorizon.client.render.guis.QuiverScreen;
import com.kenhorizon.beyondhorizon.client.render.guis.VoidBagScreen;
import com.kenhorizon.beyondhorizon.client.render.guis.hud.overlay.AbilityHud;
import com.kenhorizon.beyondhorizon.client.render.guis.workbench.WorkbenchScreen;
import com.kenhorizon.beyondhorizon.client.render.guis.accessory.AccessorySlotScreen;
import com.kenhorizon.beyondhorizon.client.render.guis.hud.GameHudDisplay;
import com.kenhorizon.beyondhorizon.client.render.guis.hud.overlay.ManaHud;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.items.ClientTooltipRegister;
import com.kenhorizon.beyondhorizon.client.render.item.AccessoryItemDecorations;
import com.kenhorizon.beyondhorizon.client.render.item.BHArmorRenderProperties;
import com.kenhorizon.beyondhorizon.client.render.item.BHItemRenderProperties;
import com.kenhorizon.beyondhorizon.client.particle.*;
import com.kenhorizon.beyondhorizon.client.render.entity.*;
import com.kenhorizon.beyondhorizon.client.render.entity.misc.BHFallingBlocksRenderer;
import com.kenhorizon.beyondhorizon.client.render.shaders.BakedModelShadeLayerFullbright;
import com.kenhorizon.beyondhorizon.client.util.EmissiveBlocks;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItem;
import com.kenhorizon.beyondhorizon.server.block.spawner.data.SpawnerConfig;
import com.kenhorizon.beyondhorizon.server.entity.BHBossInfo;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.BlazingInferno;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.InfernoShield;
import com.kenhorizon.beyondhorizon.server.entity.boss.pyrolliger.Pyrolliger;
import com.kenhorizon.beyondhorizon.server.entity.mobs.DragonHornet;
import com.kenhorizon.beyondhorizon.server.entity.mobs.FayeFlares;
import com.kenhorizon.beyondhorizon.server.entity.mobs.FayeWildfire;
import com.kenhorizon.beyondhorizon.server.init.*;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundAccessoryInventoryPacket;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"removal"})
public class ClientProxy extends ServerProxy {
    public static int shaderLoadAttemptCooldown = 0;
    public static final Map<UUID, BHBossInfo.BossBar> BOSS_BAR_REGISTRY = new HashMap<>();
    @Override
    public void serverHandler() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::registerKeybinds);
        bus.addListener(this::registerParticles);
        bus.addListener(this::entityCreationAttribute);
        bus.addListener(this::onEntityAttributeModification);
        bus.addListener(this::addResourcesBuiltin);
        bus.addListener(this::registerGuiOverlays);
        bus.addListener(this::registerNewRegsitry);
        bus.addListener(this::onRegisterItemDecorations);
        ClientTooltipRegister.register();
    }


    private void onRegisterItemDecorations(final RegisterItemDecorationsEvent event) {
        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof IAccessoryItem accessoryItem) {
                event.register(item, new AccessoryItemDecorations(item, accessoryItem));
            }
        }
    }


    public void registerNewRegsitry(DataPackRegistryEvent.NewRegistry event) {
        BeyondHorizon.LOGGER.info("Custom Registry is registered and created!");
        event.dataPackRegistry(BHRegistries.Keys.SPAWNER_BUILDER, SpawnerConfig.MAP_CODEC.codec());
    }

    private void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelow(VanillaGuiOverlay.ARMOR_LEVEL.id(), "mana_hud", new ManaHud());
        event.registerBelow(VanillaGuiOverlay.ITEM_NAME.id(), "ability_hud", new AbilityHud());
    }

    private void addResourcesBuiltin(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFileInfo info = ModList.get().getModFileById(BeyondHorizon.ID);
            IModFile file = info.getFile();
            event.addRepositorySource(res -> {
                Pack pack = Pack.readMetaAndCreate("beyondhorizon:game_art", Component.translatable(Tooltips.TOOLTIP_BUILTIN_RESOURCE)
                ,false, id -> new ModResouces(id, file, "resourcepacks/game_art"), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);
                if (pack != null) res.accept(pack);
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void clientHandler() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new GameHudDisplay());
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new TooltipsEventHandler());
        bus.addListener(this::bakeModels);
        //bus.addListener(this::addRegisteredLayers);
        //
        EntityRenderers.register(BHEntity.CAMERA_SHAKE.get(), RenderNothing::new);
        EntityRenderers.register(BHEntity.BOLT_SHOCK.get(), RenderNothing::new);
        EntityRenderers.register(BHEntity.CLEAVE_ABILITY.get(), RenderNothing::new);
        EntityRenderers.register(BHEntity.CLEAVE_CONE_ABILITY.get(), RenderNothing::new);
        EntityRenderers.register(BHEntity.INFERNAL_SPEAR.get(), InfernalSpearRenderer::new);
        EntityRenderers.register(BHEntity.PYROBOLT.get(), PyroboltRenderer::new);
        EntityRenderers.register(BHEntity.BURNING_HEX_TRAP.get(), BurningHexTrapRenderer::new);
        //
        EntityRenderers.register(BHEntity.FAYE_FLARES.get(), FayeFlaresRenderer::new);
        EntityRenderers.register(BHEntity.FAYE_WILDFIRE.get(), FayeWildfireRenderer::new);
        EntityRenderers.register(BHEntity.BLAZING_INFERNO.get(), BlazingInfernoRenderer::new);
        EntityRenderers.register(BHEntity.PYROLLIGER.get(), PyrolligerRenderer::new);
        EntityRenderers.register(BHEntity.BLAZING_ROD.get(), BlazingRodRenderer::new);
        EntityRenderers.register(BHEntity.INFERNO_SHIELD.get(), InfernoShieldRenderer::new);
        EntityRenderers.register(BHEntity.BLAZING_SPEAR.get(), BlazingSpearRenderer::new);
        EntityRenderers.register(BHEntity.PYRO_LANCE.get(), PyroLanceRenderer::new);
        EntityRenderers.register(BHEntity.ERUPTION.get(), EruptionRenderer::new);
        EntityRenderers.register(BHEntity.BLAZING_INFERNO_RAY.get(), BlazingInfernoRayRenderer::new);
        EntityRenderers.register(BHEntity.INFERNAL_RAY.get(), InfernalRayRenderer::new);
        EntityRenderers.register(BHEntity.FALLING_BLOCKS.get(), BHFallingBlocksRenderer::new);
        EntityRenderers.register(BHEntity.DRAGON_HORNET.get(), DragonHornetRenderer::new);
        EntityRenderers.register(BHEntity.HEALING_ORB.get(), HealingOrbRenderer::new);
        //
        BlockEntityRenderers.register(BHBlockEntity.BASE_SPAWNER.get(), BaseSpawnerRenderer::new);
        BlockEntityRenderers.register(BHBlockEntity.GATE.get(), GateDoorRenderer::new);

        MenuScreens.register(BHMenu.ACCESSORY_MENU.get(), AccessorySlotScreen::new);
        MenuScreens.register(BHMenu.QUIVER_MENU.get(), QuiverScreen::new);
        MenuScreens.register(BHMenu.WORKBENCH_MENU.get(), WorkbenchScreen::new);
        MenuScreens.register(BHMenu.VOID_BAG_MENU.get(), VoidBagScreen::new);

        registerRaidMobs();

        ItemBlockRenderTypes.setRenderLayer(BHBlocks.IRON_LATTICE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BHBlocks.BLACK_IRON_LATTICE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BHBlocks.TATTERED_IRON_LATTICE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BHBlocks.TATTERED_BLACK_IRON_LATTICE.get(), RenderType.cutout());
    }

    private static void registerRaidMobs() {
        Raid.RaiderType.create("ILLUSIONER", EntityType.ILLUSIONER, new int[]{0, 0, 1, 2, 2, 3, 4, 5});
    }

    private void bakeModels(final ModelEvent.ModifyBakingResult e) {
        long time = System.currentTimeMillis();
        for (ResourceLocation id : e.getModels().keySet()) {
            if (EmissiveBlocks.registered().stream().anyMatch(str -> id.toString().startsWith(str))) {
                e.getModels().put(id, new BakedModelShadeLayerFullbright(e.getModels().get(id)));
            }
        }
        BeyondHorizon.LOGGER.info("Loaded emissive block models in {} ms", System.currentTimeMillis() - time);

    }

    public void entityCreationAttribute(EntityAttributeCreationEvent event) {
        event.put(BHEntity.FAYE_WILDFIRE.get(), FayeWildfire.createAttributes());
        event.put(BHEntity.FAYE_FLARES.get(), FayeFlares.createAttributes());
        event.put(BHEntity.BLAZING_INFERNO.get(), BlazingInferno.createAttributes());
        event.put(BHEntity.PYROLLIGER.get(), Pyrolliger.createAttributes());
        event.put(BHEntity.INFERNO_SHIELD.get(), InfernoShield.createAttributes());
        event.put(BHEntity.DRAGON_HORNET.get(), DragonHornet.createAttributes());
    }

//    @OnlyIn(Dist.CLIENT)
//    public void addRegisteredLayers(final EntityRenderersEvent.AddLayers event) {
//        List<EntityType<? extends LivingEntity>> entityTypes = ImmutableList.copyOf(ForgeRegistries.ENTITY_TYPES.getValues().stream()
//                .filter(DefaultAttributes::hasSupplier)
//                .map(entityType -> (EntityType<? extends LivingEntity>) entityType)
//                .collect(Collectors.toList()));
//        entityTypes.forEach((entityType -> {
//            addLayerIfApplicable(entityType, event);
//        }));
//
//        for (String skinType : event.getSkins()) {
//            event.getSkin(skinType).addLayer(new BHEntitiesLayer(event.getSkin(skinType)));
//        }
//    }
//    private void addLayerIfApplicable(EntityType<? extends LivingEntity> entityType, EntityRenderersEvent.AddLayers event) {
//        LivingEntityRenderer renderer = null;
//        if (entityType != EntityType.ENDER_DRAGON) {
//            try {
//                renderer = event.getRenderer(entityType);
//            } catch (Exception e) {
//                BeyondHorizon.LOGGER.warn("Could not apply radiation glow layer to {}, has custom renderer that is not LivingEntityRenderer.", ForgeRegistries.ENTITY_TYPES.getKey(entityType));
//            }
//            if (renderer != null) {
//                renderer.addLayer(new BHEntitiesLayer(renderer));
//            }
//        }
//    }

    public void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> type : event.getTypes()) {
            event.add(type, BHAttributes.LETHALITY.get());
            event.add(type, BHAttributes.DAMAGE_DEALT.get());
            event.add(type, BHAttributes.DAMAGE_TAKEN.get());
            event.add(type, BHAttributes.MAGIC_RESISTANCE.get());
            event.add(type, BHAttributes.RANGED_DAMAGE.get());
            event.add(type, BHAttributes.ABILITY_POWER.get());
            event.add(type, BHAttributes.EVADE.get());
            event.add(type, BHAttributes.ARMOR_PENETRATION.get());
            event.add(type, BHAttributes.FLAT_MAGIC_PENETRATION.get());
            event.add(type, BHAttributes.PERCENTAGE_MAGIC_PENETRATION.get());
            event.add(type, BHAttributes.OMNIVAMP.get());
            event.add(type, BHAttributes.PHYSICALVAMP.get());
            event.add(type, BHAttributes.SPELLVAMP.get());
            event.add(type, BHAttributes.HEALING.get());
            event.add(type, BHAttributes.SHIELDING.get());
            event.add(type, BHAttributes.OXYGEN_BONUS.get());
            event.add(type, BHAttributes.BURNING_TIME.get());
            event.add(type, BHAttributes.FALLDAMAGE_MULTIPLIER.get());
            event.add(type, BHAttributes.WATER_MINING_EFFICIENCY.get());
            event.add(type, BHAttributes.MOVEMENT_EFFICIENCY.get());
            if (type == EntityType.PLAYER) {
                event.add(type, BHAttributes.STEALTH.get());
                event.add(type, BHAttributes.SNEAKING_SPEED.get());
                event.add(type, BHAttributes.SWEEP_DAMAGE.get());
                event.add(type, BHAttributes.MINING_EFFICIENCY.get());
                event.add(type, BHAttributes.CAST_TIME.get());
                event.add(type, BHAttributes.COOLDOWN.get());
                event.add(type, BHAttributes.CRITICAL_CHANCE.get());
                event.add(type, BHAttributes.CRITICAL_DAMAGE.get());
                event.add(type, BHAttributes.MINING_SPEED.get());
                event.add(type, BHAttributes.MAX_MANA.get());
                event.add(type, BHAttributes.MANA_COST.get());
                event.add(type, BHAttributes.MANA_REGENERATION.get());
                event.add(type, BHAttributes.HEALTH_REGENERATION.get());
            }
        }
    }

    public void registerParticles(RegisterParticleProvidersEvent event) {
        BeyondHorizon.LOGGER.info("Registering Particles!!");
        event.registerSpriteSet(BHParticle.DRAGONIC_FLAME.get(), FlameParticle.Provider::new);
        event.registerSpriteSet(BHParticle.RED_SKULL.get(), FlameParticle.Provider::new);
        event.registerSpriteSet(BHParticle.BLEED.get(), BleedParticle.Provider::new);
        event.registerSpriteSet(BHParticle.BLEED.get(), BleedParticle.Provider::new);
        event.registerSpriteSet(BHParticle.SLASH.get(), SlashParticles.Provider::new);
        event.registerSpriteSet(BHParticle.RING.get(), RingParticles.Provider::new);
        event.registerSpriteSet(BHParticle.RING_BIG.get(), RingParticles.Provider::new);
        event.registerSpriteSet(BHParticle.ROAR.get(), RoarParticles.Provider::new);
        event.registerSpriteSet(BHParticle.TRAILS.get(), TrailParticles.Provider::new);
        event.registerSpecial(BHParticle.AFTERIMAGE.get(), new AfterImageParticle.Provider());
        event.registerSpecial(BHParticle.DAMAGE_INDICATOR.get(), new DamageIndicatorParticle.Provider());
        event.registerSpecial(BHParticle.STUN_PARTICLES.get(),new StunParticles.Provider());
        event.registerSpecial(BHParticle.LIGHTNING.get(), new LightningParticle.Provider());
        event.registerSpecial(BHParticle.CIRCLE_LIGHTNING.get(), new CircleLightningParticle.Provider());

    }
    private void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(Keybinds.LEVEL_SYSTEM);
        event.register(Keybinds.QUIVER_INVENTORY);
        event.register(Keybinds.ACCESSORY_SLOTS);
        event.register(Keybinds.SKILL_SELECT);
    }

    @Override
    public void openScreen(Screen screen) {
        if (screen != null) {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.setScreen(screen);
        }
    }

    @Override
    public void playSound(AbstractSoundInstance instance) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getSoundManager().play(instance);
    }

    @Override
    public boolean isKeyDown(KeyMapping keyMapping) {
        return keyMapping.isDown();
    }

    @Override
    public boolean isKeyPressed(KeyMapping keyMapping) {
        return keyMapping.consumeClick();
    }

    @Override
    public Player clientPlayer() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player;
    }

    @Override
    public Object getCustomItemRenderer() {
        return new BHItemRenderProperties();
    }

    @Override
    public Object getCustomArmorRenderer() {
        return new BHArmorRenderProperties();
    }

    @Override
    public void syncAccessoryToPlayer(int slot, ItemStack itemStack, ServerPlayer player) {
        NetworkHandler.sendToPlayer(new ServerboundAccessoryInventoryPacket(slot, player.getId(), itemStack), player);
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public Object getFontRenderer() {
        Minecraft mc = Minecraft.getInstance();
        return mc.font;
    }
}
