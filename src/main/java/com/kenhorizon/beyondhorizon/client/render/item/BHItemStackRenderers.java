package com.kenhorizon.beyondhorizon.client.render.item;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public class BHItemStackRenderers extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation DEFAULT_ICON_TEXTURE = BeyondHorizon.resourceGui("book/icon_default.png");
    private static final Map<String, ResourceLocation> LOADED_ICONS = new HashMap<>();

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
//        if (itemStack.getItem() == BHItems.ICON_ITEMS.get()) {
//            ResourceLocation texture = DEFAULT_ICON_TEXTURE;
//            if (itemStack.getTag() != null && itemStack.getTag().contains("IconLocation")) {
//                String iconLocationStr = itemStack.getTag().getString("IconLocation");
//                if(LOADED_ICONS.containsKey(iconLocationStr)){
//                    texture = LOADED_ICONS.get(iconLocationStr);
//                }else{
//                    texture = BeyondHorizon.resource(iconLocationStr);
//                    LOADED_ICONS.put(iconLocationStr, texture);
//                }
//            }
//            poseStack.pushPose();
//            poseStack.translate(0, 0, 0.5F);
//            RenderSystem.setShader(GameRenderer::getPositionTexShader);
//            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//            RenderSystem.setShaderTexture(0, texture);
//            Tesselator tessellator = Tesselator.getInstance();
//            BufferBuilder bufferbuilder = tessellator.getBuilder();
//            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
//            Matrix4f mx = poseStack.last().pose();
//            int br = 255;
//            bufferbuilder.vertex(mx, (float) 1, (float) 1, (float) 0).uv(1, 0).color(br, br, br, 255).uv2(packedLight).endVertex();
//            bufferbuilder.vertex(mx, (float) 0, (float) 1, (float) 0).uv(0, 0).color(br, br, br, 255).uv2(packedLight).endVertex();
//            bufferbuilder.vertex(mx, (float) 0, (float) 0, (float) 0).uv(0, 1).color(br, br, br, 255).uv2(packedLight).endVertex();
//            bufferbuilder.vertex(mx, (float) 1, (float) 0, (float) 0).uv(1, 1).color(br, br, br, 255).uv2(packedLight).endVertex();
//            tessellator.end();
//            poseStack.popPose();
//        }

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
