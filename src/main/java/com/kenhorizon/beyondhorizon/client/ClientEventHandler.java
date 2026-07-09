package com.kenhorizon.beyondhorizon.client;


import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.keybinds.Keybinds;
import com.kenhorizon.beyondhorizon.client.render.BHBossBar;
import com.kenhorizon.beyondhorizon.client.render.guis.LevelSystemScreen;
import com.kenhorizon.beyondhorizon.client.render.guis.accessory.AccessorySlotButton;
import com.kenhorizon.beyondhorizon.client.render.guis.accessory.AccessorySlotScreen;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.AttributeTooltips;
import com.kenhorizon.beyondhorizon.client.sound.BossMusicPlayer;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessories;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryHelper;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryStackHandler;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItem;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.entity.BHBossInfo;
import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundAcessoryKeyPacket;
import com.kenhorizon.libs.client.ModelAnimationHandler;
import com.kenhorizon.libs.client.ModelAnimations;
import com.kenhorizon.libs.client.WeaponArmPose;
import com.kenhorizon.libs.client.event.PlayerModelEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.loading.FMLLoader;

import javax.annotation.Nullable;
import java.util.*;

public class ClientEventHandler {
    private WeaponArmPose weaponRightArmPose = WeaponArmPose.EMPTY;
    private WeaponArmPose weaponLeftArmPose = WeaponArmPose.EMPTY;
    private static final ResourceLocation PHOSPOR = BeyondHorizon.resource("shaders/post/phospor_effect.json");
    private static final ResourceLocation GHOUL_WILL = BeyondHorizon.resource("shaders/post/ghoul_will.json");

    @SubscribeEvent
    public void onDebugInformation(CustomizeGuiOverlayEvent.DebugText event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft minecraft = Minecraft.getInstance();
        var leftInfo = event.getLeft();
        var rightInfo = event.getRight();
        if (BHConfigs.REDUCE_DEBUG && minecraft.options.renderDebug) {
            leftInfo.clear();
            renderNewDebug(guiGraphics);
        }
    }

