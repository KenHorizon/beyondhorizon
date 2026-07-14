package com.kenhorizon.beyondhorizon.server.item.base.weapons;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.Utils;
import com.kenhorizon.beyondhorizon.server.api.IAttack;
import com.kenhorizon.beyondhorizon.server.api.ISkillSlots;
import com.kenhorizon.beyondhorizon.server.capability.QuiverItemStackHandler;
import com.kenhorizon.beyondhorizon.server.init.BHCapabilties;
import com.kenhorizon.beyondhorizon.server.item.*;
import com.kenhorizon.beyondhorizon.server.item.base.SkillBaseItems;
import com.kenhorizon.beyondhorizon.server.item.materials.MeleeWeaponMaterials;
import com.kenhorizon.beyondhorizon.server.api.skills.SkillBuilder;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.libs.client.WeaponAnimations;
import com.kenhorizon.libs.client.WeaponArmPose;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
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
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class SwordBaseItem extends SwordItem implements ISkillItems, IReloadable, ILeftClick, ICustomSweepParticle, ICustomHitSound, IArmPose {
    private final float attackDamage;
    private final float attackSpeed;
    private final float attackRange;
    public final MeleeWeaponMaterials materials;
    protected final SkillBuilder skillBuilder;
    public List<Skill> skills = ImmutableList.of();
    public List<Optional<Skill>> activeSkills = ImmutableList.of();
    protected Multimap<Attribute, AttributeModifier> attributeModifiers;
    protected final Multimap<Attribute, AttributeModifier> otherAttributeModifiers = HashMultimap.create();
    protected final SkillBaseItems skillBaseItems;
    private int skillSlots = 0;

    public SwordBaseItem(MeleeWeaponMaterials materials, float attackDamage, float attackSpeed, float attackRange, Properties properties, SkillBuilder skillbuilder) {
        super(materials, 0, attackSpeed, materials.fireImmune() ? properties.fireResistant() : properties);
        this.materials = materials;
        this.skillBuilder = skillbuilder;
        this.attackDamage = materials.getAttackDamageBonus() + attackDamage - 1.0f;
        this.attackSpeed = attackSpeed - 4.0F;
        this.attackRange = (float) (attackRange - 3.0F);
        this.skillBaseItems = new SkillBaseItems(this);
        ReloadableHandler.addToReloadList(this);
    }

    public SwordBaseItem(MeleeWeaponMaterials materials, float[] stats, Properties properties) {
        this(materials, stats[0], stats[1], stats.length == 2 ? 0 : stats[2], properties, SkillBuilder.NONE);
    }

    public SwordBaseItem(MeleeWeaponMaterials materials, float[] stats, Properties properties, SkillBuilder skillBuilder) {
        this(materials, stats[0], stats[1], stats.length == 2 ? 0 : stats[2], properties, skillBuilder);
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
        this.activeSkills = this.registerAllActiveSkills();
        this.setupDefault();
        this.skillBaseItems.setSkills(this.skills, this.activeSkills);
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
                abilityTraits.entityProperties().ifPresent(callback -> {
                    callback.addAttributes(mapBuilder);
                });
            });
        }
        this.attributeModifiers = mapBuilder.build();
    }

    private ImmutableList<Skill> registerAllSkills() {
        ImmutableList.Builder<Skill> builder = ImmutableList.builder();
        builder.addAll(this.skillBuilder.getSkills());
        builder.addAll(this.materials.getSkills());
        return builder.build();
    }
    private ImmutableList<Optional<Skill>> registerAllActiveSkills() {
        ImmutableList.Builder<Optional<Skill>> builder = ImmutableList.builder();
        builder.addAll(this.skillBuilder.getActionSkills());
        return builder.build();
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 0;
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return super.canDisableShield(stack, shield, entity, attacker);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseDuration) {
        this.skillBaseItems.onUseTick(level, entity, itemStack, remainingUseDuration);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        return super.useOn(useOnContext);
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        int value = this.skillBaseItems.getUseDuration(itemStack);
        return value > 0 ? value : super.getUseDuration(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int timeCharged) {
        this.skillBaseItems.releaseUsing(level, entity, itemStack, timeCharged);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            return this.skillBaseItems.finishUsingItem(level, player, itemStack);
        }
        return super.finishUsingItem(itemStack, level, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        return this.skillBaseItems.use(level, player, itemStack, hand);
    }

    @Override
    public WeaponAnimations getWeaponAnimations(Player player, ItemStack itemStack) {
        return this.skillBaseItems.getWeaponAnimations(player, itemStack);
    }

    @Override
    public WeaponArmPose getWeaponArmPose(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        return this.skillBaseItems.getWeaponPose(player, itemStack);
    }
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean isSelected) {
        this.skillBaseItems.inventoryTick(itemStack, level, entity, slot, isSelected);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        this.skillBaseItems.appendHoverText(itemStack, tooltip);
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
        boolean value = this.skillBaseItems.canApplyAtEnchantingTable(stack, enchantment, this.materials);
        return !value ? super.canApplyAtEnchantingTable(stack, enchantment) : value;
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
    public List<Skill> getSkills() {
        return this.skills;
    }

    @Override
    public List<Optional<Skill>> getActiveSkills() {
        return activeSkills;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemStack, Player player, Entity entity) {
        for (Skill skill : this.skills) {
            Optional<IAttack> properties = skill.attack();
            if (properties.isPresent()) {
                return properties.get().onLeftClickEntity(itemStack, player, entity);
            }
        }
        return super.onLeftClickEntity(itemStack, player, entity);
    }

    @Override
    public boolean preventClickOthers(ItemStack stack, Player player) {
        for (Skill skill : this.skills) {
            Optional<IAttack> properties = skill.attack();
            if (properties.isPresent()) {
                return properties.get().onLeftClickProperties(stack, player);
            }
        }
        return ILeftClick.super.preventClickOthers(stack, player);
    }

    @Override
    public boolean onLeftClick(ItemStack stack, Player player) {
        for (Skill skill : this.skills) {
            Optional<IAttack> properties = skill.attack();
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
