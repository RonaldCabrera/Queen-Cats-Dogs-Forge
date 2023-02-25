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
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.pevori.queencats.entity.ModEntityTypes;
import net.pevori.queencats.entity.variants.HumanoidCatVariant;
import net.pevori.queencats.item.ModItems;
import software.bernie.geckolib3.core.IAnimatable;

import javax.annotation.Nullable;

public class QueenCatEntity extends HumanoidCatEntity{
    public QueenCatEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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

    /* Tamable Entity */
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob){
        PrincessCatEntity baby = ModEntityTypes.PRINCESS_CAT.get().create(serverLevel);
        HumanoidCatVariant variant = Util.getRandom(HumanoidCatVariant.values(), this.random);
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

        Item itemForTaming = ModItems.GOLDEN_FISH.get();

        Ingredient itemForHealing = Ingredient.of(Items.COD, Items.SALMON, Items.TROPICAL_FISH, ModItems.GOLDEN_FISH.get());
        Ingredient equippableArmor = Ingredient.of(Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.GOLDEN_CHESTPLATE,
                Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE);

        if (isFood(itemstack)) {
            return super.mobInteract(player, hand);
        }

        if (item instanceof DyeItem && this.isOwnedBy(player)) {
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
            this.setItemSlotAndDropWhenKilled(EquipmentSlot.CHEST, itemstack.copy());
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if ((itemForHealing.test(itemstack)) && isTame() && this.getHealth() < getMaxHealth()) {
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

                    this.playSound(this.getEatingSound(itemstack), 1.0f, 1.0f);
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
                    this.playSound(this.getEatingSound(itemstack), 1.0f, 1.0f);
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
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == ModItems.KEMOMIMI_POTION.get();
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
        HumanoidCatVariant variant = Util.getRandom(HumanoidCatVariant.values(), this.random);
        setVariant(variant);
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }
}
