package com.kenhorizon.beyondhorizon.datagen;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHDamageTypes;
import com.kenhorizon.beyondhorizon.server.tags.BHDamageTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BHDamageTypesTagProvider extends DamageTypeTagsProvider {
    public BHDamageTypesTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> holder, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, holder, BeyondHorizon.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BHDamageTypeTags.PHYSICAL_DAMAGE).add(DamageTypes.PLAYER_ATTACK, DamageTypes.MOB_ATTACK, DamageTypes.MOB_ATTACK_NO_AGGRO, BHDamageTypes.PHYSICAL_DAMAGE);
        this.tag(BHDamageTypeTags.MAGIC_DAMAGE).add(BHDamageTypes.MAGIC_DAMAGE, DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, BHDamageTypes.BLAZING_ROD);
        this.tag(BHDamageTypeTags.IS_MAGIC_PENETRATION).add(BHDamageTypes.MAGIC_PENETRATION);
        this.tag(BHDamageTypeTags.IS_ARMOR_PENETRATION).add(BHDamageTypes.ARMOR_PENETRATION, BHDamageTypes.LETHALITY, BHDamageTypes.TRUE_DAMAGE);
        this.tag(BHDamageTypeTags.IS_TRUE_DAMAGE).add(BHDamageTypes.TRUE_DAMAGE).add(DamageTypes.INDIRECT_MAGIC);
        this.tag(BHDamageTypeTags.TRUE_DAMAGE).add(BHDamageTypes.TRUE_DAMAGE);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(BHDamageTypes.BLEED, BHDamageTypes.TRUE_DAMAGE);
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(BHDamageTypes.IGNORE_ENCHANTMENT_PROTECTION);
        this.tag(DamageTypeTags.IS_EXPLOSION).add(BHDamageTypes.BLEED, BHDamageTypes.BLAZING_ROD, BHDamageTypes.BEAM);
        this.tag(DamageTypeTags.BYPASSES_COOLDOWN).add(BHDamageTypes.BLAZING_ROD, BHDamageTypes.BEAM);
        this.tag(DamageTypeTags.IS_PROJECTILE).add(BHDamageTypes.BLAZING_ROD);
        this.tag(BHDamageTypeTags.CANT_STORE_DAMAGE).add(BHDamageTypes.BLEED);

    }
}