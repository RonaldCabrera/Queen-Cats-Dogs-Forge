package net.pevori.queencats.entity.client;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.PrincessCowEntity;
import net.pevori.queencats.entity.variants.HumanoidCowVariant;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Map;

public class PrincessCowRenderer extends GeoEntityRenderer<PrincessCowEntity> {
    public static final Map<HumanoidCowVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(HumanoidCowVariant.class), (map) -> {
                map.put(HumanoidCowVariant.COFFEE,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_cow/humanoid_cow_coffee.png"));
                map.put(HumanoidCowVariant.MILKSHAKE,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_cow/humanoid_cow_milkshake.png"));
                map.put(HumanoidCowVariant.MOOSHROOM,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_cow/humanoid_cow_mooshroom.png"));
                map.put(HumanoidCowVariant.MOOBLOOM,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_cow/humanoid_cow_moobloom.png"));
                map.put(HumanoidCowVariant.WOOLY,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_cow/humanoid_cow_wooly.png"));
            });

    public PrincessCowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new PrincessCowModel());
    }


    @Override
    public ResourceLocation getTextureLocation(PrincessCowEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }
}
