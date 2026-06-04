package com.kenhorizon.beyondhorizon.server.api.stackable_tags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StackableTagHandler implements IStackableInstance {
    Map<String, Tag> nbtMap = new HashMap<>();

    @Override
    public void tick(LivingEntity entity) {
        for (var tags : this.getAllTags()) {
            tags.tick(entity);
        }
    }

    @Override
    public void instance(LivingEntity entity) {
        for (var tags : this.getAllTags()) {
            if (nbtMap.containsKey(tags.getName())) tags.readNbt(nbtMap.get(tags.getName()));
        }
    }

    @Override
    public List<StackableTags> getInstance() {
        return getAllTags();
    }

    @Override
    public StackableTags getInstance(StackableTags instance) {
        for (var tags : this.getAllTags()) {
            if (tags == instance) {
                return tags;
            }
        }
        return null;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        for (var tags : this.getAllTags()) {
            CompoundTag sTagData = tags.writeNbt();
            if (!sTagData.isEmpty()) {
                nbt.put(tags.getName(), sTagData);
            }
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        Set<String> keys = nbt.getAllKeys();
        for (String effectName : keys) {
            nbtMap.put(effectName, nbt.get(effectName));
        }
    }

    public List<StackableTags> getAllTags() {
        return StackableTagInstance.getTags();
    }

}
