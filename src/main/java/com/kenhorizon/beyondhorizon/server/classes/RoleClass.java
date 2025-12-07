package com.kenhorizon.beyondhorizon.server.classes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.accessory.Accessories;
import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.Option;
import java.util.Locale;
import java.util.Optional;

public class RoleClass implements IAttack, IRoleClass {
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
    public static final String REQUIRED_LEVEL_TAGS = "required_level";
    public static final String DEX_TAGS = "dex";
    public static final String INT_TAGS = "int";
    public static final String AGI_TAGS = "agi";
    public static final String CONS_TAGS = "cons";
    public static final String VIT_TAGS = "vit";
    public static final String STR_TAGS = "str";
    public static final String POINTS_TAGS = "points";
    public static final String LEVELS_TAGS = "level";
    public static final String CLASSES_TAGS = "role_class";
    public static final String ATTRIBUTES_TAGS = "attribute_modifiers";
    public RoleClassTypes roleClassTypes;
    private int dex;
    private int inte;
    private int agi;
    private int cons;
    private int vit;
    private int str;
    private int levels;
    public Player player;
    public int expRequired = 30;
    public final int maxLevel = 30;
    public final int REQUIRED_LEVEL = Constant.LEVEL_SYSTEM_UNLOCKED;
    private boolean alreadyReachedRequiredLevel = false;
    public RoleClass(RoleClassTypes types, Player player) {
        this.roleClassTypes = types;
        this.player = player;
    }

    public String getName() {
        return this.roleClassTypes.getName();
    }

    public Optional<IAttack> IAttack() {
        return Optional.of(this);
    }

    public void addLevel(int level) {
        this.levels += level;
    }

    public void setLevel(int level) {
        this.levels = level;
    }

    public int getLevel() {
        return this.levels;
    }

    public boolean isAlreadyReachedRequiredLevel() {
        return alreadyReachedRequiredLevel;
    }

    public void consumeExp(int amount) {
        this.player.giveExperiencePoints(-amount);
    }

    public Player getPlayer() {
        return player;
    }

    public void setRoles(RoleClassTypes roles) {
        this.roleClassTypes = roles;
    }

    public RoleClassTypes getRoles() {
        return this.roleClassTypes;
    }

    public void addStr(int amount) {
        this.str += amount;
    }

    public void removeStr(int amount) {
        this.str = Math.max(0, amount);
    }

    public void addVit(int amount) {
        this.vit += amount;
    }

    public void removeVit(int amount) {
        this.vit = Math.max(0, amount);
    }

    public void addAgi(int amount) {
        this.agi += amount;
    }

    public void removeAgi(int amount) {
        this.agi = Math.max(0, amount);
    }

    public void addCons(int amount) {
        this.cons += amount;
    }

    public void removeCons(int amount) {
        this.cons = Math.max(0, amount);
    }

    public void addInte(int amount) {
        this.inte += amount;
    }

    public void removeInte(int amount) {
        this.inte = Math.max(0, amount);
    }

    public void addDex(int amount) {
        this.dex += amount;
    }

    public void removeDex(int amount) {
        this.dex = Math.max(0, amount);
    }

    public int getStr() {
        return str;
    }

    public int getVit() {
        return vit;
    }

    public int getCons() {
        return cons;
    }

    public int getAgi() {
        return agi;
    }

    public int getDex() {
        return dex;
    }

    public int getInte() {
        return inte;
    }


    @Override
    public RoleClass getInstance() {
        return this;
    }

    public void tick() {
        if (this.player.experienceLevel > this.REQUIRED_LEVEL && !this.alreadyReachedRequiredLevel) {
            this.alreadyReachedRequiredLevel = true;
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString(CLASSES_TAGS, this.roleClassTypes.getName());
        nbt.putInt(LEVELS_TAGS, this.levels);
        nbt.putInt(STR_TAGS, this.str);
        nbt.putInt(AGI_TAGS, this.agi);
        nbt.putInt(VIT_TAGS, this.vit);
        nbt.putInt(CONS_TAGS, this.cons);
        nbt.putInt(DEX_TAGS, this.dex);
        nbt.putInt(INT_TAGS, this.inte);
        nbt.putBoolean(REQUIRED_LEVEL_TAGS, this.alreadyReachedRequiredLevel);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.levels = nbt.getInt(LEVELS_TAGS);
        this.str = nbt.getInt(STR_TAGS);
        this.agi = nbt.getInt(AGI_TAGS);
        this.vit = nbt.getInt(VIT_TAGS);
        this.cons = nbt.getInt(CONS_TAGS);
        this.dex = nbt.getInt(DEX_TAGS);
        this.inte = nbt.getInt(INT_TAGS);
        this.alreadyReachedRequiredLevel = nbt.getBoolean(REQUIRED_LEVEL_TAGS);
        this.roleClassTypes = RoleClassTypes.valueOf(nbt.getString(CLASSES_TAGS).toUpperCase(Locale.ROOT));
    }
}
