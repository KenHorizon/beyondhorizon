package com.kenhorizon.beyondhorizon.server.api.skills;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SkillBuilder implements IReloadable {
    public static final SkillBuilder NONE = new SkillBuilder(SkillTypes.UNIVERSAL, List.of(Skills.NONE));
    public static final SkillBuilder RADIANT_SWORD = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.RADIANT));
    public static final SkillBuilder GUARDIAN = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.GUARDIAN_SWORD_TRAIT, com.kenhorizon.beyondhorizon.server.api.skills.Skills.BLAZING_CLEAVE, com.kenhorizon.beyondhorizon.server.api.skills.Skills.INFERNO_STRIKE));
    public static final SkillBuilder BLAZING_BEACON = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.RANGED, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.INFERNAL_RAY));
    public static final SkillBuilder SOLARFLARE = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.GUARDIAN_SWORD_TRAIT, com.kenhorizon.beyondhorizon.server.api.skills.Skills.BLAZING_CLEAVE, com.kenhorizon.beyondhorizon.server.api.skills.Skills.INFERNO_STRIKE, com.kenhorizon.beyondhorizon.server.api.skills.Skills.INFERNAL_RAY));
    public static final SkillBuilder ELUDICATOR = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.DARK_BLADE));
    public static final SkillBuilder DARK_REPULSOR = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.PIERCING_EDEGE));
    public static final SkillBuilder RUINED_BLADE = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.RUINED_BLADE));
    public static final SkillBuilder BLADE_EDGE = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.BLADE_EDGE));
    public static final SkillBuilder RADIANT = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.RADIANT));
    public static final SkillBuilder HARVESTER = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.RADIANT));
    public static final SkillBuilder INFLICT_FIRE = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.UNIVERSAL, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.BURN_EFFECT));
    public static final SkillBuilder GIANT_SLAYER_SWORD = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.TRANNY));
    public static final SkillBuilder CLAYMORE = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.HEAVY_HITTER));
    public static final SkillBuilder STELLAR_AXE = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.CELESTIAL_STRIKE));
    public static final SkillBuilder HEAVENLY_EDGE_DARK_SWORD = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.CELESTIAL_STRIKE));
    public static final SkillBuilder HEAVENLY_EDGE_LIGHT_SWORD = new SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes.MELEE, List.of(com.kenhorizon.beyondhorizon.server.api.skills.Skills.CELESTIAL_STRIKE));

    protected List<Supplier<? extends Skill>> suppliers = new ArrayList<>();
    protected List<Skill> skills = new ArrayList<>();
    protected List<Optional<Skill>> actionTrait = new ArrayList<>();
    protected List<Skill> filter = new ArrayList<>();
    protected com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes skillTypes;

    public SkillBuilder(com.kenhorizon.beyondhorizon.server.api.skills.SkillTypes skillTypes, List<Supplier<? extends Skill>> skills) {
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
            skill.innateSkill().forEach(innate -> {
                if (!this.filter.contains(innate.get())) {
                    this.filter.add(innate.get());
                }
            });
        });

        this.skills = this.filter.stream().filter(skill -> {
            boolean isValid = this.skillTypes.getFilter().test(skill) && skill != com.kenhorizon.beyondhorizon.server.api.skills.Skills.NONE.get();
            if (isValid && skill.isActive()) {
                this.actionTrait.add(Optional.of(skill));
            }
            if (!isValid) {
                BeyondHorizon.LOGGER.error(skill.errorNotMatch(skill));
            }
            return isValid;
        }).collect(Collectors.toUnmodifiableList());

    }

    public List<Skill> getSkills() {
        return this.skills;
    }

    public List<Optional<Skill>> getActionSkills() {
        return actionTrait;
    }
}
