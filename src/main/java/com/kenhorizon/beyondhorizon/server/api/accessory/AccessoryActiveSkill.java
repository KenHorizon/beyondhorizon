package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.client.render.misc.tooltips.Tooltips;
import com.kenhorizon.beyondhorizon.server.api.entity.player.PlayerData;
import com.kenhorizon.beyondhorizon.server.capability.Capabilities;
import com.kenhorizon.beyondhorizon.server.data.IAttack;
import com.kenhorizon.beyondhorizon.server.data.IEntityProperties;
import com.kenhorizon.beyondhorizon.server.init.BHAttributes;
import com.kenhorizon.beyondhorizon.server.network.NetworkHandler;
import com.kenhorizon.beyondhorizon.server.network.packet.server.ServerboundAcessoryKeyPacket;
import com.kenhorizon.beyondhorizon.server.util.MathUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccessoryActiveSkill extends Accessory implements IEntityProperties, IAttack, IAccessoryEvent {

    protected float keypressInterval = 0;
    protected float prevKeypressInterval = 0;
    protected int manaCost = 0;

    public AccessoryActiveSkill(float magnitude, int level) {
        super(magnitude, level);
    }

    public AccessoryActiveSkill() {
        super(0, 1);
    }

    @Override
    public Optional<IEntityProperties> IEntityProperties() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAttack> IAttackCallback() {
        return Optional.of(this);
    }

    @Override
    public Optional<IAccessoryEvent> IAccessory() {
        return Optional.of(this);
    }

    @Override
    public void onChangePrevAccessorySlot(Player player, ItemStack itemStack) {
        this.prevKeypressInterval = this.keypressInterval;
    }

    @Override
    public void onChangePostAccessorySlot(Player player, ItemStack itemStack) {
        this.keypressInterval = this.prevKeypressInterval;
    }

    @Override
    protected List<MutableComponent> tooltipDescriptionList(ItemStack itemStack) {
        List<MutableComponent> list = new ArrayList<>();
        list.add(Component.translatable(Tooltips.TOOLTIP_MANA_COST, this.manaCost));
        list.add(Component.translatable(this.createId(0), MathUtils.format0(this.getMagnitude())));
        return list;
    }
}
