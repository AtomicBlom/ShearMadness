package com.github.atomicblom.shearmadness.capability;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.utility.ItemStackUtils;
import com.google.common.base.Objects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChiseledSheepCapability implements IChiseledSheepCapability, IWriteExtraData
{
    @Nullable
    private ItemStack itemStack = null;
    private int itemIdentifier;
    private NBTTagCompound customData = null;

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

    @Override
    public void unChisel() {
        itemStack = null;
        itemIdentifier = 0;
    }

    @Override
    public ItemStack getChiselItemStack()
    {
        return itemStack;
    }

    @Override
    public int getItemIdentifier()
    {
        return itemIdentifier;
    }

    @Override
    public NBTTagCompound getExtraData() {
        if (customData == null) {
            customData = new NBTTagCompound();
        }
        return customData;
    }

    @Override
    public void setExtraData(NBTTagCompound extraData) {
        customData = extraData;
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
}

