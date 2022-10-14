package net.pevori.queencats.entity.custom;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.Team;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.variants.HumanoidCatVariant;
import net.pevori.queencats.entity.variants.HumanoidDogVariant;
import net.pevori.queencats.item.ModItems;
import net.pevori.queencats.sound.ModSounds;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class QueenDogEntity  extends HumanoidDogEntity implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(QueenDogEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(QueenDogEntity.class, EntityDataSerializers.INT);

    public QueenDogEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0, 9.0f, 2.0f, false));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.0f, Ingredient.of(ModItems.GOLDEN_FISH.get()), false));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving() && !this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.humanoiddog.walk", true));
            return PlayState.CONTINUE;
        }

        if (this.isSitting()) {
            event.getController()
                    .setAnimation(new AnimationBuilder().addAnimation("animation.humanoiddog.sitting", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.humanoiddog.idle", true));
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationEvent event){
        if(this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)){
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.humanoiddog.attack", false));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
        data.addAnimationController(new AnimationController(this, "attackController",
                0, this::attackPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if(this.isAggressive()){
            return ModSounds.HUMANOID_DOG_ANGRY.get();
        }

        return ModSounds.HUMANOID_DOG_AMBIENT.get();
    }

    @Override
    public SoundEvent getEatingSound(ItemStack stack) {
        return ModSounds.HUMANOID_DOG_EAT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.HUMANOID_DOG_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.HUMANOID_DOG_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15f, 1.0f);
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
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = ModItems.GOLDEN_BONE.get();
        Ingredient equippableArmor = Ingredient.of(Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.GOLDEN_CHESTPLATE,
                Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE);

        if (isFood(itemstack)) {
            return super.mobInteract(player, hand);
        }

        if (item instanceof DyeItem && this.isOwnedBy(player)) {
            DyeColor dyeColor = ((DyeItem) item).getDyeColor();
            if (dyeColor == DyeColor.BLACK) {
                this.setVariant(HumanoidDogVariant.HUSKY);
            } else if (dyeColor == DyeColor.WHITE) {
                this.setVariant(HumanoidDogVariant.SHIRO);
            } else if (dyeColor == DyeColor.PINK) {
                this.setVariant(HumanoidDogVariant.CREAM);
            }

            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            //this.setPersistent();
            this.setPersistenceRequired();
            return InteractionResult.CONSUME;
        }

        if (this.hasItemInSlot(EquipmentSlot.CHEST) && isTame() && this.isOwnedBy(player) && !this.level.isClientSide() && hand == InteractionHand.MAIN_HAND
                && player.isShiftKeyDown()) {
            if (!player.getAbilities().instabuild) {
                player.addItem(this.getItemBySlot(EquipmentSlot.CHEST));
            }
            this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            return InteractionResult.CONSUME;
        } else if (equippableArmor.test(itemstack) && isTame() && this.isOwnedBy(player) && !this.hasItemInSlot(EquipmentSlot.CHEST)) {
            this.setItemSlot(EquipmentSlot.CHEST, itemstack.copy());
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if ((isMeat(itemstack)) && isTame() && this.getHealth() < getMaxHealth()) {
            if (this.level.isClientSide()) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (!this.level.isClientSide()) {
                    this.heal(10.0f);

                    if (this.getHealth() > getMaxHealth()) {
                        this.setHealth(getMaxHealth());
                    }

                    this.playSound(ModSounds.HUMANOID_DOG_EAT.get(), 1.0f, 1.0f);
                }

                return InteractionResult.SUCCESS;
            }
        }

        else if (item == itemForTaming && !isTame()) {
            if (this.level.isClientSide()) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (!this.level.isClientSide()) {
                    this.playSound(ModSounds.HUMANOID_CAT_EAT.get(), 1.0f, 1.0f);
                    super.tame(player);
                    this.navigation.recomputePath();
                    this.setTarget(null);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                    setSitting(true);
                    this.setHealth(getMaxHealth());
                }

                return InteractionResult.SUCCESS;
            }
        }

        if (isTame() && this.isOwnedBy(player) && !this.level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        if (itemstack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean wantsToAttack(LivingEntity entity, LivingEntity owner) {
        if (!(entity instanceof Creeper) && !(entity instanceof Ghast)) {
            if (entity instanceof HumanoidCatEntity) {
                HumanoidCatEntity humanoid = (HumanoidCatEntity)entity;
                return !humanoid.isTame() || humanoid.getOwner() != owner;
            } else if (entity instanceof HumanoidDogEntity) {
                HumanoidDogEntity humanoid = (HumanoidDogEntity)entity;
                return !humanoid.isTame() || humanoid.getOwner() != owner;
            } else if (entity instanceof Player && owner instanceof Player && !((Player)owner).canHarmPlayer((Player)entity)) {
                return false;
            } else if (entity instanceof AbstractHorse && ((AbstractHorse)entity).isTamed()) {
                return false;
            } else {
                return !(entity instanceof TamableAnimal) || !((TamableAnimal)entity).isTame();
            }
        } else {
            return false;
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBoolean("isSitting"));
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
        tag.putInt("Variant", this.getTypeVariant());
    }

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
    }

    public boolean isMeat(ItemStack pStack) {
        Item item = pStack.getItem();
        return item.isEdible() && pStack.getFoodProperties(this).isMeat();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == ModItems.KEMOMIMI_POTION.get();
    }

    public void setSitting(boolean sitting){
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
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

    public HumanoidDogVariant getVariant() {
        return HumanoidDogVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    protected void setVariant(HumanoidDogVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
}
