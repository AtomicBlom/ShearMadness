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

    public static class ExtentionStorage implements Capability.IStorage<IChiseledSheepCapability> {
        public static final ExtentionStorage instance = new ExtentionStorage();

        @Override
        public NBTBase writeNBT(Capability<IChiseledSheepCapability> capability, IChiseledSheepCapability instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setBoolean("isChiseled", instance.isChiseled());
            if (instance.isChiseled()) {
                NBTTagCompound targetTag = new NBTTagCompound();
                instance.getChiselItemStack().writeToNBT(targetTag);
                compound.setTag("chiselDefinition", targetTag);
            }
            return compound;
        }

        @Override
        public void readNBT(Capability<IChiseledSheepCapability> capability, IChiseledSheepCapability instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound compound = (NBTTagCompound)nbt;

            instance.setChiseled(compound.getBoolean("isChiseled"));
            if (instance.isChiseled()) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("chiselDefinition"));
                instance.setChiselItemStack(stack);
            }
        }
    }

}

