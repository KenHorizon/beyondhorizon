package com.kenhorizon.beyondhorizon.server.skills;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SkillBuilder implements IReloadable {
    public static final SkillBuilder NONE = new SkillBuilder(SkillTypes.UNIVERSAL, List.of(Skills.NONE));
    public static final SkillBuilder RUINED_BLADE = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.RUINED_BLADE));
    public static final SkillBuilder BLADE_EDGE = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.BLADE_EDGE));
    public static final SkillBuilder RADIANT = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.RADIANT));
    public static final SkillBuilder INFLICT_FIRE = new SkillBuilder(SkillTypes.UNIVERSAL, List.of(Skills.BURN_EFFECT));
    public static final SkillBuilder GIANT_SLAYER_SWORD = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.TRANNY, Skills.RETRIBUTION));

    protected List<Supplier<? extends Skill>> suppliers = new ArrayList<>();
    protected List<Skill> skills = new ArrayList<>();
    protected List<Skill> filter = new ArrayList<>();
    protected SkillTypes skillTypes;

    public SkillBuilder(SkillTypes skillTypes, List<Supplier<? extends Skill>> skills) {
        this.skillTypes = skillTypes;
        this.suppliers = skills;
        ReloadableHandler.addToReloadList(this);
    }

    @Override
    public void reload() {
        this.suppliers.forEach(supplier -> {
            Skill skill = supplier.get();
            if (!this.filter.contains(skill)) {
                this.filter.add(skill);
            }
        });

        this.skills = this.filter.stream().filter(skill -> {
            boolean isValid = this.skillTypes.getFilter().test(skill) && skill != Skills.NONE.get();
            if (!isValid) {
                SkillLoggers.error(skill.errorNotMatch(skill));
            }
            return isValid;
        }).collect(Collectors.toUnmodifiableList());
    }

    public List<Skill> getSkills() {
        return this.skills;
    }
}
