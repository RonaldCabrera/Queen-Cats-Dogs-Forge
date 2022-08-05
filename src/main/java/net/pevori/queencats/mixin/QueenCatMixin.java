package net.pevori.queencats.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.ModEntities;
import net.pevori.queencats.entity.custom.QueenCatEntity;
import net.pevori.queencats.item.ModItems;

@Mixin(CatEntity.class)
public abstract class QueenCatMixin {
    /*
     * This Mixin injection is to make the cat turn into a queen cat
     * when fed with golden fish. 
     */
    @Inject(method = "interactMob", at = @At("HEAD"))
    protected void injectInteractMethod(PlayerEntity player, Hand hand, CallbackInfoReturnable info) {
        CatEntity thisCat = ((CatEntity)(Object)this);
        Item usedItem = player.getStackInHand(hand).getItem();

        /* 
         * This is the logic to generate a new Queen Cat, pretty much the same as
         * a pig getting hit by lightning.
         */
        if(thisCat.isTamed() && thisCat.isOwner(player) && usedItem == ModItems.GOLDEN_FISH){
            if (!player.getAbilities().creativeMode) {
                player.getStackInHand(hand).decrement(1);
            }
            
            QueenCatEntity queenCatEntity = ModEntities.QUEEN_CAT.create(thisCat.world);
            queenCatEntity.refreshPositionAndAngles(thisCat.getX(), thisCat.getY(), thisCat.getZ(), thisCat.getYaw(), thisCat.getPitch());
            queenCatEntity.setAiDisabled(thisCat.isAiDisabled());
    
            if (thisCat.hasCustomName()) {
                queenCatEntity.setCustomName(thisCat.getCustomName());
                queenCatEntity.setCustomNameVisible(thisCat.isCustomNameVisible());
            }
            
            queenCatEntity.setPersistent();
            queenCatEntity.setOwnerUuid(thisCat.getOwnerUuid());
            queenCatEntity.setTamed(true);
            queenCatEntity.setSitting(thisCat.isSitting());
            thisCat.world.spawnEntity(queenCatEntity);
            thisCat.discard();            
        }
    }
}
