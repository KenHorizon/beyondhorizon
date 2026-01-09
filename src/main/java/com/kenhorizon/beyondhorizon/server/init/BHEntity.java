package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.entity.CameraShake;
import com.kenhorizon.beyondhorizon.server.entity.ability.BlazingInfernoRayAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.CleaveAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.EruptionAbility;
import com.kenhorizon.beyondhorizon.server.entity.ability.FlameStrikeAbility;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.BlazingInferno;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.BlazingSpear;
import com.kenhorizon.beyondhorizon.server.entity.boss.blazing_inferno.InfernoShield;
import com.kenhorizon.beyondhorizon.server.entity.misc.BHFallingBlocks;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.BlazingRod;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.HellfireOrb;
import com.kenhorizon.beyondhorizon.server.entity.projectiles.HellfireRod;
import com.kenhorizon.libs.registry.RegistryEntity;
import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class BHEntity {
    public static final RegistryObject<EntityType<BlazingInferno>> BLAZING_INFERNO = RegistryEntity
            .register("blazing_inferno", BlazingInferno::new)
            .lang("Blazing Inferno")
            .mobCategory(MobCategory.CREATURE)
            .properties(p -> p.sized(0.85F, 2.75F))
            .properties(EntityType.Builder::fireImmune)
            .tag(Tags.EntityTypes.BOSSES)
            .register();

    public static final RegistryObject<EntityType<BlazingSpear>> BLAZING_SPEAR = RegistryEntity
            .<BlazingSpear>register("blazing_spear", BlazingSpear::new)
            .lang("Blazing Spear")
            .mobCategory(MobCategory.MISC)
            .properties(p -> p.sized(0.5F, 0.5F))
            .register();

    public static final RegistryObject<EntityType<EruptionAbility>> ERUPTION = RegistryEntity
            .register("eruption", EruptionAbility::new)
            .lang("Eruption")
            .mobCategory(MobCategory.MISC)
            .properties(p -> p.sized(1.0F, 1.0F))
            .register();

    public static final RegistryObject<EntityType<HellfireRod>> HELLFIRE_ROD = RegistryEntity
            .<HellfireRod>register("hellfire_rod", HellfireRod::new)
            .lang("Hellfire Rod")
            .mobCategory(MobCategory.MISC)
            .properties(p -> p.sized(0.5F, 0.5F))
            .register();

    public static final RegistryObject<EntityType<BlazingRod>> BLAZING_ROD = RegistryEntity
            .<BlazingRod>register("blazing_rod", BlazingRod::new)
            .lang("Blazing Rod")
            .mobCategory(MobCategory.MISC)
            .properties(p -> p.sized(0.5F, 0.5F))
            .register();

    public static final RegistryObject<EntityType<InfernoShield>> INFERNO_SHIELD = RegistryEntity
            .<InfernoShield>register("inferno_shield", InfernoShield::new)
            .lang("Inferno Shield")
            .mobCategory(MobCategory.MISC)
            .properties(p -> p.sized(1.25F, 2.45F))
            .properties(EntityType.Builder::fireImmune)
            .register();

    public static final RegistryObject<EntityType<CameraShake>> CAMERA_SHAKE = RegistryEntity
            .<CameraShake>register("camera_shake", CameraShake::new)
            .lang("Camera Shake")
            .mobCategory(MobCategory.MISC)
            .properties(p -> p.sized(0.0F, 0.0F))
            .register();

    public static final RegistryObject<EntityType<BlazingInfernoRayAbility>> BLAZING_INFERNO_RAY = RegistryEntity
            .<BlazingInfernoRayAbility>register("blazing_inferno_ray", BlazingInfernoRayAbility::new)
            .lang("Blazing Inferno Ray")
            .mobCategory(MobCategory.MISC)
            .properties(p -> p.sized(0.5F, 0.5F))
            .register();

    public static final RegistryObject<EntityType<FlameStrikeAbility>> FLAME_STRIKE = RegistryEntity
            .<FlameStrikeAbility>register("flame_strike", FlameStrikeAbility::new)
            .lang("Flame Strike")
            .mobCategory(MobCategory.MISC)
            .properties(p -> p.sized(0.5F, 0.5F))
            .register();

    public static final RegistryObject<EntityType<HellfireOrb>> HELLFIRE_ORB = RegistryEntity
            .<HellfireOrb>register("hellfire_orb", HellfireOrb::new)
            .lang("Hellfire Orb")
            .mobCategory(MobCategory.MISC)
            .properties(p -> p.sized(0.5F, 0.5F))
            .register();

    public static final RegistryObject<EntityType<BHFallingBlocks>> FALLING_BLOCKS = RegistryEntity
            .<BHFallingBlocks>register("beyondhorizon_falling_block", BHFallingBlocks::new)
            .lang("Falling Block")
            .mobCategory(MobCategory.MISC)
            .properties(p -> p.sized(1.0F, 1.0F))
            .properties(EntityType.Builder::noSummon)
            .register();

    public static final RegistryObject<EntityType<CleaveAbility>> CLEAVE_ABILITY = RegistryEntity
            .register("cleave_ability", CleaveAbility::new)
            .lang("Cleave")
            .mobCategory(MobCategory.MISC)
            .properties(p -> p.sized(0.5F, 0.5F))
            .register();

    public static void register(IEventBus eventBus) {
        RegistryEntries.ENTITY_TYPES.register(eventBus);
    }
}
