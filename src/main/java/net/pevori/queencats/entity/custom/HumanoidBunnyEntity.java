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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.Team;
import net.pevori.queencats.entity.variants.HumanoidBunnyVariant;
import net.pevori.queencats.item.ModItems;
import net.pevori.queencats.sound.ModSounds;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class HumanoidBunnyEntity extends HumanoidAnimalEntity implements GeoEntity {
    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    protected Item itemForTaming = ModItems.GOLDEN_WHEAT.get();
    protected Item itemForGrowth = ModItems.KEMOMIMI_POTION.get();
    protected Ingredient itemForHealing = Ingredient.of(Items.CARROT, Items.WHEAT, ModItems.GOLDEN_WHEAT.get(), Items.GOLDEN_CARROT);
    protected static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(HumanoidBunnyEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(HumanoidBunnyEntity.class, EntityDataSerializers.INT);
    public static final String pekoSan = "pekora";

    protected HumanoidBunnyEntity(EntityType<? extends HumanoidAnimalEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    public boolean isAlmond(){
        String name = this.getName().getString();
        return name.toLowerCase().contains(pekoSan);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == ModItems.KEMOMIMI_POTION.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.HUMANOID_BUNNY_AMBIENT.get();
    }

    @Override
    public SoundEvent getEatingSound(ItemStack stack) {
        return ModSounds.HUMANOID_BUNNY_EAT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.HUMANOID_BUNNY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.HUMANOID_BUNNY_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.RABBIT_JUMP, 0.15f, 1.0f);
    }

    private PlayState predicate(AnimationState animationState) {
        if (this.isSitting()) {
            animationState.getController().setAnimation(RawAnimation.begin().then("animation.humanoidbunny.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (animationState.isMoving()) {
            animationState.getController().setAnimation(RawAnimation.begin().then("animation.humanoidbunny.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        animationState.getController().setAnimation(RawAnimation.begin().then("animation.humanoidbunny.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationState state) {
        if(this.swinging && state.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            state.getController().forceAnimationReset();
            state.getController().setAnimation(RawAnimation.begin().then("animation.humanoidbunny.attack", Animation.LoopType.PLAY_ONCE));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller",
                0, this::predicate));
        controllers.add(new AnimationController(this, "attackController",
                0, this::attackPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    /* TAMEABLE ENTITY */
    public void setSitting(boolean sitting){
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
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
    public HumanoidBunnyVariant getVariant() {
        return HumanoidBunnyVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    protected void setVariant(HumanoidBunnyVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
}
