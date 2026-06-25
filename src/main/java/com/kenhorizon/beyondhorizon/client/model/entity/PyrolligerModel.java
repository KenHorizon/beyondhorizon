package com.kenhorizon.beyondhorizon.client.model.entity;

import com.kenhorizon.beyondhorizon.client.model.animation.PyrolligerAnim;
import com.kenhorizon.beyondhorizon.server.entity.boss.pyrolliger.Pyrolliger;
import com.kenhorizon.libs.client.model.entity.AdvanceEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PyrolligerModel extends AdvanceEntityModel<Pyrolliger> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart nose;
    private final ModelPart leftArm;
    private final ModelPart leftArmBone;
    private final ModelPart rightArm;
    private final ModelPart rightArmBone;
    private final ModelPart rightLeg;
    private final ModelPart rightLegBone;
    private final ModelPart leftLeg;
    private final ModelPart leftLegBone;

    public PyrolligerModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.nose = this.head.getChild("nose");
        this.leftArm = this.body.getChild("leftArm");
        this.leftArmBone = this.leftArm.getChild("leftArmBone");
        this.rightArm = this.body.getChild("rightArm");
        this.rightArmBone = this.rightArm.getChild("rightArmBone");
        this.rightLeg = this.root.getChild("rightLeg");
        this.rightLegBone = this.rightLeg.getChild("rightLegBone");
        this.leftLeg = this.root.getChild("leftLeg");
        this.leftLegBone = this.leftLeg.getChild("leftLegBone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 38).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 20.0F, 6.0F, new CubeDeformation(0.25F))
                .texOffs(16, 20).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -4.0F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(44, 22).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -10.0F, 0.0F));

        PartDefinition leftArmBone = leftArm.addOrReplaceChild("leftArmBone", CubeListBuilder.create().texOffs(44, 32).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 4.0F, 0.0F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(44, 22).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -10.0F, 0.0F));

        PartDefinition rightArmBone = rightArm.addOrReplaceChild("rightArmBone", CubeListBuilder.create().texOffs(44, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 4.0F, 0.0F));

        PartDefinition rightLeg = root.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

        PartDefinition rightLegBone = rightLeg.addOrReplaceChild("rightLegBone", CubeListBuilder.create().texOffs(0, 28).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition leftLeg = root.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -12.0F, 0.0F));

        PartDefinition leftLegBone = leftLeg.addOrReplaceChild("leftLegBone", CubeListBuilder.create().texOffs(0, 28).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 6.0F, -2.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Pyrolliger entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        this.resetModelDefault();
        this.headLook(this.head, yaw, pitch);
        if (entity.walkAnimation.isMoving()) {
            this.animateWalk(PyrolligerAnim.WALKING_RANGED, limbSwing, limbSwingAmount, 1.0F, 1.0F);
        }
        this.animate(entity.animationPyrobolt, PyrolligerAnim.FIREBALL1, ageInTicks);
        this.animate(entity.animationPyrolance, PyrolligerAnim.FIREBALL2, ageInTicks);
        this.animate(entity.animationBurningHexTrap, PyrolligerAnim.SUMMON_HEX, ageInTicks);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
