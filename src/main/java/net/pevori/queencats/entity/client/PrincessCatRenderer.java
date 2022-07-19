package net.pevori.queencats.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.PrincessCatEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PrincessCatRenderer extends GeoEntityRenderer<PrincessCatEntity> {
    public PrincessCatRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PrincessCatModel());
    }

    @Override
    public Identifier getTextureLocation(PrincessCatEntity instance) {
        return new Identifier(QueenCats.MOD_ID, "textures/entity/queen_cat/humanoid_cat_white.png");
    }
}
