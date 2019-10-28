package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.math.BlockPos;
import java.util.function.Supplier;

@SuppressWarnings({"NoopMethodInAbstractClass", "ClassHasNoToStringMethod"})
public abstract class BehaviourBase {
    private final SheepEntity entity;
    private final Supplier<Boolean> configuration;

    protected BehaviourBase(SheepEntity sheep) {
        this(sheep, () -> true);
    }

    protected BehaviourBase(SheepEntity entity, Supplier<Boolean> configuration)
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

    public boolean isEquivalentTo(BehaviourBase other) {
        return entity.getUniqueID().equals(other.getEntity().getUniqueID()) &&
                this.getClass().equals(other.getClass());
    }

    public SheepEntity getEntity()
    {
        return entity;
    }
}
