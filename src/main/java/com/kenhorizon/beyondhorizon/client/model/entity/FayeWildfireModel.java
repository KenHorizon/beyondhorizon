package com.kenhorizon.beyondhorizon.client.model.entity;

import com.kenhorizon.beyondhorizon.client.model.animation.BlazingInfernoAnim;
import com.kenhorizon.beyondhorizon.client.model.animation.FayeWildfireAnim;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.BlazingInferno;
import com.kenhorizon.beyondhorizon.server.entity.mobs.FayeWildfire;
import com.kenhorizon.libs.client.model.entity.AdvanceEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FayeWildfireModel extends AdvanceEntityModel<FayeWildfire> {
    private final ModelPart root;
    private final ModelPart rod0;
    private final ModelPart rod1;
    private final ModelPart rod2;
    private final ModelPart core;
    private final ModelPart head;
    private final ModelPart jaw;

    public FayeWildfireModel(ModelPart root) {
        this.root = root.getChild("root");
        this.rod0 = this.root.getChild("rod0");
        this.rod1 = this.root.getChild("rod1");
        this.rod2 = this.root.getChild("rod2");
        this.core = this.root.getChild("core");
        this.head = this.root.getChild("head");
        this.jaw = this.head.getChild("jaw");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition rod0 = root.addOrReplaceChild("rod0", CubeListBuilder.create().texOffs(32, 0).addBox(-5.75F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-0.75F, -4.0F, -7.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-1.75F, -4.0F, 5.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(4.25F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.25F, -10.0F, 0.0F));

        PartDefinition rod1 = root.addOrReplaceChild("rod1", CubeListBuilder.create().texOffs(32, 0).addBox(-10.5F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-0.5F, -4.0F, -12.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-1.5F, -4.0F, 10.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(8.5F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -25.0F, 0.0F));

        PartDefinition rod2 = root.addOrReplaceChild("rod2", CubeListBuilder.create().texOffs(32, 0).addBox(-6.75F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-0.75F, -4.0F, -8.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-1.75F, -4.0F, 6.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(5.25F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.25F, -28.0F, 0.0F));

        PartDefinition core = root.addOrReplaceChild("core", CubeListBuilder.create().texOffs(16, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 24).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -21.0F, 0.0F));

        PartDefinition coreRings_r1 = core.addOrReplaceChild("coreRings_r1", CubeListBuilder.create().texOffs(16, 24).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 26).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -30.0F, -1.0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 5.25F));

        PartDefinition headwear_r1 = jaw.addOrReplaceChild("headwear_r1", CubeListBuilder.create().texOffs(32, 10).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -4.0F, -5.0F, 0.1745F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(FayeWildfire entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        this.resetModelDefault();
        this.headLook(this.head, yaw, pitch);
        if (entity.walkAnimation.isMoving()) {
            this.applyStatic(FayeWildfireAnim.WALKING);
        }
        this.animateIdle(FayeWildfireAnim.GENERAL, ageInTicks, 0.75F);
        this.animate(entity.animationBlazingRod, FayeWildfireAnim.SHOOT, ageInTicks);
        this.animate(entity.animationPrepDeathRay, FayeWildfireAnim.PREPARE_DEATH_RAY, ageInTicks);
        this.animate(entity.animationDeathRay, FayeWildfireAnim.DEATH_RAY, ageInTicks);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
