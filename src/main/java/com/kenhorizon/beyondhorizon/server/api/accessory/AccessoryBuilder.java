package com.kenhorizon.beyondhorizon.server.api.accessory;

import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AccessoryBuilder implements IReloadable {
    public static final AccessoryBuilder NONE = new AccessoryBuilder(List.of(Accessories.NONE));

    public static final AccessoryBuilder POWER_CLAW = new AccessoryBuilder(List.of(Accessories.POWER_CLAW));
    public static final AccessoryBuilder THORNMAIL = new AccessoryBuilder(List.of(Accessories.THORNS));
    public static final AccessoryBuilder BLOOD_OF_BERSERKER = new AccessoryBuilder(List.of(Accessories.RAGE));
    public static final AccessoryBuilder GOLDEN_BOOTS = new AccessoryBuilder(List.of(Accessories.BERSERKER_BOOTS, Accessories.IRON_PLATED_BOOTS, Accessories.FIRE_IMMUNITY));
    public static final AccessoryBuilder BASIC_BOOTS = new AccessoryBuilder(List.of(Accessories.BOOTS));
    public static final AccessoryBuilder TOUGH_CLOTH = new AccessoryBuilder(List.of(Accessories.TOUGH_CLOTH));
    public static final AccessoryBuilder VITALITY_STONE = new AccessoryBuilder(List.of(Accessories.VITALITY_STONE));
    public static final AccessoryBuilder CHAIN_VEST = new AccessoryBuilder(List.of(Accessories.CHAIN_VEST));
    public static final AccessoryBuilder LEATHER_AGILITY = new AccessoryBuilder(List.of(Accessories.LEATHER_AGILITY));
    public static final AccessoryBuilder NULL_MAGIC_RUNE = new AccessoryBuilder(List.of(Accessories.NULL_MAGIC_RUNE));
    public static final AccessoryBuilder FIREFLY_FAYE = new AccessoryBuilder(List.of(Accessories.FIREFLY_FAYE));
    public static final AccessoryBuilder SAPPHIRE_CRYSTAL = new AccessoryBuilder(List.of(Accessories.SAPPHIRE_CRYSTAL));
    public static final AccessoryBuilder RUMINATIVE_BEADS = new AccessoryBuilder(List.of(Accessories.RUMINATIVE_BEADS));
    public static final AccessoryBuilder NEGATE_FALL_DAMAGE = new AccessoryBuilder(List.of(Accessories.FEATHER_FEET));
    public static final AccessoryBuilder SPECTRAL_CLOAK = new AccessoryBuilder(List.of(Accessories.SPECTRAL_CLOAK));
    public static final AccessoryBuilder UNSTABLE_RUNIC_TOME = new AccessoryBuilder(List.of(Accessories.UNSTABLE_RUNIC_TOME));
    public static final AccessoryBuilder MAGICAL_OPS = new AccessoryBuilder(List.of(Accessories.MAGICAL_OPS));
    public static final AccessoryBuilder CRYSTALLIZED_PLATE = new AccessoryBuilder(List.of(Accessories.CRYSTALLIZED_PLATE));
    public static final AccessoryBuilder CINDER_STONE = new AccessoryBuilder(List.of(Accessories.CINDER_STONE, Accessories.BURN_EFFECT));
    public static final AccessoryBuilder AGILE_DAGGER = new AccessoryBuilder(List.of(Accessories.AGILE_DAGGER));
    public static final AccessoryBuilder POWER_GLOVES = new AccessoryBuilder(List.of(Accessories.POWER_GLOVES));
    public static final AccessoryBuilder SWIFT_DAGGER = new AccessoryBuilder(List.of(Accessories.SWIFT_DAGGER));
    public static final AccessoryBuilder AETHER_WISP = new AccessoryBuilder(List.of(Accessories.AETHER_WISP));
    public static final AccessoryBuilder BERSERKER_BOOTS = new AccessoryBuilder(List.of(Accessories.BERSERKER_BOOTS));
    public static final AccessoryBuilder IRON_PLATED_BOOTS = new AccessoryBuilder(List.of(Accessories.IRON_PLATED_BOOTS));
    public static final AccessoryBuilder MINING_BOOTS = new AccessoryBuilder(List.of(Accessories.MINING_BOOTS));
    public static final AccessoryBuilder ANCIENT_PICKAXE = new AccessoryBuilder(List.of(Accessories.ANCIENT_PICKAXE));
    public static final AccessoryBuilder ANCIENT_CHISEL = new AccessoryBuilder(List.of(Accessories.ANCIENT_CHISEL));
    public static final AccessoryBuilder OVERGROWTH = new AccessoryBuilder(List.of(Accessories.OVERGROWTH));
    public static final AccessoryBuilder BROKEN_HERO_SWORD = new AccessoryBuilder(List.of(Accessories.BRAVERY));
    public static final AccessoryBuilder RECTRIX = new AccessoryBuilder(List.of(Accessories.RECTRIX));
    public static final AccessoryBuilder FORTUNE_SHIKIGAMI = new AccessoryBuilder(List.of(Accessories.FORTUNE_SHIKIGAMI));
    public static final AccessoryBuilder DESPAIR_AND_DEFY = new AccessoryBuilder(List.of(Accessories.DESPAIR_AND_DEFY));

    protected List<Supplier<? extends Accessory>> suppliers = new ArrayList<>();
    protected List<Accessory> accessories = new ArrayList<>();
    protected List<Accessory> filter = new ArrayList<>();

    public AccessoryBuilder(List<Supplier<? extends Accessory>> lists) {
        this.suppliers = lists;
        ReloadableHandler.addToReloadList(this);
    }

    @Override
    public void reload() {
        AtomicReference<Accessory> builder = new AtomicReference<Accessory>(null);
        this.suppliers.forEach(supplier -> {
            Accessory accessory = supplier.get();
            if (!this.filter.contains(accessory)) {
                this.filter.add(accessory);
            }
        });

        this.accessories = this.filter.stream().collect(Collectors.toUnmodifiableList());
    }

    public List<Accessory> getAccessories() {
        return this.accessories;
    }
}
