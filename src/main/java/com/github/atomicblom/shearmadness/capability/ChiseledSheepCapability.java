package com.github.atomicblom.shearmadness.capability;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.utility.ItemStackUtils;
import com.google.common.base.Objects;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ChiseledSheepCapability implements IChiseledSheepCapability, IWriteExtraData
{
    private ItemStack itemStack = ItemStack.EMPTY;
    private int itemIdentifier;
    private NBTTagCompound customData = null;

    @Override
    public boolean isChiseled()
    {
        return itemStack != null;
    }

    @Override
    public void chisel(ItemStack itemStack)
    {
        this.itemStack = itemStack;
        itemIdentifier = ItemStackUtils.getHash(itemStack);
    }

    @Override
    public void unChisel() {
        itemStack = ItemStack.EMPTY;
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

