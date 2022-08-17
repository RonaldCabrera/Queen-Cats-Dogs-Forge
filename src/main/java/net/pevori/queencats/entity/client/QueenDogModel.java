package net.pevori.queencats.entity.client;

import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.QueenDogEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class QueenDogModel extends AnimatedGeoModel<QueenDogEntity> {
    @Override
    public Identifier getModelLocation(QueenDogEntity object) {
        if(object.hasStackEquipped(EquipmentSlot.CHEST)){
            return new Identifier(QueenCats.MOD_ID, "geo/humanoid_dog_armor.geo.json");
        }

        return new Identifier(QueenCats.MOD_ID, "geo/humanoid_dog.geo.json");
    }

    @Override
    public Identifier getTextureLocation(QueenDogEntity queenDogEntity) {
        return QueenDogRenderer.LOCATION_BY_VARIANT.get(queenDogEntity.getVariant());
    }

    @Override
    public Identifier getAnimationFileLocation(QueenDogEntity animatable) {
        return new Identifier(QueenCats.MOD_ID, "animations/humanoid_dog.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(QueenDogEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
