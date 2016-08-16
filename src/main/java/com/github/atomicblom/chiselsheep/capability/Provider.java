package com.github.atomicblom.chiselsheep.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class Provider implements ICapabilityProvider, INBTSerializable<NBTBase> {

    @CapabilityInject(IChiseledSheepCapability.class)
    private static Capability<IChiseledSheepCapability> CAPABILITY = null;

    private IChiseledSheepCapability capability;

    public Provider() {
        capability = new ChiseledSheepCapability();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CAPABILITY) {
            return (T)this.capability;
        }
        return null;
    }

    @Override
    public NBTBase serializeNBT() {
        return ChiseledSheepCapability.ExtentionStorage.instance.writeNBT(CAPABILITY, capability, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        ChiseledSheepCapability.ExtentionStorage.instance.readNBT(CAPABILITY, capability, null, nbt);
    }
}
