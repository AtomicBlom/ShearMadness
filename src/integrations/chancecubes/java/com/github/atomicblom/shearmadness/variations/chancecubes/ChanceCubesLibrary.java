package com.github.atomicblom.shearmadness.variations.chancecubes;

import net.minecraft.block.Block;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(ChanceCubesReference.CHANCE_CUBES_MODID)
public class ChanceCubesLibrary
{
    public static final Block chance_cube;

    public static final Block chance_icosadedron;

    public static final Block chance_cube_giant_compact;

    public static final Block chance_cube_giant;

    static {
        chance_cube = null;
        chance_icosadedron = null;
        chance_cube_giant = null;
        chance_cube_giant_compact = null;
    }
}
