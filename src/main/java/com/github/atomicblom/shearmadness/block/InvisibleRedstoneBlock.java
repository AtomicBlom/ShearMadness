package com.github.atomicblom.shearmadness.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by codew on 23/08/2016.
 */
public class InvisibleRedstoneBlock extends InvisibleBlock
{
    public InvisibleRedstoneBlock()
    {
    }

    @Deprecated
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Deprecated
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 15;
    }
}
