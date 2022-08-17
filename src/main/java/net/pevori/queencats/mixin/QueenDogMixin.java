package net.pevori.queencats.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.pevori.queencats.entity.ModEntities;
import net.pevori.queencats.entity.custom.QueenCatEntity;
import net.pevori.queencats.entity.custom.QueenDogEntity;
import net.pevori.queencats.item.ModItems;

@Mixin(WolfEntity.class)
public abstract class QueenDogMixin {
    /*
     * This Mixin injection is to make the wolf turn into a queen dog
     * when fed with golden bone. 
     */
    @Inject(method = "interactMob", at = @At("HEAD"))
    protected void injectInteractMethod(PlayerEntity player, Hand hand, CallbackInfoReturnable info) {
        WolfEntity thisWolf = ((WolfEntity)(Object)this);
        Item usedItem = player.getStackInHand(hand).getItem();

        /* 
         * This is the logic to generate a new Queen Dog, pretty much the same as
         * a pig getting hit by lightning.
         */
        if(thisWolf.isTamed() && thisWolf.isOwner(player) && usedItem == ModItems.GOLDEN_BONE){
            if (!player.getAbilities().creativeMode) {
                player.getStackInHand(hand).decrement(1);
            }
            
            QueenDogEntity queenDogEntity = ModEntities.QUEEN_DOG.create(thisWolf.world);
            queenDogEntity.refreshPositionAndAngles(thisWolf.getX(), thisWolf.getY(), thisWolf.getZ(), thisWolf.getYaw(), thisWolf.getPitch());
            queenDogEntity.setAiDisabled(thisWolf.isAiDisabled());
    
            if (thisWolf.hasCustomName()) {
                queenDogEntity.setCustomName(thisWolf.getCustomName());
                queenDogEntity.setCustomNameVisible(thisWolf.isCustomNameVisible());
            }
            
            queenDogEntity.setPersistent();
            queenDogEntity.setOwnerUuid(thisWolf.getOwnerUuid());
            queenDogEntity.setTamed(true);
            queenDogEntity.setSitting(thisWolf.isSitting());
            thisWolf.world.spawnEntity(queenDogEntity);
            thisWolf.discard();            
        }
    }
}
