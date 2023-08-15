package net.pevori.queencats.entity.custom;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.variants.HumanoidDogVariant;
import net.pevori.queencats.item.ModItems;

import javax.annotation.Nullable;

public class QueenDogEntity  extends HumanoidDogEntity{
    public QueenDogEntity(EntityType<? extends HumanoidDogEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f).build();
    }

    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0, 8.0f, 3.0f, false));        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.0f, Ingredient.of(ModItems.GOLDEN_BONE.get()), false));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    /* Tamable Entity */
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob){
        PrincessDogEntity baby = ModEntityTypes.PRINCESS_DOG.get().create(serverLevel);
        HumanoidDogVariant variant = Util.getRandom(HumanoidDogVariant.values(), this.random);
        baby.setVariant(variant);

        if(this.isTame()){
            baby.tame((Player) this.getOwner());
            baby.setOwnerUUID(this.getOwnerUUID());
        }

        return baby;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        Level level = this.getCommandSenderWorld();
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();

        super.mobInteract(player, hand);

        if (isFood(itemStack)) {
            return super.mobInteract(player, hand);
        }

        if (item instanceof DyeItem && this.isOwnedBy(player) && !player.isShiftKeyDown()) {
            DyeColor dyeColor = ((DyeItem) item).getDyeColor();
            if (dyeColor == DyeColor.BLACK) {
                this.setVariant(HumanoidDogVariant.HUSKY);
            } else if (dyeColor == DyeColor.WHITE) {
                this.setVariant(HumanoidDogVariant.SHIRO);
            } else if (dyeColor == DyeColor.PINK) {
                this.setVariant(HumanoidDogVariant.CREAM);
            }

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            this.setPersistenceRequired();
            return InteractionResult.CONSUME;
        }

        if ((isMeat(itemStack)) && isTame() && this.getHealth() < getMaxHealth() && !player.isShiftKeyDown()) {
            if (level.isClientSide()) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                if (!level.isClientSide()) {
                    this.heal(10.0f);

                    if (this.getHealth() > getMaxHealth()) {
                        this.setHealth(getMaxHealth());
                    }

                    this.playSound(this.getEatingSound(itemStack), 1.0f, 1.0f);
                }

                return InteractionResult.SUCCESS;
            }
        }

        else if (item == itemForTaming && !isTame()) {
            if (level.isClientSide()) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                if (!level.isClientSide()) {
                    this.playSound(this.getEatingSound(itemStack), 1.0f, 1.0f);
                    super.tame(player);
                    this.navigation.recomputePath();
                    this.setTarget(null);
                    level.broadcastEntityEvent(this, (byte) 7);
                    setSitting(true);
                    this.setHealth(getMaxHealth());
                }

                return InteractionResult.SUCCESS;
            }
        }

        if (isTame() && this.isOwnedBy(player) && !player.isShiftKeyDown() && !level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        if (itemStack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }


    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if(tamed){
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(60.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.0D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double) 0.3f);
        }
        else{
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double) 0.3f);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_,
                                        MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_,
                                        @Nullable CompoundTag p_146750_) {
        HumanoidDogVariant variant = Util.getRandom(HumanoidDogVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }
}
