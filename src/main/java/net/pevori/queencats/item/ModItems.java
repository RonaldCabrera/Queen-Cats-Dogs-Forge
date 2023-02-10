package net.pevori.queencats.item;

import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.ModEntityTypes;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, QueenCats.MOD_ID);

    public static final RegistryObject<Item> GOLDEN_FISH = ITEMS.register("golden_fish",
            () -> new GlintedItem(new GlintedItem.Properties().food(Foods.GOLDEN_APPLE).tab(ModCreativeModeTab.QUEENCATS_TAB)));

    public static final RegistryObject<Item> GOLDEN_BONE = ITEMS.register("golden_bone",
            () -> new GlintedItem(new GlintedItem.Properties().tab(ModCreativeModeTab.QUEENCATS_TAB)));

    public static final RegistryObject<Item> GOLDEN_WHEAT = ITEMS.register("golden_wheat",
            () -> new GlintedItem(new GlintedItem.Properties().tab(ModCreativeModeTab.QUEENCATS_TAB)));

    public static final RegistryObject<Item> KEMOMIMI_POTION = ITEMS.register("kemomimi_potion",
            () -> new GlintedItem(new GlintedItem.Properties().tab(ModCreativeModeTab.QUEENCATS_TAB)));

    public static final RegistryObject<Item> QUEEN_CAT_SPAWN_EGG = ITEMS.register("queen_cat_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.QUEEN_CAT,0xF3F7FA, 0xB9EDFE,
                    new Item.Properties().tab(ModCreativeModeTab.QUEENCATS_TAB)));

    public static final RegistryObject<Item> PRINCESS_CAT_SPAWN_EGG = ITEMS.register("princess_cat_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.PRINCESS_CAT,0xF3F7FA, 0xB9EDFE,
                    new Item.Properties().tab(ModCreativeModeTab.QUEENCATS_TAB)));

    public static final RegistryObject<Item> QUEEN_DOG_SPAWN_EGG = ITEMS.register("queen_dog_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.QUEEN_DOG,0xF3F7FA, 0x844204,
                    new Item.Properties().tab(ModCreativeModeTab.QUEENCATS_TAB)));

    public static final RegistryObject<Item> PRINCESS_DOG_SPAWN_EGG = ITEMS.register("princess_dog_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.PRINCESS_DOG,0xF3F7FA, 0x844204,
                    new Item.Properties().tab(ModCreativeModeTab.QUEENCATS_TAB)));

    public static final RegistryObject<Item> QUEEN_BUNNY_SPAWN_EGG = ITEMS.register("queen_bunny_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.QUEEN_BUNNY,0xF3F7FA, 0xA020F0,
                    new Item.Properties().tab(ModCreativeModeTab.QUEENCATS_TAB)));

    public static final RegistryObject<Item> PRINCESS_BUNNY_SPAWN_EGG = ITEMS.register("princess_bunny_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.PRINCESS_BUNNY,0xF3F7FA, 0xDB68ED,
                    new Item.Properties().tab(ModCreativeModeTab.QUEENCATS_TAB)));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
