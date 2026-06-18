package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.particle.TrailParticles;
import com.kenhorizon.beyondhorizon.client.particle.world.ParticleTrailOptions;
import com.kenhorizon.beyondhorizon.server.entity.util.EntityData;
import com.kenhorizon.beyondhorizon.server.entity.util.IBHDataEntity;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class StalkerAccessory extends AccessoryActiveSkill {
    protected boolean isInvisible;
    private static final UUID STEALTH_UUID = UUID.fromString("20c13c52-4226-4724-bf7a-b0ce3dbcf00a");
    private static final AttributeModifier STEALTH = new AttributeModifier(STEALTH_UUID, "Bonus stealth", 1.0D, AttributeModifier.Operation.ADDITION);

    public StalkerAccessory() {
        super(ManaCostType.PER_SECOND);
        this.manaCost = 2;
    }
    @Override
    public void onChangePrevAccessorySlot(Player player, ItemStack itemStack) {
        CompoundTag nbt = EntityData.getOrCreateTag(player);
        BeyondHorizon.LOGGER.debug("Item has been taken? {}", itemStack.getItem());
        nbt.remove("stalker_ability");
        player.getAttribute(BHAttributes.STEALTH.get()).removeModifier(STEALTH_UUID);
    }

    @Override
    public void onEntityUpdate(LivingEntity entity, ItemStack itemStack) {
        if (entity instanceof Player player) {
            CompoundTag nbt = EntityData.getOrCreateTag(player);
            if (!nbt.contains("stalker_ability")) {
                nbt.putBoolean("stalker_ability", false);
            }
            boolean flag = nbt.getBoolean("stalker_ability");
            if (!flag) {
                player.getAttribute(BHAttributes.STEALTH.get()).removeModifier(STEALTH_UUID);
            }
            if (this.manaNotEnough(player)) {
                nbt.putBoolean("stalker_ability", false);
                player.setInvisible(false);
                this.isInvisible = false;
                player.getAttribute(BHAttributes.STEALTH.get()).removeModifier(STEALTH_UUID);
            }
        }
    }

    @Override
    public void onActiveAbility(Player player, ItemStack itemStack, int slot) {
        CompoundTag nbt = EntityData.getOrCreateTag(player);
        nbt.putBoolean("stalker_ability", !nbt.getBoolean("stalker_ability"));
        boolean flag = nbt.getBoolean("stalker_ability");
        if (player.level() instanceof ServerLevel sLevel) {
            for(int i = 0; i < 12; i++) {
                sLevel.sendParticles(new ParticleTrailOptions(20, 0, 186, 255, 255, 1.0F,
                        TrailParticles.Behavior.FADE, new Vec3(player.getRandomX(0.50D), player.getY() + player.getBbHeight() / 2, player.getRandomZ(0.50D))
                ),player.getX(), player.getY() + player.getBbHeight() / 2, player.getZ(),1,0,0, 0, 0);
            }
            sLevel.playSound(null, BlockPos.containing(player.position()), SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS);
        }
        this.active = flag;
        player.setInvisible(flag);
        player.getAttribute(BHAttributes.STEALTH.get()).addTransientModifier(STEALTH);
    }
}
