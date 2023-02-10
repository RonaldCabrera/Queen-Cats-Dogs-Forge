package net.pevori.queencats.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.*;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, QueenCats.MOD_ID);

    public static final RegistryObject<EntityType<QueenCatEntity>> QUEEN_CAT = ENTITY_TYPES.register("queen_cat",
        () -> EntityType.Builder.of(QueenCatEntity::new, MobCategory.CREATURE)
                .sized(0.8f,2.0f)
                .build(new ResourceLocation(QueenCats.MOD_ID, "queen_cat").toString()));

    public static final RegistryObject<EntityType<QueenDogEntity>> QUEEN_DOG = ENTITY_TYPES.register("queen_dog",
        () -> EntityType.Builder.of(QueenDogEntity::new, MobCategory.CREATURE)
                .sized(0.8f,2.0f)
                .build(new ResourceLocation(QueenCats.MOD_ID, "queen_dog").toString()));

    public static final RegistryObject<EntityType<QueenBunnyEntity>> QUEEN_BUNNY = ENTITY_TYPES.register("queen_bunny",
        () -> EntityType.Builder.of(QueenBunnyEntity::new, MobCategory.CREATURE)
                .sized(0.8f,2.0f)
                .build(new ResourceLocation(QueenCats.MOD_ID, "queen_bunny").toString()));

    public static final RegistryObject<EntityType<PrincessCatEntity>> PRINCESS_CAT = ENTITY_TYPES.register("princess_cat",
        () -> EntityType.Builder.of(PrincessCatEntity::new, MobCategory.CREATURE)
                .sized(0.5f,1.5f)
                .build(new ResourceLocation(QueenCats.MOD_ID, "princess_cat").toString()));

    public static final RegistryObject<EntityType<PrincessDogEntity>> PRINCESS_DOG = ENTITY_TYPES.register("princess_dog",
        () -> EntityType.Builder.of(PrincessDogEntity::new, MobCategory.CREATURE)
                .sized(0.5f,1.5f)
                .build(new ResourceLocation(QueenCats.MOD_ID, "princess_dog").toString()));

    public static final RegistryObject<EntityType<PrincessBunnyEntity>> PRINCESS_BUNNY = ENTITY_TYPES.register("princess_bunny",
        () -> EntityType.Builder.of(PrincessBunnyEntity::new, MobCategory.CREATURE)
                .sized(0.8f,2.0f)
                .build(new ResourceLocation(QueenCats.MOD_ID, "princess_bunny").toString()));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }

}
