package com.github.atomicblom.shearmadness.capability;

import com.github.atomicblom.shearmadness.utility.ItemStackUtils;
import com.google.common.base.Objects;
import net.minecraft.item.ItemStack;

public class ChiseledSheepCapability implements IChiseledSheepCapability
{
    private ItemStack itemStack = null;
    private boolean isChiseled = false;
    private int itemIdentifier;

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
            itemIdentifier = 0;
        } else
        {
            setChiseled(true);
            this.itemStack = itemStack;
            if (itemStack != null)
            {
                itemIdentifier = ItemStackUtils.getHash(itemStack);
            } else {
                itemIdentifier = 0;
            }


        }
    }

    @Override
    public void setChiseled(boolean chiseled)
    {
        if (!chiseled)
        {
            itemStack = null;
            itemIdentifier = 0;
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
                .add("itemIdentifier", itemIdentifier)
                .add("itemStack", itemStack)
                .add("isChiseled", isChiseled)
                .toString();
    }

    @Override
    public int getItemIdentifier()
    {
        return itemIdentifier;
    }
}

