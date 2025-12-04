package com.kenhorizon.beyondhorizon.client.level.guis.hud;

import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class HudInfo {
    public float health;
    public float maxHealth;
    public float absorption;
    public float maxAbsorption;
    public float armor;
    public boolean hasAbsroption;
    public int scaledWindowWidth;
    public int scaledWindowHeight;

    public void update() {
        Minecraft minecraft = Minecraft.getInstance();
        //noinspection ConstantConditions -- can be null
        if (minecraft == null) return;
        Player player = minecraft.player;
        if (player == null) return;
        this.scaledWindowWidth = minecraft.getWindow().getGuiScaledWidth();
        this.scaledWindowHeight = minecraft.getWindow().getGuiScaledHeight();
        this.health = player.getHealth();
        this.maxHealth = player.getMaxHealth();
        this.absorption = player.getAbsorptionAmount();
        this.hasAbsroption = this.absorption > 0.0F;
        this.armor = player.getArmorValue();
        if (player.hasEffect(MobEffects.ABSORPTION)) {
            int absorptionLevel = player.getEffect(MobEffects.ABSORPTION).getAmplifier();
            this.maxAbsorption = (int) Math.min(255, ((absorptionLevel + 1) * 4));
        }
    }
}
