package com.kenhorizon.beyondhorizon.server.init;

import com.kenhorizon.beyondhorizon.server.util.Maths;
import com.kenhorizon.libs.registry.RegistryEntry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BHPotions {
    public static final RegistryObject<Potion> THORN_POTION = potion("thorns", () -> new Potion(new MobEffectInstance(BHEffects.THORNS.get(), Maths.sec(30))));
    public static final RegistryObject<Potion> LONG_THORN_POTION = potionLong("thorns", () -> new Potion("thorns", new MobEffectInstance(BHEffects.THORNS.get(), Maths.sec(50))));
    public static final RegistryObject<Potion> STRONG_THORN_POTION = potionStrong("thorns", () -> new Potion("thorns", new MobEffectInstance(BHEffects.THORNS.get(), Maths.sec(20), 1)));

    public static final RegistryObject<Potion> FEAR_POTION = potion("fear", () -> new Potion(new MobEffectInstance(BHEffects.FEAR.get(), Maths.sec(30))));
    public static final RegistryObject<Potion> LONG_FEAR_POTION = potionLong("fear", () -> new Potion("fear", new MobEffectInstance(BHEffects.FEAR.get(), Maths.sec(50))));

    public static final RegistryObject<Potion> LIGHTNING_POTION = potion("lightning", () -> new Potion(new MobEffectInstance(BHEffects.LIGHTNING.get(), Maths.sec(30))));
    public static final RegistryObject<Potion> LONG_LIGHTNING_POTION = potionLong("lightning", () -> new Potion("lightning", new MobEffectInstance(BHEffects.LIGHTNING.get(), Maths.mins(1, 30))));
    public static final RegistryObject<Potion> STRONG_LIGHTNING_POTION = potionStrong("lightning", () -> new Potion("lightning", new MobEffectInstance(BHEffects.LIGHTNING.get(), Maths.sec(20), 1)));

    public static final RegistryObject<Potion> RAGE_POTION = potion("rage", () -> new Potion(new MobEffectInstance(BHEffects.RAGE.get(), Maths.sec(30))));
    public static final RegistryObject<Potion> LONG_RAGE_POTION = potionLong("rage", () -> new Potion("rage", new MobEffectInstance(BHEffects.RAGE.get(), Maths.mins(1, 50))));
    public static final RegistryObject<Potion> STRONG_RAGE_POTION = potionStrong("rage", () -> new Potion("rage", new MobEffectInstance(BHEffects.RAGE.get(), Maths.sec(50), 1)));

    public static final RegistryObject<Potion> RESISTANCE_POTION = potion("resistance", () -> new Potion(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Maths.mins(3, 10))));
    public static final RegistryObject<Potion> LONG_RESISTANCE_POTION = potionLong("resistance", () -> new Potion("resistance", new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Maths.mins(4, 20))));
    public static final RegistryObject<Potion> STRONG_RESISTANCE_POTION = potionStrong("resistance", () -> new Potion("resistance", new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Maths.mins(1, 30), 1)));

    public static final RegistryObject<Potion> HUNGER_POTION = potion("hunger", () -> new Potion(new MobEffectInstance(MobEffects.HUNGER, Maths.mins(1, 30))));
    public static final RegistryObject<Potion> LONG_HUNGER_POTION = potionLong("hunger", () -> new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, Maths.mins(2, 30))));
    public static final RegistryObject<Potion> STRONG_HUNGER_POTION = potionStrong("hunger", () -> new Potion("hunger", new MobEffectInstance(MobEffects.HUNGER, Maths.sec(30), 1)));

    public static final RegistryObject<Potion> IRON_SKIN_POTION = potion("iron_skin", () -> new Potion(new MobEffectInstance(BHEffects.IRON_SKIN.get(), Maths.mins(0, 30))));
    public static final RegistryObject<Potion> LONG_IRON_SKIN_POTION = potionLong("iron_skin", () -> new Potion("iron_skin", new MobEffectInstance(BHEffects.IRON_SKIN.get(), Maths.mins(1, 30))));
    public static final RegistryObject<Potion> STRONG_IRON_SKIN_POTION = potionStrong("iron_skin", () -> new Potion("iron_skin", new MobEffectInstance(BHEffects.IRON_SKIN.get(), Maths.mins(0, 30), 1)));

    public static final RegistryObject<Potion> VULNERABLE_POTION = potion("vulnerable", () -> new Potion(new MobEffectInstance(BHEffects.VULNERABLE.get(), Maths.sec(50))));
    public static final RegistryObject<Potion> LONG_VULNERABLE_POTION = potionLong("vulnerable", () -> new Potion("vulnerable", new MobEffectInstance(BHEffects.VULNERABLE.get(), Maths.mins(1, 20))));
    public static final RegistryObject<Potion> STRONG_VULNERABLE_POTION = potionStrong("vulnerable", () -> new Potion("vulneralbe", new MobEffectInstance(BHEffects.VULNERABLE.get(), Maths.sec(20), 1)));

    public static final RegistryObject<Potion> LETHAL_POISON_POTION = potion("lethal_poison", () -> new Potion(new MobEffectInstance(BHEffects.LETHAL_POISON.get(), Maths.sec(20))));
    public static final RegistryObject<Potion> LONG_LETHAL_POISON_POTION = potionLong("lethal_poison", () -> new Potion("lethal_poison", new MobEffectInstance(BHEffects.LETHAL_POISON.get(), Maths.mins(0, 30))));
    public static final RegistryObject<Potion> STRONG_LETHAL_POISON_POTION = potionStrong("lethal_poison", () -> new Potion("lethal_poison", new MobEffectInstance(BHEffects.LETHAL_POISON.get(), Maths.sec(10), 1)));


    private static <T extends Potion> RegistryObject<Potion> potion(String name, Supplier<T> supplier) {
        return RegistryEntry.POTIONS.register(name, supplier);
    }

    private static <T extends Potion> RegistryObject<Potion> potionLong(String name, Supplier<T> supplier) {
        return RegistryEntry.POTIONS.register("long_" + name, supplier);
    }

    private static <T extends Potion> RegistryObject<Potion> potionStrong(String name, Supplier<T> supplier) {
        return RegistryEntry.POTIONS.register("strong_" + name, supplier);
    }

    public static void setup() {

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
        RegistryEntry.POTIONS.register(eventBus);
    }
}
