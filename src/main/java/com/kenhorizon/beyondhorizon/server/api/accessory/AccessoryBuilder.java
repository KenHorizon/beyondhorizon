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

    public static final AccessoryBuilder CARBONIZED_BONE = new AccessoryBuilder(List.of(Accessories.PRESERVED));
    public static final AccessoryBuilder CURSED_BLINDFOLD = new AccessoryBuilder(List.of(Accessories.SEEK_ONE_SEEK_TWICE));
    public static final AccessoryBuilder MASK_OF_BEWILDERED = new AccessoryBuilder(List.of(Accessories.UNBOTHERED));
    public static final AccessoryBuilder BROKEN_SHULKER_SHELL = new AccessoryBuilder(List.of(Accessories.WEIGHTS));
    public static final AccessoryBuilder ARMOR_PLATE = new AccessoryBuilder(List.of(Accessories.INVULNERABLE));
    public static final AccessoryBuilder ADHESIVE_BANDAGES = new AccessoryBuilder(List.of(Accessories.HEMORRHAGE_CONTROL));
    public static final AccessoryBuilder CURSED_APPLE = new AccessoryBuilder(List.of(Accessories.BAD_APPLE));
    public static final AccessoryBuilder VITAMINS = new AccessoryBuilder(List.of(Accessories.VITAMINS));

    public static final AccessoryBuilder LIGHTNING_STONE = new AccessoryBuilder(List.of(Accessories.ENERGIZED, Accessories.ELECTROSHOCK));
    public static final AccessoryBuilder GHOUL_HEART = new AccessoryBuilder(List.of(Accessories.GHOUL));
    public static final AccessoryBuilder DUSKBLADE = new AccessoryBuilder(List.of(Accessories.DUSKBLADE, Accessories.NIGHTSTALKER));
    public static final AccessoryBuilder FORBIDDEN_EYE = new AccessoryBuilder(List.of(Accessories.PENALTY_0, Accessories.EXCORIATE));
    public static final AccessoryBuilder TWILIGHT_EDGE = new AccessoryBuilder(List.of(Accessories.TWO_WORLD));
    public static final AccessoryBuilder LIGHT_STRING_BOW = new AccessoryBuilder(List.of(Accessories.FLUOROCARBON));
    public static final AccessoryBuilder HEAVY_STRING_BOW = new AccessoryBuilder(List.of(Accessories.POLYETHYLENE));
    public static final AccessoryBuilder KRAKEN_SLAYER = new AccessoryBuilder(List.of(Accessories.BRING_IT_DOWN));
    public static final AccessoryBuilder TITAN_GLOVES = new AccessoryBuilder(List.of(Accessories.SPELL_BLADE_2));
    public static final AccessoryBuilder FLAME_OF_TORMENT = new AccessoryBuilder(List.of(Accessories.TORMENT));
    public static final AccessoryBuilder ETERNAL_LIFE = new AccessoryBuilder(List.of(Accessories.ETERNAL_LIFE));
    public static final AccessoryBuilder NULL_SWORD = new AccessoryBuilder(List.of(Accessories.NULLIFY));
    public static final AccessoryBuilder INFINITY_SWORD = new AccessoryBuilder(List.of(Accessories.LETHAL_BURST));
    public static final AccessoryBuilder TRUE_HERO_SWORD = new AccessoryBuilder(List.of(Accessories.TRUE_HERO_SWORD));
    public static final AccessoryBuilder ASCENDED_HERO_SWORD = new AccessoryBuilder(List.of(Accessories.TRUE_HERO_SWORD, Accessories.LETHAL_BURST, Accessories.NULLIFY));
    public static final AccessoryBuilder SPRING_LOCK = new AccessoryBuilder(List.of(Accessories.JUMP_BOOST));
    public static final AccessoryBuilder POISON_VILE = new AccessoryBuilder(List.of(Accessories.VENOM));
    public static final AccessoryBuilder DWARF_MINER_RING = new AccessoryBuilder(List.of(Accessories.DWARF_MINER_RING));
    public static final AccessoryBuilder MINER_GLOVES = new AccessoryBuilder(List.of(Accessories.MINER_GLOVES));
    public static final AccessoryBuilder BOOK_OF_KNOWLEDGE = new AccessoryBuilder(List.of(Accessories.KNOWLEDGE_1));
    public static final AccessoryBuilder ULTIMA = new AccessoryBuilder(List.of(Accessories.ULTIMA, Accessories.KNOWLEDGE_2));
    public static final AccessoryBuilder POWER_CLAW = new AccessoryBuilder(List.of(Accessories.POWER_CLAW));
    public static final AccessoryBuilder THORNMAIL = new AccessoryBuilder(List.of(Accessories.THORNS));
    public static final AccessoryBuilder BLOOD_OF_BERSERKER = new AccessoryBuilder(List.of(Accessories.RAGE));
    public static final AccessoryBuilder GOLDEN_BOOTS = new AccessoryBuilder(List.of(Accessories.MINING_BOOTS, Accessories.BERSERKER_BOOTS, Accessories.IRON_PLATED_BOOTS, Accessories.BOOTS_2, Accessories.FIRE_IMMUNITY));
    public static final AccessoryBuilder BASIC_BOOTS = new AccessoryBuilder(List.of(Accessories.BOOTS_1));
    public static final AccessoryBuilder DEATH_CONTRACT = new AccessoryBuilder(List.of(Accessories.DEATH, Accessories.TAXS));
    public static final AccessoryBuilder TOUGH_CLOTH = new AccessoryBuilder(List.of(Accessories.TOUGH_CLOTH));
    public static final AccessoryBuilder VITALITY_STONE = new AccessoryBuilder(List.of(Accessories.VITALITY_STONE));
    public static final AccessoryBuilder CHAIN_VEST = new AccessoryBuilder(List.of(Accessories.CHAIN_VEST));
    public static final AccessoryBuilder ABYSSAL_TOOTH = new AccessoryBuilder(List.of(Accessories.ABYSSAL_TOOTH, Accessories.CORRUPTED_BITE));
    public static final AccessoryBuilder ASHES_OF_FLAME = new AccessoryBuilder(List.of(Accessories.INFLAME));
    public static final AccessoryBuilder CURSED_SKULL = new AccessoryBuilder(List.of(Accessories.CURSED_SKULL));
    public static final AccessoryBuilder RECURVE_ARROW = new AccessoryBuilder(List.of(Accessories.STING));
    public static final AccessoryBuilder TALISMAN_OF_ASCENSION = new AccessoryBuilder(List.of(Accessories.ASCENSION));
    public static final AccessoryBuilder WARDEN_MAIL = new AccessoryBuilder(List.of(Accessories.ROCK_SOLID));
    public static final AccessoryBuilder WARD_CLEANSE = new AccessoryBuilder(List.of(Accessories.CLEANSE));
    public static final AccessoryBuilder SOUL_SIPHON = new AccessoryBuilder(List.of(Accessories.LIFE_SIPHON));
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
    public static final AccessoryBuilder HARPOON_HEAD = new AccessoryBuilder(List.of(Accessories.HARPOON_HEAD));
    public static final AccessoryBuilder POWER_GLOVES = new AccessoryBuilder(List.of(Accessories.POWER_GLOVES));
    public static final AccessoryBuilder SWIFT_DAGGER = new AccessoryBuilder(List.of(Accessories.SWIFT_DAGGER));
    public static final AccessoryBuilder AETHER_WISP = new AccessoryBuilder(List.of(Accessories.AETHER_WISP));
    public static final AccessoryBuilder BERSERKER_BOOTS = new AccessoryBuilder(List.of(Accessories.BOOTS_2, Accessories.BERSERKER_BOOTS));
    public static final AccessoryBuilder IRON_PLATED_BOOTS = new AccessoryBuilder(List.of(Accessories.BOOTS_2, Accessories.IRON_PLATED_BOOTS));
    public static final AccessoryBuilder MINER_BOOTS = new AccessoryBuilder(List.of(Accessories.BOOTS_2, Accessories.MINING_BOOTS));
    public static final AccessoryBuilder ANCIENT_PICKAXE = new AccessoryBuilder(List.of(Accessories.ANCIENT_PICKAXE));
    public static final AccessoryBuilder ANCIENT_CHISEL = new AccessoryBuilder(List.of(Accessories.ANCIENT_CHISEL));
    public static final AccessoryBuilder HEART_OF_THE_TREE = new AccessoryBuilder(List.of(Accessories.OVERGROWTH));
    public static final AccessoryBuilder BROKEN_HERO_SWORD = new AccessoryBuilder(List.of(Accessories.BRAVERY));
    public static final AccessoryBuilder TWILIGHT_SWORD = new AccessoryBuilder(List.of(Accessories.TWILIGHT_SWORD, Accessories.SPELL_BLADE_1));
    public static final AccessoryBuilder SHEEN = new AccessoryBuilder(List.of(Accessories.SHEEN, Accessories.SPELL_BLADE_0));
    public static final AccessoryBuilder SPEAR_OF_CHAOS = new AccessoryBuilder(List.of(Accessories.SPEAR_OF_CHAOS));
    public static final AccessoryBuilder VOID_STAFF = new AccessoryBuilder(List.of(Accessories.VOID_STAFF));
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
            accessory.innateSkill().forEach(innate -> {
                if (!this.filter.contains(innate.get())) {
                    this.filter.add(innate.get());
                }
            });
        });

        this.accessories = this.filter.stream().collect(Collectors.toUnmodifiableList());
    }

    public List<Accessory> getAccessories() {
        return this.accessories;
    }
}
