package com.kenhorizon.beyondhorizon.client.particle;


import com.kenhorizon.beyondhorizon.client.particle.world.AfterImageParticleOptions;
import com.kenhorizon.beyondhorizon.mixins.client.accessor.LivingEntityRendererAccessory;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

//https://github.com/seymourimadeit/Piglin-Proliferation/blob/main/src/main/java/tallestred/piglinproliferation/client/particles/AfterImageParticle.java
public class AfterImageParticle extends Particle {
    private final int EntityId;
    public AfterImageParticle(ClientLevel world, double x, double y, double z, int r, int g, int b,int entityId, int lifetimes) {
        super(world, x, y, z);
        this.setSize(6.0F, 6.0F);
        this.x = x;
        this.y = y;
        this.z = z;
        this.rCol = r;
        this.gCol = g;
        this.bCol =  b;
        this.EntityId = entityId;
        this.lifetime = lifetimes;
    }


    public AABB getRenderBoundingBox(float partialTicks) {
        return getBoundingBox().inflate(0.0);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();

        }
        Entity from = this.getFromEntity();
        if(from == null){
            remove();
        }
    }

    public Entity getFromEntity() {
        return EntityId == -1 ? null : level.getEntity(EntityId);
    }

    @Override
    public void render(@NotNull VertexConsumer vertex, Camera camera, float tick) {
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 vec3 = camera.getPosition();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

        double lerpX = Mth.lerp(tick, this.xo, this.x);
        double lerpY = Mth.lerp(tick, this.yo, this.y);
        double lerpZ = Mth.lerp(tick, this.zo, this.z);
        float colorR = this.rCol / 255.0F;
        float colorG = this.gCol / 255.0F;
        float colorB = this.bCol / 255.0F;
        float colorA = 0.5F / Math.abs((float) age + 1);

        PoseStack stack = new PoseStack();
        Entity entity = this.getFromEntity();

        if (entity instanceof LivingEntity living) {
            EntityRenderer<? super LivingEntity> rendererRaw = entityRenderDispatcher.getRenderer(living);

            if (rendererRaw instanceof LivingEntityRenderer) {
                LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) rendererRaw;
                java.util.function.Function<VertexConsumer, VertexConsumer> createTintedBuffer = (originalBuffer) -> new VertexConsumer() {
                    @Override
                    public VertexConsumer vertex(double x, double y, double z) {
                        originalBuffer.vertex(x, y, z);
                        return this;
                    }

                    @Override
                    public VertexConsumer color(int r, int g, int b, int a) {
                        originalBuffer.color(
                                (int) (r * colorR),
                                (int) (g * colorG),
                                (int) (b * colorB),
                                (int) (a * colorA)
                        );
                        return this;
                    }

                    @Override
                    public VertexConsumer uv(float u, float v) {
                        originalBuffer.uv(u, v);
                        return this;
                    }

                    @Override
                    public VertexConsumer overlayCoords(int u, int v) {
                        originalBuffer.overlayCoords(u, v); // 1.21.1의 setUv1 역할
                        return this;
                    }

                    @Override
                    public VertexConsumer uv2(int u, int v) {
                        originalBuffer.uv2(u, v); // 1.21.1의 setUv2 역할
                        return this;
                    }

                    @Override
                    public VertexConsumer normal(float x, float y, float z) {
                        originalBuffer.normal(x, y, z);
                        return this;
                    }

                    @Override
                    public void endVertex() {
                        originalBuffer.endVertex();
                    }

                    @Override
                    public void defaultColor(int r, int g, int b, int a) {
                        originalBuffer.defaultColor(
                                (int) (r * colorR),
                                (int) (g * colorG),
                                (int) (b * colorB),
                                (int) (a * colorA)
                        );
                    }

                    @Override
                    public void unsetDefaultColor() {
                        originalBuffer.unsetDefaultColor();
                    }
                };

                Minecraft minecraft = Minecraft.getInstance();
                boolean flag = !living.isInvisible();
                boolean flag1 = !flag && !living.isInvisibleTo(minecraft.player);
                boolean flag2 = minecraft.shouldEntityAppearGlowing(living);

                MultiBufferSource tintedSource = (requestedType) -> {
                    boolean hasTexture = requestedType.format().getElements().contains(DefaultVertexFormat.ELEMENT_UV0);
                    boolean hasNormal = requestedType.format().getElements().contains(DefaultVertexFormat.ELEMENT_NORMAL);

                    if (hasTexture && hasNormal) {
                        RenderType ghostType = this.getRenderType(living, renderer, flag, flag1, flag2);
                        VertexConsumer originalBuffer = multibuffersource$buffersource.getBuffer(ghostType);
                        return createTintedBuffer.apply(originalBuffer);
                    } else {
                        VertexConsumer originalBuffer = multibuffersource$buffersource.getBuffer(requestedType);
                        return createTintedBuffer.apply(originalBuffer);
                    }
                };

                entityRenderDispatcher.render(living,
                        lerpX - vec3.x(), lerpY - vec3.y(), lerpZ - vec3.z(),
                        living.getYRot(), tick, stack, tintedSource, entityRenderDispatcher.getPackedLightCoords(living, tick));
            }
        }
    }

    public RenderType getRenderType(LivingEntity entity, LivingEntityRenderer<LivingEntity, ?> renderer, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        ResourceLocation resourcelocation = renderer.getTextureLocation(entity);
        if (p_230496_3_) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (p_230496_2_) {
            return RenderType.entityTranslucent(resourcelocation);
        } else {
            return p_230496_4_ ? RenderType.outline(resourcelocation) : null;
        }

    }


    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<AfterImageParticleOptions> {
        @Override
        public Particle createParticle(AfterImageParticleOptions data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AfterImageParticle particle;
            particle = new AfterImageParticle(level, x, y, z, data.getR(), data.getG(),data.getB(),data.getEntityId(), data.getDuration());
            return particle;
        }
    }
}