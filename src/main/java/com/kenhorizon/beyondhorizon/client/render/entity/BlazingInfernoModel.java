package com.kenhorizon.beyondhorizon.client.render.entity;

import com.kenhorizon.beyondhorizon.server.entity.boss.BlazingInferno;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlazingInfernoModel extends HierarchicalModel<BlazingInferno> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart rod0;
    private final ModelPart rod1;
    private final ModelPart armor;
    private final ModelPart rightShell;
    private final ModelPart rightFrontLens;
    private final ModelPart rightBackLens;
    private final ModelPart rightShoulderArmor;
    private final ModelPart leftShell;
    private final ModelPart leftFrontLens;
    private final ModelPart leftBackLens;
    private final ModelPart leftShoulderArmor;
    private final ModelPart lowerArmor;
    private final ModelPart leftLowerArmor;
    private final ModelPart rightLowerArmor;
    private final ModelPart core;
    private final ModelPart head;

    public BlazingInfernoModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.rod0 = this.root.getChild("rod0");
        this.rod1 = this.root.getChild("rod1");
        this.armor = this.root.getChild("armor");
        this.rightShell = this.armor.getChild("rightShell");
        this.rightFrontLens = this.rightShell.getChild("rightFrontLens");
        this.rightBackLens = this.rightShell.getChild("rightBackLens");
        this.rightShoulderArmor = this.rightShell.getChild("rightShoulderArmor");
        this.leftShell = this.armor.getChild("leftShell");
        this.leftFrontLens = this.leftShell.getChild("leftFrontLens");
        this.leftBackLens = this.leftShell.getChild("leftBackLens");
        this.leftShoulderArmor = this.leftShell.getChild("leftShoulderArmor");
        this.lowerArmor = this.armor.getChild("lowerArmor");
        this.leftLowerArmor = this.armor.getChild("leftLowerArmor");
        this.rightLowerArmor = this.armor.getChild("rightLowerArmor");
        this.core = this.root.getChild("core");
        this.head = this.root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(64, 0).addBox(-2.0F, 0.6F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -20.6F, 0.0F));

        PartDefinition rod0 = root.addOrReplaceChild("rod0", CubeListBuilder.create().texOffs(0, 32).addBox(-5.75F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-0.75F, -4.0F, -7.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-1.75F, -4.0F, 5.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(4.25F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.25F, -16.0F, 0.0F));

        PartDefinition rod1 = root.addOrReplaceChild("rod1", CubeListBuilder.create().texOffs(0, 32).addBox(-10.5F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-0.5F, -4.0F, -12.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-1.5F, -4.0F, 10.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(8.5F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -25.0F, 0.0F));

        PartDefinition armor = root.addOrReplaceChild("armor", CubeListBuilder.create(), PartPose.offset(0.0F, -21.0F, 0.0F));

        PartDefinition rightShell = armor.addOrReplaceChild("rightShell", CubeListBuilder.create().texOffs(40, 61).addBox(-4.25F, -4.0F, -5.0F, 5.0F, 9.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 67).addBox(0.75F, -4.0F, -3.0F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.3F, -12.0F, 0.0F));

        PartDefinition rightFrontLens = rightShell.addOrReplaceChild("rightFrontLens", CubeListBuilder.create().texOffs(64, 31).mirror().addBox(-3.0F, -2.5F, -4.0F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.3F, 0.5F, -2.0F));

        PartDefinition rightBackLens = rightShell.addOrReplaceChild("rightBackLens", CubeListBuilder.create().texOffs(64, 31).mirror().addBox(-1.5F, -3.0F, -0.5F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.8F, 1.0F, 5.5F));

        PartDefinition rightShoulderArmor = rightShell.addOrReplaceChild("rightShoulderArmor", CubeListBuilder.create(), PartPose.offset(0.7F, -3.0F, 0.5F));

        PartDefinition leftShell = armor.addOrReplaceChild("leftShell", CubeListBuilder.create().texOffs(40, 61).mirror().addBox(-0.75F, -4.0F, -5.0F, 5.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 67).mirror().addBox(-2.75F, -4.0F, -3.0F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.2F, -12.0F, 0.0F));

        PartDefinition leftFrontLens = leftShell.addOrReplaceChild("leftFrontLens", CubeListBuilder.create().texOffs(64, 31).addBox(-2.5F, -3.0F, -0.5F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.7F, 1.0F, -5.5F));

        PartDefinition leftBackLens = leftShell.addOrReplaceChild("leftBackLens", CubeListBuilder.create().texOffs(64, 31).addBox(-1.5F, -3.0F, -0.5F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.7F, 1.0F, 5.5F));

        PartDefinition leftShoulderArmor = leftShell.addOrReplaceChild("leftShoulderArmor", CubeListBuilder.create(), PartPose.offset(-3.8F, -3.5F, 0.5F));

        PartDefinition lowerArmor = armor.addOrReplaceChild("lowerArmor", CubeListBuilder.create(), PartPose.offset(0.0F, -5.5F, 0.0F));

        PartDefinition lowerArmor_r1 = lowerArmor.addOrReplaceChild("lowerArmor_r1", CubeListBuilder.create().texOffs(16, 24).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition leftLowerArmor = armor.addOrReplaceChild("leftLowerArmor", CubeListBuilder.create(), PartPose.offset(-5.5F, -6.5F, 0.0F));

        PartDefinition rightLowerArmor = armor.addOrReplaceChild("rightLowerArmor", CubeListBuilder.create(), PartPose.offset(5.5F, -6.5F, 0.0F));

        PartDefinition core = root.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -31.0F, 0.0F));

        PartDefinition coreRings_r1 = core.addOrReplaceChild("coreRings_r1", CubeListBuilder.create().texOffs(0, 24).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, -16.0F, -3.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -40.0F, -1.0F));

        return LayerDefinition.create(meshdefinition, 80, 80);
    }

    @Override
    public void setupAnim(BlazingInferno entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

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
