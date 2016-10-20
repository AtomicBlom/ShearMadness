package com.github.atomicblom.shearmadness.capability;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

import static com.github.atomicblom.shearmadness.api.Capability.CHISELED_SHEEP;

@SuppressWarnings({"ObjectEquality", "ConstantConditions", "ClassHasNoToStringMethod"})
public class CapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTBase>
{
    private final IChiseledSheepCapability capability;

    public CapabilityProvider()
    {
        capability = new ChiseledSheepCapability();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CHISELED_SHEEP;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CHISELED_SHEEP)
        {
            return CHISELED_SHEEP.cast(this.capability);
        }
        //noinspection ReturnOfNull
        return null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return ChiseledSheepCapabilityStorage.instance.writeNBT(CHISELED_SHEEP, capability, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        ChiseledSheepCapabilityStorage.instance.readNBT(CHISELED_SHEEP, capability, null, nbt);
    }
}

