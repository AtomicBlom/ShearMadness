package com.github.atomicblom.shearmadness.capability;

import com.google.common.base.Objects;
import net.minecraft.item.ItemStack;

public class ChiseledSheepCapability implements IChiseledSheepCapability
{
    private ItemStack itemStack = null;
    private boolean isChiseled = false;

    @Override
    public boolean isChiseled()
    {
        return isChiseled;
    }

    @Override
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

    @Override
    public void setChiseled(boolean chiseled)
    {
        if (!chiseled)
        {
            itemStack = null;
        }
        isChiseled = chiseled;
    }

    @Override
    public ItemStack getChiselItemStack()
    {
        return itemStack;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("itemStack", itemStack)
                .add("isChiseled", isChiseled)
                .toString();
    }
}

