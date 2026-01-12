package com.kenhorizon.beyondhorizon.server.item.base.weapons;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.item.ICustomHitSound;
import com.kenhorizon.beyondhorizon.server.item.ICustomSweepParticle;
import com.kenhorizon.beyondhorizon.server.item.ILeftClick;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class SwordBaseItem extends SwordItem implements ISkillItems<SwordBaseItem>, IReloadable, ILeftClick, ICustomSweepParticle, ICustomHitSound {
    private final float attackDamage;
    private final float attackSpeed;
    private final float attackRange;
    protected List<Skill> skills = ImmutableList.of();
    public final MeleeWeaponMaterials materials;
    protected final SkillBuilder skillBuilder;
    protected Multimap<Attribute, AttributeModifier> attributeModifiers;
    protected final Multimap<Attribute, AttributeModifier> otherAttributeModifiers = HashMultimap.create();

    public SwordBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, float attackRange, Properties properties, SkillBuilder skillbuilder) {
        super(materials, 0, attackSpeed, materials.fireImmune() ? properties.fireResistant() : properties);
        this.materials = materials;
        this.skillBuilder = skillbuilder;
        this.attackDamage = materials.getAttackDamageBonus() + attackDamage - 1.0f;
        this.attackSpeed = attackSpeed - 4.0F;
        this.attackRange = (float) (attackRange - 3.0F);
        ReloadableHandler.addToReloadList(this);
    }

    public SwordBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, float attackRange, Properties properties) {
        this(materials, attackDamage, attackSpeed, attackRange, properties, SkillBuilder.NONE);
    }

    public SwordBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, Properties properties, SkillBuilder skillBuilder) {
        this(materials, attackDamage, attackSpeed, 0, properties, skillBuilder);
    }

    public SwordBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, Properties properties) {
        this(materials, attackDamage, attackSpeed, 0, properties, SkillBuilder.NONE);
    }

    private void setupDefault() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> mapBuilder = ImmutableMultimap.builder();
        mapBuilder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon Modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
        mapBuilder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon Modifier", (double) this.attackSpeed, AttributeModifier.Operation.ADDITION));

        if (this.attackRange > 0) {
            mapBuilder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(UUID.fromString("8604572b-e75f-470d-8b7b-227b3017c83a"), "Weapon Modifier", (double) this.attackRange, AttributeModifier.Operation.ADDITION));
            mapBuilder.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(UUID.fromString("e5cb440a-e41a-44fa-8138-1354e5b7d75b"), "Weapon Modifier", (double) this.attackRange, AttributeModifier.Operation.ADDITION));
        }
        this.otherAttributeModifiers.forEach((attribute, modifier) -> {
            if (attribute != null && modifier != null) {
                mapBuilder.put(attribute, modifier);
            }
        });
        if (this.skills != null) {
            this.skills.forEach((abilityTraits) -> {
                abilityTraits.IEntityProperties().ifPresent(callback -> {
                    callback.addAttributes(mapBuilder);
                });
            });
        }
        this.attributeModifiers = mapBuilder.build();
    }

    public SwordBaseItem addAttribues(Attribute attribute, String uuid, double amount, AttributeModifier.Operation operation) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(uuid), "Attribute Modifier", amount, operation);
        this.otherAttributeModifiers.put(attribute, attributemodifier);
        return this;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void reload() {
        this.skills = this.registerAllSkills();
        if (!this.skills.isEmpty()) {
            BeyondHorizon.LOGGER.debug("Setting up the skills {}", this.skills);
        }
        this.setupDefault();
    }

    private ImmutableList<Skill> registerAllSkills() {
        ImmutableList.Builder<Skill> builder = ImmutableList.builder();
        builder.addAll(this.skillBuilder.getSkills());
        builder.addAll(this.materials.getSkills());
        return builder.build();
    }

//    @Override
//    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
//        return this.materials == MeleeWeaponMaterials.WOOD ? 200 : 0;
//    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return super.canDisableShield(stack, shield, entity, attacker);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean isSelected) {
        if (entity instanceof LivingEntity living) {
            if (this.skills != null) {
                this.skills.forEach((skill) -> {
                    skill.IEntityProperties().ifPresent(callback -> {
                        callback.onItemUpdate(itemStack, level, living, slot, isSelected);
                    });
                });
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if (this.skills != null) {
            for (int i = 0; i < this.skills.size(); i++) {
                Skill skill = this.skills.get(i);
                if (!skill.getAttributeModifiers().isEmpty()) {
                    skill.addTooltipAttributes(itemStack, tooltip);
                }
                skill.addTooltip(itemStack, tooltip, this.skills.size(), Utils.isShiftPressed(), i == 0, i == (this.skills.size() - 1));
            }
        }
        super.appendHoverText(itemStack, level, tooltip, isAdvanced);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (this.materials.getUses() < 0) {
            super.setDamage(stack, 0);
        }
        super.setDamage(stack, damage);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        for (Skill skill : this.skills) {
            if (skill.isEnchantmentCompatible(enchantment)) {
                return true;
            } else if (skill.isEnchantmentIncompatible(enchantment)) {
                return false;
            }
        }
        if (enchantment instanceof MendingEnchantment && this.materials.getUses() < 0) {
            return false;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return false;
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity entity) {
        return false;
    }

    @Override
    public SwordBaseItem getItem() {
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
    
    @Override
    public boolean onLeftClickEntity(ItemStack itemStack, Player player, Entity entity) {
        for (Skill skill : this.skills) {
            Optional<IAttack> properties = skill.IAttackCallback();
            if (properties.isPresent()) {
                return properties.get().onLeftClickEntity(itemStack, player, entity);
            }
        }
        return super.onLeftClickEntity(itemStack, player, entity);
    }

    @Override
    public boolean onLeftClick(ItemStack stack, Player player) {
        for (Skill skill : this.skills) {
            Optional<IAttack> properties = skill.IAttackCallback();
            if (properties.isPresent()) {
                properties.get().onLeftClick(stack, player);
                return true;
            }
        }
        return false;
    }

    private boolean isCharged(Player player, ItemStack stack){
        return player.getAttackStrengthScale(0.5F) > 0.9F;
    }

    @Override
    public boolean hitSound(Level level, Player player, LivingEntity entity) {
        return false;
    }

    @Override
    public boolean sweepParticles(Player player) {
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) BeyondHorizon.PROXY.getCustomItemRenderer());
    }
}
