package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.api.IStackIconOverlay;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagInstance;
import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTags;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SupremacyAccessory extends AccessoryPassiveSkill implements IStackIconOverlay {

    float damageIncreasePerStacks;
    float loseOnDeath;
    public SupremacyAccessory(float stacksOnKill, float damageIncreasePerStacks, float loseOnDeath) {
        this.setMagnitude(stacksOnKill);
        this.damageIncreasePerStacks = damageIncreasePerStacks;
        this.loseOnDeath = loseOnDeath;
    }

    @Override
    protected List<MutableComponent> makeTooltips(ItemStack itemStack) {
        Player player = BeyondHorizon.PROXY.clientPlayer();
        var stackTags = Capabilities.stackable(player);
        int stacks = 0;
        if (stackTags != null) {
            var sTag = stackTags.makeInstance(StackableTagInstance.SAINT_DEMON_CROWN_STACKS);
            if (sTag != null) {
                stacks = sTag.getStack();
            }
        }
        List<MutableComponent> list = new ArrayList<>();
        list.add(Component.translatable(this.createId(0), stacks));
        list.add(Component.translatable(this.createId(1), Maths.format(this.getMagnitude()), Maths.format(100 * this.damageIncreasePerStacks)));
        list.add(Component.translatable(this.createId(2), Maths.format(100 * (this.damageIncreasePerStacks * stacks))).withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable(this.createId(3), Maths.format(100 * this.loseOnDeath)));
        return list;
    }

    @Override
    public float preMigitationDamage(DamageContext context, DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target == null || attacker == null) return context.damage();
        if (attacker instanceof Player player) {
            var stackTags = Capabilities.stackable(player);
            if (stackTags != null) {
                var sTag = stackTags.makeInstance(StackableTagInstance.SAINT_DEMON_CROWN_STACKS);
                return context.multiply(this.damageIncreasePerStacks * sTag.getStack());
            }
        }
        return context.damage();
    }

    @Override
    public void onEntityKilled(DamageSource source, LivingEntity attacker, LivingEntity target) {
        if (target instanceof Animal) return;
        var stackTags = Capabilities.stackable(attacker);
        if (stackTags != null) {
            var sTag = stackTags.makeInstance(StackableTagInstance.SAINT_DEMON_CROWN_STACKS);
            sTag.add(1);
            BeyondHorizon.LOGGER.debug("{}", sTag);
        }
    }

    @Override
    public boolean onEntityDeath(LivingEntity entity, ItemStack itemStack) {
        var stackTags = Capabilities.stackable(entity);
        if (stackTags != null) {
            var sTag = stackTags.makeInstance(StackableTagInstance.SAINT_DEMON_CROWN_STACKS);
            sTag.remove((int) (sTag.getStack() * this.loseOnDeath));
        }
        return super.onEntityDeath(entity, itemStack);
    }

    @Override
    public StackableTags getStacks() {
        return StackableTagInstance.SAINT_DEMON_CROWN_STACKS;
    }
}
