package com.github.atomicblom.shearmadness.capability;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
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

        final NBTTagCompound extraData = instance.getExtraData();
        compound.setTag("extraData", extraData);
        compound.setInteger("itemVariantId", instance.getItemVariantIdentifier());
        return compound;
    }

    @Override
    public void readNBT(Capability<IChiseledSheepCapability> capability, IChiseledSheepCapability instance, EnumFacing side, NBTBase nbt)
    {
        final NBTTagCompound compound = (NBTTagCompound) nbt;

        final boolean isChiseled = compound.getBoolean("isChiseled");
        if (isChiseled)
        {
            final ItemStack stack = new ItemStack(compound.getCompoundTag("chiselDefinition"));
            instance.chisel(stack);
        }

        instance.setItemVariantIdentifier(compound.getInteger("itemVariantId"));

        if (instance instanceof IWriteExtraData) {
            ((IWriteExtraData) instance).setExtraData(compound.getCompoundTag("extraData"));
        }
    }
}
