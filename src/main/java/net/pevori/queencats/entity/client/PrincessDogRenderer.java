package net.pevori.queencats.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.PrincessCatEntity;
import net.pevori.queencats.entity.custom.PrincessDogEntity;
import net.pevori.queencats.entity.variants.HumanoidCatVariant;
import net.pevori.queencats.entity.variants.HumanoidDogVariant;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class PrincessDogRenderer extends GeoEntityRenderer<PrincessDogEntity> {
    public static final Map<HumanoidDogVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(HumanoidDogVariant.class), (map) -> {
                map.put(HumanoidDogVariant.SHIRO,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_husky.png"));
                map.put(HumanoidDogVariant.HUSKY,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_shiro.png"));
                map.put(HumanoidDogVariant.CREAM,
                        new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_cream.png"));
            });

    public PrincessDogRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new PrincessDogModel());
    }


    @Override
    public ResourceLocation getTextureLocation(PrincessDogEntity instance) {
        if(instance.isDoog()){
            return new ResourceLocation(QueenCats.MOD_ID, "textures/entity/queen_dog/humanoid_dog_doog.png");
        }

        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public RenderType getRenderType(PrincessDogEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn,
                textureLocation);
    }
}
