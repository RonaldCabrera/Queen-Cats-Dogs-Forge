package net.pevori.queencats.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.pevori.queencats.QueenCats;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            QueenCats.MOD_ID);

    public static RegistryObject<CreativeModeTab> QUEENCATS_TAB = CREATIVE_MODE_TABS.register("queencats_tab", () ->
            CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.GOLDEN_FISH.get()))
                    .title(Component.literal("Queen Cats & Dogs")).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
