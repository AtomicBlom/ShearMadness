package com.github.atomicblom.shearmadness.api.capability;

import net.minecraft.item.ItemStack;

public interface IChiseledSheepCapability
{
    boolean isChiseled();

    void unChisel();

    void chisel(ItemStack itemStack);

    ItemStack getChiselItemStack();

    int getItemIdentifier();
}
