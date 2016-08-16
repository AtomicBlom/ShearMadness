package com.github.atomicblom.chiselsheep.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import javax.annotation.Nullable;

/**
 * Created by codew on 16/08/2016.
 */
public class FakeWorld implements IBlockAccess
{
    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos)
    {
        return null;
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue)
    {
        return 0;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos)
    {
        return null;
    }

    @Override
    public boolean isAirBlock(BlockPos pos)
    {
        return false;
    }

    @Override
    public Biome getBiomeGenForCoords(BlockPos pos)
    {
        return null;
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction)
    {
        return 0;
    }

    @Override
    public WorldType getWorldType()
    {
        return null;
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default)
    {
        return false;
    }
}
