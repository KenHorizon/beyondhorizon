package com.kenhorizon.beyondhorizon.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BleedParticle extends TextureSheetParticle {
    protected BleedParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
        super(level, x, y, z);
        this.friction = 0F;
        this.quadSize = 0.25F;
        this.lifetime = 200 + level.random.nextInt(40);
        this.hasPhysics = true;
        this.gravity = 1.25F;
        this.xd = dx;
        this.yd = dy - (double) (this.random.nextFloat() / 500.0F);
        this.zd = dz;
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        fadeOut();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.onGround && this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd -= 0.04D * (double) this.gravity;
            this.move(this.xd, this.yd, this.zd);
            if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
                this.xd *= 1.1D;
                this.zd *= 1.1D;
            }
            this.xd *= (double) this.friction;
            this.yd *= (double) this.friction;
            this.zd *= (double) this.friction;
            if (this.onGround) {
                this.xd *= (double)0.7F;
                this.zd *= (double)0.7F;
            }

        }
    }

    private void fadeOut() {
        this.alpha = (-(1 / (float) lifetime) * age + 1);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        Vec3 vec3 = camera.getPosition();
        if (this.onGround) {
            float f = (float) (Mth.lerp((double) partialTick, this.xo, this.x) - vec3.x());
            float f1 = (float) (Mth.lerp((double) partialTick, this.yo, this.y) - vec3.y() + 0.0222F);
            float f2 = (float) (Mth.lerp((double) partialTick, this.zo, this.z) - vec3.z());
            Quaternionf quaternion = Axis.XP.rotationDegrees(90F);
            Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
            vector3f1.rotate(quaternion);
            Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
            float f4 = this.getQuadSize(partialTick);

            for (int i = 0; i < 4; ++i) {
                Vector3f vector3f = avector3f[i];
                vector3f.rotate(quaternion);
                vector3f.mul(f4);
                vector3f.add(f, f1, f2);
            }

            float f7 = this.getU0();
            float f8 = this.getU1();
            float f5 = this.getV0();
            float f6 = this.getV1();
            int j = this.getLightColor(partialTick);
            vertexConsumer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            vertexConsumer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            vertexConsumer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            vertexConsumer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        } else {
            super.render(vertexConsumer, camera, partialTick);
        }

    }

    @Override
    public float getQuadSize(float scaleFactor) {
        if (this.onGround) {
            return this.quadSize * Mth.clamp(((float) this.age + scaleFactor) / (float) this.lifetime, 0.0F, 1.0F) * 0.50F;
        } else {
            return this.quadSize * 0.50F;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            BleedParticle particle = new BleedParticle(level, x, y, z, dx, dy, dz);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}