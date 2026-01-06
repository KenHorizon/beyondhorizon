package com.kenhorizon.beyondhorizon.datagen;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.libs.registry.RegistryItemModels;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BHItemModelProvider extends ItemModelProvider {
    public BHItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BeyondHorizon.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        final RegistryItemModels registryItemModels = new RegistryItemModels(this);
        registryItemModels.registerModels();
    }
}
