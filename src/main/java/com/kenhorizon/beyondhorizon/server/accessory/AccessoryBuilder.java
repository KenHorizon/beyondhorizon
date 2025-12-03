package com.kenhorizon.beyondhorizon.server.accessory;

import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AccessoryBuilder implements IReloadable {
    public static final AccessoryBuilder NONE = new AccessoryBuilder(List.of(Accessories.NONE));
    public static final AccessoryBuilder BOOTS_0 = new AccessoryBuilder(List.of(Accessories.BOOTS_0));
    public static final AccessoryBuilder BASIC_CRIT_0 = new AccessoryBuilder(List.of(Accessories.BASIC_CRIT));
    public static final AccessoryBuilder BASIC_CRIT_1 = new AccessoryBuilder(List.of(Accessories.UPGRADED_CRIT));
    public static final AccessoryBuilder NEGATE_FALL_DAMAGE = new AccessoryBuilder(List.of(Accessories.FEATHER_FEET));

    public static final AccessoryBuilder AGILE_DAGGER = new AccessoryBuilder(List.of(Accessories.BASIC_CRIT, Accessories.BASIC_MOVEMENT_SPEED));
    public static final AccessoryBuilder POWER_GLOVES = new AccessoryBuilder(List.of(Accessories.POWER_GLOVES));
    public static final AccessoryBuilder SWIFT_DAGGER = new AccessoryBuilder(List.of(Accessories.SWIFT_DAGGER));
    public static final AccessoryBuilder AETHER_WISP = new AccessoryBuilder(List.of(Accessories.AETHER_WISP));
    public static final AccessoryBuilder BERSERKER_BOOTS = new AccessoryBuilder(List.of(Accessories.BOOTS_1, Accessories.BERSERKER_BOOTS));
    public static final AccessoryBuilder IRON_PLATED_BOOTS = new AccessoryBuilder(List.of(Accessories.BOOTS_1, Accessories.IRON_PLATED_BOOTS));
    public static final AccessoryBuilder MINING_BOOTS = new AccessoryBuilder(List.of(Accessories.BOOTS_1, Accessories.MINING_BOOTS));
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
