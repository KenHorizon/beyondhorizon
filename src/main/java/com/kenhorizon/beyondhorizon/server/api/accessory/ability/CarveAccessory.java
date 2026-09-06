package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableInfo;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagInstance;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CarveAccessory extends StackingSkillAccessory {
    public CarveAccessory() {
        super(StackableTagInstance.CARVE);
    }

    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        var def = StackableInfo.get(StackableTagInstance.CARVE);
        return Component.translatable(this.createId(),
                def.getDisplayName(),
                (int) (Maths.tick(def.getMaxDuration())),
                def.getMaxStacks(),
                Maths.format(100.0F * Mth.abs(Constant.CARVE_ARMOR_REMOVE)),
                Maths.format(100.0F * Mth.abs(Constant.CARVE_ARMOR_REMOVE * def.getMaxStacks())),
                def.getMaxStacks());
    }

    @Override
    public void onHitAttack(DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, DamageContext context) {
        if (target == null || attacker == null) return;
        if (attacker instanceof Player player) {
            if (source.is(BHDamageTypeTags.PHYSICAL_DAMAGE)) {
                var stack =  Capabilities.stackable(target);
                if (stack != null) {
                    var instance = stack.makeInstance(this.getStackableTags());
                    instance.add(1);
                }
            }
        }
    }
}
