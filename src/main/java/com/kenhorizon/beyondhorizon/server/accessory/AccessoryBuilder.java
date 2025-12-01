package com.kenhorizon.beyondhorizon.server.accessory;

import com.kenhorizon.beyondhorizon.BeyondHorizon;
import com.kenhorizon.beyondhorizon.server.skills.Skill;
import com.kenhorizon.beyondhorizon.server.skills.SkillTypes;
import com.kenhorizon.beyondhorizon.server.skills.Skills;
import com.kenhorizon.libs.server.IReloadable;
import com.kenhorizon.libs.server.ReloadableHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AccessoryBuilder implements IReloadable {
    public static final AccessoryBuilder NONE = new AccessoryBuilder(List.of(Accessories.NONE));
    public static final AccessoryBuilder BOOTS = new AccessoryBuilder(List.of(Accessories.BOOTS_0));
    public static final AccessoryBuilder BOOTS_UPGRADED = new AccessoryBuilder(List.of(Accessories.BOOTS_1));
    public static final AccessoryBuilder BROKEN_HERO_SWORD = new AccessoryBuilder(List.of(Accessories.BRAVERY));

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
