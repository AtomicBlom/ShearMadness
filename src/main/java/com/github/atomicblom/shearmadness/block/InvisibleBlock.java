package com.github.atomicblom.shearmadness.block;

import com.github.atomicblom.shearmadness.configuration.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;

public abstract class InvisibleBlock extends Block {
    protected InvisibleBlock(Properties properties)
    {
        super(
                properties
                        .doesNotBlockMovement()
                        .noDrops()
                );
    }

    @Override
    @Deprecated
    public boolean isReplaceable(BlockState blockState, BlockItemUseContext itemUseContext) {
        return true;
    }

    @Override
    @Deprecated
    public BlockRenderType getRenderType(BlockState state)
    {
        return Settings.debugInvisibleBlocks() ?
                BlockRenderType.MODEL :
                BlockRenderType.INVISIBLE;
    }
}
