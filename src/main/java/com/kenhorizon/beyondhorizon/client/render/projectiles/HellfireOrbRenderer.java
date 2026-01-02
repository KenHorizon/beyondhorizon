package com.kenhorizon.beyondhorizon.client.render.projectiles;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.model.entity.ability.HellfireOrbModel;
import com.kenhorizon.beyondhorizon.client.render.BHModelLayers;
import com.kenhorizon.beyondhorizon.client.render.BHRenderTypes;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.HellfireOrb;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class HellfireOrbRenderer extends EntityRenderer<HellfireOrb> {
    private final HellfireOrbModel model;
    public static final ResourceLocation TEXTURE = BeyondHorizon.resource("textures/entity/effect/hellfire_orb/hellfire_orb.png");
    private static final ResourceLocation TRAIL_TEXTURE = BeyondHorizon.resource("textures/particle/teletor_trail.png");
    private final RandomSource random = RandomSource.create();

    public HellfireOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new HellfireOrbModel(context.bakeLayer(BHModelLayers.HELLFIRE_ORB));
    }

    @Override
    protected int getBlockLightLevel(HellfireOrb entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(HellfireOrb entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose((new Quaternionf()).setAngleAxis(entityYaw * ((float)Math.PI / 180F), 0, -1.0F, 0));
        VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
        this.model.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, 0, 0);
        this.model.renderToBuffer(poseStack, builder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        if (entity.hasTrail()) {
            double x = Mth.lerp(partialTicks, entity.xOld, entity.getX());
            double y = Mth.lerp(partialTicks, entity.yOld, entity.getY());
            double z = Mth.lerp(partialTicks, entity.zOld, entity.getZ());
            float ran = 0.04f;
            float r = 195/255F + this.random.nextFloat() * ran * 1.5F;
            float g = 95/255F + this.random.nextFloat() * ran;
            float b = 3/255F + this.random.nextFloat() * ran;
            poseStack.pushPose();
            poseStack.translate(-x, -y, -z);
            renderTrail(entity, partialTicks, poseStack, buffer, r, g, b, 1.0F, packedLight);
            poseStack.popPose();
        }
    }
    private void renderTrail(HellfireOrb entityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, float trailR, float trailG, float trailB, float trailA, int packedLightIn) {
        int sampleSize = 10;
        float trailHeight = 0.4F;
        float trailYRot = 0;
        float trailZRot = 0;
        Vec3 topAngleVec = new Vec3(trailHeight, trailHeight, 0).yRot(trailYRot).zRot(trailZRot);
        Vec3 bottomAngleVec = new Vec3(-trailHeight, -trailHeight, 0).yRot(trailYRot).zRot(trailZRot);
        Vec3 drawFrom = entityIn.getTrailPosition(0, partialTicks);
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = bufferIn.getBuffer(BHRenderTypes.getTrailEffect(TRAIL_TEXTURE));

        for (int samples = 0; samples < sampleSize; samples++) {
            Vec3 sample = entityIn.getTrailPosition(samples + 2, partialTicks);
            float u1 = samples / (float) sampleSize;
            float u2 = u1 + 1 / (float) sampleSize;
            addVertex(vertexconsumer, matrix4f,matrix3f, drawFrom, bottomAngleVec, trailR,trailG,trailB,u1, 1F, packedLightIn);
            addVertex(vertexconsumer, matrix4f,matrix3f, sample, bottomAngleVec,  trailR,trailG,trailB,u2,1F, packedLightIn);
            addVertex(vertexconsumer, matrix4f,matrix3f, sample, topAngleVec, trailR,trailG,trailB,u2,0F, packedLightIn);
            addVertex(vertexconsumer, matrix4f,matrix3f, drawFrom, topAngleVec, trailR,trailG,trailB, u1,0F, packedLightIn);
            drawFrom = sample;
        }
    }
    private void addVertex(VertexConsumer consumer, Matrix4f matrix,Matrix3f matrix3, Vec3 pos, Vec3 offset,float r,float g,float b, float u, float v, int light) {
        consumer.vertex(matrix,
                        (float) (pos.x + offset.x),
                        (float) (pos.y + offset.y),
                        (float) (pos.z + offset.z))
                .color(r, g, b, 1.0F)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(matrix3,0.0F, 1.0F, 0.0F).endVertex();
    }
    @Override
    public ResourceLocation getTextureLocation(HellfireOrb entity) {
        return TEXTURE;
    }
}
