package net.pevori.queencats.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.custom.QueenDogEntity;
import net.pevori.queencats.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public class QueenDogMixin {
    /*
     * This Mixin injection is to make the wolf turn into a queen dog
     * when fed with golden bone.
     */
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

            QueenDogEntity queenDogEntity = ModEntityTypes.QUEEN_DOG.get().create(thisWolf.level);
            queenDogEntity.moveTo(thisWolf.getX(), thisWolf.getY(), thisWolf.getZ(), thisWolf.getYRot(), thisWolf.getXRot());
            queenDogEntity.setNoAi(thisWolf.isNoAi());

            if (thisWolf.hasCustomName()) {
                queenDogEntity.setCustomName(thisWolf.getCustomName());
                queenDogEntity.setCustomNameVisible(thisWolf.isCustomNameVisible());
            }

            queenDogEntity.setPersistenceRequired();
            queenDogEntity.setOwnerUUID(thisWolf.getOwnerUUID());
            queenDogEntity.setTame(true);
            queenDogEntity.setSitting(thisWolf.isInSittingPose());
            thisWolf.level.addFreshEntity(queenDogEntity);
            thisWolf.discard();
        }
    }
}
