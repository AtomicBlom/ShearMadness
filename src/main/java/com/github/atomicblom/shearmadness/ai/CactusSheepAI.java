package com.github.atomicblom.shearmadness.ai;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.configuration.Settings;
import com.github.atomicblom.shearmadness.configuration.Settings.Behaviours;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

/**
 * Created by codew on 23/08/2016.
 */
public class CactusSheepAI extends EntityAIBase
{
    private final EntityLiving entity;
    private IChiseledSheepCapability capability = null;
    private int lastCheckedId = 0;
    private boolean cachedIdIsCactus = false;
    private final BlockPos previousPos = null;
    private AxisAlignedBB searchBox;

    public CactusSheepAI(EntityLiving entity)
    {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute()
    {
        if (!Behaviours.allowCactus()) {
            return false;
        }

        if (capability == null) {
            capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        }

        if (!capability.isChiseled()) return false;

        if (capability.getItemIdentifier() != lastCheckedId) {
            cachedIdIsCactus = false;
            final Item item = capability.getChiselItemStack().getItem();
            if (item instanceof ItemBlock) {
                if (((ItemBlock) item).block == Blocks.CACTUS) {
                    cachedIdIsCactus = true;
                }
            }
            lastCheckedId = capability.getItemIdentifier();
        }

        return cachedIdIsCactus;
    }

    @Override
    public void updateTask()
    {
        final BlockPos currentPos = entity.getPosition();
        if (searchBox == null || !currentPos.equals(previousPos))
        {
            searchBox = new AxisAlignedBB(entity.getPosition().add(-2, -2, -2), entity.getPosition().add(2, 2, 2));
        }

        for (final Entity nearbyEntity : entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, searchBox))
        {
            final double distance = entity.getDistanceSqToEntity(nearbyEntity);

            if (distance < 1.2) {
                nearbyEntity.attackEntityFrom(DamageSource.cactus, 1.0f);
            }

        }

    }
}

