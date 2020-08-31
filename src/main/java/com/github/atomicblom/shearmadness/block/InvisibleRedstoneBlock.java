package com.github.atomicblom.shearmadness.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class InvisibleRedstoneBlock extends InvisibleBlock
{

    @Override
    @Deprecated
    public boolean canProvidePower(BlockState state)
    {
        return true;
    }

    @Override
    @Deprecated
    public int getWeakPower(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 15;
    }
}
