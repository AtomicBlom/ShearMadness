package com.github.atomicblom.shearmadness.api.behaviour;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.math.BlockPos;

public abstract class BehaviourBase<T extends BehaviourBase> {
    protected final EntitySheep entity;
    protected final IChiseledSheepCapability capability;

    public BehaviourBase(EntitySheep sheep) {
        this.capability = sheep.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        this.entity = sheep;
    }

    public abstract boolean isBehaviourEnabled();

    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {}

    public void onBehaviourStarted(BlockPos currentPos) {}

    public void onBehaviourStopped(BlockPos previousPos) {}

    public void updateTask() {}

    public boolean equals(T other) {
        return this.entity.getUniqueID().equals(other.entity.getUniqueID());
    }
}
