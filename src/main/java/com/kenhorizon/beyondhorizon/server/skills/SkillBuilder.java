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


@SuppressWarnings("unchecked")
public class SkillBuilder implements IReloadable {
    public static final SkillBuilder NONE = new SkillBuilder(SkillWeaponType.UNIVERSAL, List.of(Skills.NONE));
    public static final SkillBuilder RUINED_BLADE = new SkillBuilder(SkillWeaponType.MELEE, List.of(Skills.RUINED_BLADE));
    public static final SkillBuilder BLADE_EDGE = new SkillBuilder(SkillWeaponType.MELEE, List.of(Skills.BLADE_EDGE));

    protected List<Supplier<? extends Skill>> suppliers = new ArrayList<>();
    protected List<Skill> skills = new ArrayList<>();
    protected List<Skill> filter = new ArrayList<>();
    protected Optional<Skill> actionSkills = Optional.empty();
    protected SkillWeaponType skillWeaponType;

    public SkillBuilder(SkillWeaponType skillWeaponType, List<Supplier<? extends Skill>> skills) {
        this.skillWeaponType = skillWeaponType;
        this.suppliers = skills;
        ReloadableHandler.addToReloadList(this);
    }

    @Override
    public void reload() {
        AtomicReference<Skill> builder = new AtomicReference<Skill>(null);
        this.suppliers.forEach(supplier -> {
            Skill skill = supplier.get();
            if (!this.filter.contains(skill)) {
                this.filter.add(skill);
            }
        });

        this.skills = this.filter.stream().filter(skill -> {
            boolean isValid = this.skillWeaponType.getFilter().test(skill) && skill != Skills.NONE.get();
            BeyondHorizon.loggers().error("Skill is : {} : {}", skill, isValid);
            if (isValid) {
                if (builder.get() == null) {
                    builder.set(skill);
                } else {
                    return false;
                }
            } else if (!isValid) {
                String errorMsg = this.getError(skill);
                BeyondHorizon.loggers().error("Skill is not match: {}", errorMsg);
            }
            return isValid;
        }).collect(Collectors.toUnmodifiableList());
        Skill skill = builder.get();
        this.actionSkills = skill != null ? Optional.of(builder.get()) : Optional.empty();
    }

    private @NotNull String getError(Skill skill) {
        String errorMsg;
        if (skill.isMeleeAbility()) {
            errorMsg = String.format("Weapon is not melee - %s", skill.getName());
        } else if (skill.isRangedAbility()) {
            errorMsg = String.format("Weapon is not ranged - %s", skill.getName());
        } else {
            errorMsg = String.format("Weapon is not throwable - %s", skill.getName());
        }
        return errorMsg;
    }

    public List<Skill> getSkills() {
        return this.skills;
    }
    public Optional<Skill> getActionSkill() {
        return this.actionSkills;
    }
}
