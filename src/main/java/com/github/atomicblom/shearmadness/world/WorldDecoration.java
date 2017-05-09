package com.github.atomicblom.shearmadness.world;


import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.utility.Logger;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WorldDecoration {
    @SubscribeEvent
    public void onDecorateChunk(PopulateChunkEvent.Populate event) {
        if (event.getPhase() != EventPriority.NORMAL) return;
        if (event.getType() != PopulateChunkEvent.Populate.EventType.ANIMALS) return;
        World world = event.getWorld();
        int x = event.getChunkX() << 4;
        int z = event.getChunkZ() << 4;

        if (event.getChunkX() % 5 != 0 || event.getChunkZ() % 5 != 0) return;

        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

        //Verify Biome
        if (!isPlainsBiome(world, blockPos.setPos(x, 64, z))) return;
        if (!isPlainsBiome(world, blockPos.setPos(x | 15, 64, z))) return;
        if (!isPlainsBiome(world, blockPos.setPos(x, 64, z | 15))) return;
        if (!isPlainsBiome(world, blockPos.setPos(x | 15, 64, z | 15))) return;

        //Verify Height at target
        int height = getHeightmapHeight(world, x | 8, z | 8) + 1;

        IBlockState oakPlanks = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);
        IBlockState godSand = Blocks.SAND.getDefaultState();
        IBlockState torch = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST);
        IBlockState stairs = Blocks.OAK_STAIRS.getDefaultState();
        IBlockState sign = Blocks.WALL_SIGN.getDefaultState().withProperty(BlockWallSign.FACING, EnumFacing.WEST);

        IBlockState bottomStraightStairs = stairs
                .withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM)
                .withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
        IBlockState bottomOuterLeftStairs = stairs
                .withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM)
                .withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
        IBlockState bottomOuterRightStairs = stairs
                .withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM)
                .withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);

        BlockPos sandPos = new BlockPos(x | 10, height + 1, z | 10);

        world.setBlockState(new BlockPos(x | 10, height, z | 10), oakPlanks);
        world.setBlockState(sandPos, godSand);
        world.setBlockState(new BlockPos(x | 10, height + 2, z | 10), stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
        world.setBlockState(new BlockPos(x | 10, height + 3, z | 10), oakPlanks);

        for (int i = 0; i < 3; ++i) {
            for (int y = 0; y < 3; ++y) {
                if (y < 2) {
                    world.setBlockState(new BlockPos(x | 9 + i, height + y, z | 8), oakPlanks);
                    world.setBlockState(new BlockPos(x | 9 + i, height + y, z | 12), oakPlanks);
                }
                world.setBlockState(new BlockPos(x | 11, height + y, z | 9 + i), oakPlanks);
            }
            if (i < 2) {
                world.setBlockState(new BlockPos(x | 9 + i, height + 2, z | 12), bottomStraightStairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH));
                world.setBlockState(new BlockPos(x | 9 + i, height + 2, z | 8), bottomStraightStairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
            }
        }
        world.setBlockState(new BlockPos(x | 9, height+3, z | 10), oakPlanks);
        world.setBlockState(new BlockPos(x | 11, height+3, z | 10), oakPlanks);

        world.setBlockState(new BlockPos(x | 9, height+0, z | 10), bottomStraightStairs.withProperty(BlockStairs.FACING, EnumFacing.EAST));
        world.setBlockState(new BlockPos(x | 10, height+3, z | 11), bottomStraightStairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH));
        world.setBlockState(new BlockPos(x | 10, height+3, z | 9), bottomStraightStairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
        world.setBlockState(new BlockPos(x | 10, height+0, z | 11), bottomStraightStairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH));
        world.setBlockState(new BlockPos(x | 10, height+0, z | 9), bottomStraightStairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
        world.setBlockState(new BlockPos(x | 9, height+3, z | 11), bottomOuterLeftStairs.withProperty(BlockStairs.FACING, EnumFacing.EAST));
        world.setBlockState(new BlockPos(x | 9, height+3, z | 9), bottomOuterRightStairs.withProperty(BlockStairs.FACING, EnumFacing.EAST));
        world.setBlockState(new BlockPos(x | 9, height+0, z | 11), bottomOuterLeftStairs.withProperty(BlockStairs.FACING, EnumFacing.EAST));
        world.setBlockState(new BlockPos(x | 9, height+0, z | 9), bottomOuterRightStairs.withProperty(BlockStairs.FACING, EnumFacing.EAST));

        world.setBlockState(new BlockPos(x | 11, height+2, z | 12), bottomOuterRightStairs.withProperty(BlockStairs.FACING, EnumFacing.WEST));
        world.setBlockState(new BlockPos(x | 11, height+2, z | 8), bottomOuterLeftStairs.withProperty(BlockStairs.FACING, EnumFacing.WEST));
        world.setBlockState(new BlockPos(x | 11, height+3, z | 11), bottomOuterRightStairs.withProperty(BlockStairs.FACING, EnumFacing.WEST));
        world.setBlockState(new BlockPos(x | 11, height+3, z | 9), bottomOuterLeftStairs.withProperty(BlockStairs.FACING, EnumFacing.WEST));

        world.setBlockState(new BlockPos(x | 10, height + 2, z | 9), torch);
        world.setBlockState(new BlockPos(x | 10, height + 2, z | 11), torch);
        BlockPos signPos = new BlockPos(x | 8, height + 3, z | 10);
        world.setBlockState(signPos, sign);
        TileEntitySign signTileEntity = (TileEntitySign)world.getTileEntity(signPos);
        signTileEntity.signText[1] = new TextComponentString("Praise");
        signTileEntity.signText[2] = new TextComponentString("the");
        signTileEntity.signText[3] = new TextComponentString("Sand");

        double arcStart = 240;
        double arcEnd = 300;
        double sheepCount = 4;
        double distance = (arcEnd - arcStart) / (sheepCount - 1);

        for (double d = arcStart; d <= arcEnd; d += distance) {
            double sheepX = Math.sin(d * (Math.PI / 180)) * 4;
            double sheepZ = Math.cos(d * (Math.PI / 180)) * 4;
            SpawnSheep(event, world, x, z, height, sandPos, sheepX, sheepZ);
        }

        arcStart = 232;
        arcEnd = 308;
        sheepCount = 7;
        distance = (arcEnd - arcStart) / (sheepCount - 1);
        for (double d = arcStart; d <= arcEnd; d += distance) {
            double sheepX = Math.sin(d * (Math.PI / 180)) * 6;
            double sheepZ = Math.cos(d * (Math.PI / 180)) * 6;
            SpawnSheep(event, world, x, z, height, sandPos, sheepX, sheepZ);
        }


        //tp player935 216 69 285
        Logger.info("2. Created shrine at %s", blockPos.toString());
    }

    private void SpawnSheep(PopulateChunkEvent.Populate event, World world, int x, int z, int height, BlockPos sandPos, double sheepX, double sheepZ) {
        EntitySheep sheep = new EntitySheep(world);

        IChiseledSheepCapability capability = sheep.getCapability(Capability.CHISELED_SHEEP, null);
        if (capability != null) {
            capability.chisel(new ItemStack(Blocks.SAND));
        }

        if (event.getRand().nextInt(1000000) != 0) {
            for (EntityAITasks.EntityAITaskEntry entityAITaskEntry : Lists.newArrayList(sheep.tasks.taskEntries)) {
                sheep.tasks.removeTask(entityAITaskEntry.action);
            }
        } else {
            sheep.setCustomNameTag("Spiteful Sheep");
        }

        sheep.setCustomNameTag("Disheeple");
        sheep.setPosition(x + sheepX + 10 + 0.5f, height, z + sheepZ + 10 + 0.5f);

        EntityLookHelper lookHelper = sheep.getLookHelper();
        lookHelper.setLookPosition(sandPos.getX() + 0.5f, sandPos.getY() + 0.5f, sandPos.getZ() + 0.5f, 359, 359);
        lookHelper.onUpdateLook();
        sheep.renderYawOffset = sheep.rotationYawHead;
        sheep.prevRenderYawOffset = sheep.rotationYawHead;
        world.spawnEntityInWorld(sheep);
        sheep.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(sheep)), null);
        sheep.onLivingUpdate();
    }

    private int getHeightmapHeight(World world, int x, int z) {
        Chunk chunkFromChunkCoords = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int y = 255; y >= 0; --y) {
            pos.setPos(x, y, z);
            IBlockState blockState = chunkFromChunkCoords.getBlockState(pos);
            if (!blockState.getBlock().isReplaceable(world, pos)) {
                return y;
            }
        }
        return 0;
    }

    public static boolean isPlainsBiome(World world, BlockPos blockPos) {
        return world.getBiomeForCoordsBody(blockPos).getBiomeClass() == BiomePlains.class;
    }
}
