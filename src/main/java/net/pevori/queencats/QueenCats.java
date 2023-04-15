package net.pevori.queencats;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.client.*;
import net.pevori.queencats.gui.menu.ModMenuTypes;
import net.pevori.queencats.item.ModCreativeModeTab;
import net.pevori.queencats.item.ModItems;
import net.pevori.queencats.sound.ModSounds;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(QueenCats.MOD_ID)
public class QueenCats {
    public static final String MOD_ID = "queencats";
    private static final Logger LOGGER = LogUtils.getLogger();

    public QueenCats() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModSounds.register(eventBus);
        ModEntityTypes.register(eventBus);
        ModMenuTypes.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::addCreative);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntityTypes.QUEEN_CAT.get(), QueenCatRenderer::new);
        EntityRenderers.register(ModEntityTypes.QUEEN_DOG.get(), QueenDogRenderer::new);
        EntityRenderers.register(ModEntityTypes.QUEEN_BUNNY.get(), QueenBunnyRenderer::new);

        EntityRenderers.register(ModEntityTypes.PRINCESS_CAT.get(), PrincessCatRenderer::new);
        EntityRenderers.register(ModEntityTypes.PRINCESS_DOG.get(), PrincessDogRenderer::new);
        EntityRenderers.register(ModEntityTypes.PRINCESS_BUNNY.get(), PrincessBunnyRenderer::new);

        ModMenuTypes.registerScreen(event);
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event){
        if(event.getTab() == ModCreativeModeTab.QUEENCATS_TAB){
            event.accept(ModItems.GOLDEN_FISH);
            event.accept(ModItems.GOLDEN_BONE);
            event.accept(ModItems.GOLDEN_WHEAT);
            event.accept(ModItems.KEMOMIMI_POTION);

            event.accept(ModItems.QUEEN_CAT_SPAWN_EGG);
            event.accept(ModItems.PRINCESS_CAT_SPAWN_EGG);
            event.accept(ModItems.QUEEN_DOG_SPAWN_EGG);
            event.accept(ModItems.PRINCESS_DOG_SPAWN_EGG);
            event.accept(ModItems.QUEEN_BUNNY_SPAWN_EGG);
            event.accept(ModItems.PRINCESS_BUNNY_SPAWN_EGG);
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Queen Cats, Online >:3");
    }
}
