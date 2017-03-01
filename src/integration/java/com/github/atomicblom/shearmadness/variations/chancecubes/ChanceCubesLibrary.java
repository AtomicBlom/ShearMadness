package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.variations.chancecubes.capability.IChanceCubeParticipationCapability;
import com.github.atomicblom.shearmadness.variations.immersiveengineering.IEReference;
import net.minecraft.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ChanceCubesReference.CHANCE_CUBES)
public class ChanceCubesLibrary
{
    @ObjectHolder("chance_Cube")
    public static final Block chance_cube;

    @CapabilityInject(IChanceCubeParticipationCapability.class)
    public static final Capability<IChanceCubeParticipationCapability> CHANCE_CUBE_PARTICIPATION;

    static {
        chance_cube = null;
        CHANCE_CUBE_PARTICIPATION = null;
    }
}
