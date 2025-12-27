package com.kenhorizon.beyondhorizon.server.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.keybinds.Keybinds;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessories;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.classes.RoleClass;
import com.kenhorizon.beyondhorizon.server.api.classes.RoleClasses;
import com.kenhorizon.beyondhorizon.server.init.BHCreativeTabs;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.skills.Skills;
import com.kenhorizon.beyondhorizon.client.level.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.libs.registry.RegistryLanguage;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.function.Supplier;

public class BHLangProvider extends LanguageProvider {

    public BHLangProvider(PackOutput output) {
        super(output, BeyondHorizon.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        RegistryLanguage.ADD_ENCHANTMENT_TRANSLATION.forEach(this::addEnchantment);
        RegistryLanguage.ADD_ITEM_TRANSLATION.forEach(this::addItem);
        RegistryLanguage.ADD_BLOCK_TRANSLATION.forEach(this::addBlock);
        RegistryLanguage.ADD_ENTITY_TRANSLATION.forEach(this::addEntityType);
        RegistryLanguage.ADD_SOUNDS_TRANSLATION.forEach(this::addSoundEvents);
        RegistryLanguage.ADD_MOB_EFFECT_TRANSLATION.forEach(this::addEffect);
        RegistryLanguage.ADD_ATTRIBUTE_TRANSLATION.forEach(this::addAttributes);
        RegistryLanguage.ADD_PAINTING_TRANSLATION_AUTHOR.forEach(this::addPaintingAuthor);
        RegistryLanguage.ADD_PAINTING_TRANSLATION_TITLE.forEach(this::addPaintingTitle);
        RegistryLanguage.ADD_ITEM_LORE.forEach(this::add);
        Keybinds.KEYBINDING.forEach(this::add);

        this.add(Tooltips.TOOLTIP_BUILTIN_RESOURCE, "Beyond Horizon: Builtin Resources");

        this.add(Tooltips.COMMAND_ROLE_SET_FAILED, "Failed to change the role to %s");
        this.add(Tooltips.COMMAND_ROLE_SET_SUCCESS, "Successfully changed the role to %s");
        this.add(Tooltips.COMMAND_LEVEL_SET_FAILED, "Failed to set the level %s");
        this.add(Tooltips.COMMAND_LEVEL_SET_SUCCESS, "Successfully set the level %s");
        this.add(Tooltips.COMMAND_POINTS_FAILED, "Failed to set the points %s");
        this.add(Tooltips.COMMAND_POINTS_SUCCESS, "Successfully set the points %s");
        this.add(Tooltips.COMMAND_RESET_FAILED, "Failed to reset the roles");
        this.add(Tooltips.COMMAND_RESET_SUCCESS, "Successfully reset the roles");

        this.add(Tooltips.BOSS_IS_DEFEATED, "The %s is defeated");
        this.addBossMessage(BHEntity.BLAZING_INFERNO.get(), "The ocean and desert guardian has awoken...");

        this.add(Tooltips.SKILL_TYPE, "%s");
        this.add(Tooltips.TOOLTIP_MINING_SPEED, "%s Mining Speed");
        this.addSkills(Skills.RUINED_BLADE.get(), "Ruined Blade", "Deal additional +%s%% target's Current HP");
        this.addSkills(Skills.BLADE_EDGE.get(), "Blade Edge", "Deal additional +%s%% target's Max HP");
        this.addSkills(Skills.RADIANT.get(), "Radiant", "Deal additional damage to undead by +%s%%");
        this.addSkills(Skills.TRANNY.get(), "Tranny", "Gain Attack Damage equal to %s%% Max HP (%s)");
        this.addSkills(Skills.RETRIBUTION.get(), "Retribution", "Increase damage dealt 0-%s%% based on Missing HP");
        this.addSkills(Skills.BURN_EFFECT.get(), "Burn", "Set target on fire for %s seconds");
        this.addSkills(Skills.DEATH.get(), "Death", "Dealing post-mitigaion damage that would leave below %s%% of their Max HP, Execute them");
        this.addSkills(Skills.LETHALITY.get(), "Lethality", "Grant %s%% increased physical damage");
        this.addSkills(Skills.KINETIC_STRIKE.get(), "Kinetic Strike", "Grant %s%%(+%s%% per %s%%movement speed) increased damage");
        //
        this.addAccessory(Accessories.FEATHER_FEET.get(), "Feather Feet", "Negate fall damage");
        this.addAccessory(Accessories.OVERGROWTH.get(), "Overgrowth", "Increase Bonus Max HP by %s%% (%s)");
        this.addAccessory(Accessories.DESPAIR_AND_DEFY.get(), "Despair And Defy", "Reduce %s%% post-mitigation damage and stored it and bleed per second of stored damage");
        this.addAccessory(Accessories.BURN_EFFECT.get(), "Burn", "Set target on fire for %s seconds");
        this.addAccessory(Accessories.FIRE_IMMUNITY.get(), "Fire Immunity", "Grant immunity from fire block");
        this.addAccessory(Accessories.RAGE.get(), "Rage", "Grant 0-%s%%(based on Missing HP) increased damage");
        this.addAccessory(Accessories.THORNS.get(), "Thorns", "When struck by basic attack on-hit, deal %s (+%s%% bonus Armor) magic damage to the attacker and inflict Wounded for 3 seconds");
        this.addAccessory(Accessories.KNOWLEDGE.get(), "Knowledge", "Increase drop experience by %s%%");
        this.addAccessory(Accessories.VENOM.get(), "Venom", "Attacks have %s%% chance inflict either Poison or Lethal Poison for %s seconds");
        this.addAccessory(Accessories.JUMP_BOOST.get(), "Jump Boost", "Increase the jump height by %s%%");
        //
        this.addRole(RoleClasses.ASSASSIN.get(), "Assassin", "Specialty of stealth to ambush and kill the target");
        this.addRole(RoleClasses.MARKSMAN.get(), "Marksman", "Specialize in precision, long range engagement and delivering consistent, high damage from safe distance");
        this.addRole(RoleClasses.CASTER.get(), "Caster", "Caster or Mage, specialize the casting powerful spell and ability");
        this.addRole(RoleClasses.VANGUARD.get(), "Vanguard", "Vanguard or Tank that specialize defense and utility control in combat");
        this.addRole(RoleClasses.STRIKER.get(), "Striker", "A close combat class that specialize powerful strike and engage to the enemy");
        this.addRole(RoleClasses.SUPPORT.get(), "Support", "A sub-class to the mage who specialize the casting powerful healing and buff to allied");
        //
        creativeTabs(BHCreativeTabs.BH_INGREDIENTS, "Beyond Horizon: Ingredients");
        creativeTabs(BHCreativeTabs.BH_TOOLS, "Beyond Horizon: Tools");
        creativeTabs(BHCreativeTabs.BH_ACCESSORY, "Beyond Horizon: Accessorry");
        creativeTabs(BHCreativeTabs.BH_WEAPONS, "Beyond Horizon: Weapons");
        creativeTabs(BHCreativeTabs.BH_BLOCKS, "Beyond Horizon: Blocks");
        creativeTabs(BHCreativeTabs.BH_SPAWN_EGG, "Beyond Horizon: Spawn Egg");
        creativeTabs(BHCreativeTabs.BH_DEBUG_ITEMS, "Beyond Horizon: Debug Items");
        //
        this.add(Tooltips.TOOLTIP_ACCESSORY, "Accessory");
        this.add(Tooltips.TOOLTIP_ACCESSORY_TYPE, "Unique");
        this.add(Tooltips.TOOLTIP_INVENTORY, "Inventory");
        this.add(Tooltips.TOOLTIP_HEALTH_RECOVERY_POTION, "Restore %s Health");
        this.add(Tooltips.TOOLTIP_MANA_RECOVERY_POTION, "Restore %s Mana");

        this.add(BeyondHorizon.ID + ".attributes.plus.percent", "+%s%% %s");
        this.add(BeyondHorizon.ID + ".attributes.take.percent", "-%s%% %s");
        this.add(BeyondHorizon.ID + ".attributes.plus.0", "+%s %s");
        this.add(BeyondHorizon.ID + ".attributes.plus.1", "+%s%% %s");
        this.add(BeyondHorizon.ID + ".attributes.plus.2", "+%s%% %s");
        this.add(BeyondHorizon.ID + ".attributes.take.0", "-%s %s");
        this.add(BeyondHorizon.ID + ".attributes.take.1", "-%s%% %s");
        this.add(BeyondHorizon.ID + ".attributes.take.2", "-%s%% %s");
    }
    private void addAccessory(Accessory accessory, String name) {
        this.add(accessory.getDescriptionId(), name);
    }

    private void addAccessory(Accessory accessory, String name, String... descriptions) {
        this.add(accessory.getDescriptionId(), name);
        for (int i = 0; i < descriptions.length; i++) {
            if (i == 0) {
                this.add(accessory.getDescriptionId() + ".desc", descriptions[i]);
            } else {
                this.add(accessory.getDescriptionId() + ".desc." + i, descriptions[i]);
            }

        }
    }
    private void addRole(RoleClass roleClass, String name) {
        this.add(roleClass.getDescriptionId(), name);
    }

    private void addRole(RoleClass roleClass, String name, String... descriptions) {
        this.add(roleClass.getDescriptionId(), name);
        for (int i = 0; i < descriptions.length; i++) {
            if (i == 0) {
                this.add(roleClass.getDescriptionId() + ".desc", descriptions[i]);
            } else {
                this.add(roleClass.getDescriptionId() + ".desc." + i, descriptions[i]);
            }

        }
    }
    private void addSkills(Skill skill, String name) {
        this.add(skill.getDescriptionId(), name);
    }

    private void addSkills(Skill skill, String name, String... descriptions) {
        this.add(skill.getDescriptionId(), name);
        for (int i = 0; i < descriptions.length; i++) {
            if (i == 0) {
                this.add(skill.getDescriptionId() + ".desc", descriptions[i]);
            } else {
                this.add(skill.getDescriptionId() + ".desc." + i, descriptions[i]);
            }

        }
    }

    private void addBossMessage(EntityType<?> entityType, String... descriptions) {
        for (int i = 0; i < descriptions.length; i++) {
            if (i == 0) {
                this.add(Tooltips.getBossMessage(entityType), descriptions[i]);
            } else {
                this.add(Tooltips.getBossMessage(entityType) + "." + i, descriptions[i]);
            }

        }
    }

    private void addAttributes(Supplier<? extends Attribute> attribute, String name) {
        this.add(attribute.get().getDescriptionId(), name);
    }

    private void addSoundEvents(String name, String subtitles) {
        this.add(name, subtitles);
    }

    private void addPaintingAuthor(String addPaintings, String name) {
        this.add(String.format("painting.%s.%s.title", BeyondHorizon.ID, addPaintings), name);
    }

    private void addPaintingTitle(String addPaintings, String title) {
        this.add(String.format("painting.%s.%s.author", BeyondHorizon.ID, addPaintings), title);
    }

    private void creativeTabs(Supplier<? extends CreativeModeTab> creativeTabs, String name) {
        this.add(creativeTabs.get().getDisplayName().getString(), name);
    }
}
