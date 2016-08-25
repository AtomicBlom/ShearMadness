package com.github.atomicblom.shearmadness.ai;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by codew on 23/08/2016.
 */
public class GlowstoneSheepAI extends EntityAIBase
{
    private final EntityLiving entity;
    private IChiseledSheepCapability capability = null;
    private int lastCheckedId = 0;
    private boolean cachedIdIsGlowstone = false;
    private BlockPos previousPos = null;

    public GlowstoneSheepAI(EntityLiving entity)
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

        if (capability.getItemIdentifier() != lastCheckedId) {
            cachedIdIsGlowstone = false;
            final Item item = capability.getChiselItemStack().getItem();
            if (item instanceof ItemBlock) {
                if (((ItemBlock) item).block == Blocks.GLOWSTONE) {
                    cachedIdIsGlowstone = true;
                }
            }
            lastCheckedId = capability.getItemIdentifier();
        }

        return cachedIdIsGlowstone;
    }

    @Override
    public void updateTask()
    {
        final BlockPos currentPos = entity.getPosition();
        if (!currentPos.equals(previousPos)) {
            final World world = entity.worldObj;
            BlockPos pos;
            if (previousPos != null)
            {
                pos = previousPos;
                if (!entity.isChild())
                {
                    pos = pos.up();
                }
                final IBlockState blockState = world.getBlockState(pos);
                if (blockState.getBlock() == BlockLibrary.invisibleGlowstone)
                {
                    world.setBlockToAir(pos);
                }
            }

            pos = currentPos;
            if (!entity.isChild()) {
                pos = pos.up();
            }

            if (world.isAirBlock(pos)) {
                world.setBlockState(pos, BlockLibrary.invisibleGlowstone.getDefaultState(), 3);
            }

            previousPos = currentPos;
        }
    }
}

