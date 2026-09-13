package com.kenhorizon.beyondhorizon.server.api.skills;

import com.google.common.collect.ImmutableSet;
import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;


import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SkillBuilder implements IReloadable {
    public static final SkillBuilder NONE = new SkillBuilder(SkillTypes.UNIVERSAL, List.of(Skills.NONE));
    public static final SkillBuilder MACE = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.SMASH_ATTACK), ToolActions.SWORD_DIG);
    public static final SkillBuilder RADIANT_SWORD = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.RADIANT));
    public static final SkillBuilder GUARDIAN = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.GUARDIAN_SWORD_TRAIT, Skills.INFERNO_STRIKE));
    public static final SkillBuilder BLAZING_BEACON = new SkillBuilder(SkillTypes.RANGED, List.of(Skills.INFERNAL_RAY));
    public static final SkillBuilder SOLARFLARE = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.GUARDIAN_SWORD_TRAIT, Skills.INFERNO_STRIKE, Skills.INFERNAL_RAY));
    public static final SkillBuilder ELUDICATOR = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.DARK_BLADE));
    public static final SkillBuilder DARK_REPULSOR = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.PIERCING_EDEGE));
    public static final SkillBuilder RUINED_BLADE = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.RUINED_BLADE));
    public static final SkillBuilder BLADE_EDGE = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.BLADE_EDGE));
    public static final SkillBuilder RADIANT = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.RADIANT));
    public static final SkillBuilder HARVESTER = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.RADIANT));
    public static final SkillBuilder INFLICT_FIRE = new SkillBuilder(SkillTypes.UNIVERSAL, List.of(Skills.BURN_EFFECT));
    public static final SkillBuilder GIANT_SLAYER_SWORD = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.TRANNY));
    public static final SkillBuilder CLAYMORE = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.HEAVY_HITTER));
    public static final SkillBuilder STELLAR_AXE = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.CELESTIAL_STRIKE));
    public static final SkillBuilder HEAVENLY_EDGE_DARK_SWORD = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.CELESTIAL_STRIKE));
    public static final SkillBuilder HEAVENLY_EDGE_LIGHT_SWORD = new SkillBuilder(SkillTypes.MELEE, List.of(Skills.CELESTIAL_STRIKE));
    //

    public static final SkillBuilder THUNDERZAPPER = new SkillBuilder(SkillTypes.RANGED, List.of(Skills.BOLTSHOCK));
    public static final SkillBuilder STORMSURGE = new SkillBuilder(SkillTypes.RANGED, List.of(Skills.THUNDER));
    public static final SkillBuilder NIGHT_RAY = new SkillBuilder(SkillTypes.RANGED, List.of(Skills.TWILIGHT_RAY));
    public static final SkillBuilder WAND = new SkillBuilder(SkillTypes.RANGED, List.of(Skills.MAGIC_MISSILE));
     //

    protected List<Supplier<? extends Skill>> suppliers = new ArrayList<>();
    protected List<Skill> skills = new ArrayList<>();
    protected List<Optional<Skill>> actionTrait = new ArrayList<>();
    protected List<Skill> filter = new ArrayList<>();
    protected SkillTypes skillTypes;
    protected Set<ToolAction> toolActions = new HashSet<>();

    public SkillBuilder(SkillTypes skillTypes, List<Supplier<? extends Skill>> skills, Set<ToolAction> toolActions) {
        this.skillTypes = skillTypes;
        this.toolActions = toolActions;
        this.suppliers = skills;
        ReloadableHandler.addToReloadList(this);
    }
    public SkillBuilder(SkillTypes skillTypes, List<Supplier<? extends Skill>> skills, ToolAction... toolActions) {
        this(skillTypes, skills, ImmutableSet.copyOf(toolActions));
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
            boolean isValid = this.skillTypes.getFilter().test(skill) && skill != Skills.NONE.get();
            if (isValid && skill.isActive()) {
                this.actionTrait.add(Optional.of(skill));
            }
            if (!isValid) {
                BeyondHorizon.LOGGER.error(skill.errorNotMatch(skill));
            }
            return isValid;
        }).collect(Collectors.toUnmodifiableList());

    }
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return this.toolActions.contains(toolAction);
    }

    public List<Skill> getSkills() {
        return this.skills;
    }

    public List<Optional<Skill>> getActionSkills() {
        return actionTrait;
    }
}
