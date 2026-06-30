package com.kenhorizon.beyondhorizon.server.entity.projectiles;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

public class ExtendedThrownWeapon extends ExtendedProjectile implements IEntityAdditionalSpawnData {
    protected static final EntityDataAccessor<Byte> LOYALTY = SynchedEntityData.defineId(ExtendedThrownWeapon.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Boolean> CAN_PIERCE = SynchedEntityData.defineId(ExtendedThrownWeapon.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> FOIL = SynchedEntityData.defineId(ExtendedThrownWeapon.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<ItemStack> WEAPON_ID = SynchedEntityData.defineId(ExtendedThrownWeapon.class, EntityDataSerializers.ITEM_STACK);
    protected boolean dealtDamage;
    private ItemStack itemStack;
    private DamageSource damageSource;
    protected int ticksInAir;
    protected boolean isReturning = false;
    public int clientSideReturnTridentTickCount;
    private boolean ignoreImmunityFrame;


    public ExtendedThrownWeapon(EntityType<? extends ExtendedThrownWeapon> entityType, Level level) {
        super(entityType, level);
    }

    public ExtendedThrownWeapon(EntityType<? extends ExtendedThrownWeapon> entityType, Level level, LivingEntity shooter, double x, double y, double z, ItemStack itemStack) {
        super(entityType, level);
        this.moveTo(x, y, z, shooter.getYRot(), shooter.getXRot());
        this.reapplyPosition();
    }

    public ExtendedThrownWeapon(EntityType<? extends ExtendedThrownWeapon> entityType, Level level, LivingEntity shooter, ItemStack itemStack) {
        this(entityType, level, shooter, shooter.getX(), shooter.getEyeY(), shooter.getX(), itemStack);
        this.setDataWeaponId(itemStack);
        this.setOwner(shooter);
        this.setRot(shooter.getYRot(), shooter.getXRot());
    }

    public ItemStack getItemStack() {
        if (this.level().isClientSide()) {
            return this.entityData.get(WEAPON_ID);
        } else {
            return this.itemStack;
        }
    }

    public void setDataWeaponId(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.entityData.set(WEAPON_ID, itemStack);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WEAPON_ID, ItemStack.EMPTY);
        this.entityData.define(LOYALTY, (byte) 0);
        this.entityData.define(CAN_PIERCE, false);
        this.entityData.define(FOIL, false);
    }

    public void setCanPierce(boolean canPierce) {
        this.entityData.set(CAN_PIERCE, canPierce);
    }

    public boolean canPierce() {
        return this.entityData.get(CAN_PIERCE);
    }

    public void setDamageSource(DamageSource damageSource) {
        this.damageSource = damageSource;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        int loyalty = this.entityData.get(LOYALTY);
        boolean physics = this.isNoPhysics();
        if (loyalty > 0 && (this.dealtDamage || this.isNoPhysics() || this.isReturning()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide()) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            } else {
                if (!isReturning) {
                    setNoPhysics(true);
                    inGround = false;
                    isReturning = true;
                    setNoGravity(true);
                }
                this.setNoPhysics(true);
                Vec3 getEyePos = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + getEyePos.y * 0.015D * (double) loyalty, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }
                double d0 = 0.05D * (double) loyalty;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(getEyePos.normalize().scale(d0)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }
                ++this.clientSideReturnTridentTickCount;
            }
        }
        if(!inGround) ++ticksInAir;
        else if (ticksInAir != 0) ticksInAir = 0;
        super.tick();
    }
    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    protected ItemStack getPickupItem() {
        return this.entityData.get(WEAPON_ID).copy();
    }

    public boolean isFoil() {
        return this.entityData.get(FOIL);
    }

    protected SoundEvent projectileHit() {
        return SoundEvents.TRIDENT_HIT;
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 start, Vec3 end) {
        return super.findHitEntity(start, end);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {

    }

    @Override
    protected boolean canHitEntity(@NotNull Entity entity) {
        return super.canHitEntity(entity) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(entity.getId())) && !this.ignoredEntities.contains(entity.getId());
    }

    public boolean isChanneling() {
        return EnchantmentHelper.hasChanneling(getItemStack());
    }

    protected boolean tryPickup(Player player) {
        return this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }
    protected boolean attemptCatch(Player player) {
        Level level = level();
        if(!level.isClientSide()) {
            boolean canBePickedUp = pickup == PickupType.ALLOWED || pickup == PickupType.CREATIVE_ONLY && player.getAbilities().instabuild;
            if (pickup == PickupType.ALLOWED) {
                boolean pickUpAsNewItem = true;
                ItemStack weapon = getItemStack();
                ItemStack pickUpStack = weapon.copy();
                removeEnchantments(pickUpStack);
                canBePickedUp = player.getInventory().add(pickUpStack);
            }

            if (canBePickedUp) {
                player.take(this, 1);
                discard();
            }

            return canBePickedUp;
        }
        return false;
    }

    public boolean isReturning() {
        return isReturning;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Projectile", 10)) {
            this.itemStack = ItemStack.of(tag.getCompound("Projectile"));
        }
        this.dealtDamage = tag.getBoolean("DamageDealt");
        this.entityData.set(LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.itemStack));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Projectile", this.itemStack.save(new CompoundTag()));
        tag.putBoolean("DamageDealt", this.dealtDamage);
    }

    @Override
    public void tickDespawn() {
        int i = this.entityData.get(LOYALTY);
        if (this.pickup != PickupType.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    protected void removeEnchantments(ItemStack stack) {
        Level level = level();
        if (stack.isEnchanted()) {
            EnchantmentHelper.setEnchantments(ImmutableMap.of(), stack);
            if(!level.isClientSide() && level instanceof ServerLevel slevel) {
                slevel.sendParticles(ParticleTypes.WITCH, this.getX(), this.getY(), this.getZ(), 10, 0.1D, 0.1D, 0.1D, 0.2D);
            }
        }
    }

    public int getTicksInAir() {
        return ticksInAir;
    }

    protected void dropAsItem() {
        ItemStack stack = this.getItemStack();
        this.removeEnchantments(stack);
        this.spawnAtLocation(stack, 0.1F);
    }

    protected boolean canBeCaughtInMidair(Entity shooter, Entity entityHit) {
        return shooter == entityHit && isNoPhysics();
    }

    protected float getWaterInertia() {
        return setWaterInertia() > 0.0F ? setWaterInertia() : 0.6F;
    }

    public float setWaterInertia() {
        return 0.0F;
    }

    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        double x;
        double y;
        double z;
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        setDeltaMovement(x, y, z);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeDouble(getDeltaMovement().x);
        buffer.writeDouble(getDeltaMovement().y);
        buffer.writeDouble(getDeltaMovement().z);
    }
}