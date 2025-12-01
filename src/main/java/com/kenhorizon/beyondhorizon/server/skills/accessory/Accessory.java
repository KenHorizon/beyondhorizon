package com.kenhorizon.beyondhorizon.server.skills.accessory;

import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IItemGeneric;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.skills.Skill;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.*;
import java.util.function.Supplier;

public class Accessory extends Skill implements IAttack, IItemGeneric {
    private float magnitude = 0.0F;
    private int level = 1;
    private List<Accessory> accessoryList;

    public Accessory(float magnitude, int level) {
        this.magnitude = magnitude;
        this.level = level;
    }
    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getLevel() {
        return level;
    }
    public float getMagnitude() {
        return magnitude;
    }

    @Override
    public Optional<IAttack> IAttackCallback() {
        return Optional.of(this);
    }

    @Override
    public Optional<IItemGeneric> IItemGeneric() {
        return Optional.of(this);
    }

    public boolean hasAccessory(Supplier<? extends  Accessory> accessory) {
        return this == accessory.get();
    }

    public boolean hasAccessory(TagKey<Accessory> tagKey) {
        ForgeRegistry<Accessory> registry = RegistryManager.ACTIVE.getRegistry(Accessories.ACCESSORY_ITEMS.getRegistryName());
        ITagManager<Accessory> tagManager = registry.tags();
        ITag<Accessory> tag = tagManager.getTag(tagKey);
        this.accessoryList = tag.stream().toList();
        for (Accessory accessory : tag.stream().toList()) {
            return this == accessory;
        }
        return false;
    }

    public ListTag getAccessoryTags(ItemStack itemStack) {
        CompoundTag nbt = itemStack.getTag();
        return nbt != null ? nbt.getList("Accessories", 10) : new ListTag();
    }

    public boolean haveAccessoryTags(CompoundTag nbt) {
        if (nbt != null && nbt.contains("Accessories", 9)) {
            return !nbt.getList("Accessories", 10).isEmpty();
        } else {
            return false;
        }
    }
    protected List<Attribute> randomAttributes() {
        List<Attribute> attributes = new ArrayList<>(new HashSet<>());
        attributes.add(Attributes.ATTACK_DAMAGE);
        attributes.add(Attributes.ATTACK_SPEED);
        attributes.add(Attributes.ATTACK_KNOCKBACK);
        attributes.add(Attributes.ARMOR);
        attributes.add(Attributes.MOVEMENT_SPEED);
        attributes.add(Attributes.MAX_HEALTH);
        attributes.add(BHAttributes.ABILITY_POWER.get());
        attributes.add(BHAttributes.CRITICAL_STRIKE.get());
        attributes.add(BHAttributes.CRITICAL_DAMAGE.get());
        attributes.add(BHAttributes.DAMAGE_DEALT.get());
        attributes.add(BHAttributes.DAMAGE_TAKEN.get());
        attributes.add(BHAttributes.MANA_REGENERATION.get());
        return attributes;
    }
}
