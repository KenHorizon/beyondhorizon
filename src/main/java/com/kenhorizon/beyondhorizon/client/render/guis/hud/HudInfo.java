package com.kenhorizon.beyondhorizon.client.render.guis.hud;

import com.kenhorizon.beyondhorizon.server.api.level.IAbilityInfo;
import com.kenhorizon.beyondhorizon.server.api.skills.ISkillItems;
import com.kenhorizon.beyondhorizon.server.api.skills.Skill;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
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
    public float mana;
    public float maxMana;
    public boolean hasAbsroption;
    public int scaledWindowWidth;
    public int scaledWindowHeight;
    public int casttime = 0;
    public int maxcasttime = 0;
    public String selectedAbility = "";
    public boolean isHeldingSkillItems = false;


    public void update() {
        Minecraft minecraft = Minecraft.getInstance();
        //noinspection ConstantConditions -- can be null
        if (minecraft == null) return;
        Player player = minecraft.player;
        if (player == null) return;
        ItemStack stacks = player.getMainHandItem();
        PlayerData playerData = Capabilities.data(player);
        if (playerData == null) return;
        this.scaledWindowWidth = minecraft.getWindow().getGuiScaledWidth();
        this.scaledWindowHeight = minecraft.getWindow().getGuiScaledHeight();
        this.health = player.getHealth();
        this.maxHealth = player.getMaxHealth();
        this.absorption = player.getAbsorptionAmount();
        this.mana = (float) playerData.getMana();
        this.maxMana = (float) player.getAttributeValue(BHAttributes.MAX_MANA.get());
        this.hasAbsroption = this.absorption > 0.0F;
        this.armor = player.getArmorValue();
        this.isHeldingSkillItems = !stacks.isEmpty() && this.isSkillItem(stacks);
        if (!stacks.isEmpty()) {
            if (this.isSkillItem(stacks)) {
                this.casttime = this.getCastTime(stacks);
                this.maxcasttime = this.getMaxCastTime(stacks);
                this.selectedAbility = this.getAbilityUsing(stacks);
            }
        }
    }
    private boolean isSkillItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof ISkillItems;
    }

    private String getAbilityUsing(ItemStack stack) {
        if (stack.getItem() instanceof ISkillItems skillItems) {
            var skills =  skillItems.getActionSkils();
            if (skills != null) {
                Component name = Component.translatable(skills.getDescriptionId());
                return name.getString();
            } else {
                return "";
            }
        }
        return "";
    }
    private int getCastTime(ItemStack stack) {
        if (stack.getItem() instanceof ISkillItems skillItems) {
            var skills =  skillItems.getActionSkils();
            if (skills instanceof IAbilityInfo info) {
                return info.getCastTime();
            }
        }
        return 0;
    }
    private int getMaxCastTime(ItemStack stack) {
        if (stack.getItem() instanceof ISkillItems skillItems) {
            var skills =  skillItems.getActionSkils();
            if (skills instanceof IAbilityInfo info) {
                return info.getMaxCastTime();
            }
        }
        return 0;
    }
}
