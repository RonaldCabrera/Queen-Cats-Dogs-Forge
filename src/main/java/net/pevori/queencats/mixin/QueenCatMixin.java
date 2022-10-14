package net.pevori.queencats.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.custom.QueenCatEntity;
import net.pevori.queencats.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cat.class)
public class QueenCatMixin {
    /*
     * This Mixin injection is to make the cat turn into a queen cat
     * when fed with golden fish.
     */
    @Inject(method = "mobInteract", at = @At("HEAD"))
    protected void injectInteractMethod(Player player, InteractionHand hand, CallbackInfoReturnable info) {
        Cat thisCat = ((Cat)(Object)this);
        Item usedItem = player.getItemInHand(hand).getItem();

        /*
         * This is the logic to generate a new Queen Cat, pretty much the same as
         * a pig getting hit by lightning.
         */
        if(thisCat.isTame() && thisCat.isOwnedBy(player) && usedItem == ModItems.GOLDEN_FISH.get()){
            if (!player.getAbilities().instabuild) {
                player.getItemInHand(hand).shrink(1);
            }

            QueenCatEntity queenCatEntity = ModEntityTypes.QUEEN_CAT.get().create(thisCat.level);
            queenCatEntity.moveTo(thisCat.getX(), thisCat.getY(), thisCat.getZ(), thisCat.getYRot(), thisCat.getXRot());
            queenCatEntity.setNoAi(thisCat.isNoAi());

            if (thisCat.hasCustomName()) {
                queenCatEntity.setCustomName(thisCat.getCustomName());
                queenCatEntity.setCustomNameVisible(thisCat.isCustomNameVisible());
            }

            queenCatEntity.setPersistenceRequired();
            queenCatEntity.setOwnerUUID(thisCat.getOwnerUUID());
            queenCatEntity.setTame(true);
            queenCatEntity.setSitting(thisCat.isInSittingPose());
            thisCat.level.addFreshEntity(queenCatEntity);
            thisCat.discard();
        }
    }
}
