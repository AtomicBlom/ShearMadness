package com.github.atomicblom.shearmadness.variations.chancecubes.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ChanceCubeParticipationStorage implements IStorage<IChanceCubeParticipationCapability>
{
    public static final IStorage<IChanceCubeParticipationCapability> instance = new ChanceCubeParticipationStorage();

    @Override
    public NBTBase writeNBT(Capability<IChanceCubeParticipationCapability> capability, IChanceCubeParticipationCapability instance, EnumFacing side)
    {
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("isParticipating", instance.isParticipating());
        return compound;
    }

    @Override
    public void readNBT(Capability<IChanceCubeParticipationCapability> capability, IChanceCubeParticipationCapability instance, EnumFacing side, NBTBase nbt)
    {
        final NBTTagCompound compound = (NBTTagCompound) nbt;
        instance.setParticipation(compound.getBoolean("isParticipating"));
    }
}
