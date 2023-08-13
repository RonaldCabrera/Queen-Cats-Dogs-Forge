package net.pevori.queencats.mixin;

import net.minecraft.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.custom.*;
import net.pevori.queencats.entity.variants.HumanoidCatVariant;
import net.pevori.queencats.entity.variants.HumanoidDogVariant;
import net.pevori.queencats.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cat.class)
public abstract class QueenCatMixin extends Animal {
    protected QueenCatMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    //This Mixin injection is to make the Cat turn into a Humanoid Cat when fed with a Kemomimi Potion.
    @Inject(method = "mobInteract", at = @At("HEAD"))
    protected void injectInteractMethod(Player player, InteractionHand hand, CallbackInfoReturnable info) {
        Cat thisCat = ((Cat)(Object)this);
        Item usedItem = player.getItemInHand(hand).getItem();

        /*
         * This is the logic to generate a new Humanoid Cat, pretty much the same as
         * a pig getting hit by lightning.
         */
        if(thisCat.isTame() && thisCat.isOwnedBy(player) && usedItem == ModItems.KEMOMIMI_POTION.get()){
            if (!player.getAbilities().instabuild) {
                player.getItemInHand(hand).shrink(1);
            }

            QueenCatEntity queenCatEntity = ModEntityTypes.QUEEN_CAT.get().create(thisCat.level);
            queenCatEntity.moveTo(thisCat.getX(), thisCat.getY(), thisCat.getZ(), thisCat.getYRot(), thisCat.getXRot());
            queenCatEntity.setNoAi(thisCat.isNoAi());

            if (thisCat.isBaby()) {
                PrincessCatEntity princessCat = ModEntityTypes.PRINCESS_CAT.get().create(thisCat.level);
                spawnHumanoidCat(princessCat, thisCat, player);
            }
            else{
                QueenCatEntity queenCat = ModEntityTypes.QUEEN_CAT.get().create(thisCat.level);
                spawnHumanoidCat(queenCat, thisCat, player);
            }
        }
    }

    public void spawnHumanoidCat(HumanoidCatEntity humanoidCatEntity, Cat cat, Player player){
        humanoidCatEntity.moveTo(cat.getX(), cat.getY(), cat.getZ(), cat.getYRot(), cat.getXRot());
        humanoidCatEntity.setNoAi(cat.isNoAi());

        if (cat.hasCustomName()) {
            humanoidCatEntity.setCustomName(cat.getCustomName());
            humanoidCatEntity.setCustomNameVisible(cat.isCustomNameVisible());
        }

        humanoidCatEntity.setPersistenceRequired();
        humanoidCatEntity.setOwnerUUID(player.getUUID());
        humanoidCatEntity.setTame(true);
        humanoidCatEntity.setSitting(true);

        HumanoidCatVariant variant = Util.getRandom(HumanoidCatVariant.values(), this.random);
        humanoidCatEntity.setVariant(variant);

        cat.level.addFreshEntity(humanoidCatEntity);
        cat.discard();
    }
}
