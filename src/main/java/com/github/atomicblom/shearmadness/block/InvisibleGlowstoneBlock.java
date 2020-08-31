package com.github.atomicblom.shearmadness.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class InvisibleGlowstoneBlock extends InvisibleBlock
{

    @Override
    public int getLightValue(BlockState state, IBlockAccess world, BlockPos pos)
    {
        return 15;
    }
}
