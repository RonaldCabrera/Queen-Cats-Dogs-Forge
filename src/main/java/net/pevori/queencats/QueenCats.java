package net.pevori.queencats;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.client.PrincessCatRenderer;
import net.pevori.queencats.entity.client.PrincessDogRenderer;
import net.pevori.queencats.entity.client.QueenCatRenderer;
import net.pevori.queencats.entity.client.QueenDogRenderer;
import net.pevori.queencats.item.ModItems;
import net.pevori.queencats.sound.ModSounds;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(QueenCats.MOD_ID)
public class QueenCats {
    public static final String MOD_ID = "queencats";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public QueenCats() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);

        ModSounds.register(eventBus);

        ModEntityTypes.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        GeckoLib.initialize();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntityTypes.QUEEN_CAT.get(), QueenCatRenderer::new);
        EntityRenderers.register(ModEntityTypes.QUEEN_DOG.get(), QueenDogRenderer::new);
        EntityRenderers.register(ModEntityTypes.PRINCESS_CAT.get(), PrincessCatRenderer::new);
        EntityRenderers.register(ModEntityTypes.PRINCESS_DOG.get(), PrincessDogRenderer::new);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
    }
}
