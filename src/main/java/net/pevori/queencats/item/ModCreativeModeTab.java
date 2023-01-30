package net.pevori.queencats.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.pevori.queencats.QueenCats;

@Mod.EventBusSubscriber(modid = QueenCats.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTab {
    public static CreativeModeTab QUEENCATS_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event){
        QUEENCATS_TAB = event.registerCreativeModeTab(new ResourceLocation(QueenCats.MOD_ID, "queencats_tab"),
                builder -> builder.icon(() -> new ItemStack(ModItems.GOLDEN_FISH.get())).title(Component.literal("Queen Cats Tab")).build());
    }
}
