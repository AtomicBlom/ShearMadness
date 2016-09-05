package com.github.atomicblom.shearmadness.api.behaviour;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.math.BlockPos;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class BehaviourBase<T extends BehaviourBase> {
    protected final EntitySheep entity;
    private final Supplier<Boolean> configuration;
    protected final IChiseledSheepCapability capability;

    protected BehaviourBase(EntitySheep sheep) {
        this(sheep, () -> true);
    }

    protected BehaviourBase(EntitySheep sheep, Supplier<Boolean> configuration)
    {

        this.capability = sheep.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        this.entity = sheep;
        this.configuration = configuration;
    }

    public boolean isBehaviourEnabled() {
        return configuration.get();
    }

    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {}

    public void onBehaviourStarted(BlockPos currentPos) {}

    public void onBehaviourStopped(BlockPos previousPos) {}

    public void updateTask() {}

    public boolean equals(T other) {
        return this.entity.getUniqueID().equals(other.entity.getUniqueID());
    }
}
