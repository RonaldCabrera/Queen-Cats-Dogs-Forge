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
import net.minecraft.world.entity.*;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.pevori.queencats.entity.variants.HumanoidCatVariant;
import net.pevori.queencats.item.ModItems;
import net.pevori.queencats.sound.ModSounds;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Formattable;

public class HumanoidCatEntity extends HumanoidAnimalEntity implements IAnimatable {
    public static final String okayuSan = "okayu";
    protected AnimationFactory factory = new AnimationFactory(this);
    protected Item itemForTaming = ModItems.GOLDEN_FISH.get();
    protected Item itemForGrowth = ModItems.KEMOMIMI_POTION.get();
    protected Ingredient itemForHealing = Ingredient.of(Items.COD, Items.SALMON, Items.TROPICAL_FISH, ModItems.GOLDEN_FISH.get());
    protected static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(HumanoidCatEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(HumanoidCatEntity.class, EntityDataSerializers.INT);

    protected HumanoidCatEntity(EntityType<? extends HumanoidAnimalEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == ModItems.KEMOMIMI_POTION.get();
    }

    public boolean isMogu(){
        String s = this.getName().getString();
        return (s != null && s.toLowerCase().contains(okayuSan));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving() && !this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.humanoidcat.walk", true));
            return PlayState.CONTINUE;
        }

        if (this.isSitting()) {
            event.getController()
                    .setAnimation(new AnimationBuilder().addAnimation("animation.humanoidcat.sitting", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.humanoidcat.idle", true));
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationEvent event){
        if(this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)){
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.humanoidcat.attack", false));
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
        return ModSounds.HUMANOID_CAT_AMBIENT.get();
    }

    @Override
    public SoundEvent getEatingSound(ItemStack stack) {
        return ModSounds.HUMANOID_CAT_EAT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.HUMANOID_CAT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.HUMANOID_CAT_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15f, 1.0f);
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

    public HumanoidCatVariant getVariant() {
        return HumanoidCatVariant.byId(this.getTypeVariant() & 255);
    }

    protected int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public void setVariant(HumanoidCatVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
}
