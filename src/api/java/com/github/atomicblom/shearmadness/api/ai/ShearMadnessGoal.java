package com.github.atomicblom.shearmadness.api.ai;

import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.world.World;

import java.util.function.Supplier;

public abstract class ShearMadnessGoal extends Goal {
    protected final SheepEntity entity;
    private final Supplier<Boolean> configuration;
    protected final World world;

    protected ShearMadnessGoal(SheepEntity sheep) {
        this(sheep, () -> true);
    }

    protected ShearMadnessGoal(SheepEntity entity, Supplier<Boolean> configuration)
    {
        this.entity = entity;
        this.world = entity.world;
        this.configuration = configuration;
    }

    public boolean isEquivalentTo(BehaviourBase other) {
        return entity.getUniqueID().equals(other.getEntity().getUniqueID()) &&
                this.getClass().equals(other.getClass());
    }

    public SheepEntity getEntity()
    {
        return entity;
    }

    @Override
    public boolean shouldExecute() {
        return configuration.get();
    }

    public abstract int getPriority();

    public void onDeath() {

    }
}
