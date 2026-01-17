package com.kenhorizon.beyondhorizon.server.api.bonus_set;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveAbility;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityUtils;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.util.Constant;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ArmorBonusSetMagnitude extends ArmorBonusSet implements IAttack, IEntityProperties {
    private static final UUID WILDFIRE_INCREASED_DAMAGE = UUID.fromString("3a4ae5af-4b46-4c57-9b3b-2ca40be2c89a");
    private float magnitude;
    private float level;

    public ArmorBonusSetMagnitude(float magnitude, float level, String id, int tooltipLines, ItemStack head, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        super(id, tooltipLines, head, chestplate, leggings, boots);
        this.magnitude = magnitude;
        this.level = level;
    }
    public ArmorBonusSetMagnitude(float magnitude, String id, int tooltipLines, ItemStack head, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this(magnitude, 1.0F, id, tooltipLines, head, chestplate, leggings, boots);
    }

    public ArmorBonusSetMagnitude(String id, int tooltipLines, ItemStack head, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this(0.0F, 1.0F, id, tooltipLines, head, chestplate, leggings, boots);
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public float getLevel() {
        return level;
    }

    @Override
    public void addTooltips(List<Component> tooltips, ItemStack itemStack, Player player) {
        super.addTooltips(tooltips, itemStack, player);
        for (int i = 0; i < this.tooltipLines(); i++) {
            String desc = String.format("%s.%s.%s.desc.%s", ArmorBonusSet.PREFIX, this.getId().getNamespace(), this.getId().getPath(), i);
            if (this.getMagnitude() > 0.0F) {
                tooltips.add(this.space().append(Component.translatable(desc, Maths.format(this.getMagnitude() * 100.0F), Maths.format(this.getLevel() * 100.0F)).withStyle(ChatFormatting.DARK_GREEN)));
            } else {
                tooltips.add(this.space().append(Component.translatable(desc).withStyle(ChatFormatting.GRAY)));
            }
        }
    }

    @Override
    public void applyBonus(Player player) {
        super.applyBonus(player);
        if (this == ArmorBonusSets.WILDFIRE_ARMOR_SET) {
            AttributeInstance instance = player.getAttribute(BHAttributes.DAMAGE_DEALT.get());
            if (instance != null && instance.getModifier(WILDFIRE_INCREASED_DAMAGE) == null) {
                instance.addPermanentModifier(new AttributeModifier(WILDFIRE_INCREASED_DAMAGE, "Wildfire Bonuses", this.getMagnitude(), AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
        }
    }

    @Override
    public void removeBonus(Player player) {
        super.removeBonus(player);
        if (this == ArmorBonusSets.WILDFIRE_ARMOR_SET) {
            AttributeInstance instance = player.getAttribute(BHAttributes.DAMAGE_DEALT.get());
            if (instance != null) instance.removeModifier(WILDFIRE_INCREASED_DAMAGE);
        }
    }

    @Override
    public float damageTaken(float damageDealt, DamageSource source, LivingEntity entity) {
        if (entity == null) return damageDealt;
        if (this == ArmorBonusSets.WILDFIRE_ARMOR_SET) {
            float totalDamage = (float) EntityUtils.getAttackDamage(entity);
            float dealDamage = this.getLevel() + (totalDamage * this.getMagnitude());
            CleaveAbility.spawn(entity.level(), entity, entity, dealDamage, 8.0F, CleaveAbility.Type.CIRCLE);
        }
        return damageDealt;
    }


    @Override
    public void onHitAttack(DamageSource damageSource, ItemStack itemStack, LivingEntity target, LivingEntity attacker, float damageDealt) {
        if (this == ArmorBonusSets.WILDFIRE_ARMOR_SET) {
            target.setSecondsOnFire(Constant.FIRE_EFFECT);
        }
    }

    @Override
    public Optional<IAttack> attack() {
        return Optional.of(this);
    }

    @Override
    public Optional<IEntityProperties> entityProperties() {
        return Optional.of(this);
    }
}
