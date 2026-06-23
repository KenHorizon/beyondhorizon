package com.kenhorizon.beyondhorizon.client.model.entity;

import com.kenhorizon.beyondhorizon.server.entity.ability.InfernalSlashAbility;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingSpear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class InfernalSlashModel extends EntityModel<InfernalSlashAbility> {
    private final ModelPart root;
    private final ModelPart core;
    private final ModelPart blade;
    private final ModelPart handle;

    public InfernalSlashModel(ModelPart root) {
        this.root = root.getChild("root");
        this.core = this.root.getChild("core");
        this.blade = this.root.getChild("blade");
        this.handle = this.root.getChild("handle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition core = root.addOrReplaceChild("core", CubeListBuilder.create().texOffs(30, 33).addBox(-2.0F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(30, 33).mirror().addBox(1.0F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(20, 23).addBox(-1.0F, -2.5F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.5F, 0.0F));

        PartDefinition blade = root.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -6.2322F, -5.075F, 1.0F, 40.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -52.5178F, 0.0F));

        PartDefinition cube_r1 = blade.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 48).addBox(-0.5F, -3.5F, -3.5F, 1.0F, 7.0F, 7.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, 11.75F, -0.075F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r2 = blade.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 0).addBox(-0.5F, -3.5F, -3.5F, 1.0F, 7.0F, 7.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, -6.25F, -0.075F, -0.7854F, 0.0F, 0.0F));

        PartDefinition handle = root.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(22, 33).addBox(-1.0F, -0.063F, -0.8172F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.937F, -0.1828F));

        PartDefinition handle_r1 = handle.addOrReplaceChild("handle_r1", CubeListBuilder.create().texOffs(38, 0).addBox(0.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, -3.813F, -7.0672F, -0.7854F, 0.0F, 0.0F));

        PartDefinition handle_r2 = handle.addOrReplaceChild("handle_r2", CubeListBuilder.create().texOffs(38, 0).addBox(0.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, -1.313F, 0.9328F, -0.7854F, 0.0F, 0.0F));

        PartDefinition handle_r3 = handle.addOrReplaceChild("handle_r3", CubeListBuilder.create().texOffs(21, 13).addBox(-1.0F, -2.0F, -3.5F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.813F, 3.6828F, -0.1309F, 0.0F, 0.0F));

        PartDefinition handle_r4 = handle.addOrReplaceChild("handle_r4", CubeListBuilder.create().texOffs(21, 13).addBox(-1.0F, -2.0F, -3.5F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.813F, -4.3172F, 0.1309F, 0.0F, 0.0F));

        PartDefinition handle_r5 = handle.addOrReplaceChild("handle_r5", CubeListBuilder.create().texOffs(50, 2).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 14.1441F, 0.2257F, -0.7854F, 0.0F, 0.0F));

        PartDefinition handle_r6 = handle.addOrReplaceChild("handle_r6", CubeListBuilder.create().texOffs(38, 0).addBox(0.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, -3.813F, 7.9328F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(InfernalSlashAbility entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.root.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.root.xRot = headPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
