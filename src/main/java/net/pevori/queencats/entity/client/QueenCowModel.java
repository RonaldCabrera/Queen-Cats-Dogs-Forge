package net.pevori.queencats.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.QueenCowEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class QueenCowModel extends GeoModel<QueenCowEntity> {
    @Override
    public ResourceLocation getModelResource(QueenCowEntity object) {
        if(object.hasItemInSlot(EquipmentSlot.CHEST)){
            return new ResourceLocation(QueenCats.MOD_ID, "geo/humanoid_cow_armor.geo.json");
        }

        return new ResourceLocation(QueenCats.MOD_ID, "geo/humanoid_cow.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(QueenCowEntity object) {
        return QueenCowRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(QueenCowEntity animatable) {
        return new ResourceLocation(QueenCats.MOD_ID, "animations/humanoid_cow.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setCustomAnimations(QueenCowEntity animatable, long instanceId, AnimationState<QueenCowEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        CoreGeoBone head = getAnimationProcessor().getBone("head");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null) {
            head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }
}
