package net.pevori.queencats.util;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.pevori.queencats.entity.ModEntities;
import net.pevori.queencats.entity.custom.PrincessCatEntity;
import net.pevori.queencats.entity.custom.QueenCatEntity;

public class ModRegistries {
    public static void registerQueenCats() {
        registerAttributes();
    }

    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.QUEEN_CAT, QueenCatEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.PRINCESS_CAT, PrincessCatEntity.setAttributes());
    }
}
