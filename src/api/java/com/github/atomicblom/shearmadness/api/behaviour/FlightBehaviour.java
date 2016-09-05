package com.github.atomicblom.shearmadness.api.behaviour;

import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapability;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class FlightBehaviour extends BehaviourBase {

    private final Random random;
    private float destinationYaw;
    private int framesTillNextTurn;
    private double destinationMotionY;
    private double currentMotionY;

    public FlightBehaviour(EntitySheep sheep) {
        super(sheep);
        random = new Random();
    }

    @Override
    public void onBehaviourStarted(BlockPos currentPos) {
        //initialize directions
        destinationYaw = entity.rotationYaw;
        framesTillNextTurn = 500;
        destinationMotionY = 0.1;
        currentMotionY = 0.01;
    }

    @Override
    public boolean isBehaviourEnabled() {
        return true;
    }

    @Override
    public void updateTask() {
        final BlockPos height = entity.worldObj.getHeight(entity.getPosition());
        final double actualHeight = entity.posY - height.getY();
        entity.moveEntityWithHeading(0, 0.6f);

        if (currentMotionY < destinationMotionY) {
            currentMotionY *= 1.05;
        }

        if (actualHeight < 10) {
            entity.motionY = currentMotionY;
        }
        if (entity.motionY > 0.5) {
            entity.motionY = 0.5;
        }
        if (entity.motionY < 0) {
            entity.motionY = 0;
        }

        entity.setJumping(true);

        entity.rotationYaw = updateRotation(entity.rotationYaw, destinationYaw, 2);
        if (MathHelper.wrapDegrees(entity.rotationYaw - destinationYaw) < 2) {
            if (framesTillNextTurn <= 0)
            {
                destinationYaw = random.nextFloat() * 360;//2 * Math.PI;
                //Logger.info("New Turn Destination is %f", destinationYaw);
                framesTillNextTurn = 200;
            } else {
                //Logger.info("Counting down");
                framesTillNextTurn--;
            }
        }
    }

    private static float updateRotation(float angle, float targetAngle, float maxIncrease)
    {
        float degrees = MathHelper.wrapDegrees(targetAngle - angle);

        if (degrees > maxIncrease)
        {
            degrees = maxIncrease;
        }

        if (degrees < -maxIncrease)
        {
            degrees = -maxIncrease;
        }

        return angle + degrees;
    }
}
