package com.github.atomicblom.chiselsheep.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import javax.annotation.Nullable;

public class ChiseledSheepCapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTBase> {

    @CapabilityInject(IChiseledSheepCapability.class)
    public static final Capability<IChiseledSheepCapability> CHISELED_SHEEP = null;

    private final IChiseledSheepCapability capability;

    public ChiseledSheepCapabilityProvider() {
        capability = new ChiseledSheepCapability();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CHISELED_SHEEP;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CHISELED_SHEEP) {
            return CHISELED_SHEEP.cast(this.capability);
        }
        return null;
    }

    @Override
    public NBTBase serializeNBT() {
        return ChiseledSheepCapabilitySerializer.instance.writeNBT(CHISELED_SHEEP, capability, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        ChiseledSheepCapabilitySerializer.instance.readNBT(CHISELED_SHEEP, capability, null, nbt);
    }
}

