package com.github.atomicblom.shearmadness.api;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public final class Capability {
    @CapabilityInject(IChiseledSheepCapability.class)
    public static final net.minecraftforge.common.capabilities.Capability<IChiseledSheepCapability> CHISELED_SHEEP;

    static {
        CHISELED_SHEEP = null;
    }
}
