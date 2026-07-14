package com.kenhorizon.beyondhorizon.server.entity.misc;

import com.kenhorizon.beyondhorizon.server.entity.ILinkedEntity;
import com.kenhorizon.beyondhorizon.server.init.BHEntity;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundAbilityEffectPacket;
import com.kenhorizon.beyondhorizon.server.util.Maths;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class HealingOrb extends Entity implements ILinkedEntity, TraceableEntity, OwnableEntity {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(HealingOrb.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> ENTITY_ID = SynchedEntityData.defineId(HealingOrb.class, EntityDataSerializers.INT);

    private static final int LIFETIME = Maths.sec(6);
    private static final int ENTITY_SCAN_PERIOD = 20;
    private static final int MAX_FOLLOW_DIST = 8;
    private static final int ORB_GROUPS_PER_AREA = 40;
    private static final double ORB_MERGE_DISTANCE = 0.5D;
    private int age;
    private int health = 5;
    public int value = 0;
    private int count = 1;
    private LivingEntity followingEntity;
    private LivingEntity cachedCaster;
    private Consumer<HealingOrb> healingEffect = null;
    public static final String NBT_OWNER = "owner_uuid";

    public HealingOrb(Level level, double x, double y, double z, LivingEntity owner, Consumer<HealingOrb> healingEffect) {
        this(BHEntity.HEALING_ORB.get(), level);
        this.setPos(x, y, z);
        this.setYRot((float)(this.random.nextDouble() * 360.0D));
        this.setDeltaMovement((this.random.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D);
        this.healingEffect = healingEffect;
        this.setOwnerUUID(owner.getUUID());
    }

    public HealingOrb(EntityType<? extends HealingOrb> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(ENTITY_ID, -1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        UUID uuid;
        if (nbt.hasUUID(NBT_OWNER)) {
            uuid = nbt.getUUID(NBT_OWNER);
        } else {
            String string = nbt.getString(NBT_OWNER);
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), string);
        }
        if (uuid != null) {
            this.setOwnerUUID(uuid);
        }
        this.health = nbt.getShort("Health");
        this.age = nbt.getShort("Age");
        this.value = nbt.getShort("Value");
        this.count = Math.max(nbt.getInt("Count"), 1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        if (this.getOwnerUUID() != null) {
            nbt.putUUID(NBT_OWNER, this.getOwnerUUID());
        }
        nbt.putShort("Health", (short)this.health);
        nbt.putShort("Age", (short)this.age);
        nbt.putShort("Value", (short)this.value);
        nbt.putInt("Count", this.count);
    }

    public void setOwnerUUID(UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        if (this.getOwnerUUID() != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.getOwnerUUID());
            if (entity instanceof LivingEntity) {
                this.cachedCaster = (LivingEntity) entity;
                NetworkHandler.sendAll(new ServerboundAbilityEffectPacket(this, this.cachedCaster), this);
            }
        }
        return this.cachedCaster;
    }

    @Override
    public @Nullable UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).isPresent() ? this.entityData.get(OWNER_UUID).get() : null;
    }

    @Override
    public void link(Entity entity) {
        if (entity instanceof LivingEntity) {
            this.cachedCaster = (LivingEntity) entity;
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        if (this.isEyeInFluid(FluidTags.WATER)) {
            this.setUnderwaterMovement();
        } else if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
        }

        if (this.level().getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
            this.setDeltaMovement((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F), (double)0.2F, (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F));
        }

        if (!this.level().noCollision(this.getBoundingBox())) {
            this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
        }

        if (this.tickCount % ENTITY_SCAN_PERIOD == 1) {
            this.scanForEntities();
        }

        if (this.followingEntity != null && (this.followingEntity.isSpectator() || this.followingEntity.isDeadOrDying())) {
            this.followingEntity = null;
        }

        if (this.followingEntity != null) {
            Vec3 vec3 = new Vec3(this.followingEntity.getX() - this.getX(), this.followingEntity.getY() + (double)this.followingEntity.getEyeHeight() / 2.0D - this.getY(), this.followingEntity.getZ() - this.getZ());
            double d0 = vec3.lengthSqr();
            if (d0 < 7.0D) {
                double d1 = 1.0D - Math.sqrt(d0) / 8.0D;
                this.setDeltaMovement(this.getDeltaMovement().add(vec3.normalize().scale(d1 * d1 * 0.1D)));
            }
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        float f = 0.98F;
        if (this.onGround()) {
            BlockPos pos = getBlockPosBelowThatAffectsMyMovement();
            f = this.level().getBlockState(pos).getFriction(this.level(), pos, this) * 0.98F;
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply((double)f, 0.98D, (double)f));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));
        }

        ++this.age;
        if (this.age >= LIFETIME) {
            this.discard();
        }

    }

    protected BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return this.getOnPos(0.999999F);
    }

    private void scanForEntities() {
        if (this.followingEntity == null || this.followingEntity.distanceToSqr(this) > 7.0D) {
            if (this.getOwner() != null) {
                this.followingEntity = this.getOwner();
            }
        }
    }

    private void setUnderwaterMovement() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x * (double)0.99F, Math.min(vec3.y + (double)5.0E-4F, (double)0.06F), vec3.z * (double)0.99F);
    }

    @Override
    protected void doWaterSplashEffect() {
    }
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.level().isClientSide || this.isRemoved()) return false; //Forge: Fixes MC-53850
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else if (this.level().isClientSide) {
            return true;
        } else {
            this.markHurt();
            this.health = (int)((float)this.health - pAmount);
            if (this.health <= 0) {
                this.discard();
            }

            return true;
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (!this.level().isClientSide()) {
            player.take(this, 1);
            if (this.healingEffect != null) {
                this.healingEffect.accept(this);
            }
            player.heal(this.value);
            --this.count;
            if (this.count == 0) {
                this.discard();
            }
        }
    }

    public static void spawnOrbs(LivingEntity owner, ServerLevel level, Vec3 vec3, int amount, Consumer<HealingOrb> value) {
        while (amount > 0) {
            int i = getExperienceValue(amount);
            amount -= i;
            level.addFreshEntity(new HealingOrb(level, vec3.x(), vec3.y(), vec3.z(), owner, value));
        }
    }

    public int getValue() {
        return this.value;
    }

    public int getIcon() {
        if (this.value >= 2477) {
            return 10;
        } else if (this.value >= 1237) {
            return 9;
        } else if (this.value >= 617) {
            return 8;
        } else if (this.value >= 307) {
            return 7;
        } else if (this.value >= 149) {
            return 6;
        } else if (this.value >= 73) {
            return 5;
        } else if (this.value >= 37) {
            return 4;
        } else if (this.value >= 17) {
            return 3;
        } else if (this.value >= 7) {
            return 2;
        } else {
            return this.value >= 3 ? 1 : 0;
        }
    }

    public static int getExperienceValue(int value) {
        if (value >= 2477) {
            return 2477;
        } else if (value >= 1237) {
            return 1237;
        } else if (value >= 617) {
            return 617;
        } else if (value >= 307) {
            return 307;
        } else if (value >= 149) {
            return 149;
        } else if (value >= 73) {
            return 73;
        } else if (value >= 37) {
            return 37;
        } else if (value >= 17) {
            return 17;
        } else if (value >= 7) {
            return 7;
        } else {
            return value >= 3 ? 3 : 1;
        }
    }

    public boolean isAttackable() {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }
}