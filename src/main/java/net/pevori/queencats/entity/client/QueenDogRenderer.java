package net.pevori.queencats.entity.client;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.QueenDogEntity;
import net.pevori.queencats.entity.variants.HumanoidDogVariant;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Map;

public class QueenDogRenderer extends GeoEntityRenderer<QueenDogEntity> {
    public static final Map<HumanoidDogVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(HumanoidDogVariant.class), (map) -> {
                map.put(HumanoidDogVariant.SHIRO,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_husky.png"));
                map.put(HumanoidDogVariant.HUSKY,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_shiro.png"));
                map.put(HumanoidDogVariant.CREAM,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_cream.png"));
                map.put(HumanoidDogVariant.GRAY,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_gray.png"));

            });

    public QueenDogRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new QueenDogModel());
    }

    @Override
    public ResourceLocation getTextureLocation(QueenDogEntity instance) {
        if(instance.isDoog()){
            return new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_doog.png");
        }

        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }
}
