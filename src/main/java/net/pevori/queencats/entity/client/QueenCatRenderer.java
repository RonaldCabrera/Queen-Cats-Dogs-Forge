package net.pevori.queencats.entity.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.PrincessCatEntity;
import net.pevori.queencats.entity.custom.QueenCatEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class QueenCatRenderer extends GeoEntityRenderer<QueenCatEntity> {
    public QueenCatRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new QueenCatModel());
    }

    @Override
    public Identifier getTextureLocation(QueenCatEntity instance) {
        return new Identifier(QueenCats.MOD_ID, "textures/entity/queen_cat/humanoid_cat_white.png");
    }

    @Override
    public RenderLayer getRenderType(QueenCatEntity animatable, float partialTicks, MatrixStack stack,
            VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
            Identifier textureLocation) {

        /*if(animatable.isBaby()) {
            stack.scale(0.5f, 0.5f, 0.5f);
        } else {
            stack.scale(1f, 1f, 1f);
        }*/

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn,
                textureLocation);
    }
}
