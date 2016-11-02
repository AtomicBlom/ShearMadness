package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.math.BlockPos;
import java.util.function.Supplier;

@SuppressWarnings({"NoopMethodInAbstractClass", "ClassHasNoToStringMethod", "WeakerAccess"})
public abstract class BehaviourBase<T extends BehaviourBase> {
    private final EntitySheep entity;
    private final Supplier<Boolean> configuration;

    protected BehaviourBase(EntitySheep sheep) {
        this(sheep, () -> true);
    }

    protected BehaviourBase(EntitySheep entity, Supplier<Boolean> configuration)
    {
        this.entity = entity;
        this.configuration = configuration;
    }

    public boolean isBehaviourEnabled() {
        return configuration.get();
    }

    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {}

    public void onBehaviourStarted(BlockPos currentPos) {}

    public void onBehaviourStopped(BlockPos previousPos) {}

    public void updateTask() {}

    public boolean isEquivalentTo(T other) {
        return entity.getUniqueID().equals(other.getEntity().getUniqueID());
    }

    public EntitySheep getEntity()
    {
        return entity;
    }
}
