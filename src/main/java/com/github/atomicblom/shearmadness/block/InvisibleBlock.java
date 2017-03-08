package com.github.atomicblom.shearmadness.block;

import com.github.atomicblom.shearmadness.configuration.Settings;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class InvisibleBlock extends Block
{
    InvisibleBlock()
    {
        super(Material.AIR);
    }

    @Override
    public boolean isCollidable()
    {
        return false;
    }

    @Nullable
    @Override
    @Deprecated
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return Block.NULL_AABB;
    }

    @Override
    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return 0;
    }

    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return Settings.debugInvisibleBlocks() ?
                EnumBlockRenderType.MODEL :
                EnumBlockRenderType.INVISIBLE;
    }

    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
