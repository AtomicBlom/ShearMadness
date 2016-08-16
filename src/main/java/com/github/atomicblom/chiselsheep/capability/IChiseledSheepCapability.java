package com.github.atomicblom.chiselsheep.capability;

import net.minecraft.item.ItemStack;

/**
 * Created by steblo on 16/08/2016.
 */
public interface IChiseledSheepCapability {
    boolean isChiseled();

    void setChiseled(boolean chiseled);

    void setChiselItemStack(ItemStack itemStack);

    ItemStack getChiselItemStack();
}
