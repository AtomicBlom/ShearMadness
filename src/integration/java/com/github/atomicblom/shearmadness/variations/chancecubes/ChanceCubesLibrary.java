package com.github.atomicblom.shearmadness.variations.chancecubes;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ChanceCubesReference.CHANCE_CUBES_MODID)
public class ChanceCubesLibrary
{
    @ObjectHolder("chance_Cube")
    public static final Block chance_cube;

    static {
        chance_cube = null;
    }
}
