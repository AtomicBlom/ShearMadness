package com.github.atomicblom.shearmadness.capability;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.utility.ItemStackUtils;
import com.google.common.base.Objects;
import net.minecraft.item.ItemStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChiseledSheepCapability implements IChiseledSheepCapability
{
    @Nullable
    private ItemStack itemStack = null;
    private int itemIdentifier;

    @Override
    public boolean isChiseled()
    {
        return itemStack != null;
    }

    @Override
    public void chisel(@Nonnull ItemStack itemStack)
    {
        this.itemStack = itemStack;
        itemIdentifier = ItemStackUtils.getHash(itemStack);
    }

    public void unChisel() {
        itemStack = null;
        itemIdentifier = 0;
    }

    /*@Override
    public void setChiseled(boolean chiseled)
    {
        if (!chiseled)
        {
            itemStack = null;
            itemIdentifier = 0;
        }
        isChiseled = chiseled;
    }*/

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
                .add("isChiseled", isChiseled())
                .toString();
    }

    @Override
    public int getItemIdentifier()
    {
        return itemIdentifier;
    }
}

