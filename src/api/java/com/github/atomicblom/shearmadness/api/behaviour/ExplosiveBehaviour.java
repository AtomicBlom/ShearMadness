package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;

import java.util.function.Supplier;

@SuppressWarnings("ClassHasNoToStringMethod")
public class ExplosiveBehaviour extends BehaviourBase {

    private Long primedTime = null;
    private BlockPos aboveCurrentPosition = null;

    public ExplosiveBehaviour(SheepEntity sheep, Supplier<Boolean> configuration) {
        super(sheep, configuration);
    }

    @Override
    public void onBehaviourStarted(BlockPos currentPos, Goal goal)
    {
        onSheepMovedBlock(null, currentPos);
    }

    @Override
    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {
        aboveCurrentPosition = newLocation.up();
    }

    @Override
    public void updateTask() {
        boolean blockPowered = getEntity().getEntityWorld().isBlockPowered(getEntity().getPosition());
        if (!getEntity().isChild()) {
            blockPowered |= getEntity().getEntityWorld().isBlockPowered(aboveCurrentPosition);
        }

        final long totalWorldTime = getEntity().getEntityWorld().getGameTime();
        if (blockPowered && primedTime == null) {
            primedTime = totalWorldTime;
            getEntity().playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
        }

        if (primedTime != null && totalWorldTime > primedTime + 80) {
            getEntity().getEntityWorld().createExplosion(null, getEntity().posX, getEntity().posY + getEntity().getHeight() / 16.0F, getEntity().posZ, 4.0F, Explosion.Mode.BREAK);
        }
    }
}
