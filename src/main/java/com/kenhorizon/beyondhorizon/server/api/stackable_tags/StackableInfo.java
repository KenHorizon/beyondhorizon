package com.kenhorizon.beyondhorizon.server.api.stackable_tags;

import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.init.BHChatformatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class StackableInfo {
    private final StackableTags stackableTags;
    protected StackableInfo(StackableTags stackableTags) {
        this.stackableTags = stackableTags;
    }

    public static StackableInfo get(StackableTags stackableTags) {
        return new StackableInfo(stackableTags);
    }

    public String getName() {
        return this.stackableTags.getName();
    }

    public MutableComponent getDisplayName() {
        return Component.literal(Utils.builderName(this.stackableTags.getName())).withStyle(BHChatformatting.EFFECTS);
    }

    public int getStacks() {
        return this.stackableTags.getStack();
    }

    public int getMaxStacks() {
        return this.stackableTags.getMaxStack();
    }

    public int getDuration() {
        return this.stackableTags.getDuration();
    }

    public int getMaxDuration() {
        return this.stackableTags.getMaxDuration();
    }
}
