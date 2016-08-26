package com.github.atomicblom.shearmadness.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class InvisibleRedstoneBlock extends InvisibleBlock
{

    @Override
    @Deprecated
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    @Deprecated
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 15;
    }
}
