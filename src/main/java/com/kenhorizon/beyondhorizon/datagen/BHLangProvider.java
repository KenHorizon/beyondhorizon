package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.keybinds.Keybinds;
import com.kenhorizon.beyondhorizon.client.render.guis.guide_book.GuideBookScreen;
import com.kenhorizon.beyondhorizon.configs.Configs;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessories;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.bonus_set.ArmorBonusSet;
import com.kenhorizon.beyondhorizon.server.api.bonus_set.ArmorBonusSets;
import com.kenhorizon.beyondhorizon.server.init.*;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.skills.Skills;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.item.PlayerTrackerItem;
import com.kenhorizon.libs.registry.RegistryLanguage;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
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

        this.add(Configs.CHANGE_DAMAGE_CALCULATION, "Change Damage Calculation");

        this.add(Tooltips.TOOLTIP_KEYBIND, "Button: %s + %s");
        this.add(Tooltips.TOOLTIP_COOLDOWN, "Cooldown: %s");
        this.add(Tooltips.TOOLTIP_MANA_COST, "Mana: %s");
        this.add(Tooltips.TOOLTIP_MANA_COST_PERCENTAGES, "Mana: %s%%");
        this.add(Tooltips.TOOLTIP_MANA_COST_PER_SECOND, "Mana: %s per second");
        this.add(Tooltips.TOOLTIP_MANA_NOT_ENOUGH, "Mana not enough!");

        this.add(Tooltips.TOOLTIP_WORKBENCH, "Workbench");
        this.add(Tooltips.TOOLTIP_WORKBENCH_ITEMS, "Items");
        this.add(Tooltips.TOOLTIP_WORKBENCH_INGREDIENTS, "Ingredients");
        this.add(Tooltips.TOOLTIP_WORKBENCH_FORGE, "Forge");
        this.add(Tooltips.TOOLTIP_WORKBENCH_HELP_0, "Accessory/Artifacts will not shown on the Items Category");
        this.add(Tooltips.TOOLTIP_WORKBENCH_HELP_1, "unless you have one of them ingredients/recipes");

        this.add(Tooltips.TOOLTIP_IMMUNE_TO, "Gain Immunity to %s");
        this.add(Tooltips.TOOLTIP_BUILTIN_RESOURCE, "Beyond Horizon: Builtin Resources");

        this.add(Tooltips.COMMAND_POINTS_FAILED, "Failed to change the role to %s");
        this.add(Tooltips.COMMAND_POINTS_SUCCESS, "Successfully changed the role to %s");
        this.add(Tooltips.COMMAND_LEVEL_SET_FAILED, "Failed to set the level %s");
        this.add(Tooltips.COMMAND_LEVEL_SET_SUCCESS, "Successfully set the level %s");
        this.add(Tooltips.COMMAND_RESET_FAILED, "Failed to reset the roles");
        this.add(Tooltips.COMMAND_RESET_SUCCESS, "Successfully reset the roles");

        this.add(Tooltips.BOSS_IS_DEFEATED, "The %s is defeated");
        this.addBossMessage(BHEntity.BLAZING_INFERNO.get(), "The ocean and desert guardian has awoken...");

        //

        //
        this.addEnchantmentDesc(BHEnchantments.LIFESTEAL, "Grant healing equal to percentage of the damage dealt");
        this.addEnchantmentDesc(BHEnchantments.BUTCHERING, "Deal additional damage to animals");
        this.addEnchantmentDesc(BHEnchantments.AQUATIC_BANE, "Increased post-mitigation damage dealt by percentage to aquatic creatures");
        this.addEnchantmentDesc(BHEnchantments.ILLAGER_BANE, "Increased post-mitigation damage dealt by percentage to illagers");
        this.addEnchantmentDesc(BHEnchantments.VOID_BANE, "Increased post-mitigation damage dealt by percentage to enderman");
        this.addEnchantmentDesc(BHEnchantments.DYNAMO_HIT, "Increased critical damage but reduce attack effectiviness");
        this.addEnchantmentDesc(BHEnchantments.PIERCE, "Gain armor penetration");
        this.addEnchantmentDesc(BHEnchantments.CRITICAL_HIT, "Increased critical chances");
        this.addEnchantmentDesc(BHEnchantments.CRITICAL_DAMAGE, "Increased critical strike damage");
        this.addEnchantmentDesc(BHEnchantments.DRAGON_SLAYER, "Increased damage dealt and Increased damage against to dragons");
        this.addEnchantmentDesc(BHEnchantments.VIBRANCY, "Increased total max health");
        this.addEnchantmentDesc(BHEnchantments.SWIFTNESS, "Increased total movement speed");
        this.addEnchantmentDesc(BHEnchantments.SPELL_BLADE, "Convert percentage of the physical damage dealt into magic damage");
        this.addEnchantmentDesc(BHEnchantments.ECHO, "Small chances to attack twice");
        this.addEnchantmentDesc(BHEnchantments.DRAW_SPEED, "Reduce use time while using bow");
        this.addEnchantmentDesc(BHEnchantments.STUNNING, "Chance to stun the target");
        this.addEnchantmentDesc(BHEnchantments.SMELTER, "Chance to auto smelt the item");
         //
        this.addArmorBonusSet(ArmorBonusSets.WILDFIRE_ARMOR_SET, 0, "Chance to release a shockwave dealing %s + %s%% total of attack damage");
        this.addArmorBonusSet(ArmorBonusSets.WILDFIRE_ARMOR_SET, 1, "Attacks inflict burning and increased %s%% damage dealt");
         //
        this.add(Tooltips.SKILL_TYPE, "%s");
        this.add(Tooltips.TOOLTIP_BONUS_ARMOR_SET, "Bonus set:");
        this.add(Tooltips.TOOLTIP_MINING_SPEED, "%s Mining Speed");
        this.addSkills(Skills.RUINED_BLADE.get(), "Ruined Blade", "Deal additional %.2f%% target's Current HP");
        this.addSkills(Skills.BLADE_EDGE.get(), "Blade Edge", "Deal additional +%s%% target's Max HP");
        this.addSkills(Skills.RADIANT.get(), "Radiant", "Deal additional damage to undead by +%s%%");
        this.addSkills(Skills.TRANNY.get(), "Tranny", "Gain Attack Damage equal to %s%% Max HP (%s)");
        this.addSkills(Skills.RETRIBUTION.get(), "Retribution", "Increase damage dealt 0-%.2f%% based on Missing HP");
        this.addSkills(Skills.BURN_EFFECT.get(), "Burn", "Set target on fire for %s seconds");
        this.addSkills(Skills.DEATH.get(), "Death", "Dealing post-mitigaion damage that would leave below %s%% of their Max HP, Execute them");
        this.addSkills(Skills.LETHALITY.get(), "Lethality", "Grant %s%% increased physical damage");
        this.addSkills(Skills.KINETIC_STRIKE.get(), "Kinetic Strike", "Grant %s(+%s%% per %s%% movement speed)%% increased damage");
        this.addSkills(Skills.PERFECTION.get(), "Perfection", "When critically striking, convert %s%% critical strike damage to true damage");
        this.addSkills(Skills.PIERCING_EDEGE.get(), "Piercing Edge", "Increase physical damage dealt by %s (+%s%% target's armor)%% to armored target");
        this.addSkills(Skills.BLAZING_CLEAVE.get(), "Blazing Cleave", "On-hit attack release a powerful shockwave that deal %s%% damage within %s range");
        //
        this.addAccessory(Accessories.ENERGIZED.get(), "Energized", "Moving and basic attacking generates Energize stacks, up to 100 (6 if attacking, 1 if moving)");
        this.addAccessory(Accessories.ELECTROSHOCK.get(), "Electro Shock", "When fully Energized, your next basic attack strike the target with bolt of lightning dealing bonus 40% damage dealt magic damage of 2.5 radius unit");
        this.addAccessory(Accessories.FEATHER_FEET.get(), "Feather Feet", "Negate fall damage");
        this.addAccessory(Accessories.OVERGROWTH.get(), "Overgrowth", "Increase Bonus Max HP by %s%% (%s)");
        this.addAccessory(Accessories.DESPAIR_AND_DEFY.get(), "Despair And Defy", "Reduce %.2f%% post-mitigation damage and stored it and bleed per second of stored damage");
        this.addAccessory(Accessories.BURN_EFFECT.get(), "Burn", "Set target on fire for %s seconds");
        this.addAccessory(Accessories.FIRE_IMMUNITY.get(), "Fire Immunity", "Grant immunity from fire block");
        this.addAccessory(Accessories.RAGE.get(), "Rage", "Grant 0-%s%%(based on Missing HP) increased damage");
        this.addAccessory(Accessories.THORNS.get(), "Thorns", "When struck by basic attack on-hit, deal %s (+%s%% bonus Armor) magic damage to the attacker and inflict Wounded for 3 seconds");
        this.addAccessory(Accessories.KNOWLEDGE_1.get(), "Knowledge", "Increase drop experience by %s%%");
        this.addAccessory(Accessories.KNOWLEDGE_2.get(), "Ultima Knowledge", "Increase drop experience by %s%%");
        this.addAccessory(Accessories.VENOM.get(), "Venom", "Attacks have %.2f%% chance inflict either Poison or Lethal Poison for %s seconds");
        this.addAccessory(Accessories.JUMP_BOOST.get(), "Jump Boost", "Increase the jump height by %s%%");
        this.addAccessory(Accessories.ETERNAL_LIFE.get(), "Eternal Life", "Revive on death and consume Undying Totem in the inventory");
        this.addAccessory(Accessories.NULLIFY.get(), "Nullify", "Ignore enchantment protection by %s%% at same time your attack effectivness reduced by %s%%");
        this.addAccessory(Accessories.LIFE_SIPHON.get(), "Life Siphon", "Deal additional %s%% target's current health");
        this.addAccessory(Accessories.ASCENSION.get(), "Ascension", "All attributes are increased by 200%");
        this.addAccessory(Accessories.CLEANSE.get(), "Cleanse", "Healing and Shielding are increased by 200%");
        this.addAccessory(Accessories.ROCK_SOLID.get(), "Rock Solid", "Reduce basic attack pre-mitigation damage by %s%%");
        this.addAccessory(Accessories.INFLAME.get(), "Inflame", "Attacks inflict Inflame that burn target for 0.5 magic damage per 0.5 seconds");
        this.addAccessory(Accessories.SPELL_BLADE_0.get(), "Spell Blade", "If damage not taken for %s seconds deal additional %s%% base AD physical damage on-hit");
        this.addAccessory(Accessories.SPELL_BLADE_1.get(), "Spell Blade", "If damage not taken for %s seconds deal additional (+%s%% AP) magic damage on-hit");
        this.addAccessory(Accessories.SPELL_BLADE_2.get(), "Spell Blade", "If damage not taken for %s seconds deal additional (+%s%% AD) physical damage on-hit");
        this.addAccessory(Accessories.DEATH.get(), "Death", "If you deal post-mitigation damage that would leave a target below %s%% of their max health, execute them");
        this.addAccessory(Accessories.TAXS.get(), "Taxs", "Killing a target will drop 1 emerald");
        this.addAccessory(Accessories.CORRUPTED_BITE.get(), "Corrupted Bite", "Deal additional (%s%% Total damage dealt) magic damage");
        this.addAccessory(Accessories.STING.get(), "Sting", "Basic Attack deal additional %s physical damage");
        this.addAccessory(Accessories.TORMENT.get(), "Torment", "Dealing damage burn dealing 1% of the target's max health magic damage every 0.5 seconds for 3 seconds");
        this.addAccessory(Accessories.BRING_IT_DOWN.get(), "Bring It Down", "Basic attack grant a stack for 3 seconds, up to 2 stacks, at 2 stacks, the next attack consumes all stacks to deal (+%s per level) physical damage on-hit increased by %s%% based on target's missing health");
        this.addAccessory(Accessories.POLYETHYLENE.get(), "Polyethylene", "Increase ranged damage by %s%% and knockback by %s%%");
        this.addAccessory(Accessories.FLUOROCARBON.get(), "Fluorocarbon", "Reduce the draw time of Item");
        this.addAccessory(Accessories.DARK_SUN.get(), "Dark Sun", "Convert 25% damage dealt into addtional bonus true damage on-hit");
        this.addAccessory(Accessories.NIGHTSTALKER.get(), "Nightstalker", "Increased all damage by 0-%s%% based on target missing health");
        this.addAccessory(Accessories.PENALTY_0.get(), "Penalty", "Reduce the attack effectiveness by %s%%");
        this.addAccessory(Accessories.EXCORIATE.get(), "Excoriate", "Gain random bonus critidal damage upto 0-%s%%, with the value of this changing of every 0.25 seconds");
        this.addAccessory(Accessories.GHOUL.get(), "Ghoul", "Hunger exhaustion increased by 150%, On-kill restore 5 hunger points and gain buff of Ghoul Will, and prevent user eat edible item");
        this.addAccessory(Accessories.FADED_MOON.get(), "Faded Moon", "Convert 25% Max Mana into additional bonus magic damage on-hit");
        this.addAccessory(Accessories.TWO_WORLD.get(), "Two World", "Gain Dark Sun and Faded Moon, If you have more than bonus attack damage to ability power enter Dark Sun and if you have more than ability power to attack damage enter Faded Moon ");
        this.addAccessory(Accessories.TITANIC_CRESCENT.get(), "Titanic Crescent", "Basic attack on-hit deal bonus %s%% Max HP to the target and %s%% Max HP to others entity in a cone in the direction of the primary target");
        this.addAccessory(Accessories.SWIFTNESS.get(), "Swiftness", "Inflict Speed boost effect for 5 seconds");
        this.addAccessory(Accessories.STALKER.get(), "Stalker", "Gain 100% Stealth and becoming invisible");
        //

        creativeTabs(BHCreativeTabs.BH_INGREDIENTS, "Beyond Horizon | Ingredients");
        creativeTabs(BHCreativeTabs.BH_TOOLS, "Beyond Horizon | Tools");
        creativeTabs(BHCreativeTabs.BH_ACCESSORY, "Beyond Horizon | Accessorry");
        creativeTabs(BHCreativeTabs.BH_COMBATS, "Beyond Horizon | Combats");
        creativeTabs(BHCreativeTabs.BH_BLOCKS, "Beyond Horizon | Blocks");
        creativeTabs(BHCreativeTabs.BH_SPAWN_EGG, "Beyond Horizon | Spawn Egg");
        creativeTabs(BHCreativeTabs.BH_DEBUG_ITEMS, "Beyond Horizon | Debug Items");
        //
        this.add(Tooltips.TOOLTIP_ACCESSORY, "Accessory");
        this.add(Tooltips.TOOLTIP_ACCESSORY_TYPE, "Unique");
        this.add(Tooltips.TOOLTIP_ACCESSORY_SKILL_TYPE, "%s");
        this.add(Tooltips.TOOLTIP_INVENTORY, "Inventory");
        this.add(Tooltips.TOOLTIP_HEALTH_RECOVERY_POTION, "Restore %s Health");
        this.add(Tooltips.TOOLTIP_MANA_RECOVERY_POTION, "Restore %s Mana");

        this.add(PlayerTrackerItem.NBT_PLAYER_HEALTH, "Health:");
        this.add(PlayerTrackerItem.NBT_PLAYER_MANA, "Mana:");
        this.add(PlayerTrackerItem.NBT_PLAYER_ATTACK_DAMAGE, "Attack Damage:");
        this.add(PlayerTrackerItem.NBT_PLAYER_ABILITY_POWER, "Ability Power:");
        this.add(PlayerTrackerItem.NBT_PLAYER_ARMOR, "Armor:");
        this.add(PlayerTrackerItem.NBT_PLAYER_MAGIC_ARMOR, "Magic Resistance:");
        this.add(PlayerTrackerItem.NBT_PLAYER_DAMAGE_AMP, "Damage Dealt:");
        this.add(PlayerTrackerItem.NBT_PLAYER_DAMAGE_TAKEN, "Damage Taken:");
        this.add(PlayerTrackerItem.NBT_PLAYER_MOVEMENT_SPEED, "Movement Speed:");
        this.add(PlayerTrackerItem.NBT_PLAYER_MOVEMENT_SPEED_DIR, "B/S");
        this.add(PlayerTrackerItem.NBT_PLAYER_KNOCKBACK_RESISTANCE, "Knockback Resistance:");
        this.add(PlayerTrackerItem.NBT_PLAYER_CRIT, "Critical Strike:");
        this.add(PlayerTrackerItem.NBT_PLAYER_CRIT_DAMAGE, "Critical Damage:");

        this.add(BeyondHorizon.ID + ".attributes.plus.percent", "+%s%% %s");
        this.add(BeyondHorizon.ID + ".attributes.take.percent", "-%s%% %s");
        this.add(BeyondHorizon.ID + ".attributes.plus.0", "+%s %s");
        this.add(BeyondHorizon.ID + ".attributes.plus.1", "+%s%% %s");
        this.add(BeyondHorizon.ID + ".attributes.plus.2", "+%s%% %s");
        this.add(BeyondHorizon.ID + ".attributes.take.0", "-%s %s");
        this.add(BeyondHorizon.ID + ".attributes.take.1", "-%s%% %s");
        this.add(BeyondHorizon.ID + ".attributes.take.2", "-%s%% %s");
        //
        this.addDeathMessage(BHDamageTypes.PHYSICAL_DAMAGE, "%1$s was killed", "%1$s was killed by %2$s", "%1$s was killed by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.MAGIC_DAMAGE, "%1$s was killed by magic", "%1$s was killed by magic whilst trying to escape %2$s", "%1$s was killed by magic whilst trying to escape %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.TRUE_DAMAGE, "%%1$s was consumed", "%1$s was consumed by %2$s", "%1$s was consumed by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.BLEED, "%1$s was bleeding to death", "%1$s was bleeding to death while killed by %2$s", "%1$s was bleeding to death while killed by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.BEAM, "%1$s was evaporated", "%1$s was evaporated by %2$s", "%1$s was evaporated by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.BLAZING_ROD, "%1$s was evaporated", "%1$s was evaporated by %2$s", "%1$s was evaporated by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.PET_DAMAGE_PHYSICAL, "%1$s was killed", "%1$s was evaporated by %2$s", "%1$s was evaporated by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.PET_DAMAGE_MAGIC, "%1$s was killed", "%1$s was killed by %2$s", "%1$s was killed by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.PET_DAMAGE_TRUE_DAMAGE, "%1$s was killed", "%1$s was killed by %2$s", "%1$s was killed by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.MAGIC_BURNING, "%1$s was burned", "%1$s was burned to death by %2$s", "%1$s was consumed by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.TRUE_DAMAGE_BURNING, "%1$s was burned", "%1$s was burned to death by %2$s", "%1$s was consumed by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.IGNORE_ENCHANTMENT_PROTECTION, "%1$s was thought protection gonna save it", "%1$s was killed by %2$s", "%1$s was consumed by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.LETHALITY, "%1$s was thought protection gonna save it", "%1$s was killed by %2$s", "%1$s was consumed by %2$s using %3$s");
        this.addDeathMessage(BHDamageTypes.MAGIC_PENETRATION, "%1$s was thought protection gonna save it", "%1$s was killed by %2$s", "%1$s was consumed by %2$s using %3$s");
        //
        this.addGuideBookIndexes(GuideBookScreen.Pages.INTRODUCTION, "Introduction");
        this.addGuideBookIndexes(GuideBookScreen.Pages.DAMAGE_TYPES, "Damage Types");
        this.addGuideBookIndexes(GuideBookScreen.Pages.STATS, "Stats");
        this.addGuideBookIndexes(GuideBookScreen.Pages.GAME_MECHANICS, "Mechanics");
        this.addGuideBookIndexes(GuideBookScreen.Pages.ACCESSORY, "Accessory");
        this.addGuideBookIndexes(GuideBookScreen.Pages.LEVEL_SYSTEM, "Level System");
        this.addGuideBookIndexes(GuideBookScreen.Pages.DIFFICULTY, "Difficulty");
        this.addGuideBookIndexes(GuideBookScreen.Pages.EFFECT_TYPES, "Effect Types");
    }
    private void addEnchantmentDesc(Supplier<? extends Enchantment> enchantments, String description) {
        this.add(enchantments.get().getDescriptionId() + ".desc", description);
    }
    private void addArmorBonusSet(ArmorBonusSet registry, int lines, String description) {
        String name = String.format("%s.%s.%s.desc.%s", ArmorBonusSet.PREFIX,registry.getId().getNamespace(), registry.getId().getPath(), lines);
        this.add(name, description);
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
    private void addGuideBookIndexes(GuideBookScreen.Pages pages, String name) {
        this.add("guidebooks." + pages.toString().toLowerCase(), name);
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
    private void addDeathMessage(ResourceKey<DamageType> damageTypes, String name, String player, String item) {
        String path = damageTypes.location().getPath();
        this.add(String.format("death.attack.%s", path), name);
        this.add(String.format("death.attack.%s.player", path), player);
        this.add(String.format("death.attack.%s.item", path), item);
    }
    private void addItemLore(Supplier<? extends Item> item, String... desc) {
        for (int i = 0; i < desc.length; i++) {
            if (i == 0) {
                this.add(Utils.getObjectDescription(item), desc[i]);
            } else {
                this.add(Utils.getObjectDescription(item) + "." + i, desc[i]);
            }

        }
    }
}
