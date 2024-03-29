package net.pevori.queencats;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.pevori.queencats.config.QueenCatsConfig;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.client.*;
import net.pevori.queencats.gui.menu.ModMenuTypes;
import net.pevori.queencats.item.ModItems;
import net.pevori.queencats.sound.ModSounds;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.io.File;
import java.util.stream.Collectors;

@Mod(QueenCats.MOD_ID)
public class QueenCats {
    public static final String MOD_ID = "queencats";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static boolean enableCatSounds = true;
    public static boolean enableDogSounds = true;
    public static boolean enableBunnySounds = true;
    public static boolean enableCowSounds = true;

    public QueenCats() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, QueenCatsConfig.SERVER_SPEC, "queencats-common.toml");
        QueenCatsConfig.loadConfig(QueenCatsConfig.SERVER_SPEC,
                FMLPaths.CONFIGDIR.get().resolve("queencats-common.toml").toString());

        enableCatSounds = QueenCatsConfig.SERVER.enable_humanoid_cat_sounds.get();
        enableDogSounds = QueenCatsConfig.SERVER.enable_humanoid_dog_sounds.get();
        enableBunnySounds = QueenCatsConfig.SERVER.enable_humanoid_bunny_sounds.get();
        enableCowSounds = QueenCatsConfig.SERVER.enable_humanoid_cow_sounds.get();

        ModItems.register(eventBus);
        ModSounds.register(eventBus);
        ModEntityTypes.register(eventBus);
        ModMenuTypes.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        GeckoLib.initialize();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntityTypes.QUEEN_CAT.get(), QueenCatRenderer::new);
        EntityRenderers.register(ModEntityTypes.QUEEN_DOG.get(), QueenDogRenderer::new);
        EntityRenderers.register(ModEntityTypes.QUEEN_BUNNY.get(), QueenBunnyRenderer::new);
        EntityRenderers.register(ModEntityTypes.QUEEN_COW.get(), QueenCowRenderer::new);

        EntityRenderers.register(ModEntityTypes.PRINCESS_CAT.get(), PrincessCatRenderer::new);
        EntityRenderers.register(ModEntityTypes.PRINCESS_DOG.get(), PrincessDogRenderer::new);
        EntityRenderers.register(ModEntityTypes.PRINCESS_BUNNY.get(), PrincessBunnyRenderer::new);
        EntityRenderers.register(ModEntityTypes.PRINCESS_COW.get(), PrincessCowRenderer::new);

        ModMenuTypes.registerScreen(event);
    }

    private void setup(final FMLCommonSetupEvent event) {
        QueenCats.LOGGER.info("Queen Cats Online >:3");
    }
}
