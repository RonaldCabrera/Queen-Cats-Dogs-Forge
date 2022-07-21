package net.pevori.queencats.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.ModEntities;

public class ModItems {
    public static final Item GOLDEN_FISH = registerItem("golden_fish", 
        new GlintedItem(new FabricItemSettings().group(ModItemGroup.QUEENCATS).food(ModFoodComponents.GOLDEN_FISH))
    );

    public static final Item NEKOMIMI_POTION = registerItem("nekomimi_potion", 
    new GlintedItem(new FabricItemSettings().group(ModItemGroup.QUEENCATS))
);

    public static final Item QUEEN_CAT_SPAWN_EGG = registerItem("queen_cat_spawn_egg",
        new SpawnEggItem(ModEntities.QUEEN_CAT, 0xF3F7FA, 0xB9EDFE,
        new FabricItemSettings().group(ModItemGroup.QUEENCATS).maxCount(1))
    );

    public static final Item PRINCESS_CAT_SPAWN_EGG = registerItem("princess_cat_spawn_egg",
        new SpawnEggItem(ModEntities.PRINCESS_CAT, 0xF3F7FA, 0xB9EDFE,
        new FabricItemSettings().group(ModItemGroup.QUEENCATS).maxCount(1))
    );

    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(QueenCats.MOD_ID, name), item);
    }

    private static Item registerItem(String name, GlintedItem item){
        return Registry.register(Registry.ITEM, new Identifier(QueenCats.MOD_ID, name), item);
    }

    public static void registerModItems(){
        QueenCats.LOGGER.info("Registering Mod Items for" + QueenCats.MOD_ID);
    }

}
