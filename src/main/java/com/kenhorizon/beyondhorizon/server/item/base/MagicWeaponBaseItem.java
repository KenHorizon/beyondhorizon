package com.kenhorizon.beyondhorizon.server.item.base;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.item.materials.MagicWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class MagicWeaponBaseItem extends TieredItem implements ISkillItems<MagicWeaponBaseItem>, IReloadable {
    private final float attackDamage;
    private final float attackSpeed;
    private final float attackRange;
    private final MagicWeaponMaterials materials;
    private final SkillBuilder skillBuilder;
    protected List<Skill> skills = ImmutableList.of();
    protected Multimap<Attribute, AttributeModifier> attributeModifiers;

    public MagicWeaponBaseItem(MagicWeaponMaterials tier, float attackDamage, float attackSpeed, float attackRange, Properties properties, SkillBuilder skillBuilder) {
        super(tier, properties);
        this.materials = tier;
        this.skillBuilder = skillBuilder;
        this.attackDamage = materials.getAttackDamageBonus() + (attackDamage == 0 ? attackDamage : attackDamage - 1.0F);
        this.attackSpeed = attackSpeed - 4.0F;
        this.attackRange = (float) (attackRange - 3.0F);
        ReloadableHandler.addToReloadList(this);
    }

    @Override
    public void reload() {
        this.skills = this.registerSkills();
        BeyondHorizon.LOGGER.debug("Setting up the skills {}", this.skills);
        this.setupDefault();
    }

    private ImmutableList<Skill> registerSkills() {
        ImmutableList.Builder<Skill> builder = ImmutableList.builder();
        builder.addAll(this.skillBuilder.getSkills());
        builder.addAll(this.materials.getSkills());
        return builder.build();
    }

    private void setupDefault() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> mapBuilder = ImmutableMultimap.builder();
        mapBuilder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon Modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
        mapBuilder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon Modifier", (double) this.attackSpeed, AttributeModifier.Operation.ADDITION));
        if (this.attackRange > 0) {
            mapBuilder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(UUID.fromString("8604572b-e75f-470d-8b7b-227b3017c83a"), "Weapon Modifier", (double) this.attackRange, AttributeModifier.Operation.ADDITION));
        }
        if (this.skills != null) {
            this.skills.forEach((abilityTraits) -> {
                abilityTraits.IEntityProperties().ifPresent(callback -> {
                    callback.addAttributes(mapBuilder);
                });
            });
        }
        this.attributeModifiers = mapBuilder.build();
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public MagicWeaponMaterials getMaterials() {
        return materials;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public float getAttackRange() {
        return attackRange;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (this.materials.getUses() < 0) {
            super.setDamage(stack, 0);
        }
        if (stack.getDamageValue() > 0) {
            super.setDamage(stack, 0);
        } else {
            super.setDamage(stack, damage);
        }
    }

    @Override
    public MagicWeaponBaseItem getItem() {
        return this;
    }

    @Override
    public boolean hasSkill(Skill skill) {
        return this.skills.contains(skill);
    }

    @Override
    public Skill getFirstSkill(Skill skill) {
        for (Skill skills : this.skills) {
            if (skills == skill) {
                return skills;
            }
        }
        return null;
    }

    @Override
    public List<Skill> getSkillOf(Skill skill) {
        if (this.skills.isEmpty()) return ImmutableList.of();
        return this.skills.stream().filter((_skill) ->
                _skill == skill
        ).toList();
    }

    @Override
    public int skillPresent() {
        return this.skills.size();
    }

    @Override
    public List<Skill> getSkills() {
        return this.skills;
    }
}
