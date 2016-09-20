package com.github.atomicblom.shearmadness.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ConfigureBreedingContainer extends Container {
    private static final int PLAYER_INVENTORY_ROWS = 3;
    private static final int PLAYER_INVENTORY_COLUMNS = 9;

    public ConfigureBreedingContainer(InventoryPlayer inventory) {
        addPlayerInventory(inventory, 8, 142);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    void addPlayerInventory(InventoryPlayer playerInventory, int xOffset, int yOffset)
    {
        for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex)
        {
            addInventoryRowSlots(playerInventory, xOffset, yOffset, inventoryRowIndex);
        }

        addActionBarSlots(playerInventory, xOffset, yOffset);
    }

    private void addInventoryRowSlots(InventoryPlayer playerInventory, int xOffset, int yOffset, int rowIndex)
    {
        for (int inventoryColumnIndex = 0; inventoryColumnIndex < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex)
        {
            //noinspection ObjectAllocationInLoop
            addSlotToContainer(new Slot(playerInventory, inventoryColumnIndex + rowIndex * 9 + 9, xOffset + inventoryColumnIndex * 18, yOffset + rowIndex * 18));
        }
    }

    private void addActionBarSlots(InventoryPlayer playerInventory, int xOffset, int yOffset)
    {
        for (int actionBarSlotIndex = 0; actionBarSlotIndex < 9; ++actionBarSlotIndex)
        {
            //noinspection ObjectAllocationInLoop
            addSlotToContainer(new Slot(playerInventory, actionBarSlotIndex, xOffset + actionBarSlotIndex * 18, yOffset + 58));
        }
    }
}
