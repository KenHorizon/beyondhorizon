package com.kenhorizon.beyondhorizon.client.model.entity;

import com.kenhorizon.beyondhorizon.client.model.animation.BlazingInfernoAnim;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.BlazingInferno;
import com.kenhorizon.libs.client.model.entity.AdvanceEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlazingInfernoModel extends AdvanceEntityModel<BlazingInferno> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart rod0;
    private final ModelPart rod1;
    private final ModelPart armor;
    private final ModelPart rightShell;
    private final ModelPart rightShoulderArmor;
    private final ModelPart leftShell;
    private final ModelPart leftShoulderArmor;
    private final ModelPart lowerArmor;
    private final ModelPart core;
    private final ModelPart head;

    public BlazingInfernoModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.rod0 = this.root.getChild("rod0");
        this.rod1 = this.root.getChild("rod1");
        this.armor = this.root.getChild("armor");
        this.rightShell = this.armor.getChild("rightShell");
        this.rightShoulderArmor = this.rightShell.getChild("rightShoulderArmor");
        this.leftShell = this.armor.getChild("leftShell");
        this.leftShoulderArmor = this.leftShell.getChild("leftShoulderArmor");
        this.lowerArmor = this.armor.getChild("lowerArmor");
        this.core = this.root.getChild("core");
        this.head = this.root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(64, 0).addBox(-2.0F, 0.6F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -20.6F, 0.0F));

        PartDefinition rod0 = root.addOrReplaceChild("rod0", CubeListBuilder.create().texOffs(0, 32).addBox(-5.75F, 2.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-0.75F, 2.0F, -7.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-1.75F, 2.0F, 5.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(4.25F, 2.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.25F, -22.0F, 0.0F));

        PartDefinition rod1 = root.addOrReplaceChild("rod1", CubeListBuilder.create().texOffs(0, 32).addBox(-10.5F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-0.5F, -4.0F, -12.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-1.5F, -4.0F, 10.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(8.5F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -25.0F, 0.0F));

        PartDefinition armor = root.addOrReplaceChild("armor", CubeListBuilder.create(), PartPose.offset(0.0F, -21.0F, 0.0F));

        PartDefinition rightShell = armor.addOrReplaceChild("rightShell", CubeListBuilder.create().texOffs(40, 61).addBox(-4.25F, -4.0F, -5.0F, 5.0F, 9.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 67).addBox(0.75F, -4.0F, -3.0F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(64, 31).mirror().addBox(-4.3F, -2.0F, 5.0F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(64, 31).mirror().addBox(-4.3F, -2.0F, -6.0F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.3F, -12.0F, 0.0F));

        PartDefinition rightShoulderArmor_r1 = rightShell.addOrReplaceChild("rightShoulderArmor_r1", CubeListBuilder.create().texOffs(46, 41).addBox(-1.0F, -0.5F, -4.5F, 8.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.45F, -3.5F, 0.5F, 0.0F, 0.0F, 1.309F));

        PartDefinition rightShoulderArmor = rightShell.addOrReplaceChild("rightShoulderArmor", CubeListBuilder.create(), PartPose.offset(0.7F, -3.0F, 0.5F));

        PartDefinition leftShell = armor.addOrReplaceChild("leftShell", CubeListBuilder.create().texOffs(40, 61).mirror().addBox(-0.75F, -4.0F, -5.0F, 5.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 67).mirror().addBox(-2.75F, -4.0F, -3.0F, 2.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(64, 31).addBox(1.2F, -2.0F, -6.0F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(64, 31).addBox(1.2F, -2.0F, 5.0F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.2F, -12.0F, 0.0F));

        PartDefinition leftShoulderArmor_r1 = leftShell.addOrReplaceChild("leftShoulderArmor_r1", CubeListBuilder.create().texOffs(46, 41).addBox(-1.0F, -0.5F, -4.5F, 8.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.05F, -3.5F, 0.5F, 0.0F, 0.0F, 1.8326F));

        PartDefinition leftShoulderArmor = leftShell.addOrReplaceChild("leftShoulderArmor", CubeListBuilder.create(), PartPose.offset(-3.8F, -3.5F, 0.5F));

        PartDefinition lowerArmor = armor.addOrReplaceChild("lowerArmor", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition lowerArmor_r1 = lowerArmor.addOrReplaceChild("lowerArmor_r1", CubeListBuilder.create().texOffs(16, 24).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition core = root.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -31.0F, 0.0F));

        PartDefinition coreRings_r1 = core.addOrReplaceChild("coreRings_r1", CubeListBuilder.create().texOffs(0, 24).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, -16.0F, -3.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -39.0F, -1.0F));

        return LayerDefinition.create(meshdefinition, 80, 80);
    }

    @Override
    public void animations(BlazingInferno entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        this.resetModelDefault();
        this.headLook(this.head, yaw, pitch);
        if (entity.walkAnimation.isMoving()) {
            this.applyStatic(BlazingInfernoAnim.WALKING);
        }
        this.animate(BlazingInfernoAnim.GENERAL, ageInTicks, 0.75F);
        this.animate(entity.animationActive, BlazingInfernoAnim.ACTIVE, ageInTicks);
        this.animate(entity.animationInactive, BlazingInfernoAnim.INACTIVE, ageInTicks);
        this.animate(entity.animationDeath, BlazingInfernoAnim.DEATH, ageInTicks);
        this.animate(entity.animationEnragedPhase, BlazingInfernoAnim.SECOND_PHASE, ageInTicks);
        this.animate(entity.animationBlazingRod, BlazingInfernoAnim.SHOOTING, ageInTicks);
        this.animate(entity.animationPrepareDeathRay, BlazingInfernoAnim.PREPARE_DEATH_RAY, ageInTicks);
        this.animate(entity.animationDeathRay, BlazingInfernoAnim.DEATH_RAY, ageInTicks);
        this.animate(entity.animationEruption, BlazingInfernoAnim.SHOCKWAVE, ageInTicks);
        this.animate(entity.animationDashes, BlazingInfernoAnim.DASH, ageInTicks);
        this.animate(entity.animationGroundSlam, BlazingInfernoAnim.GROUND_SLAM, ageInTicks);
        this.animate(entity.animationShockwave, BlazingInfernoAnim.SHOCKWAVE, ageInTicks);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
