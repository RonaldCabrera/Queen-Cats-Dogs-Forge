package net.pevori.queencats.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.PrincessCowEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class PrincessCowModel extends GeoModel<PrincessCowEntity> {
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
    public void setCustomAnimations(PrincessCowEntity entity, long uniqueID, AnimationState<PrincessCowEntity> animationState) {
        super.setCustomAnimations(entity, uniqueID, animationState);
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null) {
            head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }
}