package com.github.atomicblom.shearmadness.api.behaviour;

import com.github.atomicblom.shearmadness.configuration.Settings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class DamageBehaviour extends BehaviourBase<DamageBehaviour> {
    private final DamageSource damageSource;
    private AxisAlignedBB searchBox = null;

    public DamageBehaviour(EntitySheep sheep, DamageSource damageSource) {
        super(sheep);
        this.damageSource = damageSource;
    }

    @Override
    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {
        searchBox = new AxisAlignedBB(entity.getPosition().add(-2, -2, -2), entity.getPosition().add(2, 2, 2));
    }

    @Override
    public boolean isBehaviourEnabled() {
        return Settings.Behaviours.allowCactus();
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
