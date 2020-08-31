package com.github.atomicblom.shearmadness.api.behaviour;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("ClassHasNoToStringMethod")
public class PlaceInvisibleBlockBehaviour extends BehaviourBase {

    private final World world;
    private final BlockState blockState;

    public PlaceInvisibleBlockBehaviour(SheepEntity sheep, Supplier<Boolean> configuration, BlockState blockState) {
        super(sheep, configuration);
        world = sheep.getEntityWorld();
        this.blockState = blockState;
    }

    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        resetBlock(previousPos);
    }

    @Override
    public void onBehaviourStarted(BlockPos currentPos, Goal goal) {
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
        final SheepEntity entity = getEntity();
        if (!entity.isChild())
        {
            pos = pos.up();
        }
        final BlockState blockAtSheep = world.getBlockState(pos);
        if (blockState.equals(blockAtSheep))
        {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public boolean isEquivalentTo(BehaviourBase other) {

        if (!super.isEquivalentTo(other)) return false;
        PlaceInvisibleBlockBehaviour otherBehaviour = (PlaceInvisibleBlockBehaviour)other;
        return Objects.equals(blockState, otherBehaviour.blockState);
    }
}
