package com.kenhorizon.beyondhorizon.server.api.classes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public class MasterySkill implements IAttack, IEntityProperties {
    public enum Slot implements StringRepresentable {
        SKILL,
        ONE(3),
        TWO(3),
        THREE(3);

        private int max;
        Slot(int max) {
            this.max = max;
        }
        Slot() {
            this(1);
        }

        public int getMax() {
            return max;
        }

        @Override
        public String getSerializedName() {
            return this.name();
        }

        public String getName() {
            return this.name().toUpperCase(Locale.ROOT);
        }
    }
    public static final String ID = "id";
    public static final String SLOT_TAG = "slot";
    public static final String POINTS_TAG = "points";
    public static final String MAX_POINTS_TAG = "max_points";
    public static final String ATTRIBUTES_TAGS = "attribute_modifiers";
    protected final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
    @Nullable
    protected String descriptionId;
    private Slot slot;
    private MasterySkill masterySkill;
    private int levelRequirement;
    private int points;
    private int maxPoints;
    private final Supplier<MasterySkillCategory> masterySkillCategory;
    public MasterySkill(Supplier<MasterySkillCategory> masterySkillCategory, Slot slot, int levelRequirement, int maxPoints) {
        this.levelRequirement = levelRequirement;
        this.maxPoints = maxPoints;
        this.slot = slot;
        this.points = 0;
        this.masterySkillCategory = masterySkillCategory;
    }

    public void init() {
        MasterySkillCategory masterySkillCategorys = masterySkillCategory.get();
        masterySkillCategorys.addMasterySkill(this);
    }

    public MasterySkill(Slot slot, int levelRequirement, int maxPoints) {
        this(MasterySkillCategories.NONE, slot, levelRequirement, maxPoints);
    }

    public Supplier<MasterySkillCategory> getMasterySkillCategory() {
        return masterySkillCategory;
    }

    public void setLevelRequirement(int levelRequirement) {
        this.levelRequirement = levelRequirement;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getMaxPoints() {
        return this.maxPoints;
    }
    public int getPoints() {
        return this.points;
    }

    public String getName() {
        return BHRegistries.MASTERY_KEY.get().getKey(this).getPath();
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public String getId() {
        return BHRegistries.MASTERY_KEY.get().getKey(this).getNamespace();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = String.format("skills.%s.%s", this.getId(), this.getName());
        }
        return this.descriptionId;
    }


    protected String createId() {
        return createId(0);
    }

    protected String createId(int lines) {
        return lines == 0 ? String.format("%s.desc", this.getDescriptionId()) : String.format("%s.desc.%s", this.getDescriptionId(), lines);
    }

    public Optional<IAttack> IAttack() {
        return Optional.of(this);
    }

    public Optional<IEntityProperties> IEntityProperties() {
        return Optional.of(this);
    }

    public CompoundTag saveNbt() {
        CompoundTag nbt = new CompoundTag();
        ResourceLocation resourceLocation = BHRegistries.MASTERY_KEY.get().getKey(this);
        nbt.putString(ID, resourceLocation == null ? "beyondhorizon:none" : resourceLocation.toString());
        nbt.putString(SLOT_TAG, this.slot.getName());
        nbt.putInt(POINTS_TAG, this.points);
        nbt.putInt(MAX_POINTS_TAG, this.maxPoints);
        nbt.put(ATTRIBUTES_TAGS, new CompoundTag());
        return nbt;
    }
    public void loadNbt(CompoundTag nbt) {
        this.masterySkill = BHRegistries.MASTERY_KEY.get().getValue(ResourceLocation.parse(nbt.getString(ID)));
        this.slot = MasterySkill.Slot.valueOf(nbt.getString(SLOT_TAG));
        this.points = nbt.getInt(POINTS_TAG);
        this.maxPoints = nbt.getInt(MAX_POINTS_TAG);
    }
}
