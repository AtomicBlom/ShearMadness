package com.github.atomicblom.shearmadness.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ChiseledSheepCapabilityStorage implements IStorage<IChiseledSheepCapability>
{
    public static final IStorage<IChiseledSheepCapability> instance = new ChiseledSheepCapabilityStorage();

    @Override
    public NBTBase writeNBT(Capability<IChiseledSheepCapability> capability, IChiseledSheepCapability instance, EnumFacing side)
    {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("isChiseled", instance.isChiseled());
        if (instance.isChiseled())
        {
            final NBTTagCompound targetTag = new NBTTagCompound();
            instance.getChiselItemStack().writeToNBT(targetTag);
            compound.setTag("chiselDefinition", targetTag);
        }
        return compound;
    }

    @Override
    public void readNBT(Capability<IChiseledSheepCapability> capability, IChiseledSheepCapability instance, EnumFacing side, NBTBase nbt)
    {
        final NBTTagCompound compound = (NBTTagCompound) nbt;

        instance.setChiseled(compound.getBoolean("isChiseled"));
        if (instance.isChiseled())
        {
            final ItemStack stack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("chiselDefinition"));
            instance.setChiselItemStack(stack);
        }
    }
}