    private void renderNewDebug(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        List<String> list = new ArrayList<>();
        Entity entity = minecraft.getCameraEntity();
        BlockPos blockpos = entity.blockPosition();
        Direction direction = entity.getDirection();
        String directionText;
        switch (direction) {
            case NORTH:
                directionText = "Towards negative Z";
                break;
            case SOUTH:
                directionText = "Towards positive Z";
                break;
            case WEST:
                directionText = "Towards negative X";
                break;
            case EAST:
                directionText = "Towards positive X";
                break;
            default:
                directionText = "Invalid";
        }
        ChunkPos chunkpos = new ChunkPos(blockpos);
        Level level = entity.level();
        list.add(String.format(Locale.ROOT, "FPS: %s", minecraft.getFps()));
        list.add(String.format(Locale.ROOT, "XYZ: %.2f / Y: %.2f /Z: %.2f", entity.getX(), entity.getY(), entity.getZ()));
        list.add(String.format(Locale.ROOT, "Facing: %s (%s) (%.1f / %.1f)", direction, directionText, Mth.wrapDegrees(entity.getYRot()), Mth.wrapDegrees(entity.getXRot())));

        int top = 2;
        for (String msg : list) {
            if (msg != null && !msg.isEmpty())
            {
//                guiGraphics.fill(1, top - 1, 2 + font.width(msg) + 1, top + font.lineHeight - 1, -1873784752);
                guiGraphics.drawString(font, msg, 2, top, 14737632, false);
            }
            top += font.lineHeight;
        }
    }

    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END) {
            BossMusicPlayer.tick();
        }
        if (event.level.isClientSide()) {
            return;
        }
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
    }

    @SubscribeEvent
    public void onLevelClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
    }

    @SubscribeEvent
    public void onMovementInput(MovementInputUpdateEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = event.getEntity();
        if (player != null) {
            if (player.hasEffect(BHEffects.FEAR.get())) {
                if (minecraft.options.keyDown.isDown()) {
                    event.getInput().forwardImpulse += 2F;
                }
                if (minecraft.options.keyLeft.isDown()) {
                    event.getInput().leftImpulse -= 2F;
                }
                if (minecraft.options.keyRight.isDown()) {
                    event.getInput().leftImpulse += 2F;
                }
                if (minecraft.options.keyUp.isDown()) {
                    event.getInput().forwardImpulse -= 2F;
                }
            }
        }
    }

    @SubscribeEvent
    public void registerCustomBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        BHBossInfo.BossBar entry = ClientProxy.BOSS_BAR_REGISTRY.getOrDefault(event.getBossEvent().getId(), null);
        if (entry == null) return;
        BHBossBar bossBar = BHBossBar.BOSS_BARS.getOrDefault(entry, null);
        event.setCanceled(true);
        bossBar.renderBossBar(event);
    }

    @SubscribeEvent
    public void postRenderStage(RenderLevelStageEvent event) {
        Entity entity = Minecraft.getInstance().getCameraEntity();
        boolean firstPerson = Minecraft.getInstance().options.getCameraType().isFirstPerson();
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            GameRenderer renderer = Minecraft.getInstance().gameRenderer;
            this.doShaderEffect(entity instanceof LivingEntity afflicted && afflicted.hasEffect(BHEffects.GHOUL_WILL.get()), renderer, GHOUL_WILL);
        }
    }

    private void doShaderEffect(boolean active, GameRenderer renderer, ResourceLocation shaders) {
        if (active) {
            if (renderer.currentEffect() == null || !shaders.toString().equals(renderer.currentEffect().getName())) {
                attemptLoadShader(shaders);
            }
        } else if (renderer.currentEffect() != null && shaders.toString().equals(renderer.currentEffect().getName())) {
            renderer.checkEntityPostEffect(null);
        }
    }

    @SubscribeEvent
    public void onComputeFOVModifier(ComputeFovModifierEvent event) {
        ItemStack itemstack = event.getPlayer().getUseItem();
        if (event.getPlayer().isUsingItem()) {
            if (itemstack.getItem() instanceof BowItem) {
                int i = event.getPlayer().getTicksUsingItem();
                float f1 = (float) i / 20.0F;
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                } else {
                    f1 *= f1;
                }
                event.setNewFovModifier(event.getFovModifier() * (1.0F - f1 * 0.15F));
            }
        }
    }
    @SubscribeEvent
    public void onMouseButtonClient(InputEvent.MouseButton event) {

    }
