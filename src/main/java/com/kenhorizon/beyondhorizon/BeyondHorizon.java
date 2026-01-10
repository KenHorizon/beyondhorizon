package com.kenhorizon.beyondhorizon;

import com.kenhorizon.beyondhorizon.client.ClientProxy;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.AttributeReaderResourceParser;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.compat.ModCompats;
import com.kenhorizon.beyondhorizon.compat.ModLists;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.configs.client.ModClientConfig;
import com.kenhorizon.beyondhorizon.configs.common.ModCommonConfig;
import com.kenhorizon.beyondhorizon.configs.server.ModServerConfig;
import com.kenhorizon.beyondhorizon.server.ServerEventHandler;
import com.kenhorizon.beyondhorizon.server.ServerProxy;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessories;
import com.kenhorizon.beyondhorizon.server.api.classes.MasterySkillCategories;
import com.kenhorizon.beyondhorizon.server.api.classes.RoleClasses;
import com.kenhorizon.beyondhorizon.server.api.classes.MasterySkills;
import com.kenhorizon.beyondhorizon.server.api.handler.anvil_patch.AnvilPatchHandler;
import com.kenhorizon.beyondhorizon.server.command.RoleClassCommand;
import com.kenhorizon.beyondhorizon.server.init.*;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.api.skills.Skills;
import com.kenhorizon.beyondhorizon.server.util.attributes.AttributeModify;
import com.kenhorizon.beyondhorizon.server.util.attributes.AttributeRegistryHelper;
import com.kenhorizon.beyondhorizon.server.util.attributes.IAttributeRegistryHelper;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(BeyondHorizon.ID)
public class BeyondHorizon
{
    public static final String ID = "beyondhorizon";
    public static final String NAME = "Beyond Horizon";
    public static final String VERSION = "v1.0";
    public static ServerProxy PROXY = (ServerProxy) DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static final Logger LOGGER = LogUtils.getLogger();
    
    public BeyondHorizon(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();
        final ModLoadingContext modContext = ModLoadingContext.get();
        ModClientConfig.register(modContext);
        ModCommonConfig.register(modContext);
        ModServerConfig.register(modContext);
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::reloadListener);
        eventBus.addListener(this::registerLayerDefinitions);
        eventBus.addListener(this::completeSetup);
        eventBus.addListener(this::onConfigLoad);
        BHAttributes.register(eventBus);
        BHCreativeTabs.register(eventBus);
        BHBlocks.register(eventBus);
        BHItems.register(eventBus);
        BHBlockEntity.register(eventBus);
        BHMenu.register(eventBus);
        BHParticle.register(eventBus);
        //  BHPotions.register(eventBus);
        BHEnchantments.register(eventBus);
        BHSounds.register(eventBus);
        BHEntity.register(eventBus);
        BHEffects.register(eventBus);
        BHRecipe.register(eventBus);
        Skills.register(eventBus);
        MasterySkillCategories.register(eventBus);
        MasterySkills.register(eventBus);
        RoleClasses.register(eventBus);
        Accessories.register(eventBus);
        PROXY.serverHandler();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new AnvilPatchHandler());
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft != null) {
                if (minecraft.getResourceManager() instanceof ReloadableResourceManager resourceManager) {
                    resourceManager.registerReloadListener(AttributeReaderResourceParser.INSTANCE);
                }
            }
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        BeyondHorizon.LOGGER.info("Setting up {} {}!!", BeyondHorizon.NAME, BeyondHorizon.VERSION);
        NetworkHandler.register();
        event.enqueueWork(() -> {
            BHPotions.setup();
            this.modCompatible();
        });
    }

    private void modCompatible() {
        if (!ModList.get().isLoaded(ModLists.ATTRIBUTE_FIX)) {
            IAttributeRegistryHelper<Attribute> register = new AttributeRegistryHelper();
            AttributeModify.load(register).applyChanges(register);
        }
        IAttributeRegistryHelper<Attribute> register = new AttributeRegistryHelper();
        AttributeModify.load(register).applySyncable(register);
    }

    private void completeSetup(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            PROXY.post();
        });
    }

    @OnlyIn(Dist.CLIENT)
    public void reloadListener(RegisterClientReloadListenersEvent event) {

    }

    private void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        BHModelLayers.register(event);
    }

    @SubscribeEvent
    public void onConfigLoad(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        BeyondHorizon.LOGGER.info("Mod Config Fired!");
        BHConfigs.bake(config);
        if (config.getSpec() == ModClientConfig.SPEC) {
            ModClientConfig.reset();
        }
        if (config.getSpec() == ModCommonConfig.SPEC) {
            ModCommonConfig.reset();
        }
        if (config.getSpec() == ModServerConfig.SPEC) {
            ModServerConfig.reset();
        }

    }

    public static ResourceLocation resource(String name) {
        return ResourceLocation.fromNamespaceAndPath(BeyondHorizon.ID, name);
    }

    public static ResourceLocation resourceGui(String name) {
        return ResourceLocation.fromNamespaceAndPath(BeyondHorizon.ID, String.format("textures/gui/%s", name));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        BeyondHorizon.LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event) {
        BeyondHorizon.LOGGER.info("Registering commands...");
        RoleClassCommand.register(event.getDispatcher());
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            PROXY.clientHandler();
        });
    }
}
