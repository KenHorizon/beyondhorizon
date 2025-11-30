package com.kenhorizon.beyondhorizon.server.item.base;

import com.google.common.collect.ImmutableList;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.skills.Skill;
import com.kenhorizon.beyondhorizon.server.skills.Skills;
import com.kenhorizon.libs.server.IReloadable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@SuppressWarnings("unchecked")
public class SkillBuilder implements IReloadable {
    public static final SkillBuilder NONE = new SkillBuilder(SkillWeaponType.UNIVERSAL, Skills.NONE);
    public static final SkillBuilder RUINED_BLADE = new SkillBuilder(SkillWeaponType.MELEE, Skills.RUINED_BLADE);

    protected List<Supplier<? extends Skill>> suppliers = ImmutableList.of();
    protected List<Skill> skills = ImmutableList.of();
    protected List<Skill> filter = ImmutableList.of();
    protected SkillWeaponType skillWeaponType;

    public SkillBuilder(SkillWeaponType skillWeaponType, Supplier<? extends Skill>... abilityTraits) {
        ImmutableList.Builder<Supplier<? extends Skill>> builder = ImmutableList.builder();
        builder.add(abilityTraits);
        this.skillWeaponType = skillWeaponType;
        this.suppliers = builder.build();
        //ReloadableHandler.addToReloadList(this);
    }

    @Override
    public void reload() {
        //AtomicReference<Skill> builder = new AtomicReference<Skill>(null);
        this.suppliers.forEach(supplier -> {
            if (!filter.contains(supplier.get())) {
                filter.add(supplier.get());
            }
        });

        this.skills = this.filter.stream().filter(abilityTrait -> {
            boolean isValid = this.skillWeaponType.getFilter().test(abilityTrait);
            if (!isValid) {
                String errorMsg = getError(abilityTrait);
                BeyondHorizon.loggers().error("Skill is not match: {}", errorMsg);
            }
            return isValid;
        }).collect(Collectors.toUnmodifiableList());
    }

    private static @NotNull String getError(Skill skill) {
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
}
