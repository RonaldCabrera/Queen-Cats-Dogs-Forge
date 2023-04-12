package net.pevori.queencats.gui.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.pevori.queencats.entity.custom.HumanoidAnimalEntity;

public class HumanoidAnimalMenu extends AbstractContainerMenu {
    private final Container inventory;
    private int entityId;
    private HumanoidAnimalEntity entity;

    public HumanoidAnimalMenu(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory, new SimpleContainer(19));

        entityId = buf.readInt();
        this.entity = (HumanoidAnimalEntity) playerInventory.player.level.getEntity(entityId);
    }

    public HumanoidAnimalMenu(int syncId, Inventory playerInventory, SimpleContainer inventory) {
        super(ModMenuTypes.HUMANOID_ANIMAL_MENU.get(), syncId);

        checkContainerSize(inventory, 19);
        this.inventory = inventory;

        inventory.startOpen(playerInventory.player);

        //This will add the slot to place the armor in the HumanoidAnimal inventory.
        Slot customSlot = getCustomArmorSlot();
        this.addSlot(customSlot);

        //This will place the slot in the correct locations for a 4x5 Grid. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job
        int m, l;
        //The Humanoid Animal´s inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < getInventoryColumns(); ++l) {
                this.addSlot(new Slot(inventory, 1 + l + m * getInventoryColumns(), 62 + l * 18, 18 + m * 18));
            }
        }

        //The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }

        //The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int invSlot) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int i = this.inventory.getContainerSize();
            if (invSlot < i) {
                if (!this.moveItemStackTo(itemstack1, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i <= 2 || !this.moveItemStackTo(itemstack1, 2, i, false)) {
                int j = i + 27;
                int k = j + 9;
                if (invSlot >= j && invSlot < k) {
                    if (!this.moveItemStackTo(itemstack1, i, j, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (invSlot >= i && invSlot < j) {
                    if (!this.moveItemStackTo(itemstack1, j, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, j, j, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.inventory.stillValid(pPlayer);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.inventory.setChanged();

        this.inventory.stopOpen(player);
    }

    public HumanoidAnimalEntity getEntity(){
        return entity;
    }

    public int getInventoryColumns() {
        return 6;
    }

    // Generates the new armor slot trying to avoid null exceptions.
    public Slot getCustomArmorSlot(){
        return new Slot(inventory, 0, 8, 36) {
            public boolean mayPlace(ItemStack stack) {
                return isValidArmor(stack);
            }

            public boolean isActive() {
                return entity.hasArmorSlot();
                //return true;
            }

            public int getMaxStackSize() {
                return 1;
            }
        };
    }

    public boolean isValidArmor(ItemStack itemStack){
        return Ingredient.of(Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.GOLDEN_CHESTPLATE,
                Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE).test(itemStack);
    }

    @Override
    public void clicked(int pSlotId, int pButton, ClickType pClickType, Player pPlayer) {
        super.clicked(pSlotId, pButton, pClickType, pPlayer);
        ItemStack itemStack = inventory.getItem(pSlotId);

        /*if(entity == null){
            entity = (HumanoidAnimalEntity) pPlayer.level.getEntity(entityId);
        }*/

        if (pSlotId == 0 && pClickType == ClickType.PICKUP){
            // If the slot was empty, equip the armor on the entity
            if(itemStack.isEmpty()){
                entity.unEquipArmor();
            }
            else {
                entity.equipArmor(itemStack);
            }
        }
    }
}
