package com.kenhorizon.beyondhorizon.client.model.blockentity;


import com.kenhorizon.beyondhorizon.client.model.animation.GateDoorAnimations;
import com.kenhorizon.beyondhorizon.server.block.entity.GateBlockBlockEntity;
import com.kenhorizon.libs.client.model.entity.AdvanceEntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class GateDoorModel extends AdvanceEntityModel<Entity> {
    private final ModelPart root;
    private final ModelPart parts;

    public GateDoorModel(ModelPart modelPart) {
        this.root = modelPart.getChild("root");
        this.parts = this.root.getChild("parts");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition parts = root.addOrReplaceChild("parts", CubeListBuilder.create().texOffs(0, 32).addBox(-5.0F, -8.0F, -5.5F, 10.0F, 16.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-5.0F, -40.0F, -5.5F, 10.0F, 16.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-5.0F, -40.0F, -5.5F, 10.0F, 16.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-5.0F, -24.0F, -5.5F, 10.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 0.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart head() {
        return null;
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }

    @Override
    public void animations(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {

    }

    public void modelAnimations(GateBlockBlockEntity entity, float partialTick) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        float ageInTicks = entity.tickCount + partialTick;
        this.animate(entity.getAnimationState("opening"), GateDoorAnimations.OPENING, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("closing"), GateDoorAnimations.CLOSING, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("open"), GateDoorAnimations.OPEN, ageInTicks, 1.0F);
        this.animate(entity.getAnimationState("close"), GateDoorAnimations.CLOSE, ageInTicks, 1.0F);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
    }
}
