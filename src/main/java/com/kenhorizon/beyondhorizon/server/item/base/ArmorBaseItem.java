package com.kenhorizon.beyondhorizon.server.item.base;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.item.materials.ArmorBaseMaterials;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import net.minecraft.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ArmorBaseItem extends ArmorItem implements IReloadable {
    private Multimap<Attribute, AttributeModifier> attributeModifier;

    public static final EnumMap<Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (uuid) -> {
        uuid.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        uuid.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        uuid.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        uuid.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });
    public static final UUID HEAD = UUID.fromString("4a852732-5d02-4958-9d81-a6ed4231b6ed");
    public static final UUID CHEST = UUID.fromString("94904749-748e-4c3f-ba0b-dfec35a3fdb5");
    public static final UUID LEGS = UUID.fromString("ddca52b3-c6e9-4893-839e-0cd9bdbe9212");
    public static final UUID FEET = UUID.fromString("3884fe77-dcfd-4b35-9dfe-bcafe8a0aaf2");

    public ArmorBaseMaterials materials;
    public ArmorBaseItem(ArmorBaseMaterials material, Type armorType, Properties properties) {
        super(material, armorType, properties);
        this.materials = material;
        ReloadableHandler.addToReloadList(this);
    }

    @Override
    public void reload() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(type);
        this.materials.getAttributes(this.type).forEach((entry, value) -> {
            builder.put(entry.get(), new AttributeModifier(uuid, "Armor Attribute Modifier", value, AttributeModifier.Operation.ADDITION));
        });
        this.attributeModifier = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == this.type.getSlot() ? this.attributeModifier : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) BeyondHorizon.PROXY.getCustomArmorRenderer());
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int isUpperOrLower = (slot == EquipmentSlot.LEGS) ? 2 : 1;
        return String.format("%s:textures/models/armor/%s_layer_%s.png", BeyondHorizon.ID, this.materials.get(), isUpperOrLower);
    }
}
