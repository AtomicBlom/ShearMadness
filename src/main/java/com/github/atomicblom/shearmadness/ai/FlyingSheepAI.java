package com.github.atomicblom.shearmadness.ai;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.utility.ChiselLibrary;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.block.BlockCarvable;
import java.util.Random;

public class FlyingSheepAI extends EntityAIBase
{
    private final EntityLiving entity;
    private final Random random;
    private IChiseledSheepCapability capability = null;
    private int lastCheckedId = 0;
    private boolean cachedIdIsFan = false;
    private float destinationYaw;
    private int framesTillNextTurn;
    private double destinationMotionY;
    private double currentMotionY;

    public FlyingSheepAI(EntityLiving entity)
    {

        this.entity = entity;
        random = new Random();
    }

    @SuppressWarnings({"ConstantConditions", "BooleanVariableAlwaysNegated"})
    @Override
    public boolean shouldExecute()
    {
        if (capability == null) {
            capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        }

        if (!capability.isChiseled()) return false;
        final boolean wasCachedIdFan = cachedIdIsFan;
        if (capability.getItemIdentifier() != lastCheckedId) {
            lastCheckedId = capability.getItemIdentifier();
            cachedIdIsFan = false;
            final ItemStack chiselItemStack = capability.getChiselItemStack();
            final ICarvingVariation variation = CarvingUtils.getChiselRegistry().getVariation(chiselItemStack);
            if (variation == null) {
                return false;
            }
            if (variation.getBlock() == ChiselLibrary.technical) {
                final Integer metavalue = variation.getBlockState().getValue(((BlockCarvable) ChiselLibrary.technical).getMetaProp());
                if (metavalue == 4) {
                    cachedIdIsFan = true;
                }
            } else if (variation.getBlock() == ChiselLibrary.technical1) {
                final Integer metavalue = variation.getBlockState().getValue(((BlockCarvable) ChiselLibrary.technical1).getMetaProp());
                if (metavalue == 1) {
                    cachedIdIsFan = true;
                }
            } else {
                return true;
            }

            if (!wasCachedIdFan && cachedIdIsFan) {

                //initialize directions
                destinationYaw = entity.rotationYaw;
                framesTillNextTurn = 500;
                destinationMotionY = 0.1;
                currentMotionY = 0.01;
            }

        }

        return cachedIdIsFan;
    }

    @Override
    public void updateTask()
    {
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

