package com.kenhorizon.beyondhorizon.client.model.entity;

import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.InfernoShield;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfernoShieldModel extends HierarchicalModel<InfernoShield> {
    private final ModelPart root;

    public InfernoShieldModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -34.0F, -1.0F, 12.0F, 30.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 9).addBox(6.0F, -20.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 9).addBox(-8.0F, -20.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = base.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(28, 0).mirror().addBox(-9.75F, -7.0F, -1.0F, 10.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 25).mirror().addBox(-5.75F, 7.0F, -1.0F, 6.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 35).mirror().addBox(-4.75F, 0.0F, -1.0F, 5.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.75F, -27.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

        PartDefinition cube_r2 = base.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 25).addBox(0.0F, 0.0F, -1.0F, 6.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(0.0F, -7.0F, -1.0F, 5.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 0).addBox(0.0F, -14.0F, -1.0F, 10.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -20.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r3 = base.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(28, 35).addBox(-3.0F, -3.0F, -0.5F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -30.0F, -1.5F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(InfernoShield entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
