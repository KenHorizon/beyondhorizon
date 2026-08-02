package com.kenhorizon.beyondhorizon.client.render.guis.hud;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.api.level.IAbilityInfo;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.level.utils.AttributeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HudInfo {
    public float health;
    public float maxHealth;
    public float absorption;
    public float armor;
    public double mana;
    public double maxMana;
    public boolean hasAbsroption;
    public int scaledWindowWidth;
    public int scaledWindowHeight;
    public float casttime = 0;
    public float casttimeReduction = 0;
    public String selectedAbility = "";


    public void update() {
        Minecraft mc = Minecraft.getInstance();
        //noinspection ConstantConditions -- can be null
        if (mc == null) return;
        Player player = BeyondHorizon.PROXY.clientPlayer();
        if (player == null) return;
        ItemStack stacks = PlayerData.getHeldingItem(player);
        PlayerData playerData = Capabilities.data(player);
        if (playerData != null) {
            this.mana = playerData.getMana();
            this.maxMana = playerData.getMaxMana();
        }
        this.scaledWindowWidth = mc.getWindow().getGuiScaledWidth();
        this.scaledWindowHeight = mc.getWindow().getGuiScaledHeight();
        this.health = player.getHealth();
        this.maxHealth = player.getMaxHealth();
        this.absorption = player.getAbsorptionAmount();
        this.hasAbsroption = this.absorption > 0.0F;
        this.armor = player.getArmorValue();

        this.casttime = this.getCastTime(player, stacks);
        this.casttimeReduction = (float) AttributeUtils.getValue(player, BHAttributes.COOLDOWN.get());
        this.selectedAbility = this.getAbilityUsing(stacks);
    }

    private String getAbilityUsing(ItemStack stack) {
        if (stack.getItem() instanceof ISkillItems skillItems) {
            var skills =  skillItems.getActiveSkill(stack);
            if (skills.isPresent()) {
                Component name = Component.translatable(skills.get().getDescriptionId());
                return name.getString();
            } else {
                return "";
            }
        }
        return "";
    }
    private float getCastTime(Player player, ItemStack stack) {
        if (stack.getItem() instanceof ISkillItems skillItems) {
            var skills = skillItems.getActiveSkill(stack);
            if (skills.isPresent()) {
                if (skills.get() instanceof IAbilityInfo info) {
                    return info.getCastTimeFactor(player);
                }
            }
        }
        return 0;
    }
}
