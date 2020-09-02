package com.github.atomicblom.shearmadness.variations.vanilla.container;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ContainerWorkbenchSheep extends WorkbenchContainer
{
    private final LivingEntity entity;
    private final World world;
    private final PlayerEntity player;

    public ContainerWorkbenchSheep(int windowId, PlayerInventory playerInventory, SheepEntity sheep) {
        super(windowId, playerInventory);
        this.entity = sheep;
        this.player = playerInventory.player;
        world = sheep.world;
        onContainerOpened();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return entity.getCapability(Capability.CHISELED_SHEEP).map(capability -> {
            if (!ItemStackHelper.isStackForBlock(capability.getChiselItemStack(), Blocks.CRAFTING_TABLE)) {
                return false;
            }
            return playerIn.getDistanceSq(entity) <= 64.0D;
        }).orElse(false);
    }

    private void onContainerOpened() {
        if (!world.isRemote) {
            entity.getCapability(Capability.CHISELED_SHEEP).ifPresent(capability -> {
                final CompoundNBT extraData = capability.getExtraData();
                final CompoundNBT craftMatrixNBT = extraData.getCompound("AUTO_CRAFT");

                for (int i = 0; i < 9; ++i)
                {
                    final String key = ((Integer) i).toString();

                    if (craftMatrixNBT.contains(key)) {
                        final ItemStack itemstack = ItemStack.read(craftMatrixNBT.getCompound(key));
                        craftMatrix.setInventorySlotContents(i, itemstack);
                    }
                }

                detectAndSendChanges();
                onCraftMatrixChanged(craftMatrix);
            });
        }
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(PlayerEntity playerIn)
    {
        //if (!world.isRemote)
        //{
            entity.getCapability(Capability.CHISELED_SHEEP).ifPresent(capability -> {
                final CompoundNBT extraData = capability.getExtraData();
                final CompoundNBT craftMatrixNBT = new CompoundNBT();

                for (int i = 0; i < 9; ++i) {
                    final ItemStack itemstack = craftMatrix.removeStackFromSlot(i);

                    if (!itemstack.isEmpty()) {
                        craftMatrixNBT.put(((Integer) i).toString(), itemstack.serializeNBT());
                    }
                }

                extraData.put("AUTO_CRAFT", craftMatrixNBT);
                craftMatrixNBT.putLong("lastChanged", entity.getEntityWorld().getGameTime());
            });
        //}

        super.onContainerClosed(playerIn);
    }

    public void onCraftMatrixChanged(IInventory inventoryIn) {
        //Ignore IWorldPosCallable, because there is no block in the world.
        updateCraftingResult(this.windowId, this.entity.world, this.player, this.craftMatrix, this.craftResult);
    }

    public CraftingInventory getCraftingMatrix() {
        return craftMatrix;
    }
}
