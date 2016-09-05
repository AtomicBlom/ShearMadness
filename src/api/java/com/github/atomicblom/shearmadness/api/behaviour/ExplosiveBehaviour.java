package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import java.util.function.Supplier;

public class ExplosiveBehaviour extends BehaviourBase {

    private Long primedTime;
    private BlockPos aboveCurrentPosition;

    public ExplosiveBehaviour(EntitySheep sheep, Supplier<Boolean> configuration) {
        super(sheep, configuration);
    }

    @Override
    public void onBehaviourStarted(BlockPos currentPos)
    {
        onSheepMovedBlock(null, currentPos);
    }

    @Override
    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {
        aboveCurrentPosition = newLocation.up();
    }

    @Override
    public void updateTask() {
        boolean blockPowered = entity.worldObj.isBlockPowered(entity.getPosition());
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
