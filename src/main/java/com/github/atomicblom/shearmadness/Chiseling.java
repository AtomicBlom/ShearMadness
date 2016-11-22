package com.github.atomicblom.shearmadness;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.networking.SheepChiseledMessage;
import com.github.atomicblom.shearmadness.utility.ItemStackUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.atomicblom.shearmadness.ShearMadnessMod.CHANNEL;

@SuppressWarnings({"BooleanMethodNameMustStartWithQuestion", "UtilityClass"})
public final class Chiseling
{
    private Chiseling() {}

    public static void chiselSheep(Entity sheep, EntityPlayer entityPlayer, ItemStack activeStack)
    {
        if (sheep.hasCapability(Capability.CHISELED_SHEEP, null))
        {
            final IChiseledSheepCapability capability = sheep.getCapability(Capability.CHISELED_SHEEP, null);
            final ItemStack chiselItemStack = capability.getChiselItemStack();
            if (updateCapability(activeStack, capability, entityPlayer.isCreative()))
            {
                activeStack.damageItem(1, entityPlayer);

                if (!sheep.getEntityWorld().isRemote)
                {
                    CHANNEL.sendToAll(new SheepChiseledMessage(sheep));

                    if (chiselItemStack != null)
                    {
                        ItemStackUtils.dropItem(sheep, chiselItemStack);
                    }
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
            capability.unChisel();
            changed = true;
        }

        return changed;
    }

    private static boolean changeChiselBlock(ItemStack heldChisel, IChiseledSheepCapability capability, boolean isCreative)
    {
        final NBTTagCompound tagCompound = heldChisel.getTagCompound();
        assert tagCompound != null;
        final NBTTagCompound chiselTarget = tagCompound.getCompoundTag("chiselTarget");
        final ItemStack currentChisel = capability.getChiselItemStack();
        final ItemStack chiselItemStack = new ItemStack(chiselTarget);

        if (!checkItemStacksEqual(currentChisel, chiselItemStack))
        {
            final ItemStack copy = chiselItemStack.copy();
            copy.setCount(1);
            capability.chisel(copy);

            if (!isCreative)
            {
                chiselItemStack.shrink(1);
                if (!chiselItemStack.isEmpty())
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

    @SuppressWarnings({"ObjectEquality", "RedundantIfStatement", "MethodWithMoreThanThreeNegations"})
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
