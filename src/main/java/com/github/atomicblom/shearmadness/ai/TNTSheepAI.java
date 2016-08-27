package com.github.atomicblom.shearmadness.ai;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.configuration.Settings;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;

public class TNTSheepAI extends EntityAIBase
{
    private final EntityLiving entity;
    private IChiseledSheepCapability capability = null;
    private int lastCheckedId = 0;
    private boolean cachedIdIsTNT = false;
    private BlockPos previousPos = null;
    private Long primedTime = null;
    private BlockPos aboveCurrentPosition = null;

    public TNTSheepAI(EntityLiving entity)
    {
        this.entity = entity;
    }

    @SuppressWarnings("ConstantConditions")
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

