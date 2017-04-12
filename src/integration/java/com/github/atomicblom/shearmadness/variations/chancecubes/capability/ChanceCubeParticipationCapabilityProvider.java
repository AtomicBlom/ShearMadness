package com.github.atomicblom.shearmadness.variations.chancecubes.capability;

import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapability;
import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapabilityStorage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

import static com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubesReference.CHANCE_CUBE_PARTICIPATION;

/**
 * Created by codew on 1/03/2017.
 */
public class ChanceCubeParticipationCapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTBase> {

    private final ChanceCubeParticipationCapability capability;

    public ChanceCubeParticipationCapabilityProvider() {
        capability = new ChanceCubeParticipationCapability();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CHANCE_CUBE_PARTICIPATION;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CHANCE_CUBE_PARTICIPATION)
        {
            return CHANCE_CUBE_PARTICIPATION.cast(this.capability);
        }
        //noinspection ReturnOfNull
        return null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return ChanceCubeParticipationStorage.instance.writeNBT(CHANCE_CUBE_PARTICIPATION, capability, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        ChanceCubeParticipationStorage.instance.readNBT(CHANCE_CUBE_PARTICIPATION, capability, null, nbt);
    }
}
