package net.pevori.queencats.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.QueenCowEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class QueenCowModel extends AnimatedGeoModel<QueenCowEntity> {
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
    public void setLivingAnimations(QueenCowEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
