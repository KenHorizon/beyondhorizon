package com.kenhorizon.beyondhorizon.client.model.entity.ability;

import com.kenhorizon.beyondhorizon.server.entity.projectiles.HellfireOrb;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.phys.Vec3;

public class HellfireOrbModel extends HierarchicalModel<HellfireOrb> {
    private final ModelPart root;

    public HellfireOrbModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(HellfireOrb entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float delta = ageInTicks - entity.tickCount;
        Vec3 prevV = new Vec3(entity.prevDeltaMovementX, entity.prevDeltaMovementY, entity.prevDeltaMovementZ);
        Vec3 dv = prevV.add(entity.getDeltaMovement().subtract(prevV).scale(delta));
        double d = Math.sqrt(dv.x * dv.x + dv.y * dv.y + dv.z * dv.z);
        if (d != 0) {
            double a = dv.y / d;
            a = Math.max(-10, Math.min(1, a));
            float pitch = -(float) Math.asin(a);
            root.xRot = pitch + (float)Math.PI / 2f;

        }
        this.root.yRot = ageInTicks * 20 * ((float)Math.PI / 180F);
        this.root.xRot = ageInTicks * 20  * ((float)Math.PI / 180F);
        this.root.zRot = ageInTicks  * 20  * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
