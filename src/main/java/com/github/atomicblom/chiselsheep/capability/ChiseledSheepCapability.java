package com.github.atomicblom.chiselsheep.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by steblo on 16/08/2016.
 */
public class ChiseledSheepCapability implements IChiseledSheepCapability {
    private ItemStack itemStack;
    private boolean isChiseled = false;

    @Override
    public void setChiselItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            setChiseled(false);
        } else {
            setChiseled(true);
            this.itemStack = itemStack;
        }
    }

    public ItemStack getChiselItemStack() {
        return itemStack;
    }

    public boolean isChiseled() {
        return isChiseled;
    }

    public void setChiseled(boolean chiseled) {
        if (!chiseled) {
            itemStack = null;
        }
        isChiseled = chiseled;
    }
}

