package com.kenhorizon.beyondhorizon.server.api.accessory.ability;

import com.kenhorizon.beyondhorizon.server.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.accessory.AccessoryPassiveSkill;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.util.DamageContext;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WoundedAccessory extends AccessoryPassiveSkill {
    private DamageType damageType;
    private TagKey<DamageType> tags;
    public WoundedAccessory(int seconds, TagKey<DamageType> tags, DamageType damageSource) {
        super(seconds);
        this.damageType = damageSource;
        this.tags = tags;
    }
    public WoundedAccessory(int seconds, TagKey<DamageType> tags) {
        this(seconds, tags, null);
    }
    public WoundedAccessory(int seconds, DamageType damageSource) {
        this(seconds, null, damageSource);
    }

    @Override
    protected MutableComponent makeTooltip(ItemStack itemStack) {
        return Component.translatable(this.createId(), Maths.format(this.getMagnitude()));
    }

    @Override
    public void onHitAttack(DamageSource source, ItemStack itemStack, LivingEntity target, LivingEntity attacker, DamageContext context) {
        if (target == null || attacker == null) return;
        if (attacker instanceof Player player) {
            if (this.tags != null && source.is(this.tags)) {
                BeyondHorizon.LOGGER.info("Inflict Effect a");
                this.inflictWounded(target);
            }
            if (this.damageType != null && this.damageType == source.type()) {
                BeyondHorizon.LOGGER.info("Inflict Effect b");
                this.inflictWounded(target);
            }
        }
    }

    public void inflictWounded(LivingEntity target) {
        BeyondHorizon.LOGGER.info("Inflict Effect");
        target.addEffect(new MobEffectInstance(BHEffects.WOUNDED.get(), Maths.sec(this.getMagnitude())));
    }
}
