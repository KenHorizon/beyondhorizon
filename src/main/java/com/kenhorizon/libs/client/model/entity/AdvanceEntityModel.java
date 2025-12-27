package com.kenhorizon.libs.client.model.entity;

import com.kenhorizon.libs.client.animation.AdvanceKeyframeAnimation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.function.Function;

public abstract class AdvanceEntityModel<T extends Entity> extends EntityModel<T> {
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    public AdvanceEntityModel() {
        this(RenderType::entityCutoutNoCull);
    }

    public AdvanceEntityModel(Function<ResourceLocation, RenderType> typeFunction) {
        super(typeFunction);
    }

    public abstract ModelPart root();

    public ModelPart head() {
        return null;
    }

    public void resetModelDefault() {
        this.root().getAllParts().forEach(ModelPart::resetPose);
    }

    public Optional<ModelPart> getAnyDescendantWithName(String name) {
        return name.equals("root") ? Optional.of(this.root()) : this.root().getAllParts().filter((modelPart) -> {
            return modelPart.hasChild(name);
        }).findFirst().map((modelPart) -> {
            return modelPart.getChild(name);
        });
    }

    protected void headLook(ModelPart head, float yaw, float pitch) {
        head.xRot = pitch * ((float) Math.PI / 180F);
        head.yRot = yaw * ((float) Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        this.root().render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
    }
    protected void animate(AnimationState animation, AnimationDefinition pAnimationDefinition, float pAgeInTicks) {
        this.animate(animation, pAnimationDefinition, pAgeInTicks, 1.0F);
    }

    public void animateIdle(AnimationDefinition animationDefinition, float ageInTicks, float maxAnimationSpeed) {
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
