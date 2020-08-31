package com.github.atomicblom.shearmadness.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class InvisibleRedstoneBlock extends InvisibleBlock
{
    public InvisibleRedstoneBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    @Deprecated
    public boolean canProvidePower(BlockState p_149744_1_)
    {
        return true;
    }

    @Override
    @Deprecated
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
    {
        return 15;
    }
}
