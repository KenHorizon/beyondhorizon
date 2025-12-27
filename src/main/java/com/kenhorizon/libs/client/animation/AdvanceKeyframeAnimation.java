package com.kenhorizon.libs.client.animation;

import com.kenhorizon.libs.client.model.entity.AdvanceEntityModel;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class AdvanceKeyframeAnimation {
    public static void animate(AdvanceEntityModel<?> model, AnimationDefinition definition, long accumulatedTime, float scale, Vector3f animationVecCache) {
        float elapsedSeconds = getElapsedSeconds(definition, accumulatedTime);

        for(Map.Entry<String, List<AnimationChannel>> entry : definition.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = model.getAnyDescendantWithName(entry.getKey());
            List<AnimationChannel> list = entry.getValue();
            optional.ifPresent((modelPart) -> {
                list.forEach((channel) -> {
                    Keyframe[] listedKeyFrames = channel.keyframes();
                    int timeStamp = Math.max(0, Mth.binarySearch(0, listedKeyFrames.length, (timestamp) -> {
                        return elapsedSeconds <= listedKeyFrames[timestamp].timestamp();
                    }) - 1);
                    int minTimeStamp = Math.min(listedKeyFrames.length - 1, timeStamp + 1);
                    Keyframe getTimeStamp = listedKeyFrames[timeStamp];
                    Keyframe getMinTimeStamp = listedKeyFrames[minTimeStamp];
                    float second = elapsedSeconds - getTimeStamp.timestamp();
                    float duration;
                    if (minTimeStamp != timeStamp) {
                        duration = Mth.clamp(second / (getMinTimeStamp.timestamp() - getTimeStamp.timestamp()), 0.0F, 1.0F);
                    } else {
                        duration = 0.0F;
                    }

                    getMinTimeStamp.interpolation().apply(animationVecCache, duration, listedKeyFrames, timeStamp, minTimeStamp, scale);
                    channel.target().apply(modelPart, animationVecCache);
                });
            });
        }

    }

    private static float getElapsedSeconds(AnimationDefinition definition, long accumulatedTime) {
        float f = (float)accumulatedTime / 1000.0F;
        return definition.looping() ? f % definition.lengthInSeconds() : f;
    }

    public static Vector3f posVec(float x, float y, float z) {
        return new Vector3f(x, -y, z);
    }

    public static Vector3f degreeVec(float xDegrees, float yDegrees, float zDegrees) {
        return new Vector3f(xDegrees * ((float)Math.PI / 180F), yDegrees * ((float)Math.PI / 180F), zDegrees * ((float)Math.PI / 180F));
    }

    public static Vector3f scaleVec(double xScale, double yScale, double zScale) {
        return new Vector3f((float)(xScale - 1.0D), (float)(yScale - 1.0D), (float)(zScale - 1.0D));
    }
}
