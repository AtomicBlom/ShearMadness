package com.github.atomicblom.chiselsheep;

import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapabilityProvider;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.networking.SheepChiseledMessage;
import com.github.atomicblom.chiselsheep.utility.ItemStackUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.atomicblom.chiselsheep.ChiselSheepMod.CHANNEL;

/**
 * Created by codew on 21/08/2016.
 */
public class Chiseling
{
    public static void ChiselSheep(Entity sheep, EntityPlayer entityPlayer, ItemStack activeStack)
    {
        if (sheep.hasCapability(ChiseledSheepCapabilityProvider.CHISELED_SHEEP, null))
        {
            final IChiseledSheepCapability capability = sheep.getCapability(ChiseledSheepCapabilityProvider.CHISELED_SHEEP, null);
            final ItemStack chiselItemStack = capability.getChiselItemStack();
            if (updateCapability(activeStack, capability, entityPlayer.isCreative()))
            {
                activeStack.damageItem(1, entityPlayer);

                if (!sheep.worldObj.isRemote && chiselItemStack != null)
                {
                    CHANNEL.sendToAll(new SheepChiseledMessage(sheep));
                    ItemStackUtils.dropItem(sheep, chiselItemStack);
                }
            }
        }
    }

    private static boolean updateCapability(ItemStack heldChisel, IChiseledSheepCapability capability, boolean isCreative)
    {
        boolean changed = false;

        if (hasChiselBlock(heldChisel))
        {
            changed = changeChiselBlock(heldChisel, capability, isCreative);
        } else if (capability.isChiseled())
        {
            capability.setChiseled(false);
            changed = true;
        }

        return changed;
    }

    private static boolean changeChiselBlock(ItemStack heldChisel, IChiseledSheepCapability capability, boolean isCreative)
    {
        final NBTTagCompound tagCompound = heldChisel.getTagCompound();
        final NBTTagCompound chiselTarget = tagCompound.getCompoundTag("chiselTarget");
        final ItemStack currentChisel = capability.getChiselItemStack();
        final ItemStack chiselItemStack = ItemStack.loadItemStackFromNBT(chiselTarget);

        if (!checkItemStacksEqual(currentChisel, chiselItemStack))
        {
            final ItemStack copy = chiselItemStack.copy();
            copy.stackSize = 1;
            capability.setChiselItemStack(copy);

            if (!isCreative)
            {
                chiselItemStack.stackSize--;
                if (chiselItemStack.stackSize > 0)
                {
                    chiselItemStack.writeToNBT(chiselTarget);
                    tagCompound.setTag("chiselTarget", chiselTarget);
                } else
                {
                    tagCompound.removeTag("chiselTarget");
                }
            }
            return true;
        }
        return false;
    }

    private static boolean hasChiselBlock(ItemStack heldChisel)
    {
        final NBTTagCompound tagCompound = heldChisel.getTagCompound();
        if (tagCompound != null)
        {
            final NBTTagCompound chiselTarget = tagCompound.getCompoundTag("chiselTarget");

            if (chiselTarget.hasKey("id"))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean checkItemStacksEqual(ItemStack currentChisel, ItemStack newChisel)
    {
        if (currentChisel == null && newChisel == null)
        {
            return true;
        }
        if (currentChisel == null ^ newChisel == null)
        {
            return false;
        }
        if (newChisel.getItem() != currentChisel.getItem())
        {
            return false;
        }
        if (!ItemStack.areItemStackTagsEqual(newChisel, currentChisel))
        {
            return false;
        }
        if (!newChisel.isItemStackDamageable() && newChisel.getMetadata() != currentChisel.getMetadata())
        {
            return false;
        }
        return true;
    }
}
