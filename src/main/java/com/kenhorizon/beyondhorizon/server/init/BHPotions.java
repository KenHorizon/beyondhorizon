package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.libs.registry.RegistryEntries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BHPotions {
    public static final RegistryObject<Potion> THORN = potion("thorns", () -> new Potion(new MobEffectInstance(BHEffects.THORNS.get(), Maths.sec(30))));
    public static final RegistryObject<Potion> LONG_THORN = potionLong("thorns", () -> new Potion("thorns", new MobEffectInstance(BHEffects.THORNS.get(), Maths.sec(50))));
    public static final RegistryObject<Potion> STRONG_THORN = potionStrong("thorns", () -> new Potion("thorns", new MobEffectInstance(BHEffects.THORNS.get(), Maths.sec(20), 1)));

    public static final RegistryObject<Potion> HUNGER = potion("hunger", () -> new Potion(new MobEffectInstance(MobEffects.HUNGER, Maths.mins(1, 30))));
    public static final RegistryObject<Potion> LONG_HUNGER = potionLong("hunger", () -> new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, Maths.mins(2, 30))));
    public static final RegistryObject<Potion> STRONG_HUNGER = potionStrong("hunger", () -> new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, Maths.sec(30), 1)));

    public static final RegistryObject<Potion> IRON_SKIN = potion("iron_skin", () -> new Potion(new MobEffectInstance(BHEffects.IRON_SKIN.get(), Maths.mins(0, 30))));
    public static final RegistryObject<Potion> LONG_IRON_SKIN = potionLong("iron_skin", () -> new Potion("iron_skin", new MobEffectInstance(BHEffects.IRON_SKIN.get(), Maths.mins(1, 30))));
    public static final RegistryObject<Potion> STRONG_IRON_SKIN = potionStrong("iron_skin", () -> new Potion("iron_skin", new MobEffectInstance(BHEffects.IRON_SKIN.get(), Maths.mins(0, 30), 1)));

    public static final RegistryObject<Potion> VULNERABLE = potion("vulnerable", () -> new Potion(new MobEffectInstance(BHEffects.VULNERABLE.get(), Maths.sec(50))));
    public static final RegistryObject<Potion> LONG_VULNERABLE = potionLong("vulnerable", () -> new Potion("vulnerable", new MobEffectInstance(BHEffects.VULNERABLE.get(), Maths.mins(1, 20))));
    public static final RegistryObject<Potion> STRONG_VULNERABLE = potionStrong("vulnerable", () -> new Potion("vulneralbe", new MobEffectInstance(BHEffects.VULNERABLE.get(), Maths.sec(20), 1)));

    public static final RegistryObject<Potion> LETHAL_POISON = potion("lethal_poison", () -> new Potion(new MobEffectInstance(BHEffects.LETHAL_POISON.get(), Maths.sec(20))));
    public static final RegistryObject<Potion> LONG_LETHAL_POISON = potionLong("lethal_poison", () -> new Potion("lethal_poison", new MobEffectInstance(BHEffects.LETHAL_POISON.get(), Maths.mins(0, 30))));
    public static final RegistryObject<Potion> STRONG_LETHAL_POISON = potionStrong("lethal_poison", () -> new Potion("lethal_poison", new MobEffectInstance(BHEffects.LETHAL_POISON.get(), Maths.sec(10), 1)));


    private static <T extends Potion> RegistryObject<Potion> potion(String name, Supplier<T> supplier) {
        return RegistryEntries.POTIONS.register(name, supplier);
    }

    private static <T extends Potion> RegistryObject<Potion> potionLong(String name, Supplier<T> supplier) {
        return RegistryEntries.POTIONS.register("long_" + name, supplier);
    }

    private static <T extends Potion> RegistryObject<Potion> potionStrong(String name, Supplier<T> supplier) {
        return RegistryEntries.POTIONS.register("strong_" + name, supplier);
    }

    public static void setup() {
        potionRecipes(Potions.AWKWARD, new ItemStack(Items.RAW_IRON), BHPotions.IRON_SKIN.get(), BHPotions.LONG_IRON_SKIN.get(), BHPotions.STRONG_IRON_SKIN.get());
        potionRecipes(BHPotions.IRON_SKIN.get(), new ItemStack(Items.FERMENTED_SPIDER_EYE), BHPotions.VULNERABLE.get(), BHPotions.LONG_VULNERABLE.get(), BHPotions.STRONG_VULNERABLE.get());
        potionRecipes(Potions.STRONG_POISON, new ItemStack(Items.BLAZE_POWDER), BHPotions.LETHAL_POISON.get(), BHPotions.LONG_LETHAL_POISON.get(), BHPotions.STRONG_LETHAL_POISON.get());
        potionRecipes(Potions.AWKWARD, new ItemStack(Items.ROTTEN_FLESH), BHPotions.HUNGER.get(), BHPotions.LONG_HUNGER.get(), BHPotions.STRONG_HUNGER.get());
        potionRecipes(Potions.AWKWARD, new ItemStack(Items.PRISMARINE_SHARD), BHPotions.THORN.get(), BHPotions.LONG_THORN.get(), BHPotions.STRONG_THORN.get());
    }

    private static void potionRecipes(Potion potion, ItemStack ingredients, Potion result1, Potion result2, Potion result3) {
        normalPotion(potion, ingredients, result1);
        longPotion(potion, result2);
        strongPotion(potion, result3);
    }

    private static void normalPotion(Potion potion, ItemStack ingredients, Potion result) {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(createPotion(potion)), Ingredient.of(ingredients), createPotion(result));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(createSplashPotion(potion)), Ingredient.of(ingredients), createSplashPotion(result));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(createLingeringPotion(potion)), Ingredient.of(ingredients), createSplashPotion(result));
    }
    private static void longPotion(Potion potion, Potion result) {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(createPotion(potion)), Ingredient.of(new ItemStack(Items.REDSTONE)), createPotion(result));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(createSplashPotion(potion)), Ingredient.of(new ItemStack(Items.REDSTONE)), createSplashPotion(result));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(createLingeringPotion(potion)), Ingredient.of(new ItemStack(Items.REDSTONE)), createSplashPotion(result));
    }
    private static void strongPotion(Potion potion, Potion result) {
        BrewingRecipeRegistry.addRecipe(Ingredient.of(createPotion(potion)), Ingredient.of(new ItemStack(Items.GLOWSTONE_DUST)), createPotion(result));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(createSplashPotion(potion)), Ingredient.of(new ItemStack(Items.GLOWSTONE_DUST)), createSplashPotion(result));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(createLingeringPotion(potion)), Ingredient.of(new ItemStack(Items.GLOWSTONE_DUST)), createSplashPotion(result));
    }
    private static ItemStack createPotion(RegistryObject<Potion> potion) {
        return createPotion(potion.get());
    }

    private static ItemStack createPotion(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
    }

    private static ItemStack createSplashPotion(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion);
    }

    private static ItemStack createLingeringPotion(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), potion);
    }

    public static void register(IEventBus eventBus) {
        RegistryEntries.POTIONS.register(eventBus);
    }
}
