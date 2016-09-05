package com.github.atomicblom.shearmadness.api.behaviour;

import com.github.atomicblom.shearmadness.configuration.Settings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DamageBehaviour extends BehaviourBase<DamageBehaviour> {
    private final DamageSource damageSource;
    private AxisAlignedBB searchBox = null;

    public DamageBehaviour(EntitySheep sheep, Supplier<Boolean> configuration, DamageSource damageSource) {
        super(sheep, configuration);
        this.damageSource = damageSource;
    }

    @Override
    public void onBehaviourStarted(BlockPos currentPos)
    {
        onSheepMovedBlock(null, currentPos);
    }

    @Override
    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {
        searchBox = new AxisAlignedBB(newLocation.add(-2, -2, -2), newLocation.add(2, 2, 2));
    }

    @Override
    public void updateTask() {
        for (final Entity nearbyEntity : entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, searchBox))
        {
            final double distance = entity.getDistanceSqToEntity(nearbyEntity);

            if (distance < 1.2) {
                nearbyEntity.attackEntityFrom(damageSource, 1.0f);
            }
        }
    }

    @Override
    public boolean equals(DamageBehaviour other) {
        return super.equals(other) && this.damageSource == other.damageSource;
    }
}
