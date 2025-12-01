package com.kenhorizon.beyondhorizon.client;

import com.kenhorizon.beyondhorizon.client.level.guis.accessory.AccessorySlotScreen;
import com.kenhorizon.beyondhorizon.server.ServerProxy;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.init.BHMenu;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerBoundAccessoryInventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings({"removal"})
public class ClientProxy extends ServerProxy {
    @Override
    public void serverHandler() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onEntityAttributeModification);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void clientHandler() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

        MenuScreens.register(BHMenu.ACCESSORY_MENU.get(), AccessorySlotScreen::new);

        Raid.RaiderType.create("ILLUSIONER", EntityType.ILLUSIONER, new int[]{0, 0, 1, 2, 2, 3, 4, 5});
    }
    public void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> type : event.getTypes()) {
            event.add(type, BHAttributes.LETHALITY.get());
            event.add(type, BHAttributes.DAMAGE_DEALT.get());
            event.add(type, BHAttributes.DAMAGE_TAKEN.get());
            event.add(type, BHAttributes.MAGIC_RESISTANCE.get());
            event.add(type, BHAttributes.RANGED_DAMAGE.get());
            event.add(type, BHAttributes.ABILITY_POWER.get());
            event.add(type, BHAttributes.EVADE.get());
            event.add(type, BHAttributes.ARMOR_PENETRATION.get());
            event.add(type, BHAttributes.MAGIC_PENETRATION.get());
            event.add(type, BHAttributes.OMNIVAMP.get());
            event.add(type, BHAttributes.PHYSICALVAMP.get());
            event.add(type, BHAttributes.SPELLVAMP.get());
            event.add(type, BHAttributes.HEALING.get());
            event.add(type, BHAttributes.SHIELDING.get());
            event.add(type, BHAttributes.MOVEMENT_EFFICIENCY.get());
            event.add(type, BHAttributes.OXYGEN_BONUS.get());
            event.add(type, BHAttributes.BURNING_TIME.get());
            event.add(type, BHAttributes.FALLDAMAGE_MULTIPLIER.get());
            if (type == EntityType.PLAYER) {
                event.add(type, BHAttributes.STEALTH.get());
                event.add(type, BHAttributes.SNEAKING_SPEED.get());
                event.add(type, BHAttributes.SWEEP_DAMAGE.get());
                event.add(type, BHAttributes.MINING_EFFICIENCY.get());
                event.add(type, BHAttributes.CAST_TIME.get());
                event.add(type, BHAttributes.COOLDOWN.get());
                event.add(type, BHAttributes.CRITICAL_STRIKE.get());
                event.add(type, BHAttributes.CRITICAL_DAMAGE.get());
                event.add(type, BHAttributes.MINING_SPEED.get());
                event.add(type, BHAttributes.MAX_MANA.get());
                event.add(type, BHAttributes.MANA_COST.get());
                event.add(type, BHAttributes.MANA_REGENERATION.get());
                event.add(type, BHAttributes.HEALTH_REGENERATION.get());
            }
        }
    }
    @Override
    public Player clientPlayer() {
        return Minecraft.getInstance().player;
    }
    @Override
    public void syncAccessoryToPlayer(int slot, ItemStack itemStack, ServerPlayer player) {
        NetworkHandler.sendToPlayer(new ServerBoundAccessoryInventoryPacket(slot, player.getId(), itemStack), player);
    }
}
