package com.kenhorizon.beyondhorizon.server.api.stackable_tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundStackableTagsPacket;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class StackableTagInstance {

    public static final StackableTags ENERGIZE = StackableTags.build("energize", 100);
    public static final StackableTags SAINT_DEMON_CROWN_STACKS = StackableTags.build("saint_demon_crown_stacks");
    public static final StackableTags GLUTTONY = StackableTags.build("gluttony");
    public static final StackableTags THOUSAND_CUT = StackableTags.build("thousand_cut", Maths.sec(6));
    public static final StackableTags CARVE = StackableTags.build("carve", 5, Maths.sec(6))
            .resetOnExpired()
            .addModifiers(Attributes.ARMOR, Constant.CARVE_ARMOR_REMOVE, AttributeModifier.Operation.MULTIPLY_TOTAL);

    public static final StackableTags SEETHING_STRIKE = StackableTags.build("seething_strike", 6, Maths.sec(6))
            .addModifiers(Attributes.ATTACK_SPEED, Constant.SEETHING_STRIKE_ATK_SPD, AttributeModifier.Operation.MULTIPLY_TOTAL);

    public static final StackableTags PHANTOM = StackableTags.build("phantom", 3);
    public static final StackableTags BRING_IT_DOWN = StackableTags.build("bring_it_down", 3, Maths.sec(6));

    public static void sendPacket(LivingEntity entity) {
        if (entity.level().isClientSide()) return;
        IStackableInstance stackable = Capabilities.stackable(entity);
        if (stackable != null) {
            NetworkHandler.sendAll(new ClientboundStackableTagsPacket(entity.getId(), stackable.getAllRegistry()), entity);
        }
    }

    public static StackableTags[] TAGS = new StackableTags[] {
            ENERGIZE,
            SAINT_DEMON_CROWN_STACKS,
            CARVE,
            PHANTOM,
            SEETHING_STRIKE,
            BRING_IT_DOWN
    };

    static List<String> RENDER_WHEN_EQUIPPED = new ArrayList<>();
    static List<String> RENDER_ALWAYS = new ArrayList<>();

    public static List<String> getRenderAlways() {
        return RENDER_ALWAYS;
    }

    public static List<String> getRenderWhenEquipped() {
        return RENDER_WHEN_EQUIPPED;
    }

    public static void registerRenderAlways(StackableTags tags) {
        RENDER_ALWAYS.add(tags.getName());
    }

    public static void registerRenderWhenEquipped(StackableTags tags) {
        RENDER_WHEN_EQUIPPED.add(tags.getName());
    }

    public static void renderWhenEquipped() {
        registerRenderWhenEquipped(ENERGIZE);
        registerRenderWhenEquipped(SAINT_DEMON_CROWN_STACKS);
        registerRenderWhenEquipped(SEETHING_STRIKE);
        registerRenderWhenEquipped(BRING_IT_DOWN);
    }

    public static void renderAlways() {
        registerRenderAlways(PHANTOM);
        registerRenderAlways(CARVE);
    }
}
