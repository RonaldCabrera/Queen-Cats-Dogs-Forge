package net.pevori.queencats.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.PrincessCowEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class PrincessCowModel extends AnimatedGeoModel<PrincessCowEntity> {
    @Override
    public ResourceLocation getModelResource(PrincessCowEntity object) {
        return new ResourceLocation(QueenCats.MOD_ID, "geo/humanoid_cow_children.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PrincessCowEntity object) {
        return PrincessCowRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(PrincessCowEntity animatable) {
        return new ResourceLocation(QueenCats.MOD_ID, "animations/humanoid_cow.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(PrincessCowEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}