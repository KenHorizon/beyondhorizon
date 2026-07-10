package com.kenhorizon.libs.client.model.entity;

import com.kenhorizon.libs.client.animation.AdvanceKeyframeAnimation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public abstract class AdvanceHumaniodModel<T extends LivingEntity> extends HumanoidModel<T> implements IAnimatedModelEntity, ArmedModel, HeadedModel {
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
    public static final CubeDeformation OUTER_ARMOR_DEFORMATION = new CubeDeformation(1.0F);
    public static final CubeDeformation INNER_ARMOR_DEFORMATION = new CubeDeformation(0.5F);
    public AdvanceHumaniodModel(ModelPart modelPart, Function<ResourceLocation, RenderType> renderType) {
        super(modelPart, renderType);
    }

    public AdvanceHumaniodModel(ModelPart modelPart) {
        this(modelPart, RenderType::entityTranslucent);
    }

    public abstract ModelPart root();
    public abstract ModelPart head();

    @Override
    public Optional<ModelPart> getAnyDescendantWithName(String name) {
        return name.equals("root") ? Optional.of(this.root()) : this.root().getAllParts().filter((hasChild) -> {
            return hasChild.hasChild(name);
        }).findFirst().map((child) -> {
            return child.getChild(name);
        });
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        this.getArm(humanoidArm).translateAndRotate(poseStack);
    }

    public void resetModelDefault() {
        this.root().getAllParts().forEach(ModelPart::resetPose);
    }

    protected ModelPart getArm(HumanoidArm humanoidArm) {
        return humanoidArm == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }


    public static LayerDefinition createInnerArmorLayer() {
        return LayerDefinition.create(createMesh(INNER_ARMOR_DEFORMATION, 0.0F), 64, 32);
    }
    public static LayerDefinition createOuterArmorLayer() {
        return LayerDefinition.create(createMesh(OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        this.resetModelDefault();
        if (this.head() != null) {
            this.headLook(this.head(), yaw, pitch);
        }
        this.animations(entity,limbSwing,limbSwingAmount, ageInTicks, pitch, pitch);
    }

    public abstract void animations(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch);

    protected void headLook(ModelPart head, float yaw, float pitch) {
        head.xRot = pitch * ((float) Math.PI / 180F);
        head.yRot = yaw * ((float) Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        this.root().render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
    }

    protected void animate(AnimationState animation, AnimationDefinition definition, float ageInTicks) {
        this.animate(animation, definition, ageInTicks, 1.0F);
    }

    public void animate(AnimationDefinition animationDefinition, float ageInTicks, float maxAnimationSpeed) {
        long speed = (long) (ageInTicks * 50.0F * maxAnimationSpeed);
        AdvanceKeyframeAnimation.animate(this, animationDefinition, speed, 1.0F, ANIMATION_VECTOR_CACHE);
    }

    protected void animateWalk(AnimationDefinition definition, float limbSwing, float limbSwingAmount, float maxSpeed, float scale) {
        long speed = (long)(limbSwing * 50.0F * maxSpeed);
        float walkAccumulated = Math.min(limbSwingAmount * scale, 1.0F);
        AdvanceKeyframeAnimation.animate(this, definition, speed, walkAccumulated, ANIMATION_VECTOR_CACHE);
    }

    protected void animate(AnimationState animation, AnimationDefinition definition, float updateTime, float speed) {
        animation.updateTime(updateTime, speed);
        animation.ifStarted((state) -> {
            AdvanceKeyframeAnimation.animate(this, definition, state.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
        });
    }

    protected void applyStatic(AnimationDefinition definition) {
        AdvanceKeyframeAnimation.animate(this, definition, 0L, 1.0F, ANIMATION_VECTOR_CACHE);
    }
}
