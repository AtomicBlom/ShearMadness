package com.github.atomicblom.shearmadness.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by codew on 23/08/2016.
 */
public class InvisibleGlowstoneBlock extends InvisibleBlock
{
    public InvisibleGlowstoneBlock()
    {
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return 15;
    }
}
