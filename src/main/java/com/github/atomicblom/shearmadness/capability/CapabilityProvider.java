package com.github.atomicblom.shearmadness.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.github.atomicblom.shearmadness.api.Capability.CHISELED_SHEEP;

@SuppressWarnings({"ObjectEquality", "ClassHasNoToStringMethod"})
@ParametersAreNonnullByDefault
public class CapabilityProvider implements ICapabilityProvider, INBTSerializable<INBT>
{
    private final IChiseledSheepCapability capability;

    public CapabilityProvider()
    {
        capability = new ChiseledSheepCapability();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CHISELED_SHEEP)
        {
            return LazyOptional.of(() -> this.capability).cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT()
    {
        return ChiseledSheepCapabilityStorage.instance.writeNBT(CHISELED_SHEEP, capability, null);
    }

    @Override
    public void deserializeNBT(INBT nbt)
    {
        ChiseledSheepCapabilityStorage.instance.readNBT(CHISELED_SHEEP, capability, null, nbt);
    }
}

