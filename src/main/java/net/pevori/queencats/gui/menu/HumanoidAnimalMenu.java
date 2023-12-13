package net.pevori.queencats.gui.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.pevori.queencats.entity.custom.HumanoidAnimalEntity;

public class HumanoidAnimalMenu extends AbstractContainerMenu {
    private SimpleContainer inventory;
    private int entityId;
    private HumanoidAnimalEntity entity;

    public HumanoidAnimalMenu(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory, (HumanoidAnimalEntity) Minecraft.getInstance().level.getEntity(buf.readInt()));

        this.entityId = buf.readInt();

        this.inventory = new SimpleContainer(19);
        this.entity = (HumanoidAnimalEntity) playerInventory.player.level.getEntity(entityId);
        this.inventory.startOpen(playerInventory.player);
    }

    public HumanoidAnimalMenu(int syncId, Inventory playerInventory, HumanoidAnimalEntity entity) {
        super(ModMenuTypes.HUMANOID_ANIMAL_MENU.get(), syncId);
        SimpleContainer entityInventory = entity.getInventory();
        checkContainerSize(entityInventory, entity.getInventorySize());
        this.inventory = entityInventory;

        this.addSlot(getCustomArmorSlot());
        this.addHumanoidAnimalInventory(inventory);
        this.addPlayerInventory(playerInventory);
    }

    private void addHumanoidAnimalInventory(SimpleContainer inventory) {
        int m, l;
        //The Humanoid Animal´s inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < getInventoryColumns(); ++l) {
                this.addSlot(new Slot(inventory, 1 + l + m * getInventoryColumns(), 62 + l * 18, 18 + m * 18));
            }
        }
    }

    private void addPlayerInventory(Inventory playerInventory) {
        int l;
        int m;
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
        this.inventory.stopOpen(player);
    }

    public HumanoidAnimalEntity getEntity(){
        return entity;
    }

    public int getInventoryColumns() {
        return 6;
    }

    public Slot getCustomArmorSlot(){
        return new Slot(inventory, 0, 8, 36) {
            public boolean mayPlace(ItemStack stack) {
                return isValidArmor(stack);
            }

            public boolean isActive() {
                return entity.hasArmorSlot();
            }

            public int getMaxStackSize() {
                return 1;
            }
        };
    }

    public boolean isValidArmor(ItemStack itemStack){
        return itemStack.getItem() instanceof ArmorItem item && item.getSlot() == EquipmentSlot.CHEST;
    }
}
