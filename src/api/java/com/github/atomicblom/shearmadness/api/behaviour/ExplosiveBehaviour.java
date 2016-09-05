package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class ExplosiveBehaviour extends BehaviourBase {

    private Long primedTime;
    private BlockPos aboveCurrentPosition;
    private BlockPos currentPos;

    public ExplosiveBehaviour(EntitySheep sheep) {
        super(sheep);
    }

    @Override
    public boolean isBehaviourEnabled() {
        return true;
    }

    @Override
    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {
        aboveCurrentPosition = newLocation.up();
        currentPos = newLocation;
    }

    @Override
    public void updateTask() {
        boolean blockPowered = entity.worldObj.isBlockPowered(currentPos);
        if (!entity.isChild()) {
            blockPowered |= entity.worldObj.isBlockPowered(aboveCurrentPosition);
        }

        final long totalWorldTime = entity.worldObj.getTotalWorldTime();
        if (blockPowered && primedTime == null) {
            primedTime = totalWorldTime;
            entity.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
        }

        if (primedTime != null && totalWorldTime > primedTime + 80) {
            entity.worldObj.createExplosion(null, entity.posX, entity.posY + entity.height / 16.0F, entity.posZ, 4.0F, true);
        }
    }
}
