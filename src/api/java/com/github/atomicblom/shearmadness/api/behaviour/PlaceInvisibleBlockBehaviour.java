package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("ClassHasNoToStringMethod")
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

        if (!getEntity().isChild()) {
            pos = pos.up();
        }

        if (world.isAirBlock(pos)) {
            world.setBlockState(pos, blockState, 3);
        }
    }

    private void resetBlock(BlockPos previousLocation) {
        BlockPos pos = previousLocation;
        final EntitySheep entity = getEntity();
        if (!entity.isChild())
        {
            pos = pos.up();
        }
        final IBlockState blockAtSheep = world.getBlockState(pos);
        if (blockState.equals(blockAtSheep))
        {
            world.setBlockToAir(pos);
        }
    }

    @Override
    public boolean isEquivalentTo(PlaceInvisibleBlockBehaviour other) {
        return super.isEquivalentTo(other) && Objects.equals(blockState, other.blockState);
    }
}
