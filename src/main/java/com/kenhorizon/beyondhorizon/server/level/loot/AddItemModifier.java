package com.kenhorizon.beyondhorizon.server.level.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;


public class AddItemModifier extends LootModifier {
    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
            .and(ExtraCodecs.POSITIVE_INT.optionalFieldOf("min", 0).forGetter(m -> m.min))
            .and(ExtraCodecs.POSITIVE_INT.optionalFieldOf("max", 0).forGetter(m -> m.max))
            .apply(inst, AddItemModifier::new)));
    private final Item item;
    public int count;
    public int min;
    public int max;

    public AddItemModifier(LootItemCondition[] conditionsIn, ItemLike item) {
        super(conditionsIn);
        this.item = item.asItem();
        this.count = 1;
        this.min = 0;
        this.max = 0;
    }

    public AddItemModifier(LootItemCondition[] conditionsIn, ItemLike item, int count) {
        super(conditionsIn);
        this.item = item.asItem();
        this.count = count;
        this.min = 0;
        this.max = 0;
    }

    public AddItemModifier(LootItemCondition[] conditionsIn, ItemLike item, int min, int max) {
        super(conditionsIn);
        this.item = item.asItem();
        this.min = min;
        this.max = max;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        RandomSource random = RandomSource.create();
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }
        if (this.min > 0 && this.max > 0) {
            generatedLoot.add(new ItemStack(this.item, random.nextIntBetweenInclusive(this.min, this.max)));
        } else if (this.count > 0) {
            generatedLoot.add(new ItemStack(this.item));
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
