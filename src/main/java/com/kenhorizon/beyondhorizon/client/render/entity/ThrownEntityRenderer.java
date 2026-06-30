package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.server.entity.projectiles.ExtendedThrownWeapon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class ThrownEntityRenderer<T extends ExtendedThrownWeapon> extends EntityRenderer<T> {
    private final ItemRenderer itemRenderer;

    public ThrownEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        doRenderTransformations(entity, partialTicks, poseStack);
        poseStack.translate(-0.10D, -0.20D, 0.0D);
        ItemStack itemStackModel = entity.getItemStack();
        if (!itemStackModel.isEmpty()) {
            BakedModel bakedModel = this.itemRenderer.getModel(itemStackModel, entity.level(), (LivingEntity) null, entity.getId());
            this.itemRenderer.render(itemStackModel, ItemDisplayContext.GROUND, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, bakedModel);
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    protected void doRenderTransformations(T entity, float partialTicks, PoseStack poseStack) {
        poseStack.scale(2.0f, 2.0f, 2.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) - 45.0f));

    }
}
