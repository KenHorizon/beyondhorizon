package com.kenhorizon.beyondhorizon.server.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.accessory.Accessories;
import com.kenhorizon.beyondhorizon.server.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.init.BHCreativeTabs;
import com.kenhorizon.beyondhorizon.server.skills.Skill;
import com.kenhorizon.beyondhorizon.server.skills.Skills;
import com.kenhorizon.beyondhorizon.server.util.Tooltips;
import com.kenhorizon.libs.registry.RegistryLanguage;
import net.minecraft.data.PackOutput;
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

        this.add(Tooltips.SKILL_TYPE, "%s");
        this.addSkills(Skills.RUINED_BLADE.get(), "Ruined Blade", "Deal additional +%s%% target's Current HP");
        this.addSkills(Skills.BLADE_EDGE.get(), "Blade Edge", "Deal additional +%s%% target's Max HP");
        //
        //
        creativeTabs(BHCreativeTabs.BH_INGREDIENTS, "Beyond Horizon: Ingredients");
        creativeTabs(BHCreativeTabs.BH_TOOLS, "Beyond Horizon: Tools");
        creativeTabs(BHCreativeTabs.BH_WEAPONS, "Beyond Horizon: Weapons");
        creativeTabs(BHCreativeTabs.BH_BLOCKS, "Beyond Horizon: Blocks");
        creativeTabs(BHCreativeTabs.BH_SPAWN_EGG, "Beyond Horizon: Spawn Egg");
        creativeTabs(BHCreativeTabs.BH_DEBUG_ITEMS, "Beyond Horizon: Debug Items");
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
