package com.github.atomicblom.shearmadness.capability;

import com.github.atomicblom.shearmadness.utility.ItemStackUtils;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ChiseledSheepCapability implements IChiseledSheepCapability, IWriteExtraData
{
    private ItemStack itemStack = ItemStack.EMPTY;
    private int itemIdentifier;
    private int variantIdentifier;
    private CompoundNBT customData = null;


    @Override
    public boolean isChiseled()
    {
        return !itemStack.isEmpty();
    }

    @Override
    public void chisel(@Nonnull ItemStack itemStack)
    {
        Preconditions.checkNotNull(itemStack);
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
        return itemIdentifier * 31 + variantIdentifier;
    }

    @Override
    public void setItemVariantIdentifier(int variantIdentifier) {
        this.variantIdentifier = variantIdentifier;
    }

    @Override
    public int getItemVariantIdentifier() {
        return variantIdentifier;
    }

    @Override
    public CompoundNBT getExtraData() {
        if (customData == null) {
            customData = new CompoundNBT();
        }
        return customData;
    }

    @Override
    public void setExtraData(CompoundNBT extraData) {
        customData = extraData;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("itemIdentifier", itemIdentifier)
                .add("variantIdentifier", variantIdentifier)
                .add("itemStack", itemStack)
                .add("isChiseled", isChiseled())
                .toString();
    }
}

