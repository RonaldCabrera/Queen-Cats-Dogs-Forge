package net.pevori.queencats.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab QUEENCATS_TAB = new CreativeModeTab("queencatstab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.GOLDEN_FISH.get());
        }
    };
}