//    @SubscribeEvent
//    public  void onClientTick(TickEvent.ClientTickEvent event) {
//        Minecraft minecraft = Minecraft.getInstance();
//        Options options = minecraft.options;
//        Player player = minecraft.player;
//        for (int i = 0; i < 9; ++i) {
//            boolean flag = BeyondHorizon.PROXY.isKeyDown(Keybinds.ACCESSORY_SLOTS);
//            if (options.keyHotbarSlots[i].consumeClick()) {
//                if (player.isSpectator()) {
//                    minecraft.gui.getSpectatorGui().onHotbarSelected(i);
//                } else if (minecraft.screen != null || !flag) {
//                    player.getInventory().selected = i;
//                } else {
//                    if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
//                        var handler = AccessoryHelper.getInventory(player).resolve().get();
//                        var stacks = handler.getStacks();
//                        ItemStack itemStack = stacks.getStackInSlot(i);
//                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem) {
//                            NetworkHandler.sendToServer(new ServerboundAcessoryKeyPacket(player.getId(), itemStack, i));
//                        }
//                    }
//                }
//            }
//        }
//    }
    @SubscribeEvent
    public void onKeyPressClient(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        Options options = minecraft.options;
        Player player = minecraft.player;

        if (event.getKey() == Keybinds.LEVEL_SYSTEM.getKey().getValue() && BeyondHorizon.PROXY.isKeyPressed(Keybinds.LEVEL_SYSTEM)) {
            BeyondHorizon.PROXY.openScreen(new LevelSystemScreen());
        }
        for (int i = 0; i < 9; ++i) {
            boolean flag = BeyondHorizon.PROXY.isKeyDown(Keybinds.ACCESSORY_SLOTS);
            if (options.keyHotbarSlots[i].consumeClick()) {
                if (player.isSpectator()) {
                    minecraft.gui.getSpectatorGui().onHotbarSelected(i);
                } else if (minecraft.screen != null || !flag) {
                    player.getInventory().selected = i;
                } else {
                    BeyondHorizon.LOGGER.debug("is present {}", AccessoryHelper.getInventory(player).resolve().isPresent());
                    if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
                        BeyondHorizon.LOGGER.debug("13");
                        var handler = AccessoryHelper.getInventory(player).resolve().get();
                        var stacks = handler.getStacks();
                        ItemStack itemStack = stacks.getStackInSlot(i);
                        BeyondHorizon.LOGGER.debug("3 is {}", itemStack.isEmpty());
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IAccessoryItem) {
                            BeyondHorizon.LOGGER.debug("4");
                            NetworkHandler.sendToServer(new ServerboundAcessoryKeyPacket(player.getId(), itemStack, i));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPrePlayerRender(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        if (player != null) {
            if (player.isInvisible()) {
                if (AccessoryHelper.getAccessory(player, Accessories.STALKER.get())) {
                    event.setCanceled(true);
                }
            }
        }
    }
    @SubscribeEvent
    public void computeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity player = minecraft.getCameraEntity();
        float partialTick = minecraft.getPartialTick();
        float delta = minecraft.getFrameTime();
        float ticksExistedDelta = player.tickCount + delta;
        if (BHConfigs.SCREEN_SHAKE && !minecraft.isPaused()) {
            float shakeAmplitude = 0;
            for (CameraShake cameraShake : player.level().getEntitiesOfClass(CameraShake.class, player.getBoundingBox().inflate(64))) {
                if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                    shakeAmplitude += cameraShake.getShakeAmount((Player) player, delta);
                    shakeAmplitude *= (Mth.clamp((float) BHConfigs.SCREEN_SHAKE_AMOUNT / 100, 0.0F, 1.0F));
                }
            }
            if (shakeAmplitude > 1.0F) shakeAmplitude = 1.0F;
            event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
            event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
            event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));
        }
        if (player instanceof LivingEntity entity && entity.hasEffect(BHEffects.STUN.get())) {
            event.setRoll((float) (Math.sin((player.tickCount + partialTick) * 0.2F) * 10F));
        }
    }

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        Screen eventScreen = event.getScreen();
        RecipeBookComponent component = new RecipeBookComponent();
        Player player = BeyondHorizon.PROXY.clientPlayer();
        if (eventScreen instanceof InventoryScreen || eventScreen instanceof CreativeModeInventoryScreen) {
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) eventScreen;
            boolean isCreative = eventScreen instanceof CreativeModeInventoryScreen;
            int x = (gui.width - gui.getXSize()) / 2;
            int y = (gui.height - gui.getYSize()) / 2;
            x += isCreative ? 173 : BHConfigs.ACCESSORY_BUTTON_X;
            y += isCreative ? 65 : BHConfigs.ACCESSORY_BUTTON_Y;
            if (component.isVisible()) {
                x += 10;
                y += 10;
            }
            event.addListener(new AccessorySlotButton(eventScreen, x, y));
        }
        if (eventScreen instanceof AccessorySlotScreen) {
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) eventScreen;
            int x = (gui.width - gui.getXSize()) / 2;
            int y = (gui.height - gui.getYSize()) / 2;
            event.addListener(new AccessorySlotButton(eventScreen, x + BHConfigs.ACCESSORY_BUTTON_X, y + BHConfigs.ACCESSORY_BUTTON_Y));
        }
    }

    @SubscribeEvent
    public void onInventoryGuiDrawBackground(ScreenEvent.Render.Pre event) {
        if (!(event.getScreen() instanceof InventoryScreen gui)) {
            return;
        }
        gui.xMouse = event.getMouseX();
        gui.yMouse = event.getMouseY();
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPoseHand(PlayerModelEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntityIn();
        float limbSwing = event.getLimbSwing();
        float limbSwingAmount = event.getLimbSwingAmount();
        float yaw = event.getYaw();
        float pitch = event.getPitch();
        this.weaponRightArmPose = ModelAnimationHandler.INSTANCE.getWeaponArmPose(entity, InteractionHand.MAIN_HAND);
        this.weaponLeftArmPose = ModelAnimationHandler.INSTANCE.getWeaponArmPose(entity, InteractionHand.OFF_HAND);
        boolean isRightHanded = entity.getMainArm() == HumanoidArm.RIGHT;
        if (entity.isUsingItem()) {
            boolean isMainHand = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
            if (isMainHand == isRightHanded) {
                this.poseRightArm(entity, (HumanoidModel<LivingEntity>) event.getModel());
            } else {
                this.poseLeftArm(entity, (HumanoidModel<LivingEntity>) event.getModel());
            }
        } else {
            boolean isTwoHanded = isRightHanded ? this.weaponLeftArmPose.isTwoHanded() : this.weaponRightArmPose.isTwoHanded();
            if (isRightHanded != isTwoHanded) {
                this.poseLeftArm(entity, (HumanoidModel<LivingEntity>) event.getModel());
                this.poseRightArm(entity, (HumanoidModel<LivingEntity>) event.getModel());
            } else {
                this.poseRightArm(entity, (HumanoidModel<LivingEntity>) event.getModel());
                this.poseLeftArm(entity, (HumanoidModel<LivingEntity>) event.getModel());
            }
        }
    }
    private void poseRightArm(LivingEntity entity, HumanoidModel<LivingEntity> model) {
        switch (this.weaponRightArmPose) {
            case GUARDIAN_SWORD:
                model.rightArm.xRot = model.rightArm.xRot * 0.5F - (float) Math.PI;
                model.rightArm.yRot = 0.0F;
                break;
            case HOLDING_1:
                model.rightArm.xRot = model.head.xRot - (float) Math.toRadians(80.0F);
                model.rightArm.yRot = model.head.yRot;
                break;
            case HOLDING:
                ModelAnimations.holding(model.rightArm, model.leftArm, model.head, true);
                break;
            default:
                model.rightArmPose.applyTransform(model, entity, HumanoidArm.RIGHT);
        }
    }
    private void poseLeftArm(LivingEntity entity, HumanoidModel<LivingEntity> model) {
        switch (this.weaponLeftArmPose) {
            case GUARDIAN_SWORD:
                model.leftArm.xRot = model.leftArm.xRot * 0.5F - (float) Math.PI;
                model.leftArm.yRot = 0.0F;
                break;
            case HOLDING_1:
                model.leftArm.xRot = model.head.xRot - (float) Math.toRadians(80.0F);
                model.leftArm.yRot = model.head.yRot;
                break;
            case HOLDING:
                ModelAnimations.holding(model.rightArm, model.leftArm, model.head, false);
                break;
            default:
                model.leftArmPose.applyTransform(model, entity, HumanoidArm.LEFT);
        }
    }

    private void renderBreakingTexture(BlockState state, BlockPos pos, BlockAndTintGetter blockAndTintGetter,
                                       PoseStack poseStack, RandomSource random, VertexConsumer vertexConsumer,
                                       ModelData modelData) {
        if (state.getRenderShape() == RenderShape.MODEL) {
            BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
            BakedModel bakedmodel = blockRenderDispatcher.getBlockModel(state);
            long i = state.getSeed(pos);
            blockRenderDispatcher.getModelRenderer().tesselateBlock(blockAndTintGetter, bakedmodel, state, pos, poseStack, vertexConsumer, true, random, i, OverlayTexture.NO_OVERLAY, modelData, null);
        }
    }
    private static void attemptLoadShader(ResourceLocation resourceLocation) {
        GameRenderer renderer = Minecraft.getInstance().gameRenderer;
        if (ClientProxy.shaderLoadAttemptCooldown <= 0) {
            renderer.loadEffect(resourceLocation);
            if (!renderer.effectActive) {
                ClientProxy.shaderLoadAttemptCooldown = 12000;
                BeyondHorizon.LOGGER.warn("Could not load the shader {}, will attempt to load shader in 30 seconds", resourceLocation);
            }
        }
    }
}
