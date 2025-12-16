package com.kenhorizon.beyondhorizon.mixins.server;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerMixins extends LivingEntityMixins  {

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getSweepingDamageRatio(Lnet/minecraft/world/entity/LivingEntity;)F"))
    private float getSweepingDamageRatio(LivingEntity entityIn) {
        AttributeInstance instance = entityIn.getAttribute(BHAttributes.SWEEP_DAMAGE.get());
        float damageRatio;
        if (instance == null) {
            damageRatio = EnchantmentHelper.getSweepingDamageRatio(entityIn);
        } else {
            float damage = (float)entityIn.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float resultDamage = (float) (damage * instance.getValue());
            damageRatio = (float) (((resultDamage - 1.0F) / resultDamage) * instance.getValue());
        }
        return damageRatio;
    }
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSources;playerAttack(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/damagesource/DamageSource;"))
    private DamageSource damagePlayerAttack(DamageSources damageSources, Player playerIn) {
        ItemStack weaponStack = playerIn.getMainHandItem();
        AttributeInstance lethality = playerIn.getAttribute(BHAttributes.LETHALITY.get());
        AttributeInstance magicPenetration = playerIn.getAttribute(BHAttributes.MAGIC_PENETRATION.get());
        AttributeInstance armorPenetration = playerIn.getAttribute(BHAttributes.ARMOR_PENETRATION.get());
        if (lethality != null && lethality.getValue() > 0) {
            return BHDamageTypes.lethality(playerIn);
        }
        if (armorPenetration != null && armorPenetration.getValue() > 0) {
            return BHDamageTypes.armorPenetration(playerIn);
        }
        if (magicPenetration != null && magicPenetration.getValue() > 0) {
            return BHDamageTypes.magicPenetration(playerIn);
        }
        return damageSources.playerAttack(playerIn);
    }
}
