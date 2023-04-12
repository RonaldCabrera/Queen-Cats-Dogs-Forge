package net.pevori.queencats.entity.custom;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.variants.HumanoidCatVariant;
import net.pevori.queencats.item.ModItems;
import net.pevori.queencats.sound.ModSounds;

import javax.annotation.Nullable;

public class PrincessCatEntity extends HumanoidCatEntity{
    public PrincessCatEntity(EntityType<? extends HumanoidCatEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f).build();
    }

    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0, 8.0f, 3.0f, false));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.0f, Ingredient.of(ModItems.GOLDEN_FISH.get()), false));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        super.mobInteract(player, hand);

        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        if (item == itemForGrowth && isTame() && this.isOwnedBy(player) && !player.isShiftKeyDown()) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            startGrowth();
            return InteractionResult.CONSUME;
        }

        if (item instanceof DyeItem && this.isOwnedBy(player) && !player.isShiftKeyDown()) {
            DyeColor dyeColor = ((DyeItem) item).getDyeColor();
            if (dyeColor == DyeColor.BLACK) {
                this.setVariant(HumanoidCatVariant.BLACK);
            } else if (dyeColor == DyeColor.WHITE) {
                this.setVariant(HumanoidCatVariant.WHITE);
            } else if (dyeColor == DyeColor.ORANGE) {
                this.setVariant(HumanoidCatVariant.CALICO);
            } else if (dyeColor == DyeColor.GRAY) {
                this.setVariant(HumanoidCatVariant.CALLAS);
            }

            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            this.setPersistenceRequired();
            return InteractionResult.CONSUME;
        }

        if ((itemForHealing.test(itemstack)) && isTame() && this.getHealth() < getMaxHealth() && !player.isShiftKeyDown()) {
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

                    this.playSound(ModSounds.HUMANOID_CAT_EAT.get(), 1.0f, 1.0f);
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

        if (isTame() && this.isOwnedBy(player) && !player.isShiftKeyDown() && !this.level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        if (itemstack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    public void startGrowth() {
        HumanoidCatVariant variant = this.getVariant();
        QueenCatEntity queenCatEntity = ModEntityTypes.QUEEN_CAT.get().create(this.level);

        queenCatEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        queenCatEntity.setNoAi(this.isNoAi());
        queenCatEntity.setInventory(this.inventory);

        queenCatEntity.setVariant(variant);

        if (this.hasCustomName()) {
            queenCatEntity.setCustomName(this.getCustomName());
            queenCatEntity.setCustomNameVisible(this.isCustomNameVisible());
        }

        queenCatEntity.setPersistenceRequired();
        queenCatEntity.setOwnerUUID(this.getOwnerUUID());
        queenCatEntity.setTame(true);
        queenCatEntity.setSitting(this.isSitting());

        this.level.addFreshEntity(queenCatEntity);
        this.discard();
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if(tamed){
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.5D);
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
        HumanoidCatVariant variant = Util.getRandom(HumanoidCatVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }
}
