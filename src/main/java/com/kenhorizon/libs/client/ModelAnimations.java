package com.kenhorizon.libs.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.isEntityUpsideDown;

public class ModelAnimations {
    public static void holding(ModelPart rightArm, ModelPart leftArm, ModelPart head, boolean rightHanded) {
        ModelPart rightHand = rightHanded ? rightArm : leftArm;
        ModelPart leftHand = rightHanded ? leftArm : rightArm;
        rightHand.yRot = (rightHanded ? -0.3F : 0.3F) + head.yRot;
        leftHand.yRot = (rightHanded ? 0.6F : -0.6F) + head.yRot;
        rightHand.xRot = (-(float) Math.PI / 2F) + head.xRot + 0.1F;
        leftHand.xRot = -1.5F + head.xRot;
    }

    public static void darkingSlash(ModelPart rightArm, ModelPart leftArm, boolean rightHanded) {
        ModelPart rightHand = rightHanded ? rightArm : leftArm;
        ModelPart leftHand = rightHanded ? leftArm : rightArm;
        rightHand.xRot = rightHand.xRot * 0.5F - (float) Math.PI;
        rightHand.yRot = 0.0F;
    }

    public static void flyingAnim(AbstractClientPlayer entity, float flightTick, float yaw, float pitch, PlayerModel<?> model) {
        model.leftArm.xRot = 0.0F;
        model.rightArm.xRot = 0.0F;
        model.leftLeg.xRot = 0.0F;
        model.rightLeg.xRot = 0.0F;
    }
}
