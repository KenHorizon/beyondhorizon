package com.kenhorizon.beyondhorizon.client.model.entity;

import com.kenhorizon.beyondhorizon.client.model.animation.PyrolligerAnim;
import com.kenhorizon.beyondhorizon.server.entity.BHLibEntity;
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
    private final ModelPart eyebrowL;
    private final ModelPart eyebrowR;
    private final ModelPart nose;
    private final ModelPart leftArm;
    private final ModelPart leftArmBone;
    private final ModelPart rightArm;
    private final ModelPart rightArmBone;
    private final ModelPart sword;
    private final ModelPart rightLeg;
    private final ModelPart rightLegBone;
    private final ModelPart leftLeg;
    private final ModelPart leftLegBone;

    public PyrolligerModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.eyebrowL = this.head.getChild("eyebrowL");
        this.eyebrowR = this.head.getChild("eyebrowR");
        this.nose = this.head.getChild("nose");
        this.leftArm = this.body.getChild("leftArm");
        this.leftArmBone = this.leftArm.getChild("leftArmBone");
        this.rightArm = this.body.getChild("rightArm");
        this.rightArmBone = this.rightArm.getChild("rightArmBone");
        this.sword = this.rightArmBone.getChild("sword");
        this.rightLeg = this.root.getChild("rightLeg");
        this.rightLegBone = this.rightLeg.getChild("rightLegBone");
        this.leftLeg = this.root.getChild("leftLeg");
        this.leftLegBone = this.leftLeg.getChild("leftLegBone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 22).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 20.0F, 6.0F, new CubeDeformation(0.25F))
                .texOffs(28, 40).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 22).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition eyebrowL = head.addOrReplaceChild("eyebrowL", CubeListBuilder.create().texOffs(60, 38).addBox(-3.0F, -1.0F, 0.023F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -5.0F, -4.025F));

        PartDefinition eyebrowR = head.addOrReplaceChild("eyebrowR", CubeListBuilder.create().texOffs(60, 38).mirror().addBox(0.0F, -1.0F, 0.023F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -5.0F, -4.025F));

        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(60, 18).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -4.0F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(22, 58).mirror().addBox(-1.0F, -1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -11.0F, 0.0F));

        PartDefinition leftArmBone = leftArm.addOrReplaceChild("leftArmBone", CubeListBuilder.create().texOffs(56, 40).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 5.0F, 0.0F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(22, 58).addBox(-3.0F, -1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -11.0F, 0.0F));

        PartDefinition rightArmBone = rightArm.addOrReplaceChild("rightArmBone", CubeListBuilder.create().texOffs(56, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 5.0F, 0.0F));

        PartDefinition sword = rightArmBone.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(0, 48).addBox(-0.5F, -1.0022F, -8.3293F, 1.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.5F, -2.0022F, -28.3293F, 1.0F, 4.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(22, 48).addBox(-0.5F, -4.0022F, -10.3293F, 1.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0022F, 4.8293F));

        PartDefinition cube_r1 = sword.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(12, 60).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0022F, -10.3293F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r2 = sword.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 60).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.0022F, 3.1707F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r3 = sword.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(38, 18).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, 0.5988F, -28.314F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r4 = sword.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(42, 18).addBox(-0.5F, 0.0F, -1.85F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(42, 20).addBox(-0.5F, -1.0F, -0.85F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.001F))
                .texOffs(38, 18).addBox(-0.5F, -1.825F, -1.85F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.0F, 0.5988F, -28.314F, -0.7854F, 0.0F, 0.0F));

        PartDefinition rightLeg = root.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(38, 58).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

        PartDefinition rightLegBone = rightLeg.addOrReplaceChild("rightLegBone", CubeListBuilder.create().texOffs(56, 50).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -2.0F));

        PartDefinition leftLeg = root.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(38, 58).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -12.0F, 0.0F));

        PartDefinition leftLegBone = leftLeg.addOrReplaceChild("leftLegBone", CubeListBuilder.create().texOffs(56, 50).mirror().addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 6.0F, -2.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
    @Override
    public void animations(Pyrolliger entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        this.resetModelDefault();
        this.sword.visible = entity.isVisibleSword();
        this.headLook(this.head, yaw, pitch);
        if (entity.walkAnimation.isMoving() && entity.getMode() == Pyrolliger.Mode.RANGED) {
            this.animateWalk(PyrolligerAnim.WALKING_RANGED, limbSwing, limbSwingAmount, 1.0F, 1.0F);
        }
        if (entity.walkAnimation.isMoving() && entity.getMode() == Pyrolliger.Mode.MELEE) {
            this.animateWalk(PyrolligerAnim.WALKING_MELEE, limbSwing, limbSwingAmount, 1.0F, 1.0F);
        }
        this.animate(entity.animationIdle1, PyrolligerAnim.IDLE1, ageInTicks);
        this.animate(entity.animationIdle2, PyrolligerAnim.IDLE2, ageInTicks);
        this.animate(entity.animationPyrobolt1, PyrolligerAnim.FIREBALL1, ageInTicks);
        this.animate(entity.animationPyrolance, PyrolligerAnim.FIREBALL2, ageInTicks);
        this.animate(entity.animationBurningHexTrap, PyrolligerAnim.SUMMON_HEX, ageInTicks);
        this.animate(entity.animationRangedUlt, PyrolligerAnim.RANGED_ULT, ageInTicks);
        this.animate(entity.animationMeleeUlt, PyrolligerAnim.MELEE_STANCE, ageInTicks);
        this.animate(entity.animationStanceMelee, PyrolligerAnim.MELEE_SWITCH, ageInTicks);
        this.animate(entity.animationStanceRanged, PyrolligerAnim.RANGED_SWITCH, ageInTicks);
        this.animate(entity.animationAtk1, PyrolligerAnim.ATTACK1, ageInTicks);
        this.animate(entity.animationAtk2, PyrolligerAnim.ATTACK2, ageInTicks);
        this.animate(entity.animationAtk3, PyrolligerAnim.ATTACK3, ageInTicks);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
