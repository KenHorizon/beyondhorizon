package com.kenhorizon.beyondhorizon.client.model.entity;

import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.BlazingSpear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BlazingSpearModel extends EntityModel<BlazingSpear> {
    private final ModelPart root;
    private final ModelPart handle;
    private final ModelPart core2;
    private final ModelPart blade;
    private final ModelPart core;
    private final ModelPart core3;

    public BlazingSpearModel(ModelPart root) {
        this.root = root.getChild("root");
        this.handle = this.root.getChild("handle");
        this.core2 = this.handle.getChild("core2");
        this.blade = this.root.getChild("blade");
        this.core = this.blade.getChild("core");
        this.core3 = this.blade.getChild("core3");
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -30.0F, -1.0F, 2.0F, 60.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

        PartDefinition handle = root.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.0F, 20.1595F, -0.1524F));

        PartDefinition handle_r1 = handle.addOrReplaceChild("handle_r1", CubeListBuilder.create().texOffs(38, 33).addBox(0.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, -3.1595F, 0.9024F, -0.7854F, 0.0F, 0.0F));

        PartDefinition handle_r2 = handle.addOrReplaceChild("handle_r2", CubeListBuilder.create().texOffs(38, 41).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 12.0476F, 0.1953F, -0.7854F, 0.0F, 0.0F));

        PartDefinition core2 = handle.addOrReplaceChild("core2", CubeListBuilder.create().texOffs(8, 38).addBox(-2.0F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(8, 38).mirror().addBox(1.0F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(24, 33).addBox(-1.0F, -2.5F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.6595F, 0.1524F));

        PartDefinition blade = root.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(8, 0).addBox(-0.5F, -11.2322F, -4.5F, 1.0F, 20.0F, 9.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -33.7678F, 0.0F));

        PartDefinition handle_r3 = blade.addOrReplaceChild("handle_r3", CubeListBuilder.create().texOffs(38, 33).addBox(0.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, 11.7678F, 0.75F, -0.7854F, 0.0F, 0.0F));

        PartDefinition handle_r4 = blade.addOrReplaceChild("handle_r4", CubeListBuilder.create().texOffs(28, 14).addBox(-1.0F, -1.5F, -3.5F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.2678F, 3.5F, -0.0873F, 0.0F, 0.0F));

        PartDefinition handle_r5 = blade.addOrReplaceChild("handle_r5", CubeListBuilder.create().texOffs(28, 24).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 6.2678F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition handle_r6 = blade.addOrReplaceChild("handle_r6", CubeListBuilder.create().texOffs(28, 24).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 9.2678F, -2.5F, 1.0036F, 0.0F, 0.0F));

        PartDefinition handle_r7 = blade.addOrReplaceChild("handle_r7", CubeListBuilder.create().texOffs(8, 29).addBox(-1.0F, -1.5F, -3.5F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 9.7678F, 3.0F, -1.0036F, 0.0F, 0.0F));

        PartDefinition handle_r8 = blade.addOrReplaceChild("handle_r8", CubeListBuilder.create().texOffs(8, 29).addBox(-1.0F, -1.5F, -2.5F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, 51.0178F, 3.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition handle_r9 = blade.addOrReplaceChild("handle_r9", CubeListBuilder.create().texOffs(8, 29).addBox(-1.0F, -1.5F, -3.5F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 53.7678F, 3.5F, -1.0036F, 0.0F, 0.0F));

        PartDefinition handle_r10 = blade.addOrReplaceChild("handle_r10", CubeListBuilder.create().texOffs(28, 24).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 53.2678F, -3.25F, 1.0036F, 0.0F, 0.0F));

        PartDefinition handle_r11 = blade.addOrReplaceChild("handle_r11", CubeListBuilder.create().texOffs(28, 24).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, 51.2678F, -4.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition handle_r12 = blade.addOrReplaceChild("handle_r12", CubeListBuilder.create().texOffs(8, 29).addBox(-1.0F, -1.5F, -3.5F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 17.7678F, 3.5F, -1.0036F, 0.0F, 0.0F));

        PartDefinition handle_r13 = blade.addOrReplaceChild("handle_r13", CubeListBuilder.create().texOffs(28, 24).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 17.2678F, -3.25F, 1.0036F, 0.0F, 0.0F));

        PartDefinition handle_r14 = blade.addOrReplaceChild("handle_r14", CubeListBuilder.create().texOffs(28, 14).addBox(-1.0F, -1.5F, -3.5F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, 15.0178F, 3.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition handle_r15 = blade.addOrReplaceChild("handle_r15", CubeListBuilder.create().texOffs(28, 24).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, 15.2678F, -4.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition handle_r16 = blade.addOrReplaceChild("handle_r16", CubeListBuilder.create().texOffs(28, 24).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.2678F, -4.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition blade_r1 = blade.addOrReplaceChild("blade_r1", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -6.0F, -6.0F, 1.0F, 7.0F, 7.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.5F, -7.7322F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition core = blade.addOrReplaceChild("core", CubeListBuilder.create().texOffs(8, 38).addBox(-2.0F, 6.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(8, 38).mirror().addBox(1.0F, 6.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(24, 33).addBox(-1.0F, 6.5F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.2678F, 0.0F));

        PartDefinition core3 = blade.addOrReplaceChild("core3", CubeListBuilder.create().texOffs(8, 38).addBox(-2.0F, 6.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(8, 38).mirror().addBox(1.0F, 6.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(24, 33).addBox(-1.0F, 6.5F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.2678F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }




    @Override
    public void setupAnim(BlazingSpear entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
