package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import java.util.Random;

@SuppressWarnings("ClassHasNoToStringMethod")
public class FlightBehaviour extends BehaviourBase {

    private final Random random;
    private final int floatHeight;
    private final boolean moveForward;
    private float destinationYaw;
    private int framesTillNextTurn;
    private double destinationMotionY;
    private double currentMotionY;

    public FlightBehaviour(EntitySheep sheep, int floatHeight, boolean moveForward) {
        super(sheep);
        this.floatHeight = floatHeight;
        this.moveForward = moveForward;
        random = new Random();
    }

    @Override
    public void onBehaviourStarted(BlockPos currentPos) {
        destinationYaw = getEntity().rotationYaw;
        framesTillNextTurn = 500;
        destinationMotionY = 0.1;
        currentMotionY = 0.01;
    }

    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        getEntity().fallDistance = 0;
    }

    @Override
    public void updateTask() {
        final EntitySheep entity = getEntity();
        final BlockPos height = entity.worldObj.getHeight(entity.getPosition());
        final double actualHeight = entity.posY - height.getY();


        if (currentMotionY < destinationMotionY) {
            currentMotionY *= 1.05;
        }

        if (actualHeight < floatHeight) {
            entity.motionY = currentMotionY;
        }
        if (entity.motionY > 0.5) {
            entity.motionY = 0.5;
        }
        if (entity.motionY < 0) {
            entity.motionY = 0;
        }

        entity.setJumping(true);
        entity.fallDistance = 0;

        if (moveForward) {
            entity.moveEntityWithHeading(0, 0.6f);
            entity.rotationYaw = updateRotation(entity.rotationYaw, destinationYaw, 2);
            if (MathHelper.wrapDegrees(entity.rotationYaw - destinationYaw) < 2) {
                if (framesTillNextTurn <= 0) {
                    destinationYaw = random.nextFloat() * 360;
                    //Logger.info("New Turn Destination is %f", destinationYaw);
                    framesTillNextTurn = 200;
                } else {
                    //Logger.info("Counting down");
                    framesTillNextTurn--;
                }
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
