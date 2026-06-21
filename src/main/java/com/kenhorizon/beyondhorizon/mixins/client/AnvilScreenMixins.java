package com.kenhorizon.beyondhorizon.mixins.client;

import com.kenhorizon.beyondhorizon.configs.BHConfigs;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixins {
    @ModifyConstant(method = "renderLabels", constant = @Constant(intValue = 40, ordinal = 0))
    private int anvilPatchFix(int constant) {
        return BHConfigs.ANVIL_COST_CAP == -1 ? Integer.MAX_VALUE : BHConfigs.ANVIL_COST_CAP;
    }
}
