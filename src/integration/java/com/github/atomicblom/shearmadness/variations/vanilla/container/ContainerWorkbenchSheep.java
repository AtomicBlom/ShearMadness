package com.github.atomicblom.shearmadness.variations.vanilla.container;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.variations.vanilla.behaviour.FollowAutoCraftItems;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ContainerWorkbenchSheep extends ContainerWorkbench
{
    private final EntityLiving entity;
    private final World world;
    private final IChiseledSheepCapability capability;

    public ContainerWorkbenchSheep(InventoryPlayer playerInventory, World worldIn, EntityLiving entity)
    {
        super(playerInventory, worldIn, entity.getPosition());
        this.entity = entity;
        this.world = worldIn;
        capability = entity.getCapability(Capability.CHISELED_SHEEP, null);
        onContainerOpened();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (capability == null) {
            return false;
        }

        if (!ItemStackHelper.isStackForBlock(capability.getChiselItemStack(), Blocks.CRAFTING_TABLE)) {
            return false;
        }

        return playerIn.getDistanceSq(entity.getPosition()) <= 64.0D;
    }

    private void onContainerOpened() {
        if (!world.isRemote) {
            final NBTTagCompound extraData = capability.getExtraData();
            final NBTTagCompound craftMatrixNBT = extraData.getCompoundTag("AUTO_CRAFT");

            for (int i = 0; i < 9; ++i)
            {
                final String key = ((Integer) i).toString();

                if (craftMatrixNBT.hasKey(key)) {
                    final ItemStack itemstack = new ItemStack(craftMatrixNBT.getCompoundTag(key));
                    craftMatrix.setInventorySlotContents(i, itemstack);
                }
            }

            detectAndSendChanges();
            onCraftMatrixChanged(craftMatrix);
        }
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        if (!world.isRemote)
        {
            final NBTTagCompound extraData = capability.getExtraData();
            final NBTTagCompound craftMatrixNBT = new NBTTagCompound();

            for (int i = 0; i < 9; ++i)
            {
                final ItemStack itemstack = craftMatrix.removeStackFromSlot(i);

                if (itemstack != null)
                {
                    craftMatrixNBT.setTag(((Integer)i).toString(), itemstack.serializeNBT());
                }
            }

            extraData.setTag("AUTO_CRAFT", craftMatrixNBT);
            craftMatrixNBT.setLong("lastChanged", this.entity.getEntityWorld().getTotalWorldTime());
        }

        super.onContainerClosed(playerIn);
    }
}
