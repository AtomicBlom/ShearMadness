package com.github.atomicblom.shearmadness.ai;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.configuration.Settings;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;

/**
 * Created by codew on 23/08/2016.
 */
public class TNTSheepAI extends EntityAIBase
{
    private final EntityLiving entity;
    private IChiseledSheepCapability capability = null;
    private int lastCheckedId = 0;
    private boolean cachedIdIsTNT = false;
    private BlockPos previousPos = null;
    private Long primedTime;


    public TNTSheepAI(EntityLiving entity)
    {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute()
    {
        if (!Settings.Behaviours.allowTNT()) {
            return false;
        }

        if (capability == null) {
            capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        }

        if (!capability.isChiseled()) return false;

        if (capability.getItemIdentifier() != lastCheckedId) {
            cachedIdIsTNT = false;
            final Item item = capability.getChiselItemStack().getItem();
            if (item instanceof ItemBlock) {
                if (((ItemBlock) item).block == Blocks.TNT) {
                    cachedIdIsTNT = true;
                }
            }
            lastCheckedId = capability.getItemIdentifier();
        }

        return cachedIdIsTNT;
    }

    //BlockPos[] neighbours = new BlockPos[6];
    BlockPos aboveCurrentPosition;

    @Override
    public void updateTask()
    {
        final BlockPos currentPos = entity.getPosition();
        if (!currentPos.equals(previousPos)) {
            aboveCurrentPosition = currentPos.up();
            previousPos = currentPos;
        }

        boolean blockPowered = entity.worldObj.isBlockPowered(currentPos);
        if (!entity.isChild()) {
            blockPowered |= entity.worldObj.isBlockPowered(aboveCurrentPosition);
        }
        if (blockPowered) {
            primedTime = entity.worldObj.getTotalWorldTime();

        }

        if (primedTime != null && entity.worldObj.getTotalWorldTime() > primedTime + 80) {
            entity.worldObj.createExplosion(null, entity.posX, entity.posY + (double)(entity.height / 16.0F), entity.posZ, 4.0F, true);
        }
    }
}

