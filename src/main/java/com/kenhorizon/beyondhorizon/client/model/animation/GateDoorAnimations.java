package com.kenhorizon.beyondhorizon.client.model.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GateDoorAnimations {
        public static final AnimationDefinition OPEN = AnimationDefinition.Builder.withLength(0.0F)
                .addAnimation("parts", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();

        public static final AnimationDefinition CLOSE = AnimationDefinition.Builder.withLength(0.0F)

                .build();

        public static final AnimationDefinition OPENING = AnimationDefinition.Builder.withLength(1.0F)
                .addAnimation("parts", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -46.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();

        public static final AnimationDefinition CLOSING = AnimationDefinition.Builder.withLength(1.0F)
                .addAnimation("parts", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -46.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
}
