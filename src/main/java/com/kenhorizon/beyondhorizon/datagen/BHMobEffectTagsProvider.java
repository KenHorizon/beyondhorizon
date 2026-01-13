package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHEffects;
import com.kenhorizon.beyondhorizon.server.tags.BHEffectTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BHMobEffectTagsProvider extends IntrinsicHolderTagsProvider<MobEffect> {
    public BHMobEffectTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registry, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, Registries.MOB_EFFECT, registry, (mobEffect) ->
                        RegistryManager.ACTIVE.getRegistry(Registries.MOB_EFFECT).getResourceKey(mobEffect).orElseThrow()
                , BeyondHorizon.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BHEffectTags.BRUTE_RESISTANCE_IMMUNE_TO)
                .add(
                        MobEffects.WEAKNESS
                );
        this.tag(BHEffectTags.WEIGHT_IMMUNE_TO)
                .add(
                        MobEffects.LEVITATION
                );
        this.tag(BHEffectTags.BODY_POISON_IMMUNE_TO)
                .add(
                        MobEffects.POISON,
                        BHEffects.LETHAL_POISON.get()
                );
        this.tag(BHEffectTags.TOUGH_SKIN_IMMUNE_TO)
                .add(
                        BHEffects.BLEED.get()
                );
        this.tag(BHEffectTags.VITAMINS_IMMUNE_TO)
                .add(
                        MobEffects.HUNGER
                );
        this.tag(BHEffectTags.HAUNTING_GUISE_IMMUNE_TO)
                .add(
                        MobEffects.CONFUSION
                );
        this.tag(BHEffectTags.OWL_SIGHT_IMMUNE_TO)
                .add(
                        MobEffects.BLINDNESS
                );
        this.tag(BHEffectTags.LIGHTNESS_IMMUNE_TO)
                .add(
                        MobEffects.WITHER
                );
        this.tag(BHEffectTags.HEMORRHAGE_CONTROL_IMMUNE_TO)
                .add(
                        BHEffects.BLEED.get()
                );
        this.tag(BHEffectTags.INVULNERABLE_IMMUNE_TO)
                .add(
                        BHEffects.VULNERABLE.get()
                );
        this.tag(BHEffectTags.UNBOTHERED_IMMUNE_TO)
                .add(
                        MobEffects.CONFUSION
                );
        this.tag(BHEffectTags.PRESERVED_IMMUNE_TO)
                .add(
                        MobEffects.WITHER
                );
        this.tag(BHEffectTags.SEEK_ONE_SEEK_TWICE_IMMUNE_TO)
                .add(
                        MobEffects.BLINDNESS
                );
        this.tag(BHEffectTags.DIVINE_ANKH_IMMUNE_TO)
                .add(
                        MobEffects.HUNGER,
                        MobEffects.DIG_SLOWDOWN,
                        MobEffects.UNLUCK
                )
                .addTag(BHEffectTags.LIGHTNESS_IMMUNE_TO)
                .addTag(BHEffectTags.OWL_SIGHT_IMMUNE_TO)
                .addTag(BHEffectTags.BODY_POISON_IMMUNE_TO)
                .addTag(BHEffectTags.WEIGHT_IMMUNE_TO)
                .addTag(BHEffectTags.BRUTE_RESISTANCE_IMMUNE_TO)
                .addTag(BHEffectTags.HAUNTING_GUISE_IMMUNE_TO);
    }

    @Override
    public @NotNull String getName() {
        return "Mob Effect Tags";
    }
}