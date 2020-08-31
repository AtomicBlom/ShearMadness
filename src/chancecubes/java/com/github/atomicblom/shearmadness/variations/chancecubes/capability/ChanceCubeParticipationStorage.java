package com.github.atomicblom.shearmadness.variations.chancecubes.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ChanceCubeParticipationStorage implements IStorage<IChanceCubeParticipationCapability>
{
    public static final IStorage<IChanceCubeParticipationCapability> instance = new ChanceCubeParticipationStorage();

    @Override
    public INBT writeNBT(Capability<IChanceCubeParticipationCapability> capability, IChanceCubeParticipationCapability instance, Direction side)
    {
        final CompoundNBT compound = new CompoundNBT();
        compound.putBoolean("isParticipating", instance.isParticipating());
        return compound;
    }

    @Override
    public void readNBT(Capability<IChanceCubeParticipationCapability> capability, IChanceCubeParticipationCapability instance, Direction side, INBT nbt)
    {
        final CompoundNBT compound = (CompoundNBT) nbt;
        instance.setParticipation(compound.getBoolean("isParticipating"));
    }
}
