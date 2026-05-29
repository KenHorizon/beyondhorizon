package com.kenhorizon.beyondhorizon.server.item;

import com.kenhorizon.beyondhorizon.server.capability.CapabilityCaller;
import com.kenhorizon.beyondhorizon.server.entity.util.IBHDataEntity;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlayerTrackerItem extends BasicItem {
    public static String NBT_PLAYER_HEALTH = "player_stats.health";
    public static String NBT_PLAYER_MANA = "player_stats.mana";
    public static String NBT_PLAYER_ATTACK_DAMAGE = "player_stats.attack_damage";
    public static String NBT_PLAYER_ABILITY_POWER = "player_stats.ability_power";
    public static String NBT_PLAYER_ARMOR = "player_stats.armor";
    public static String NBT_PLAYER_MAGIC_ARMOR = "player_stats.magic_armor";
    public static String NBT_PLAYER_DAMAGE_AMP = "player_stats.damage_dealt";
    public static String NBT_PLAYER_DAMAGE_TAKEN = "player_stats.damage_taken";

    public PlayerTrackerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        Minecraft minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        var info = CapabilityCaller.data(player);
        tooltip.add(Component.translatable(NBT_PLAYER_HEALTH).append(CommonComponents.space())
                .append(Component.literal(String.format("%s/%s", player.getHealth(), player.getMaxHealth())).withStyle(ChatFormatting.GREEN)));
        tooltip.add(Component.translatable(NBT_PLAYER_MANA).append(CommonComponents.space())
                .append(Component.literal(String.format("%s/%s", info.getMana(), info.getMaxMana())).withStyle(ChatFormatting.GREEN)));
        tooltip.add(Component.translatable(NBT_PLAYER_ATTACK_DAMAGE).append(CommonComponents.space())
                .append(Component.literal(String.format("%s", player.getAttributeValue(Attributes.ATTACK_DAMAGE))).withStyle(ChatFormatting.GREEN)));
        tooltip.add(Component.translatable(NBT_PLAYER_ABILITY_POWER).append(CommonComponents.space())
                .append(Component.literal(String.format("%s", player.getAttributeValue(BHAttributes.ABILITY_POWER.get()))).withStyle(ChatFormatting.GREEN)));
        tooltip.add(Component.translatable(NBT_PLAYER_ARMOR).append(CommonComponents.space())
                .append(Component.literal(String.format("%s", player.getAttributeValue(Attributes.ARMOR))).withStyle(ChatFormatting.GREEN)));
        tooltip.add(Component.translatable(NBT_PLAYER_MAGIC_ARMOR).append(CommonComponents.space())
                .append(Component.literal(String.format("%s", player.getAttributeValue(BHAttributes.MAGIC_RESISTANCE.get()))).withStyle(ChatFormatting.GREEN)));
        tooltip.add(Component.translatable(NBT_PLAYER_DAMAGE_AMP).append(CommonComponents.space())
                .append(Component.literal(String.format("%s%%", MathUtils.format(100.0D * player.getAttributeValue(BHAttributes.DAMAGE_DEALT.get())))).withStyle(ChatFormatting.GREEN)));
        tooltip.add(Component.translatable(NBT_PLAYER_DAMAGE_TAKEN).append(CommonComponents.space())
                .append(Component.literal(String.format("%s%%", MathUtils.format(100.0D * player.getAttributeValue(BHAttributes.DAMAGE_TAKEN.get())))).withStyle(ChatFormatting.GREEN)));
    }
}
