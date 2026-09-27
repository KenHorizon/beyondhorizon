package com.kenhorizon.beyondhorizon.client.model.entity;

import com.kenhorizon.beyondhorizon.server.world.entity.boss.pyrolliger.PyroGem;
import com.kenhorizon.libs.client.model.entity.AdvanceEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PyroGemModel extends AdvanceEntityModel<PyroGem> {
    private final ModelPart root;

    public PyroGemModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -13.0F, -1.0F, 6.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 13).addBox(-2.0F, -12.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }


    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void animations(PyroGem entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
