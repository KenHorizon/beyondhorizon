package com.kenhorizon.beyondhorizon.server.classes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.client.ClientboundRoleClassSyncPacket;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class RoleClass implements IAttack {

    public enum AttributePoints implements StringRepresentable {
        STRENGHT,
        VITALITY,
        CONSTITUION,
        DEXERITY,
        AGILITY,
        INTELLIGENGE;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public String getName() {
            return Utils.capitalize(this.name().toLowerCase(Locale.ROOT));
        }
    }
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
    public static final String CLASS_TAGS = "class";
    public static final String CLASS_TRAITS_LEVEL_TAGS = "class_and_traits_unlocked";
    public static final String REQUIRED_LEVEL_TAGS = "required_level";
    public static final String DEX_TAGS = "dex";
    public static final String INT_TAGS = "int";
    public static final String AGI_TAGS = "agi";
    public static final String CONS_TAGS = "cons";
    public static final String VIT_TAGS = "vit";
    public static final String STR_TAGS = "str";
    public static final String EXP_TAGS = "exp";
    public static final String ATTRIBUTE_TAGS = "attributes";
    public static final String EXP_REQUIRED_TAGS = "exp_required";
    public static final String POINTS_TAGS = "points";
    public static final String LEVELS_TAGS = "level";
    public static final String CLASSES_TAGS = "role_class";
    public static final String ATTRIBUTES_TAGS = "attribute_modifiers";
    private static final UUID STRENGHT_ID = UUID.fromString("42198390-9873-422c-8a9d-18f67ba79fda");
    private static final UUID VITALITY_ID = UUID.fromString("038e19fa-cb25-49d1-a207-c74410df4e99");
    private static final UUID CONSTITUTION_ID = UUID.fromString("d8b02151-cf3c-4575-8bd7-d142f4ffb6b8");
    private static final UUID AGILITY_ID = UUID.fromString("83f8cf72-6425-4217-8b89-6512a2ecf4c3");
    private static final UUID DEXERITY_ID = UUID.fromString("af62fce2-34f3-42e6-9202-bd1a35770cbc");
    private static final UUID INTELLIGENCE_ID = UUID.fromString("f05bc533-96c0-4e8f-91b2-ce899d446d88");
    public RoleClassTypes roleClassTypes;
    public Player player;
    private int dex;
    private int inte;
    private int agi;
    private int cons;
    private int vit;
    private int str;
    private int levels;
    private int points;
    public float expPoints = 0;
    public float expProgress = 0;
    public float expRequired = 0;
    public final int maxRequiredXp = 280;
    public final int maxLevel = 100;
    public static final int REQUIRED_LEVEL_ATTRIBUTES = Constant.LEVEL_SYSTEM_UNLOCKED;
    public static final int REQUIRED_LEVEL_CLASS_TRAITS = Constant.CLASS_SYSTEM_UNLOCKED;
    private boolean alreadyReachedRequiredLevel = false;
    private boolean unlockedClassAndTraits = false;
    public RoleClass(Player player) {
        this.roleClassTypes = RoleClassTypes.NONE;
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

    public void addExpPoints() {
        this.expPoints += 30;
        this.expProgress += this.expPoints / this.expRequired;
        while (this.expProgress < 0.0F) {
            float f = this.expProgress * (float) this.getXpNeededForNextLevel();
            if (this.levels > 0) {
                this.setLevel(this.getLevel() - 1);
                this.expProgress = (int) (1.0F + f / this.getXpNeededForNextLevel());
            } else {
                this.setLevel(this.getLevel() - 1);
                this.expProgress = 0.0F;
            }
        }
        while (this.expProgress > 1.0F) {
            this.setLevel(Math.min(this.maxLevel, this.getLevel() + 1));
            this.setPoints(this.getPoints() + 1);
            this.expProgress /= this.getXpNeededForNextLevel();
        }

    }

    public void setExpPoints(float expPoints) {
        this.expPoints = expPoints;
    }

    public boolean isUnlockedClassAndTraits() {
        return unlockedClassAndTraits;
    }

    public void setUnlockedClassAndTraits(boolean unlockedClassAndTraits) {
        this.unlockedClassAndTraits = unlockedClassAndTraits;
    }

    public float getExpPoints() {
        return expPoints;
    }

    public void addPointOfAttributes(AttributePoints attributePoints, int amount) {
        switch (attributePoints) {
            case AGILITY -> {
                this.addAgi(amount);
            }
            case DEXERITY -> {
                this.addDex(amount);
            }
            case STRENGHT -> {
                this.addStr(amount);
            }
            case VITALITY -> {
                this.addVit(amount);
            }
            case CONSTITUION -> {
                this.addCons(amount);
            }
            case INTELLIGENGE -> {
                this.addInte(amount);
            }
        }
    }
    public void removePointOfAttributes(AttributePoints attributePoints, int amount) {
        switch (attributePoints) {
            case AGILITY -> {
                this.removeAgi(amount);
            }
            case DEXERITY -> {
                this.removeDex(amount);
            }
            case STRENGHT -> {
                this.removeStr(amount);
            }
            case VITALITY -> {
                this.removeVit(amount);
            }
            case CONSTITUION -> {
                this.removeCons(amount);
            }
            case INTELLIGENGE -> {
                this.removeInte(amount);
            }
        }
    }
    public int getPointOfSkills(AttributePoints attributePoints) {
        switch (attributePoints) {
            case AGILITY -> {
                return this.getAgi();
            }
            case DEXERITY -> {
                return this.getDex();
            }
            case STRENGHT -> {
                return this.getStr();
            }
            case VITALITY -> {
                return this.getVit();
            }
            case CONSTITUION -> {
                return this.getCons();
            }
            case INTELLIGENGE -> {
                return this.getInte();
            }
            default -> {
                return 0;
            }
        }
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


    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public float getXpNeededForNextLevel() {
        return this.expRequired = this.maxRequiredXp + (100 * this.levels);
    }

    public void resetEverything() {
        this.setRoles(RoleClassTypes.NONE);
        this.setPoints(0);
        this.setLevel(0);
        this.setExpPoints(0);
        this.expProgress = 0.0F;
        this.str = 0;
        this.vit = 0;
        this.cons = 0;
        this.agi = 0;
        this.dex = 0;
        this.inte = 0;
    }

    public void tick() {
        if (this.player.experienceLevel > REQUIRED_LEVEL_ATTRIBUTES && !this.alreadyReachedRequiredLevel) {
            this.alreadyReachedRequiredLevel = true;
        }
        if (this.player.experienceLevel > REQUIRED_LEVEL_CLASS_TRAITS && !this.unlockedClassAndTraits) {
            this.unlockedClassAndTraits = true;
        }
        AttributeInstance maxHealth = this.player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH);
        AttributeInstance attackDamage = this.player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
        AttributeInstance attackSpeed = this.player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED);
        AttributeInstance movement = this.player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
        AttributeInstance falldamage = this.player.getAttribute(BHAttributes.FALLDAMAGE_MULTIPLIER.get());
        AttributeInstance maxMana = this.player.getAttribute(BHAttributes.MAX_MANA.get());
        AttributeInstance healthRegen = this.player.getAttribute(BHAttributes.HEALTH_REGENERATION.get());
        AttributeInstance manaRegen = this.player.getAttribute(BHAttributes.MANA_REGENERATION.get());
        AttributeInstance abilityPower = this.player.getAttribute(BHAttributes.ABILITY_POWER.get());
        this.addModifiers(2, this.getPointOfSkills(AttributePoints.VITALITY), VITALITY_ID, maxHealth, AttributeModifier.Operation.ADDITION);
        this.addModifiers(1, this.getPointOfSkills(AttributePoints.STRENGHT), STRENGHT_ID, attackDamage, AttributeModifier.Operation.ADDITION);
        this.addModifiers(0.01F, this.getPointOfSkills(AttributePoints.AGILITY), AGILITY_ID, attackSpeed, AttributeModifier.Operation.ADDITION);
        this.addModifiers(0.001F, this.getPointOfSkills(AttributePoints.DEXERITY), DEXERITY_ID, movement, AttributeModifier.Operation.ADDITION);
        this.addModifiers(0.01F, this.getPointOfSkills(AttributePoints.DEXERITY), DEXERITY_ID, falldamage, AttributeModifier.Operation.ADDITION);
        this.addModifiers(2, this.getPointOfSkills(AttributePoints.INTELLIGENGE), INTELLIGENCE_ID, maxMana, AttributeModifier.Operation.ADDITION);
        this.addModifiers(2, this.getPointOfSkills(AttributePoints.INTELLIGENGE), INTELLIGENCE_ID, abilityPower, AttributeModifier.Operation.ADDITION);
        this.addModifiers(0.10F, this.getPointOfSkills(AttributePoints.CONSTITUION), CONSTITUTION_ID, healthRegen, AttributeModifier.Operation.MULTIPLY_BASE);
        this.addModifiers(0.10F, this.getPointOfSkills(AttributePoints.CONSTITUION), CONSTITUTION_ID, manaRegen, AttributeModifier.Operation.MULTIPLY_BASE);
        if (this.player instanceof ServerPlayer) {
            NetworkHandler.sendToPlayer(new ClientboundRoleClassSyncPacket(this.saveNbt()), (ServerPlayer) player);
        }
    }

    private void addModifiers(float stats, int pts, UUID uuid, AttributeInstance instance, AttributeModifier.Operation operation) {
        if (instance == null) return;
        float amount = stats * pts;
        if (amount == 0) {
            instance.removeModifier(uuid);
        } else {
            AttributeModifier modifier = new AttributeModifier(uuid, "Bonus Stats", amount, operation);
            AttributeModifier prevModifier = instance.getModifier(uuid);
            if (prevModifier == null) {
                instance.addTransientModifier(modifier);
            } else if (prevModifier.getAmount() != amount) {
                instance.removeModifier(uuid);
                instance.addTransientModifier(modifier);
            }
        }
    }

    public CompoundTag saveNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString(CLASSES_TAGS, this.roleClassTypes.getName());
        nbt.putInt(LEVELS_TAGS, this.getLevel());
        nbt.putInt(POINTS_TAGS, this.getPoints());
        nbt.putBoolean(REQUIRED_LEVEL_TAGS, this.alreadyReachedRequiredLevel);
        nbt.putBoolean(CLASS_TRAITS_LEVEL_TAGS, this.unlockedClassAndTraits);
        nbt.put(ATTRIBUTE_TAGS, this.createListSkills());
        nbt.put(CLASS_TAGS, this.createListClass());
        nbt.putFloat(EXP_TAGS, this.expProgress);
        nbt.putFloat(EXP_REQUIRED_TAGS, this.expRequired);
        return nbt;
    }
    private ListTag createListClass() {
        ListTag list = new ListTag();
        CompoundTag nbt = new CompoundTag();
        list.add(nbt);
        return list;
    }
    private ListTag createListSkills() {
        ListTag list = new ListTag();
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(STR_TAGS, this.str);
        nbt.putInt(AGI_TAGS, this.agi);
        nbt.putInt(VIT_TAGS, this.vit);
        nbt.putInt(CONS_TAGS, this.cons);
        nbt.putInt(DEX_TAGS, this.dex);
        nbt.putInt(INT_TAGS, this.inte);
        list.add(nbt);
        return list;
    }

    public void loadNbt(CompoundTag nbt) {
        this.roleClassTypes = RoleClassTypes.valueOf(nbt.getString(CLASSES_TAGS).toUpperCase(Locale.ROOT));
        this.levels = nbt.getInt(LEVELS_TAGS);
        this.points = nbt.getInt(POINTS_TAGS);
        ListTag attributeTagList = nbt.getList(ATTRIBUTE_TAGS, Tag.TAG_COMPOUND);
        for (int i = 0; i < attributeTagList.size(); i++) {
            CompoundTag attributeTags = attributeTagList.getCompound(i);
            this.str = attributeTags.getInt(STR_TAGS);
            this.agi = attributeTags.getInt(AGI_TAGS);
            this.vit = attributeTags.getInt(VIT_TAGS);
            this.cons = attributeTags.getInt(CONS_TAGS);
            this.dex = attributeTags.getInt(DEX_TAGS);
            this.inte = attributeTags.getInt(INT_TAGS);
        }
        this.alreadyReachedRequiredLevel = nbt.getBoolean(REQUIRED_LEVEL_TAGS);
        this.unlockedClassAndTraits = nbt.getBoolean(CLASS_TRAITS_LEVEL_TAGS);
        this.expProgress = nbt.getFloat(EXP_TAGS);
        this.expRequired = nbt.getFloat(EXP_REQUIRED_TAGS);
    }
}
