package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceInvisibleBlockBehaviour extends BehaviourBase<PlaceInvisibleBlockBehaviour> {

    private final World world;
    private final IBlockState blockState;

    public PlaceInvisibleBlockBehaviour(EntitySheep sheep, IBlockState blockState) {
        super(sheep);
        world = sheep.worldObj;
        this.blockState = blockState;
    }

    @Override
    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {
        resetPreviousBlock(previousLocation);

        BlockPos pos = newLocation;

        if (!entity.isChild()) {
            pos = pos.up();
        }

        if (world.isAirBlock(pos)) {
            world.setBlockState(pos, blockState, 3);
        }
    }

    private void resetPreviousBlock(BlockPos previousLocation) {
        BlockPos pos = previousLocation;
        if (!entity.isChild())
        {
            pos = pos.up();
        }
        final IBlockState blockState = world.getBlockState(pos);
        if (this.blockState.equals(blockState))
        {
            world.setBlockToAir(pos);
        }
    }

    @Override
    public boolean isBehaviourEnabled() {
        return true;
    }

    @Override
    public boolean equals(PlaceInvisibleBlockBehaviour other) {
        return super.equals(other) && this.blockState == other.blockState;
    }
}
