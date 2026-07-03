package com.kenhorizon.beyondhorizon.client.model.entity;

import com.kenhorizon.beyondhorizon.client.model.animation.DragonHornetAnim;
import com.kenhorizon.beyondhorizon.server.entity.mobs.DragonHornet;
import com.kenhorizon.libs.client.model.entity.AdvanceEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class DragonHornetModel extends AdvanceEntityModel<DragonHornet> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart wingL;
    private final ModelPart wingR;
    private final ModelPart body;
    private final ModelPart belly;

    public DragonHornetModel(ModelPart root) {
        this.root = root.getChild("root");
        this.head = this.root.getChild("head");
        this.wingL = this.root.getChild("wingL");
        this.wingR = this.root.getChild("wingR");
        this.body = this.root.getChild("body");
        this.belly = this.body.getChild("belly");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(-1.0F, 15.1131F, -0.8438F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 10).addBox(-2.0F, -1.1327F, -5.9239F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -1.6131F, -2.4063F, 0.3927F, 0.0F, 0.0F));

        PartDefinition eye_r1 = head.addOrReplaceChild("eye_r1", CubeListBuilder.create().texOffs(12, 34).mirror().addBox(-0.5F, -1.5F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 34).addBox(-5.5F, -1.5F, -1.5F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 1.6173F, -3.4239F, 0.3491F, 0.0F, 0.0F));

        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(35, 21).addBox(0.0F, -2.0F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.8673F, -6.4239F, 0.6545F, 0.0F, 0.0F));

        PartDefinition wingL = root.addOrReplaceChild("wingL", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, -5.0F, 12.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -2.1131F, 1.8438F));

        PartDefinition wingR = root.addOrReplaceChild("wingR", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, 0.0F, -5.0F, 12.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -2.1131F, 1.8438F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 24).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.8869F, -2.1563F));

        PartDefinition feet_r1 = body.addOrReplaceChild("feet_r1", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, 0.0F, -3.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition feet_r2 = body.addOrReplaceChild("feet_r2", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, 0.0F, -3.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition belly = body.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(0, 10).addBox(-3.0F, -1.5F, 0.0F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(20, 24).addBox(0.0F, 0.5F, 8.0F, 0.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 5.0F, -0.5236F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void animations(DragonHornet entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        this.resetModelDefault();
        this.animate(DragonHornetAnim.GENERAL, ageInTicks, 1.0F);
        this.animate(entity.animationDeath, DragonHornetAnim.DEATH, ageInTicks);
        this.animate(entity.animationAttack, DragonHornetAnim.ATTACK, ageInTicks);
    }
}
