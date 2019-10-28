package com.github.atomicblom.shearmadness.inventory.container;

import com.github.atomicblom.shearmadness.utility.ContainerTypeLibrary;
import com.github.atomicblom.shearmadness.utility.ItemLibrary;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;

public class NotAChiselContainer extends Container implements IInventoryChangedListener {

    private final PlayerInventory playerInventory;
    private final Hand hand;
    private final Slot lockedSlot;
    private final Inventory notAChiselInventory;

    public NotAChiselContainer(int id, PlayerInventory inv, PacketBuffer extraData) {
        this(id, inv, (Hand.values()[extraData.readInt()]));
    }

    public NotAChiselContainer(int id, PlayerInventory playerInventory, Hand hand) {
        super(ContainerTypeLibrary.not_a_chisel_container_type, id);
        this.playerInventory = playerInventory;
        this.hand = hand;
        ItemStack heldItem = playerInventory.player.getHeldItem(hand);

        ItemStack chiselItem = ItemStack.EMPTY;
        CompoundNBT possibleChiselItemStackNBT = heldItem.getOrCreateTag().getCompound("chiseldata").getCompound("target");
        if (possibleChiselItemStackNBT.contains("id")) {
            chiselItem = ItemStack.read(possibleChiselItemStackNBT);
        }

        notAChiselInventory = new Inventory(chiselItem);
        notAChiselInventory.addListener(this);

        this.addSlot(new Slot(notAChiselInventory, 0, 8 + 4 * 18, 18 + 2));

        int playerInventoryOffset = (1 - 4) * 18 + 2;

        for(int row = 0; row < 3; ++row) {
            for(int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 103 + row * 18 + playerInventoryOffset));
            }
        }

        Slot lockedSlot = null;
        for(int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            Slot slot = new Slot(playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 161 + playerInventoryOffset);
            if (hand == Hand.MAIN_HAND && hotbarSlot == playerInventory.currentItem) {
                lockedSlot = slot;
            }
            this.addSlot(slot);
        }

        this.lockedSlot = lockedSlot;
    }

    @Override
    public void onInventoryChanged(IInventory invBasic) {
        ItemStack heldItem = this.playerInventory.player.getHeldItem(hand);
        CompoundNBT itemData = heldItem.getOrCreateTag();
        CompoundNBT chiselData = new CompoundNBT();
        itemData.put("chiseldata", chiselData);
        ItemStack stackInSlot = invBasic.getStackInSlot(0);
        if (!stackInSlot.isEmpty()) {
            CompoundNBT target = new CompoundNBT();
            stackInSlot.write(target);
            chiselData.put("target", target);
        }

        playerInventory.markDirty();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return playerIn.getHeldItem(hand).getItem() == ItemLibrary.not_a_chisel;
    }

    @Override
    public ItemStack slotClick(int slotIndex, int p_184996_2_, ClickType clickType, PlayerEntity player) {
        if (lockedSlot != null && slotIndex == lockedSlot.slotNumber) {
            return ItemStack.EMPTY;
        }
        return super.slotClick(slotIndex, p_184996_2_, clickType, player);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int selectedSlotIndex) {
        ItemStack lvt_3_1_ = ItemStack.EMPTY;
        Slot selectedSlot = this.inventorySlots.get(selectedSlotIndex);
        if (selectedSlot != null && selectedSlot.getHasStack()) {
            ItemStack itemStack = selectedSlot.getStack();
            if (itemStack.getItem() == ItemLibrary.not_a_chisel) return ItemStack.EMPTY;
            lvt_3_1_ = itemStack.copy();
            if (selectedSlotIndex < 1) {
                if (!this.mergeItemStack(itemStack, 1, 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemStack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack.isEmpty()) {
                selectedSlot.putStack(ItemStack.EMPTY);
            } else {
                selectedSlot.onSlotChanged();
            }

            if (itemStack.getCount() == lvt_3_1_.getCount()) {
                return ItemStack.EMPTY;
            }

            selectedSlot.onTake(player, itemStack);
        }

        return lvt_3_1_;
    }
}
