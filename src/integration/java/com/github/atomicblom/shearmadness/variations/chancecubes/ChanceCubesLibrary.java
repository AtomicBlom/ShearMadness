package com.github.atomicblom.shearmadness.variations.chancecubes;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ChanceCubesReference.CHANCE_CUBES_MODID)
public class ChanceCubesLibrary
{
    @ObjectHolder("chance_Cube")
    public static final Block chance_cube;

    @ObjectHolder("chance_Icosahedron")
    public static final Block chance_icosadedron;

    @ObjectHolder("compact_Giant_Chance_Cube")
    public static final Block chance_cube_giant_compact;

    @ObjectHolder("giant_Chance_Cube")
    public static final Block chance_cube_giant;

    static {
        chance_cube = null;
        chance_icosadedron = null;
        chance_cube_giant = null;
        chance_cube_giant_compact = null;
    }
}
