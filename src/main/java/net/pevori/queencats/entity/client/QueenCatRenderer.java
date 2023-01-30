package net.pevori.queencats.entity.client;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.QueenCatEntity;
import net.pevori.queencats.entity.variants.HumanoidCatVariant;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Map;

public class QueenCatRenderer extends GeoEntityRenderer<QueenCatEntity> {
    public static final Map<HumanoidCatVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(HumanoidCatVariant.class), (map) -> {
                map.put(HumanoidCatVariant.WHITE,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_cat/humanoid_cat_white.png"));
                map.put(HumanoidCatVariant.BLACK,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_cat/humanoid_cat_black.png"));
                map.put(HumanoidCatVariant.CALICO,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_cat/humanoid_cat_calico.png"));
                map.put(HumanoidCatVariant.CALLAS,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_cat/humanoid_cat_callas.png"));
            });

    public QueenCatRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new QueenCatModel());
    }


    @Override
    public ResourceLocation getTextureLocation(QueenCatEntity instance) {
        if(instance.isMogu()){
            return new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_cat/humanoid_cat_mogu.png");
        }

        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }
}
