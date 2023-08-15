package net.pevori.queencats.mixin;

import net.minecraft.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.custom.*;
import net.pevori.queencats.entity.variants.HumanoidBunnyVariant;
import net.pevori.queencats.entity.variants.HumanoidDogVariant;
import net.pevori.queencats.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Rabbit.class)
public abstract class QueenBunnyMixin extends Animal {

    protected QueenBunnyMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    //This Mixin injection is to make the Rabbit turn into a Humanoid Bunny when fed with a Kemomimi Potion.
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        Rabbit rabbit = ((Rabbit) (Object) this);
        Level level = rabbit.getCommandSenderWorld();
        Item usedItem = player.getItemInHand(hand).getItem();

        /*
         * This is the logic to generate a new Humanoid Bunny, pretty much the same as
         * a pig getting hit by lightning.
         */
        if (usedItem == ModItems.KEMOMIMI_POTION.get()) {
            if (!player.getAbilities().instabuild) {
                player.getItemInHand(hand).shrink(1);
            }

            QueenBunnyEntity queenBunnyEntity = ModEntityTypes.QUEEN_BUNNY.get().create(level);
            queenBunnyEntity.moveTo(rabbit.getX(), rabbit.getY(), rabbit.getZ(), rabbit.getYRot(), rabbit.getXRot());
            queenBunnyEntity.setNoAi(rabbit.isNoAi());

            if (rabbit.isBaby()) {
                PrincessBunnyEntity princessBunny = ModEntityTypes.PRINCESS_BUNNY.get().create(level);
                spawnHumanoidBunny(princessBunny, rabbit, player);
            }
            else{
                QueenBunnyEntity queenBunny = ModEntityTypes.QUEEN_BUNNY.get().create(level);
                spawnHumanoidBunny(queenBunny, rabbit, player);
            }
        }

        return super.mobInteract(player, hand);
    }

    public void spawnHumanoidBunny(HumanoidBunnyEntity humanoidBunnyEntity, Rabbit rabbit, Player player){
        humanoidBunnyEntity.moveTo(rabbit.getX(), rabbit.getY(), rabbit.getZ(), rabbit.getYRot(), rabbit.getXRot());
        humanoidBunnyEntity.setNoAi(rabbit.isNoAi());

        if (rabbit.hasCustomName()) {
            humanoidBunnyEntity.setCustomName(rabbit.getCustomName());
            humanoidBunnyEntity.setCustomNameVisible(rabbit.isCustomNameVisible());
        }

        humanoidBunnyEntity.setPersistenceRequired();
        humanoidBunnyEntity.setOwnerUUID(player.getUUID());
        humanoidBunnyEntity.setTame(true);
        humanoidBunnyEntity.setSitting(true);

        HumanoidBunnyVariant variant = Util.getRandom(HumanoidBunnyVariant.values(), this.random);
        humanoidBunnyEntity.setVariant(variant);

        rabbit.getCommandSenderWorld().addFreshEntity(humanoidBunnyEntity);
        rabbit.discard();
    }
}
