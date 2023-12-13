package net.pevori.queencats.gui.menu;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.gui.screen.HumanoidAnimalScreen;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, QueenCats.MOD_ID);

    public static final RegistryObject<MenuType<HumanoidAnimalMenu>> HUMANOID_ANIMAL_MENU =
            registerMenuType(HumanoidAnimalMenu::new, "humanoid_animal_menu");


    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(
            IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }

    public static void menuSetup(final FMLCommonSetupEvent event)
    {
        MenuScreens.register(ModMenuTypes.HUMANOID_ANIMAL_MENU.get(), HumanoidAnimalScreen::new);
    }

    public static void registerScreen(FMLClientSetupEvent event){
        event.enqueueWork(
                () -> MenuScreens.register(HUMANOID_ANIMAL_MENU.get(), HumanoidAnimalScreen::new)
        );
    }
}
