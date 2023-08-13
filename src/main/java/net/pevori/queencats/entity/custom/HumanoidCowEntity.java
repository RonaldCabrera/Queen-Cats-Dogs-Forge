package net.pevori.queencats.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.Team;
import net.pevori.queencats.QueenCats;
import net.pevori.queencats.entity.variants.HumanoidCowVariant;
import net.pevori.queencats.item.ModItems;
import net.pevori.queencats.sound.ModSounds;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class HumanoidCowEntity extends HumanoidAnimalEntity implements IAnimatable {
    protected AnimationFactory factory = new AnimationFactory(this);
    protected Item itemForTaming = ModItems.GOLDEN_WHEAT.get();
    protected Item itemForGrowth = ModItems.KEMOMIMI_POTION.get();
    protected Ingredient itemForHealing = Ingredient.of(Items.WHEAT, ModItems.GOLDEN_WHEAT.get());

    protected HumanoidCowEntity(EntityType<? extends HumanoidAnimalEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == ModItems.KEMOMIMI_POTION.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if(!QueenCats.enableCowSounds){
            return null;
        }

        return ModSounds.HUMANOID_COW_AMBIENT.get();
    }

    @Override
    public SoundEvent getEatingSound(ItemStack stack) {
        if(!QueenCats.enableCowSounds){
            return null;
        }

        return ModSounds.HUMANOID_COW_EAT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        if(!QueenCats.enableCowSounds){
            return null;
        }

        return ModSounds.HUMANOID_COW_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        if(!QueenCats.enableCowSounds){
            return null;
        }

        return ModSounds.HUMANOID_COW_DEATH.get();
    }

    protected SoundEvent getMilkingSound(){
        if(!QueenCats.enableCowSounds){
            return null;
        }

        return ModSounds.HUMANOID_COW_MILK.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.COW_STEP, 0.15f, 1.0f);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController()
                    .setAnimation(new AnimationBuilder().addAnimation("animation.humanoidcow.sitting",  ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.humanoidcow.walk",  ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.humanoidcow.idle",  ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationEvent event) {
        if(this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.humanoidcow.attack",  ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
        animationData.addAnimationController(new AnimationController(this, "attackController",
                0, this::attackPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    /* TAMEABLE ENTITY */
    protected static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(HumanoidCowEntity.class,
            EntityDataSerializers.BOOLEAN);

    public void setSitting(boolean sitting){
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    public boolean isMilkableVariant(){
        HumanoidCowVariant variant = this.getVariant();

        if(variant != HumanoidCowVariant.MOOSHROOM || variant != HumanoidCowVariant.MOOBLOOM){
            return true;
        }

        return false;
    }

    public boolean isStewableVariant(){
        HumanoidCowVariant variant = this.getVariant();

        if(variant == HumanoidCowVariant.MOOSHROOM || variant == HumanoidCowVariant.MOOBLOOM){
            return true;
        }

        return false;
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

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    /* VARIANTS */
    protected static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(HumanoidCowEntity.class, EntityDataSerializers.INT);

    public HumanoidCowVariant getVariant() {
        return HumanoidCowVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public void setVariant(HumanoidCowVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
}
