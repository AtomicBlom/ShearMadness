package com.github.atomicblom.shearmadness.ai;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import com.github.atomicblom.shearmadness.utility.ChiselLibrary;
import com.github.atomicblom.shearmadness.utility.Logger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.block.BlockCarvable;

/**
 * Created by codew on 23/08/2016.
 */
public class FlyingSheepAI extends EntityAIBase
{
    private final EntityLiving entity;
    private IChiseledSheepCapability capability = null;
    private int lastCheckedId = 0;
    private boolean cachedIdIsFan = false;
    private double destinationYaw;
    private int framesTillNextTurn;
    private double destinationMotionY;
    private double currentMotionY;

    public FlyingSheepAI(EntityLiving entity)
    {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute()
    {
        if (capability == null) {
            capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        }

        if (!capability.isChiseled()) return false;
        boolean wasCachedIdFan = cachedIdIsFan;
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

        entity.rotationYaw = updateRotation(entity.rotationYaw, destinationYaw, 2f);
        if (MathHelper.wrapDegrees(entity.rotationYaw - destinationYaw) < 2) {
            if (framesTillNextTurn <= 0)
            {
                destinationYaw = Math.random() * 360;//2 * Math.PI;
                //Logger.info("New Turn Destination is %f", destinationYaw);
                framesTillNextTurn = 200;
            } else {
                //Logger.info("Counting down");
                framesTillNextTurn--;
            }
        }


        //entity.rotationYaw += 0.1f;
        /*float f1 = MathHelper.sin(entity.rotationYaw * 0.017453292F);
        float f2 = MathHelper.cos(entity.rotationYaw * 0.017453292F);
        entity.motionX = forward * -f1;
        entity.motionZ = forward * f2;
        entity.limbSwing = 0;
        entity.limbSwingAmount = 0;*/

    }

    private float updateRotation(double angle, double targetAngle, double maxIncrease)
    {
        double f = MathHelper.wrapDegrees(targetAngle - angle);

        if (f > maxIncrease)
        {
            f = maxIncrease;
        }

        if (f < -maxIncrease)
        {
            f = -maxIncrease;
        }

        return (float)(angle + f);
    }
}

