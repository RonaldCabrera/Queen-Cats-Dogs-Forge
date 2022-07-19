package net.pevori.queencats.entity.client;

import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.PrincessCatEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class PrincessCatModel extends AnimatedGeoModel<PrincessCatEntity> {
    @Override
    public Identifier getModelLocation(PrincessCatEntity object) {
        return new Identifier(QueenCats.MOD_ID, "geo/humanoid_cat_children.geo.json");
    }

    @Override
    public Identifier getTextureLocation(PrincessCatEntity object) {
        return new Identifier(QueenCats.MOD_ID, "textures/entity/queen_cat/humanoid_cat_white.png");
    }

    @Override
    public Identifier getAnimationFileLocation(PrincessCatEntity animatable) {
        return new Identifier(QueenCats.MOD_ID, "animations/humanoid_cat.animation.json");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(PrincessCatEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
