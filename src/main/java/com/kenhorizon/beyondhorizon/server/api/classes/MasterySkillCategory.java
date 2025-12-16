package com.kenhorizon.beyondhorizon.server.api.classes;

import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MasterySkillCategory implements IAttack, IEntityProperties {
    public static final String ID = "id";
    public static final String VALUE_TAG = "value";
    @Nullable
    protected String descriptionId;
    protected MasterySkillCategory masterySkillCategory;
    protected List<MasterySkill> masterySkills = new ArrayList<>();

    public MasterySkillCategory() {
    }

    public MasterySkillCategory getMasterySkillCategory() {
        return masterySkillCategory;
    }

    public void addMasterySkill(MasterySkill masterySkill) {
        this.masterySkills.add(masterySkill);
    }


    public String getName() {
        return BHRegistries.MASTERY_CATEGORY_KEY.get().getKey(this).getPath();
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public String getId() {
        return BHRegistries.MASTERY_CATEGORY_KEY.get().getKey(this).getNamespace();
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
        ResourceLocation roleId = BHRegistries.MASTERY_CATEGORY_KEY.get().getKey(this);
        nbt.putString(ID, roleId == null ? "beyondhorizon:none" : roleId.toString());
        this.masterySkills.forEach(masterySkill -> {
            nbt.put(VALUE_TAG, masterySkill.saveNbt());
        });
        return nbt;
    }
    public void loadNbt(CompoundTag nbt) {
        this.masterySkillCategory = BHRegistries.MASTERY_CATEGORY_KEY.get().getValue(ResourceLocation.parse(nbt.getString(ID)));
        this.masterySkills.forEach(masterySkill -> {
            masterySkill.loadNbt(nbt);
        });
    }
}
