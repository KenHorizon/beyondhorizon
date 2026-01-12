package com.kenhorizon.beyondhorizon.client.render.item;

import com.kenhorizon.beyondhorizon.client.model.armor.WildfireArmorModel;
import com.kenhorizon.beyondhorizon.server.item.base.armor.ArmorBaseItem;
import com.kenhorizon.beyondhorizon.server.item.materials.ArmorBaseMaterials;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class BHArmorRenderProperties implements IClientItemExtensions {
    @Override
    public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        if (itemStack.getItem() instanceof ArmorBaseItem armorBaseItem) {
            boolean inner = equipmentSlot == EquipmentSlot.LEGS || equipmentSlot == EquipmentSlot.HEAD;
            ArmorBaseMaterials armorToolsMaterials = armorBaseItem.materials;
            if (armorToolsMaterials == ArmorBaseMaterials.WILDFIRE) {
                return new WildfireArmorModel(inner);
            }
        }

        return original;
    }
}
