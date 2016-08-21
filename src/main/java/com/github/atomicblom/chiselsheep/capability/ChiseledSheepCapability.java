package com.github.atomicblom.chiselsheep.capability;

import net.minecraft.item.ItemStack;

/**
 * Created by steblo on 16/08/2016.
 */
public class ChiseledSheepCapability implements IChiseledSheepCapability
{
    private ItemStack itemStack;
    private boolean isChiseled = false;

    public boolean isChiseled()
    {
        return isChiseled;
    }    @Override
    public void setChiselItemStack(ItemStack itemStack)
    {
        if (itemStack == null)
        {
            setChiseled(false);
        } else
        {
            setChiseled(true);
            this.itemStack = itemStack;
        }
    }

    public void setChiseled(boolean chiseled)
    {
        if (!chiseled)
        {
            itemStack = null;
        }
        isChiseled = chiseled;
    }

    public ItemStack getChiselItemStack()
    {
        return itemStack;
    }
}

