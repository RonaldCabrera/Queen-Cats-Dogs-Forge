package net.pevori.queencats.mixin;

import net.minecraft.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.custom.*;
import net.pevori.queencats.entity.variants.HumanoidCowVariant;
import net.pevori.queencats.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cow.class)
public abstract class QueenCowMixin extends Animal {
    protected QueenCowMixin(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    protected void injectInteractMethod(Player player, InteractionHand hand, CallbackInfoReturnable info) {
        Cow cow = ((Cow) (Object) this);
        Level level = cow.getCommandSenderWorld();
        Item usedItem = player.getItemInHand(hand).getItem();

        /*
         * This is the logic to generate a new Humanoid Cow, pretty much the same as
         * a pig getting hit by lightning.
         */
        if (usedItem == ModItems.KEMOMIMI_POTION.get()) {
            if (!player.getAbilities().instabuild) {
                player.getItemInHand(hand).shrink(1);
            }

            HumanoidCowEntity humanoidCowEntity = cow.isBaby()
                    ? ModEntityTypes.PRINCESS_COW.get().create(level)
                    : ModEntityTypes.QUEEN_COW.get().create(level);

            spawnHumanoidCow(humanoidCowEntity, cow, player);
        }
    }

    public void spawnHumanoidCow(HumanoidCowEntity humanoidCowEntity, Cow cow, Player player){
        humanoidCowEntity.moveTo(cow.getX(), cow.getY(), cow.getZ(), cow.getYRot(), cow.getXRot());
        humanoidCowEntity.setNoAi(cow.isNoAi());

        if (cow.hasCustomName()) {
            humanoidCowEntity.setCustomName(cow.getCustomName());
            humanoidCowEntity.setCustomNameVisible(cow.isCustomNameVisible());
        }

        humanoidCowEntity.setPersistenceRequired();
        humanoidCowEntity.setOwnerUUID(player.getUUID());
        humanoidCowEntity.setTame(true);
        humanoidCowEntity.setSitting(true);

        HumanoidCowVariant variant = Util.getRandom(HumanoidCowVariant.values(), this.random);
        humanoidCowEntity.setVariant(variant);

        cow.getCommandSenderWorld().addFreshEntity(humanoidCowEntity);
        cow.discard();
    }
}
