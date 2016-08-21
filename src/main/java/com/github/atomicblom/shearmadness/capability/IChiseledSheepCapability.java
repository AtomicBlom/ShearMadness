package com.github.atomicblom.shearmadness.capability;

import net.minecraft.item.ItemStack;

public interface IChiseledSheepCapability
{
    boolean isChiseled();

    void setChiseled(boolean chiseled);

    ItemStack getChiselItemStack();

    void setChiselItemStack(ItemStack itemStack);
}
