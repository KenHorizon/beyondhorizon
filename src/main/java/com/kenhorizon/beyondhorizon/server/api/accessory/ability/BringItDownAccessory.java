package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.api.stackable_tags.StackableTagInstance;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHSounds;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageInfo;
import com.kenhorizon.beyondhorizon.server.level.damagesource.DamageType;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BringItDownAccessory extends StackingSkillAccessory {
    private final float increasedDamage;
    public BringItDownAccessory(float baseDamage, float increasedDamage) {
        super(StackableTagInstance.BRING_IT_DOWN);
        this.setMagnitude(baseDamage);
        this.increasedDamage = increasedDamage;
    }
    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId(), (int) this.getMagnitude(), Maths.format(100.0F * this.increasedDamage));
    }
    @Override
    public void onHitAttack(DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, DamageContext context) {
        if (target == null || attacker == null) return;
        if (attacker instanceof Player player) {
            var stack =  Capabilities.stackable(player);
            if (stack != null) {
                var roleClass = Capabilities.levelSystem(player);
                int xpLevel = roleClass != null ? roleClass.getLevel() : 1;
                float baseDamage = (this.getMagnitude() * (xpLevel + 1));
                var instance = stack.makeInstance(this.getStackableTags());
                instance.add(1);
                if (instance.getStack() == 2) {
                    attacker.level().playSound(null, target.getX(), target.getY(), target.getZ(), BHSounds.HEAVY_ATTACK.get(), SoundSource.MASTER, 1.0F, 1.0F);
                }
                if (instance.isFullyStacked()) {
                    DamageType.PHYSICAL_DAMAGE.onHit(target, attacker, DamageInfo.getMissingHealth(target, new DamageContext(baseDamage), this.increasedDamage));
                    instance.reset();
                }
            }
        }
    }
}
