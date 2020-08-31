package com.github.atomicblom.shearmadness.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class InvisibleBookshelfBlock extends InvisibleBlock
{
    public InvisibleBookshelfBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, IWorldReader world, BlockPos pos) {
        return 1;
    }
}
