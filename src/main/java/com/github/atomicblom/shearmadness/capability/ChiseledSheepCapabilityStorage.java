package com.github.atomicblom.shearmadness.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;

public class ChiseledSheepCapabilityStorage implements Capability.IStorage<IChiseledSheepCapability>
{
    public static final Capability.IStorage<IChiseledSheepCapability> instance = new ChiseledSheepCapabilityStorage();

    @Override
    public INBT writeNBT(Capability<IChiseledSheepCapability> capability, IChiseledSheepCapability instance, Direction side)
    {
        final CompoundNBT compound = new CompoundNBT();
        compound.putBoolean("isChiseled", instance.isChiseled());
        if (instance.isChiseled())
        {
            final CompoundNBT targetTag = new CompoundNBT();
            instance.getChiselItemStack().write(targetTag);
            compound.put("chiselDefinition", targetTag);
        }

        final CompoundNBT extraData = instance.getExtraData();
        compound.put("extraData", extraData);
        compound.putInt("itemVariantId", instance.getItemVariantIdentifier());
        return compound;
    }

    @Override
    public void readNBT(Capability<IChiseledSheepCapability> capability, IChiseledSheepCapability instance, Direction side, INBT nbt)
    {
        final CompoundNBT compound = (CompoundNBT) nbt;

        final boolean isChiseled = compound.getBoolean("isChiseled");
        if (isChiseled)
        {
            final ItemStack stack = ItemStack.read(compound.getCompound("chiselDefinition"));
            instance.chisel(stack);
        }

        instance.setItemVariantIdentifier(compound.getInt("itemVariantId"));

        if (instance instanceof IWriteExtraData) {
            ((IWriteExtraData) instance).setExtraData(compound.getCompound("extraData"));
        }
    }
}
