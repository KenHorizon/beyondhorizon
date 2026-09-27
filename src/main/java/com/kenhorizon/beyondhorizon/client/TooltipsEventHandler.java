package com.kenhorizon.beyondhorizon.client;

import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.AttributeTooltips;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.items.ItemStackNameRarity;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.util.Helpers;
import com.kenhorizon.beyondhorizon.server.api.armor_ability.ArmorAbility;
import com.kenhorizon.beyondhorizon.server.world.level.registry.BHRegistries;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecated")
public class TooltipsEventHandler {

    @SubscribeEvent
    public void addTootipOnItems(ItemTooltipEvent event) {
        final List<Component> additions = new ArrayList<>();
        Player player = event.getEntity();
        List<Component> tooltip = event.getToolTip();
        TooltipFlag flag = event.getFlags();
        boolean isAdvanced = event.getFlags().isAdvanced();
        ItemStack itemStack = event.getItemStack();
        AttributeTooltips attributeTooltips = new AttributeTooltips();
        int lastAttributeLine = 0;
        String prefix = "attribute.modifier";
        Tooltips.getItemLores().forEach((item, lores) -> {
            if (item == null) return;
            if (itemStack.getItem() != item.get()) return;
            tooltip.add(1, Component.translatable(Helpers.getObjectDescription(item)).withStyle(Tooltips.TOOLTIP[1]));
        });
        if (BHConfigs.ATTRIBUTE_TOOLTIP_OVERHAUl) {
            for (int i = 0; i < tooltip.size(); i++) {
                lastAttributeLine = attributeTooltips.getTooltipLine(tooltip, prefix);
            }
            attributeTooltips.makeAttributeTooltip(player, tooltip, itemStack, lastAttributeLine);
            attributeTooltips.makePotionTooltip(itemStack, tooltip, lastAttributeLine);
        }

        attributeTooltips.makeEnchantmentAttributeTooltip(player, tooltip, itemStack);
        for (ArmorAbility set : BHRegistries.ARMOR_ABILITY_KEY.get()) {
            if (set.contains(itemStack)) {
                set.addTooltips(tooltip, itemStack, player, flag);
            }
        }
    }

    @SubscribeEvent
    public void onRenderTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
        if (!stack.isEmpty()) {
            Rarity rarity = stack.getRarity();
            String[] name = Helpers.decompose(rarity.name(), ':');
            if (Objects.equals(name[0], BeyondHorizon.ID)) {
                if (!elements.isEmpty()) {
                    if (elements.get(0).left().isPresent()) {
                        elements.set(0, Either.right(new ItemStackNameRarity(stack)));
                    }
                }
            }
        }
    }
}
