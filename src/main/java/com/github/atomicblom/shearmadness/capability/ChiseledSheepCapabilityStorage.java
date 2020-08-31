package com.github.atomicblom.shearmadness.capability;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ChiseledSheepCapabilityStorage implements IStorage<IChiseledSheepCapability>
{
    public static final IStorage<IChiseledSheepCapability> instance = new ChiseledSheepCapabilityStorage();

    @Override
    public NBTBase writeNBT(Capability<IChiseledSheepCapability> capability, IChiseledSheepCapability instance, EnumFacing side)
    {
        final CompoundNBT compound = new CompoundNBT();
        compound.setBoolean("isChiseled", instance.isChiseled());
        if (instance.isChiseled())
        {
            final CompoundNBT targetTag = new CompoundNBT();
            instance.getChiselItemStack().writeToNBT(targetTag);
            compound.setTag("chiselDefinition", targetTag);
        }

        final CompoundNBT extraData = instance.getExtraData();
        compound.setTag("extraData", extraData);
        compound.setInteger("itemVariantId", instance.getItemVariantIdentifier());
        return compound;
    }

    @Override
    public void readNBT(Capability<IChiseledSheepCapability> capability, IChiseledSheepCapability instance, EnumFacing side, NBTBase nbt)
    {
        final CompoundNBT compound = (CompoundNBT) nbt;

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
