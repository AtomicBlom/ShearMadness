package com.github.atomicblom.shearmadness.variations.chancecubes.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

import static com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubesReference.CHANCE_CUBE_PARTICIPATION;

/**
 * Created by codew on 1/03/2017.
 */
public class ChanceCubeParticipationCapabilityProvider implements ICapabilityProvider, INBTSerializable<INBT> {

    private final ChanceCubeParticipationCapability capability;

    public ChanceCubeParticipationCapabilityProvider() {
        capability = new ChanceCubeParticipationCapability();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CHANCE_CUBE_PARTICIPATION)
        {
            return LazyOptional.of(() -> this.capability).cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT()
    {
        return ChanceCubeParticipationStorage.instance.writeNBT(CHANCE_CUBE_PARTICIPATION, capability, null);
    }

    @Override
    public void deserializeNBT(INBT nbt)
    {
        ChanceCubeParticipationStorage.instance.readNBT(CHANCE_CUBE_PARTICIPATION, capability, null, nbt);
    }
}
