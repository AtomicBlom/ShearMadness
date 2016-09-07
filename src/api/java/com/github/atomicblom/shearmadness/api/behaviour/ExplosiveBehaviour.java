package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import java.util.function.Supplier;

@SuppressWarnings("ClassHasNoToStringMethod")
public class ExplosiveBehaviour extends BehaviourBase {

    private Long primedTime = null;
    private BlockPos aboveCurrentPosition = null;

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
        boolean blockPowered = getEntity().worldObj.isBlockPowered(getEntity().getPosition());
        if (!getEntity().isChild()) {
            blockPowered |= getEntity().worldObj.isBlockPowered(aboveCurrentPosition);
        }

        final long totalWorldTime = getEntity().worldObj.getTotalWorldTime();
        if (blockPowered && primedTime == null) {
            primedTime = totalWorldTime;
            getEntity().playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
        }

        if (primedTime != null && totalWorldTime > primedTime + 80) {
            getEntity().worldObj.createExplosion(null, getEntity().posX, getEntity().posY + getEntity().height / 16.0F, getEntity().posZ, 4.0F, true);
        }
    }
}
