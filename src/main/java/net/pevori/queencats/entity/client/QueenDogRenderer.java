package net.pevori.queencats.entity.client;

import java.util.Map;

import com.google.common.collect.Maps;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.QueenDogEntity;
import net.pevori.queencats.entity.variant.HumanoidDogVariant;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class QueenDogRenderer extends GeoEntityRenderer<QueenDogEntity>{
    public static final Map<HumanoidDogVariant, Identifier> LOCATION_BY_VARIANT =
    Util.make(Maps.newEnumMap(HumanoidDogVariant.class), (map) -> {
        map.put(HumanoidDogVariant.SHIRO,
                new Identifier(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_shiro.png"));
        map.put(HumanoidDogVariant.HUSKY,
                new Identifier(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_husky.png"));
        map.put(HumanoidDogVariant.CREAM,
                new Identifier(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_cream.png"));
    });

    public QueenDogRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new QueenDogModel());
    }

    @Override
    public Identifier getTextureLocation(QueenDogEntity instance) {
        if(instance.isDoog()){
            return new Identifier(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_doog.png");
        }

        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public RenderLayer getRenderType(QueenDogEntity animatable, float partialTicks, MatrixStack stack,
            VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
            Identifier textureLocation) {

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn,
                textureLocation);
    }
}
