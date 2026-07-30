package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableInfo;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagInstance;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SeethingStrikeAccessory extends StackingSkillAccessory {
    public SeethingStrikeAccessory() {
        super(StackableTagInstance.SEETHING_STRIKE);
    }
    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        var def = StackableInfo.get(StackableTagInstance.CARVE);
        var def1 = StackableInfo.get(StackableTagInstance.PHANTOM);
        return Component.translatable(this.createId(),
                def.getDisplayName(),
                (int) (Maths.tick(def.getMaxDuration())),
                def.getMaxStacks(),
                Maths.format0(Mth.abs(Constant.SEETHING_STRIKE_ATK_SPD)),
                Maths.format0(Mth.abs(Constant.SEETHING_STRIKE_ATK_SPD * def.getMaxStacks())),
                def.getMaxStacks(),
                def1.getDisplayName(),
                def1.getMaxDuration(), def1.getMaxStacks(), def1.getDisplayName(), def1.getDisplayName());
    }

    @Override
    public void onHitAttack(DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        if (target == null || attacker == null) return;
        if (attacker instanceof Player player) {
            var stack =  Capabilities.stackable(player);
            if (stack != null) {
                var instance = stack.makeInstance(this.getStackableTags());
                var phantomStacks = stack.makeInstance(StackableTagInstance.PHANTOM);
                instance.add(1);
                if (instance.isFullyStacked()) {
                    phantomStacks.add(1);
                    if (phantomStacks.isFullyStacked()) {
                        for (int i = 0; i < phantomStacks.getStack(); i++) {
                            target.invulnerableTime = 0;
                            target.hurt(source, 0.01F);
                        }
                    }
                }
            }
        }
    }
}
