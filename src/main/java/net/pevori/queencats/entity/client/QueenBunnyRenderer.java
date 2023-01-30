package net.pevori.queencats.entity.client;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.QueenBunnyEntity;
import net.pevori.queencats.entity.variants.HumanoidBunnyVariant;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Map;

public class QueenBunnyRenderer extends GeoEntityRenderer<QueenBunnyEntity> {
    public static final Map<HumanoidBunnyVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(HumanoidBunnyVariant.class), (map) -> {
                map.put(HumanoidBunnyVariant.COCOA,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_bunny/humanoid_bunny_cocoa.png"));
                map.put(HumanoidBunnyVariant.SNOW,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_bunny/humanoid_bunny_snow.png"));
                map.put(HumanoidBunnyVariant.SUNDAY,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_bunny/humanoid_bunny_sunday.png"));
                map.put(HumanoidBunnyVariant.STRAWBERRY,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_bunny/humanoid_bunny_strawberry.png"));
            });

    public QueenBunnyRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new QueenBunnyModel());
    }

    @Override
    public ResourceLocation getTextureLocation(QueenBunnyEntity instance) {
        if(instance.isAlmond()){
            return new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_bunny/humanoid_bunny_almond.png");
        }

        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }
}
