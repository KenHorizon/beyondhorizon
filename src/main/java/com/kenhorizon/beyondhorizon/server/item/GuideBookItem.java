package com.kenhorizon.beyondhorizon.server.item;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.guis.guide_book.GuideBookScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuideBookItem extends BasicItem {

    public GuideBookItem(Properties properties) {
        super(properties.stacksTo(1));
    }


    @Override
    public void onCraftedBy(ItemStack stack, @NotNull Level worldIn, @NotNull Player playerIn) {
        stack.setTag(new CompoundTag());
        stack.getTag().putIntArray("Pages", new int[]{0});
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            BeyondHorizon.PROXY.openScreen(new GuideBookScreen(itemStack));
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, tooltip, flag);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean selected) {
        if (itemStack.getTag() == null) {
            itemStack.setTag(new CompoundTag());
            itemStack.getTag().putIntArray("Pages", new int[]{
                    GuideBookScreen.Pages.INTRODUCTION.ordinal(),
                    GuideBookScreen.Pages.DAMAGE_TYPES.ordinal(),
                    GuideBookScreen.Pages.EFFECT_TYPES.ordinal(),
                    GuideBookScreen.Pages.GAME_MECHANICS.ordinal(),
                    GuideBookScreen.Pages.LEVEL_SYSTEM.ordinal(),
                    GuideBookScreen.Pages.DIFFICULTY.ordinal(),
                    GuideBookScreen.Pages.STATS.ordinal()
            });
        }
    }
}
