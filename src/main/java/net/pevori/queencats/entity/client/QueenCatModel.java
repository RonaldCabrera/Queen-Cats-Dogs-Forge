package net.pevori.queencats.entity.client;

import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.QueenCatEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class QueenCatModel extends AnimatedGeoModel<QueenCatEntity> {
    @Override
    public Identifier getModelLocation(QueenCatEntity object) {
        if(object.hasStackEquipped(EquipmentSlot.CHEST)){
            return new Identifier(QueenCats.MOD_ID, "geo/humanoid_cat_armor.geo.json");
        }

        return new Identifier(QueenCats.MOD_ID, "geo/humanoid_cat.geo.json");
    }

    @Override
    public Identifier getTextureLocation(QueenCatEntity object) {
        return QueenCatRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public Identifier getAnimationFileLocation(QueenCatEntity animatable) {
        return new Identifier(QueenCats.MOD_ID, "animations/humanoid_cat.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(QueenCatEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
