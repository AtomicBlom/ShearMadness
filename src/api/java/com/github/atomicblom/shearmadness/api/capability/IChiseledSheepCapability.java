package com.github.atomicblom.shearmadness.api.capability;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface IChiseledSheepCapability
{
    boolean isChiseled();

    void unChisel();

    void chisel(ItemStack itemStack);

    ItemStack getChiselItemStack();

    int getItemIdentifier();

    void setItemVariantIdentifier(int variantIdentifier);

    int getItemVariantIdentifier();

    CompoundNBT getExtraData();
}
