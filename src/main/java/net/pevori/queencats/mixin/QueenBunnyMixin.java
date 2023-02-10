package net.pevori.queencats.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.custom.QueenBunnyEntity;
import net.pevori.queencats.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Rabbit.class)
public abstract class QueenBunnyMixin extends Animal {

    protected QueenBunnyMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /*
     * This Mixin injection is to make the rabbit turn into a queen bunny
     * when fed with golden wheat.
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        Rabbit rabbit = ((Rabbit) (Object) this);
        Item usedItem = player.getItemInHand(hand).getItem();

        /*
         * This is the logic to generate a new Queen Bunny, pretty much the same as
         * a pig getting hit by lightning.
         */
        if (usedItem == ModItems.KEMOMIMI_POTION.get()) {
            if (!player.getAbilities().instabuild) {
                player.getItemInHand(hand).shrink(1);
            }

            QueenBunnyEntity queenBunnyEntity = ModEntityTypes.QUEEN_BUNNY.get().create(rabbit.level);
            queenBunnyEntity.moveTo(rabbit.getX(), rabbit.getY(), rabbit.getZ(), rabbit.getYRot(), rabbit.getXRot());
            queenBunnyEntity.setNoAi(rabbit.isNoAi());

            if (rabbit.hasCustomName()) {
                queenBunnyEntity.setCustomName(rabbit.getCustomName());
                queenBunnyEntity.setCustomNameVisible(rabbit.isCustomNameVisible());
            }

            queenBunnyEntity.setPersistenceRequired();
            queenBunnyEntity.setOwnerUUID(player.getUUID());
            queenBunnyEntity.setTame(true);
            queenBunnyEntity.setSitting(true);
            rabbit.level.addFreshEntity(queenBunnyEntity);
            rabbit.discard();
        }

        return super.mobInteract(player, hand);
    }
}
