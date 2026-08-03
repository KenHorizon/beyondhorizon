package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.init.BHItems;
import com.kenhorizon.beyondhorizon.server.item.util.IconUtils;
import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;

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
                this.createRoot(IconUtils.create("textures/misc/icons/discover_arcane.png"), Tooltips.ADVANCEMENT_MAIN_ROOT))
                .addCriterion("discovered", inventoryChanged(Blocks.CRAFTING_TABLE))
                .save(consumer, this.create("discover_arcane"));

        Advancement fancyItems = Advancement.Builder.advancement().parent(root).display(
                        this.createTask(IconUtils.create("textures/misc/icons/accessory_equipped.png"), Tooltips.ADVANCEMENT_EQUIPPED_ACCESSORY))
                .addCriterion("has_accessory_equipped", accessoryInvChanged())
                .save(consumer, this.create("accessory_equipped"));

        Advancement healPotions = Advancement.Builder.advancement().parent(root).display(
                this.createTask(new ItemStack(BHItems.HEALING_POTION.get()), Tooltips.ADVANCEMENT_HEALING_POTION))
                .addCriterion("have_healing_potion", inventoryChanged(BHItemTags.HEALING_ITEM))
                .save(consumer, this.create("healing_potion"));

        Advancement newOres0 = Advancement.Builder.advancement().parent(root).display(
                        this.createTask(new ItemStack(BHItems.RAW_BLACK_IRON.get()), Tooltips.ADVANCEMENT_NEW_ORES_0))
                .addCriterion("have_new_ores", inventoryChanged(BHItems.RAW_BLACK_IRON.get()))
                .save(consumer, this.create("new_ores"));

        Advancement newOres1 = Advancement.Builder.advancement().parent(newOres0).display(
                        this.createTask(new ItemStack(BHItems.RAW_HELLSTONE.get()), Tooltips.ADVANCEMENT_NEW_ORES_1))
                .addCriterion("have_new_ores_hellstone", inventoryChanged(BHItems.RAW_HELLSTONE.get()))
                .save(consumer, this.create("new_ores_hellstone"));

        Advancement newOres2 = Advancement.Builder.advancement().parent(newOres1).display(
                        this.createTask(new ItemStack(BHItems.RAW_STARITE.get()), Tooltips.ADVANCEMENT_NEW_ORES_2))
                .addCriterion("have_new_ores_starite", inventoryChanged(BHItems.RAW_STARITE.get()))
                .save(consumer, this.create("new_ores_starite"));

        Advancement newOres3 = Advancement.Builder.advancement().parent(newOres2).display(
                        this.createTask(new ItemStack(BHItems.RAW_LUMINITE.get()), Tooltips.ADVANCEMENT_NEW_ORES_3))
                .addCriterion("have_new_ores_luminite", inventoryChanged(BHItems.RAW_LUMINITE.get()))
                .save(consumer, this.create("new_ores_luminite"));
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

    public InventoryChangeTrigger.TriggerInstance inventoryChanged(ItemLike... itemLike) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(itemLike);
    }

    public InventoryChangeTrigger.TriggerInstance inventoryChanged(ItemLike itemLike) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(itemLike);
    }

    public InventoryChangeTrigger.TriggerInstance inventoryChanged(ItemPredicate itemitemPredicate) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(itemitemPredicate);
    }

    public InventoryChangeTrigger.TriggerInstance inventoryChanged(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }

    public InventoryChangeTrigger.TriggerInstance accessoryInvChanged() {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BHItemTags.ONLY_ACCESSORY).build());
    }
}
