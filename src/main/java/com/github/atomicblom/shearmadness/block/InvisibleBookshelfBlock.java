package com.github.atomicblom.shearmadness.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InvisibleBookshelfBlock extends InvisibleBlock
{

    @Override
    public float getEnchantPowerBonus(World world, BlockPos pos) {
        return 1;
    }
}
