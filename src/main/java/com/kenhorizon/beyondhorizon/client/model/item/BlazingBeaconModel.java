package com.kenhorizon.beyondhorizon.client.model.item;// Made with Blockbench 5.1.6

import com.kenhorizon.beyondhorizon.client.model.animation.FayeFlaresAnim;
import com.kenhorizon.libs.client.model.entity.AdvanceEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class BlazingBeaconModel extends AdvanceEntityModel<Entity> {

    public static final AnimationDefinition GENERAL = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("heart", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

	private final ModelPart root;
	private final ModelPart heart;

	public BlazingBeaconModel(ModelPart root) {
		this.root = root.getChild("root");
		this.heart = this.root.getChild("heart");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-6.0F, -1.25F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition heart = root.addOrReplaceChild("heart", CubeListBuilder.create().texOffs(0, 48).addBox(-7.5F, -8.0F, 0.0F, 15.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 0.0F));

		PartDefinition heart_r1 = heart.addOrReplaceChild("heart_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-8.25F, -8.0F, 0.25F, 15.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 0.0F, -0.75F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void animations(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        this.animate(GENERAL, ageInTicks, 0.75F);
    }

    @Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}