package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.google.common.collect.ImmutableList;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.tags.BHEffectTags;
import com.kenhorizon.beyondhorizon.server.util.EffectUtil;
import com.kenhorizon.libs.registry.RegistryHelper;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImmunityEffectAccessory extends AccessorySkill implements IReloadable {
    private boolean removeEffectOnTick = false;
    private boolean cancelDamageOfEffect = false;
    private List<MobEffect> mobEffectList = ImmutableList.of();
    private TagKey<MobEffect> mobEffectTag = null;

    public ImmunityEffectAccessory(MobEffect... mobEffect) {
        ImmutableList.Builder<MobEffect> map = ImmutableList.builder();
        map.addAll(Arrays.asList(mobEffect));
        this.mobEffectList = map.build();
        ReloadableHandler.addToReloadList(this);
    }

    public ImmunityEffectAccessory(TagKey<MobEffect> mobEffectTags) {
        this.mobEffectTag = mobEffectTags;
        ReloadableHandler.addToReloadList(this);
    }

    @Override
    public void reload() {
        ImmutableList.Builder<MobEffect> map = ImmutableList.builder();
        map.addAll(ForgeRegistries.MOB_EFFECTS);
        this.mobEffectList = map.build();
    }

    @Override
    protected void addTooltipTitle(ItemStack itemStack, List<Component> tooltip, boolean firstType) {
        if (!this.getMobEffects().isEmpty()) {
            for (MobEffect instance : this.getMobEffects()) {
                if (EffectUtil.is(instance, this.mobEffectTag)) {
                    tooltip.add(this.spacing().append(Component.translatable(Tooltips.TOOLTIP_IMMUNE_TO, instance.getDisplayName().getString()).withStyle(ChatFormatting.GRAY)));
                }
            }
        }
    }

    public ImmunityEffectAccessory removeOnTick() {
        this.removeEffectOnTick = true;
        return this;
    }
    public ImmunityEffectAccessory cancelDamage() {
        this.cancelDamageOfEffect = true;
        return this;
    }

    public final boolean isRemoveOnTick() {
        return this.removeEffectOnTick;
    }

    public final boolean isCancelDamageOfEffect() {
        return this.removeEffectOnTick;
    }


    public List<MobEffect> getMobEffects() {
        return mobEffectList;
    }

    public MobEffect getMobEffect() {
        for (MobEffect effect : this.getMobEffects()) {
            return effect;
        }
        return null;
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        MobEffect effect = null;
        for (MobEffectInstance instance : entity.getActiveEffects()) {
            if (!entity.level().isClientSide()) {
                if (instance != null) {
                    boolean flag = EffectUtil.is(instance.getEffect(), this.mobEffectTag);
                    if (this.isRemoveOnTick() && flag) {
                        BeyondHorizon.LOGGER.debug("[Accessory/Divine Ankh] Remove Effects {}", instance.getEffect().getDisplayName());
                        effect = instance.getEffect();
                    }
                }
            }
        }
        if (effect != null) {
            entity.removeEffect(effect);
        }
    }

    @Override
    public boolean canEntiyReceiveDamage(Player player, LivingEntity target, DamageSource source) {
        if (this instanceof ImmunityEffectAccessory && this.isCancelDamageOfEffect() && source.is(DamageTypes.MAGIC)) {
            return player.hasEffect(this.getMobEffect());
        }
        return super.canEntiyReceiveDamage(player, target, source);
    }
}
