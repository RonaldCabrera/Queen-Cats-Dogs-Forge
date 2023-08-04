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
import net.pevori.queencats.entity.custom.QueenCowEntity;
import net.pevori.queencats.entity.variants.HumanoidCowVariant;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class QueenCowRenderer extends GeoEntityRenderer<QueenCowEntity> {
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

    public QueenCowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new QueenCowModel());
    }


    @Override
    public ResourceLocation getTextureLocation(QueenCowEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public RenderType getRenderType(QueenCowEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn,
                textureLocation);
    }
}