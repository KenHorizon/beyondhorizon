package com.kenhorizon.beyondhorizon.mixins.common;

import com.kenhorizon.beyondhorizon.server.api.accessory.Accessories;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryHelper;
import com.kenhorizon.beyondhorizon.server.item.base.armor.ArmorBaseItem;
import com.kenhorizon.beyondhorizon.server.tags.BHItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class PiglinAIMixins {
    @Inject(at = @At("RETURN"), method = "isWearingGold", cancellable = true)
    private static void beyondhorizon$isWearingGold(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Player player) {
            if (AccessoryHelper.canNeutralizePiglins(player)) {
                cir.setReturnValue(true);
            }
            for (ItemStack itemStack : player.getArmorSlots()) {
                boolean flag = itemStack.is(BHItemTags.IGNORE_PIGLIN_HOSTILITY);
                cir.setReturnValue(flag);
            }
        }
    }
}
