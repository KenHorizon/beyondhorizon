package com.kenhorizon.beyondhorizon.client;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.keybinds.Keybinds;
import com.kenhorizon.beyondhorizon.client.level.BHBossBar;
import com.kenhorizon.beyondhorizon.client.level.ModResouces;
import com.kenhorizon.beyondhorizon.client.level.guis.WorkbenchScreen;
import com.kenhorizon.beyondhorizon.client.level.guis.accessory.AccessorySlotScreen;
import com.kenhorizon.beyondhorizon.client.level.guis.hud.GameHudDisplay;
import com.kenhorizon.beyondhorizon.client.level.tooltips.IconAttributesTooltip;
import com.kenhorizon.beyondhorizon.client.particle.BleedParticle;
import com.kenhorizon.beyondhorizon.client.particle.DamageIndicatorParticle;
import com.kenhorizon.beyondhorizon.client.particle.RingParticles;
import com.kenhorizon.beyondhorizon.client.particle.StunParticles;
import com.kenhorizon.beyondhorizon.client.render.entity.*;
import com.kenhorizon.beyondhorizon.client.render.entity.ability.EruptionRenderer;
import com.kenhorizon.beyondhorizon.client.render.projectiles.BlazingRodRenderer;
import com.kenhorizon.beyondhorizon.server.ServerProxy;
import com.kenhorizon.beyondhorizon.server.entity.BHBossInfo;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.BlazingInferno;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.InfernoShield;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.init.BHMenu;
import com.kenhorizon.beyondhorizon.server.init.BHParticle;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundAccessoryInventoryPacket;
import com.kenhorizon.beyondhorizon.client.level.tooltips.Tooltips;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderers;
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
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import com.mojang.datafixers.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"removal"})
public class ClientProxy extends ServerProxy {
    public static final Map<UUID, BHBossInfo.BossBar> BOSS_BAR_REGISTRY = new HashMap<>();
    @Override
    public void serverHandler() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::registerKeybinds);
        bus.addListener(this::registerParticles);
        bus.addListener(this::entityCreationAttribute);
        bus.addListener(this::onEntityAttributeModification);
        bus.addListener(this::addResourcesBuiltin);
        IconAttributesTooltip.registerFactory();
        Tooltips.TitleBreakComponent.registerFactory();
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

        //bus.addListener(this::addRegisteredLayers);

        EntityRenderers.register(BHEntity.BLAZING_INFERNO.get(), BlazingInfernoRenderer::new);
        EntityRenderers.register(BHEntity.BLAZING_ROD.get(), BlazingRodRenderer::new);
        EntityRenderers.register(BHEntity.INFERNO_SHIELD.get(), InfernoShieldRenderer::new);
        EntityRenderers.register(BHEntity.CAMERA_SHAKE.get(), RenderNothing::new);
        EntityRenderers.register(BHEntity.BLAZING_SPEAR.get(), BlazingSpearRenderer::new);
        EntityRenderers.register(BHEntity.ERUPTION.get(), EruptionRenderer::new);

        MenuScreens.register(BHMenu.ACCESSORY_MENU.get(), AccessorySlotScreen::new);
        MenuScreens.register(BHMenu.WORKBENCH_MENU.get(), WorkbenchScreen::new);
        Raid.RaiderType.create("ILLUSIONER", EntityType.ILLUSIONER, new int[]{0, 0, 1, 2, 2, 3, 4, 5});
    }

    public void entityCreationAttribute(EntityAttributeCreationEvent event) {
        event.put(BHEntity.BLAZING_INFERNO.get(), BlazingInferno.createAttributes());
        event.put(BHEntity.INFERNO_SHIELD.get(), InfernoShield.createAttributes());
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
            event.add(type, BHAttributes.MAGIC_PENETRATION.get());
            event.add(type, BHAttributes.OMNIVAMP.get());
            event.add(type, BHAttributes.PHYSICALVAMP.get());
            event.add(type, BHAttributes.SPELLVAMP.get());
            event.add(type, BHAttributes.HEALING.get());
            event.add(type, BHAttributes.SHIELDING.get());
            event.add(type, BHAttributes.MOVEMENT_EFFICIENCY.get());
            event.add(type, BHAttributes.OXYGEN_BONUS.get());
            event.add(type, BHAttributes.BURNING_TIME.get());
            event.add(type, BHAttributes.FALLDAMAGE_MULTIPLIER.get());
            if (type == EntityType.PLAYER) {
                event.add(type, BHAttributes.STEALTH.get());
                event.add(type, BHAttributes.SNEAKING_SPEED.get());
                event.add(type, BHAttributes.SWEEP_DAMAGE.get());
                event.add(type, BHAttributes.MINING_EFFICIENCY.get());
                event.add(type, BHAttributes.CAST_TIME.get());
                event.add(type, BHAttributes.COOLDOWN.get());
                event.add(type, BHAttributes.CRITICAL_STRIKE.get());
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

        event.registerSpriteSet(BHParticle.BLEED.get(), BleedParticle.Provider::new);
        event.registerSpriteSet(BHParticle.RING.get(), RingParticles.Provider::new);

        event.registerSpecial(BHParticle.DAMAGE_INDICATOR.get(), new DamageIndicatorParticle.Provider());
        event.registerSpecial(BHParticle.STUN_PARTICLES.get(),new StunParticles.Provider());

    }
    private void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(Keybinds.LEVEL_SYSTEM);
    }

    @Override
    public void openScreen(Screen screen) {
        if (screen != null) {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.setScreen(screen);
        }
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
        return Minecraft.getInstance().player;
    }

    @Override
    public void syncAccessoryToPlayer(int slot, ItemStack itemStack, ServerPlayer player) {
        NetworkHandler.sendToPlayer(new ServerboundAccessoryInventoryPacket(slot, player.getId(), itemStack), player);
    }
}
