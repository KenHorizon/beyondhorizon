package com.kenhorizon.beyondhorizon.client;

import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.AttributeTooltips;
import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import com.kenhorizon.beyondhorizon.server.api.armor_ability.ArmorAbility;
import com.kenhorizon.beyondhorizon.server.registry.BHRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.ArrayList;
import java.util.List;

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
                set.addTooltips(tooltip, itemStack, player);
            }
        }

        if (!FMLLoader.isProduction() && itemStack.hasTag() && event.getFlags().isAdvanced()) {
            // Format NBT debug string
            String nbtStr = itemStack.getTag().toString();
            event.getToolTip().add(Component.literal("NBT: " + ChatFormatting.DARK_GRAY + nbtStr).withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}
