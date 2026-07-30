package com.kenhorizon.beyondhorizon.server.api.stackable_tags;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StackableTagHandler implements IStackableInstance {
    private final SortedMap<String, StackableTags> stackableTagsList = new TreeMap<>();
    private final Map<String, Tag> nbtMap = new HashMap<>();
    private boolean needsReinstancing = true;

    @Override
    public void tick(LivingEntity entity) {
//        BeyondHorizon.LOGGER.debug("Tick Level at {}", !entity.level().isClientSide() ? "Server" : "Client");
        if (this.needsReinstancing) {
            this.instance(entity);
            this.needsReinstancing = false;
        }
        for (var tags : this.getAllRegistry().values()) {
            tags.tick(entity);
        }
    }

    @Override
    public void instance(LivingEntity entity) {
        BeyondHorizon.LOGGER.debug("Instance Level at {}", !entity.level().isClientSide() ? "Server" : "Client");
        for (var tags : this.getAllRegistryOnEntity(entity)) {
            StackableTags makeInstance = tags.copy();
            this.stackableTagsList.put(makeInstance.getName(), makeInstance);
            if (this.nbtMap.containsKey(tags.getName())) {
                makeInstance.readNbt(this.nbtMap.get(tags.getName()));
            }
        }
    }

    @Override
    public Collection<StackableTags> getStackableTags() {
        return this.stackableTagsList.values();
    }

    @Override
    public Map<String, StackableTags> getAllRegistry() {
        return this.stackableTagsList;
    }

    @Override
    public StackableTags makeInstance(StackableTags instance) {
        return this.stackableTagsList.get(instance.getName());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        for (var tags : this.getAllRegistry().values()) {
            CompoundTag sTagData = tags.writeNbt();
            if (!sTagData.isEmpty()) {
                nbt.put(tags.getName(), sTagData);
            }
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.nbtMap.clear();
        Set<String> keys = nbt.getAllKeys();
        for (String effectName : keys) {
            this.nbtMap.put(effectName, nbt.get(effectName));
        }
        this.needsReinstancing = true;
    }

    @Override
    public StackableTags[] getAllRegistryOnEntity(LivingEntity entity) {
        return StackableTagInstance.TAGS;
    }
}
