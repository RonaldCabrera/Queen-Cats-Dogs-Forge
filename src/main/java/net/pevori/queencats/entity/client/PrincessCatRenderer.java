package net.pevori.queencats.entity.client;

import java.util.Map;

import com.google.common.collect.Maps;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.PrincessCatEntity;
import net.pevori.queencats.entity.variant.PrincessCatVariant;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PrincessCatRenderer extends GeoEntityRenderer<PrincessCatEntity> {
    public static final Map<PrincessCatVariant, Identifier> LOCATION_BY_VARIANT =
    Util.make(Maps.newEnumMap(PrincessCatVariant.class), (map) -> {
        map.put(PrincessCatVariant.WHITE,
                new Identifier(QueenCats.MOD_ID, "textures/entity/queen_cat/humanoid_cat_white.png"));
        map.put(PrincessCatVariant.BLACK,
                new Identifier(QueenCats.MOD_ID, "textures/entity/queen_cat/humanoid_cat_black.png"));
    });

    public PrincessCatRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PrincessCatModel());
    }

    @Override
    public Identifier getTextureLocation(PrincessCatEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }
}
