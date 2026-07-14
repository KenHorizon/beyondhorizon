package com.kenhorizon.beyondhorizon.mixins.common;

import com.kenhorizon.beyondhorizon.server.api.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.api.accessory.Accessory;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryHelper;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryItem;
import com.kenhorizon.beyondhorizon.server.api.accessory.IAccessoryStackHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixins {

//    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canWalkOnPowderedSnow(Lnet/minecraft/world/entity/LivingEntity;)Z"), method = "canEntityWalkOnPowderSnow")
//    private static boolean beyondhorizoncanEntityWalkOnPowderSnow(ItemStack instance, LivingEntity entity) {
//        if (entity instanceof Player player) {
//            boolean flag = false;
//            if (AccessoryHelper.getInventory(player).resolve().isPresent()) {
//                IAccessoryStackHandler handler = AccessoryHelper.getInventory(player).resolve().get();
//                var stacks = handler.getStacks();
//                for (int i = 0; i < stacks.getSlots(); i++) {
//                    ItemStack itemStacks = stacks.getStackInSlot(i);
//                    if (itemStacks.getItem() instanceof IAccessoryItem items) {
//                        for (Accessory accessory : items.getAccessories()) {
//                            Optional<IEntityProperties> optional = accessory.IEntityProperties();
//                            if (optional.isPresent()) {
//                                flag = optional.get().canWalkOnPoweredSnow();
//                            }
//                        }
//                    }
//                }
//            }
//            return flag;
//        }
//        return false;
//    }
}
