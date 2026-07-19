package com.kenhorizon.beyondhorizon.server.item;

import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerDataHelper;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class RecoveryPotionItem extends BasicItem {
    public float health;
    public float mana;
    private Consumer<Player> postEffectHealth;
    private Consumer<Player> postEffectMana;

    public RecoveryPotionItem(float heal, float mana, Properties properties) {
        super(properties);
        this.mana = mana;
        this.health = heal;
    }

    public RecoveryPotionItem afterDrinkingHealth(Consumer<Player> consumer) {
        this.postEffectHealth = consumer;
        return this;
    }

    public RecoveryPotionItem afterDrinkingMana(Consumer<Player> consumer) {
        this.postEffectMana = consumer;
        return this;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        Player player = livingEntity instanceof Player ? (Player) livingEntity : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, itemStack);
        }

        if (!level.isClientSide()) {
            if (this.health > 0.0F) {
                livingEntity.heal(this.health);
                if (!player.isCreative()) {
                    if (this.postEffectHealth != null) {
                        this.postEffectHealth.accept(player);
                    } else {
                        player.addEffect(new MobEffectInstance(BHEffects.HEALING_SICKNESS.get(), Maths.mins(1), 0, true, true, true));
                    }
                }
            }
            if (this.mana > 0.0F) {
                PlayerDataHelper.getPlayerData(player).ifPresent(handler -> {
                    handler.addMana(this.mana);
                    if (this.postEffectMana != null) {
                        this.postEffectMana.accept(player);
                    }
                });
            }
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        livingEntity.gameEvent(GameEvent.DRINK);
        return itemStack;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 32;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return (player.hasEffect(BHEffects.HEALING_SICKNESS.get())) ? super.use(level, player, hand) : ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (this.health > 0) {
            tooltip.add(Component.translatable(Tooltips.TOOLTIP_HEALTH_RECOVERY_POTION, (int) this.health).withStyle(Tooltips.TOOLTIP[0]));
        }
        if (this.mana > 0) {
            tooltip.add(Component.translatable(Tooltips.TOOLTIP_MANA_RECOVERY_POTION, (int) this.mana).withStyle(Tooltips.TOOLTIP[0]));
        }
        tooltip.add(CommonComponents.EMPTY);
    }
}