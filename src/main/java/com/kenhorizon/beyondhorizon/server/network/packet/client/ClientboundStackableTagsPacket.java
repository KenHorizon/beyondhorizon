package com.kenhorizon.beyondhorizon.server.network.packet.client;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.IStackableInstance;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTags;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.function.Supplier;

public class ClientboundStackableTagsPacket {
    private final int entityId;
    private final Map<String, StackableTags> index;
    public ClientboundStackableTagsPacket(int entityId, Map<String, StackableTags> index) {
        this.entityId = entityId;
        this.index = index;
    }

    public ClientboundStackableTagsPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.index = buf.readMap(ClientboundStackableTagsPacket::readID, ClientboundStackableTagsPacket::readStackableTags);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeMap(this.index, ClientboundStackableTagsPacket::writeId, ClientboundStackableTagsPacket::writeStackableTags);
    }

    private static String readID(FriendlyByteBuf buffer) {
        return buffer.readUtf();
    }

    private static StackableTags readStackableTags(FriendlyByteBuf buf) {
        String name = buf.readUtf();
        int stacks = buf.readInt();
        int maxStack = buf.readInt();
        int duration = buf.readInt();
        int maxDuration = buf.readInt();
        boolean resetOnExpired = buf.readBoolean();
        StackableTags tags = new StackableTags(name, maxStack, stacks, duration, maxDuration, resetOnExpired);
        int modifierCount = buf.readVarInt();
        for (int i = 0; i < modifierCount; i++) {
            Attribute attribute = readAttribute(buf);
            AttributeModifier modifier = readModifiers(buf);
            tags.getAttributeModifiers().put(attribute, modifier);
        }
        return tags;
    }

    private static void writeId(FriendlyByteBuf buf, String id) {
        buf.writeUtf(id);
    }

    private static void writeStackableTags(FriendlyByteBuf buf, StackableTags stackableTags) {
        buf.writeUtf(stackableTags.getName());
        buf.writeInt(stackableTags.getStack());
        buf.writeInt(stackableTags.getMaxStack());
        buf.writeInt(stackableTags.getDuration());
        buf.writeInt(stackableTags.getMaxDuration());
        buf.writeBoolean(stackableTags.isResetOnExpired());
        var modifiers = stackableTags.getAttributeModifiers();
        buf.writeVarInt(modifiers.size());
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
            writeAttributes(buf, entry.getKey());
            writeModifiers(buf, entry.getValue());
        }
    }

    private static void writeAttributes(FriendlyByteBuf buf, Attribute attribute) {
        buf.writeRegistryId(ForgeRegistries.ATTRIBUTES, attribute);
    }

    private static void writeModifiers(FriendlyByteBuf buf, AttributeModifier modifier) {
        buf.writeUUID(modifier.getId());
        buf.writeUtf(modifier.getName());
        buf.writeDouble(modifier.getAmount());
        buf.writeEnum(modifier.getOperation());
    }

    private static Attribute readAttribute(FriendlyByteBuf buf) {
        return buf.readRegistryId();
    }

    private static AttributeModifier readModifiers(FriendlyByteBuf buf) {
        return new AttributeModifier(buf.readUUID(), buf.readUtf(), buf.readDouble(), buf.readEnum(AttributeModifier.Operation.class));
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Entity levelEntity = mc.level.getEntity(this.entityId);
            if (levelEntity instanceof LivingEntity entity) {
                IStackableInstance stackable = Capabilities.stackable(entity);
                stackable.getAllRegistry().forEach((name, tags) -> {
                    StackableTags getTags = this.index.get(name);
                    if (getTags != null) {
                        tags.readNbt(getTags.writeNbt());
                    }
                });
            }
        });
        context.setPacketHandled(true);
    }
}
