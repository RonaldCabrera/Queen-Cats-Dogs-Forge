package net.pevori.queencats.mixin;

import net.minecraft.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.custom.*;
import net.pevori.queencats.entity.variants.HumanoidDogVariant;
import net.pevori.queencats.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public abstract class QueenDogMixin extends Animal {
    protected QueenDogMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    //This Mixin injection is to make the wolf turn into a Humanoid Dog when fed with a Kemomimi Potion.
    @Inject(method = "mobInteract", at = @At("HEAD"))
    protected void injectInteractMethod(Player player, InteractionHand hand, CallbackInfoReturnable info) {
        Wolf thisWolf = ((Wolf) (Object) this);
        Item usedItem = player.getItemInHand(hand).getItem();

        /*
         * This is the logic to generate a new Queen Dog, pretty much the same as
         * a pig getting hit by lightning.
         */
        if (thisWolf.isTame() && thisWolf.isOwnedBy(player) && usedItem == ModItems.KEMOMIMI_POTION.get()) {
            if (!player.getAbilities().instabuild) {
                player.getItemInHand(hand).shrink(1);
            }

            if (thisWolf.isBaby()) {
                PrincessDogEntity princessDog = ModEntityTypes.PRINCESS_DOG.get().create(thisWolf.level);
                spawnHumanoidDog(princessDog, thisWolf, player);
            }
            else{
                QueenDogEntity queenDog = ModEntityTypes.QUEEN_DOG.get().create(thisWolf.level);
                spawnHumanoidDog(queenDog, thisWolf, player);
            }
        }
    }

    public void spawnHumanoidDog(HumanoidDogEntity humanoidDogEntity, Wolf wolf, Player player){
        humanoidDogEntity.moveTo(wolf.getX(), wolf.getY(), wolf.getZ(), wolf.getYRot(), wolf.getXRot());
        humanoidDogEntity.setNoAi(wolf.isNoAi());

        if (wolf.hasCustomName()) {
            humanoidDogEntity.setCustomName(wolf.getCustomName());
            humanoidDogEntity.setCustomNameVisible(wolf.isCustomNameVisible());
        }

        humanoidDogEntity.setPersistenceRequired();
        humanoidDogEntity.setOwnerUUID(player.getUUID());
        humanoidDogEntity.setTame(true);
        humanoidDogEntity.setSitting(true);

        HumanoidDogVariant variant = Util.getRandom(HumanoidDogVariant.values(), this.random);
        humanoidDogEntity.setVariant(variant);

        wolf.level.addFreshEntity(humanoidDogEntity);
        wolf.discard();
    }
}