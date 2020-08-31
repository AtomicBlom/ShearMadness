package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;

import java.util.Random;

@SuppressWarnings("ClassHasNoToStringMethod")
public class FlightBehaviour extends BehaviourBase<FlightBehaviour> {

    private final Random random;
    private final float floatHeight;
    private final boolean moveForward;
    private float destinationYaw;
    private int framesTillNextTurn;
    private double destinationMotionY;
    private double currentMotionY;
    private BlockPos sheepLocation = BlockPos.ZERO;

    public FlightBehaviour(SheepEntity sheep, float floatHeight, boolean moveForward) {
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
    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {
        sheepLocation = newLocation;
    }

    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        getEntity().fallDistance = 0;
    }

    @Override
    public void updateTask() {
        final SheepEntity entity = getEntity();
        final BlockPos height = entity.world.getHeight(Heightmap.Type.MOTION_BLOCKING, sheepLocation);
        final double actualHeight = entity.getPosY() - height.getY();


        if (currentMotionY < destinationMotionY) {
            currentMotionY *= 1.05;
        }

        Vector3d motion = entity.getMotion();
        double motionX = motion.x, motionY = motion.y, motionZ = motion.z;
        if (actualHeight < floatHeight) {
            motionY = currentMotionY;
        }
        if (motionY > 0.3) {
            motionY = 0.3;
        }
        if (motionY < 0) {
            motionY = 0;
        }
        entity.setMotion(motionX, motionY, motionZ);

        entity.setJumping(true);
        entity.fallDistance = 0;

        if (moveForward) {
            entity.travel(new Vector3d(0, 1,0.6f));
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
