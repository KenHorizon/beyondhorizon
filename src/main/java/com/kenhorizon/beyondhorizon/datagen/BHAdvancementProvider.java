package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.guis.BHAdvancementTab;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.advancements.AccessoryChangeTrigger;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.item.util.IconUtils;
import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class BHAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {


    private String create(String name) {
        return String.format("%s:%s/%s", BeyondHorizon.ID, BeyondHorizon.ID, name);
    }

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        this.addMainStory(saver);
    }

    private void addMainStory(Consumer<Advancement> consumer) {
        Advancement root = Advancement.Builder.advancement().display(
                this.createRoot(IconUtils.generateIcons("textures/misc/icons/discover_arcane.png"), Tooltips.ADVANCEMENT_MAIN_ROOT))
                .addCriterion("discovered", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE))
                .save(consumer, this.create("discover_arcane"));

        Advancement fancyItems = Advancement.Builder.advancement().parent(root).display(
                        this.createTask(IconUtils.generateIcons("textures/misc/icons/accessory_equipped.png"), Tooltips.ADVANCEMENT_EQUIPPED_ACCESSORY))
                .addCriterion("has_accessory_equipped", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BHItemTags.ONLY_ACCESSORY).build()))
                .save(consumer, this.create("accessory_equipped"));

        Advancement healPotions = Advancement.Builder.advancement().parent(root).display(
                this.createTask(new ItemStack(BHItems.HEALING_POTION.get()), Tooltips.ADVANCEMENT_HEALING_POTION))
                .addCriterion("have_healing_potion", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BHItemTags.HEALING_ITEM).build()))
                .save(consumer, this.create("healing_potion"));
    }

    private DisplayInfo makeDisplayInfo(ItemStack icon, String name,
                                        ResourceLocation background,
                                        FrameType type,
                                        boolean showToast,
                                        boolean annouceChat,
                                        boolean isHidden) {
        Component title = Component.translatable(name);
        Component description = Component.translatable(String.format("%s.desc", name));
        return new DisplayInfo(icon, title, description, background, type, showToast, annouceChat, isHidden);
    }

    private DisplayInfo createRoot(ItemStack icon, String name) {
        return this.makeDisplayInfo(icon, name, (ResourceLocation) null, FrameType.TASK, false, false, false);
    }

    private DisplayInfo createTask(ItemStack icon, String name) {
        return this.makeDisplayInfo(icon, name, (ResourceLocation) null, FrameType.TASK, true, true, false);
    }
}
