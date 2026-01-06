package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.init.BHParticle;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ParticleDescriptionProvider;

public class BHParticleProvider extends ParticleDescriptionProvider {

    /**
     * Creates an instance of the data provider.
     *
     * @param output     the expected root directory the data generator outputs to
     * @param fileHelper the helper used to validate a texture's existence
     */
    public BHParticleProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    protected void addDescriptions() {
        this.sprite(BHParticle.ROAR.get(), register("roar"));
        this.sprite(BHParticle.TRAILS.get(), register("orb"));
        this.sprite(BHParticle.RING.get(), register("ring"));
        this.sprite(BHParticle.STUN_PARTICLES.get(), register("stun_star"));
        this.spriteSet(BHParticle.HELLFIRE_ORB_EXPLOSION.get(), register("hellfire_orb_explosion"), 7, false);
        this.spriteSet(BHParticle.HELLFIRE_ORB_TRAIL.get(), register("hellfire_orb_trail"), 12, false);
        this.spriteSet(BHParticle.BLEED.get(), register("bleed"), 4, false);

    }
    private ResourceLocation register(String particleName) {
        return BeyondHorizon.resource(particleName);
    }
    private ResourceLocation mc(String particleName) {
        return ResourceLocation.parse(particleName);
    }
}
