package net.pevori.queencats.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.PrincessCatEntity;
import net.pevori.queencats.entity.custom.QueenCatEntity;

public class ModEntities {
    public static final EntityType<QueenCatEntity> QUEEN_CAT = Registry.register(
        Registry.ENTITY_TYPE, new Identifier(QueenCats.MOD_ID, "queen_cat"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, QueenCatEntity::new)
                .dimensions(EntityDimensions.fixed(0.8f, 2.0f)).build());

    public static final EntityType<PrincessCatEntity> PRINCESS_CAT = Registry.register(
        Registry.ENTITY_TYPE, new Identifier(QueenCats.MOD_ID, "princess_cat"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PrincessCatEntity::new)
                .dimensions(EntityDimensions.fixed(0.5f, 1.5f)).build());
}
