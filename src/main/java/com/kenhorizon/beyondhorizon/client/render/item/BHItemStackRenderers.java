package com.kenhorizon.beyondhorizon.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BHItemStackRenderers extends BlockEntityWithoutLevelRenderer {

    public BHItemStackRenderers() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int overlay) {
        Minecraft minecraft = Minecraft.getInstance();
        float partialTick = minecraft.getPartialTick();
        float ageInTicks = minecraft.player == null ? 0F : minecraft.player.tickCount + partialTick;
        ClientLevel level = minecraft.level;
        boolean heldIn3d = itemDisplayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || itemDisplayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || itemDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || itemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        boolean left = itemDisplayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || itemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        float isLeft = left ? -1.0F : 1.0F;

    }
    protected float yOffset(float tickCount) {
        return tickCount * 0.01F;
    }

    protected float xOffset(float tickCount) {
        return tickCount * 0.01F;
    }

    private void renderStaticItemSprite(ItemStack spriteItem, ItemDisplayContext transformType, int combinedLightIn, int combinedOverlayIn, PoseStack poseStack, MultiBufferSource bufferIn, ClientLevel level) {
        Minecraft minecraft = Minecraft.getInstance();
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        itemRenderer.renderStatic(spriteItem, transformType, transformType == ItemDisplayContext.GROUND ? combinedLightIn : 240, combinedOverlayIn, poseStack, bufferIn, level, 0);
    }
}
