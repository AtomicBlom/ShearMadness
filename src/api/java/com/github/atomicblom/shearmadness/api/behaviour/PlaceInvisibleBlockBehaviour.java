package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlaceInvisibleBlockBehaviour extends BehaviourBase<PlaceInvisibleBlockBehaviour> {

    private final World world;
    private final IBlockState blockState;

    public PlaceInvisibleBlockBehaviour(EntitySheep sheep, Supplier<Boolean> configuration, IBlockState blockState) {
        super(sheep, configuration);
        world = sheep.worldObj;
        this.blockState = blockState;
    }

    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        resetBlock(previousPos);
    }

    @Override
    public void onBehaviourStarted(BlockPos currentPos) {
        setBlock(currentPos);
    }

    @Override
    public void onSheepMovedBlock(BlockPos previousLocation, BlockPos newLocation) {
        resetBlock(previousLocation);

        setBlock(newLocation);
    }

    private void setBlock(BlockPos newLocation) {
        BlockPos pos = newLocation;

        if (!entity.isChild()) {
            pos = pos.up();
        }

        if (world.isAirBlock(pos)) {
            world.setBlockState(pos, blockState, 3);
        }
    }

    private void resetBlock(BlockPos previousLocation) {
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
    public boolean equals(PlaceInvisibleBlockBehaviour other) {
        return super.equals(other) && this.blockState == other.blockState;
    }
}
