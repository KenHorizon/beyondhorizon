package com.kenhorizon.beyondhorizon.mixins.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixins {
    @SuppressWarnings({"ConstantConditions"})
    @Inject(method = "evaluateWhichHandsToRender", at = @At(value = "HEAD"), cancellable = true)
    private static void modifiedRendering(LocalPlayer player, CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        boolean bowItemStack = mainHand.getItem() instanceof BowItem || offHand.getItem() instanceof BowItem;
        boolean crossbowItemStack = mainHand.getItem() instanceof CrossbowItem || offHand.getItem() instanceof CrossbowItem;
        if (!bowItemStack && !crossbowItemStack) {
            cir.setReturnValue(ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS);
        } else if (player.isUsingItem()) {
            cir.setReturnValue(customModifiedSelectionUsingItemWhileHoldingBowLike(player));
        } else {
            cir.setReturnValue(customModifiedIsChargedCrossbow(mainHand) ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS);
        }
    }

    @Unique
    private static ItemInHandRenderer.HandRenderSelection customModifiedSelectionUsingItemWhileHoldingBowLike(LocalPlayer player) {
        ItemStack itemStack = player.getUseItem();
        InteractionHand interactionhand = player.getUsedItemHand();
        if (!(itemStack.getItem() instanceof BowItem) && !(itemStack.getItem() instanceof CrossbowItem)) {
            return interactionhand == InteractionHand.MAIN_HAND && customModifiedIsChargedCrossbow(player.getOffhandItem()) ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        } else {
            return ItemInHandRenderer.HandRenderSelection.onlyForHand(interactionhand);
        }
    }

    @Unique
    private static boolean customModifiedIsChargedCrossbow(ItemStack itemStack) {
        return itemStack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemStack);
    }
}
