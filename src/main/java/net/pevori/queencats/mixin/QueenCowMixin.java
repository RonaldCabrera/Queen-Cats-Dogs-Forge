package net.pevori.queencats.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.custom.QueenCowEntity;
import net.pevori.queencats.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Cow.class)
public abstract class QueenCowMixin extends Animal {
    protected QueenCowMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /*
     * This Mixin injection is to make the cow turn into a queen cow
     * when fed with golden wheat.
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        Cow cow = ((Cow) (Object) this);
        Item usedItem = player.getItemInHand(hand).getItem();

        /*
         * This is the logic to generate a new Queen Cow, pretty much the same as
         * a pig getting hit by lightning.
         */
        if (usedItem == ModItems.KEMOMIMI_POTION.get()) {
            if (!player.getAbilities().instabuild) {
                player.getItemInHand(hand).shrink(1);
            }

            QueenCowEntity queenCowEntity = ModEntityTypes.QUEEN_COW.get().create(cow.level);
            queenCowEntity.moveTo(cow.getX(), cow.getY(), cow.getZ(), cow.getYRot(), cow.getXRot());
            queenCowEntity.setNoAi(cow.isNoAi());

            if (cow.hasCustomName()) {
                queenCowEntity.setCustomName(cow.getCustomName());
                queenCowEntity.setCustomNameVisible(cow.isCustomNameVisible());
            }

            queenCowEntity.setPersistenceRequired();
            queenCowEntity.setOwnerUUID(player.getUUID());
            queenCowEntity.setTame(true);
            queenCowEntity.setSitting(true);
            cow.level.addFreshEntity(queenCowEntity);
            cow.discard();
        }

        return super.mobInteract(player, hand);
    }
}
