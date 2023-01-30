package net.pevori.queencats.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.custom.QueenCatEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class QueenCatModel extends GeoModel<QueenCatEntity> {
    @Override
    public ResourceLocation getModelResource(QueenCatEntity object) {
        if(object.hasItemInSlot(EquipmentSlot.CHEST)){
            return new ResourceLocation(QueenCats.MOD_ID, "geo/humanoid_cat_armor.geo.json");
        }

        return new ResourceLocation(QueenCats.MOD_ID, "geo/humanoid_cat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(QueenCatEntity object) {
        return QueenCatRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(QueenCatEntity animatable) {
        return new ResourceLocation(QueenCats.MOD_ID, "animations/humanoid_cat.animation.json");
    }

    @Override
    public void setCustomAnimations(QueenCatEntity animatable, long instanceId, AnimationState<QueenCatEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        CoreGeoBone head = getAnimationProcessor().getBone("head");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null) {
            head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }
}
