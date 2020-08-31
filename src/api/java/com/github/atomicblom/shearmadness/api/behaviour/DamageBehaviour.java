package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("ClassHasNoToStringMethod")
public class DamageBehaviour extends BehaviourBase {
    private final DamageSource damageSource;
    private AxisAlignedBB searchBox = null;

    public DamageBehaviour(SheepEntity sheep, Supplier<Boolean> configuration, DamageSource damageSource) {
        super(sheep, configuration);
        this.damageSource = damageSource;
    }

    @Override
    public void onBehaviourStarted(BlockPos currentPos, Goal goal)
    {
        onSheepMovedBlock(null, currentPos);
    }

    @Override
    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {
        searchBox = new AxisAlignedBB(newLocation.add(-2, -2, -2), newLocation.add(2, 2, 2));
    }

    @Override
    public void updateTask() {
        final SheepEntity entity = getEntity();
        for (final Entity nearbyEntity : entity.getEntityWorld().getEntitiesWithinAABBExcludingEntity(entity, searchBox))
        {
            final double distance = entity.getDistanceSq(nearbyEntity);

            if (distance < 1.2) {
                if (nearbyEntity instanceof AnimalEntity) {
                    final AnimalEntity animal = (AnimalEntity) nearbyEntity;
                    int wasInLove = animal.inLove;
                    if (!nearbyEntity.attackEntityFrom(damageSource, 1.0f)) {
                        animal.inLove = wasInLove;
                    }
                } else {
                    nearbyEntity.attackEntityFrom(damageSource, 1.0f);
                }
            }
        }
    }

    @Override
    public boolean isEquivalentTo(BehaviourBase other) {
        if (!super.isEquivalentTo(other)) return false;
        DamageBehaviour otherBehaviour = (DamageBehaviour)other;
        return Objects.equals(damageSource, otherBehaviour.damageSource);
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }
}
