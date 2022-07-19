package net.pevori.queencats.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.pevori.queencats.QueenCats;

public class ModItemGroup {
    public static final ItemGroup QUEENCATS = FabricItemGroupBuilder.build(new Identifier(QueenCats.MOD_ID, "queencats"),
        () -> new ItemStack(ModItems.GOLDEN_FISH));
}
