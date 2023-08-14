package net.pevori.queencats.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.custom.*;

@Mod.EventBusSubscriber(modid = QueenCats.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.QUEEN_CAT.get(), QueenCatEntity.setAttributes());
        event.put(ModEntityTypes.QUEEN_DOG.get(), QueenDogEntity.setAttributes());
        event.put(ModEntityTypes.QUEEN_BUNNY.get(), QueenBunnyEntity.setAttributes());
        event.put(ModEntityTypes.QUEEN_COW.get(), QueenCowEntity.setAttributes());

        event.put(ModEntityTypes.PRINCESS_CAT.get(), PrincessCatEntity.setAttributes());
        event.put(ModEntityTypes.PRINCESS_DOG.get(), PrincessDogEntity.setAttributes());
        event.put(ModEntityTypes.PRINCESS_BUNNY.get(), PrincessBunnyEntity.setAttributes());
        event.put(ModEntityTypes.PRINCESS_COW.get(), PrincessCowEntity.setAttributes());
    }
}
