package com.github.atomicblom.shearmadness.ai;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by codew on 23/08/2016.
 */
public class RedstoneSheepAI extends EntityAIBase
{
    private final Entity entity;
    private IChiseledSheepCapability capability = null;
    private int lastCheckedId = 0;
    private boolean cachedIdIsRedstone = false;
    private long prevX;
    private long prevY;
    private long prevZ;

    public RedstoneSheepAI(Entity entity)
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
            cachedIdIsRedstone = false;
            final Item item = capability.getChiselItemStack().getItem();
            if (item instanceof ItemBlock) {
                if (((ItemBlock) item).block == Blocks.REDSTONE_BLOCK) {
                    cachedIdIsRedstone = true;
                }
            }
            lastCheckedId = capability.getItemIdentifier();
        }

        return cachedIdIsRedstone;
    }

    @Override
    public void updateTask()
    {
        final long currentPosX = (long)entity.posX;
        final long currentPosY = (long)entity.posY;
        final long currentPosZ = (long)entity.posZ;
        if (prevX != currentPosX || prevY != currentPosY || prevZ != currentPosZ) {
            final World world = entity.worldObj;
            final BlockPos pos = new BlockPos(prevX, prevY + 1, prevZ);
            final IBlockState blockState = world.getBlockState(pos);
            if (blockState.getBlock() == BlockLibrary.invisibleRedstone) {
                world.setBlockToAir(pos);
            }
            final BlockPos up1 = new BlockPos(currentPosX, currentPosY + 1, currentPosZ);
            if (world.isAirBlock(up1)) {
                world.setBlockState(up1, BlockLibrary.invisibleRedstone.getDefaultState(), 3);
            }

            prevX = currentPosX;
            prevY = currentPosY;
            prevZ = currentPosZ;
        }
    }
}

