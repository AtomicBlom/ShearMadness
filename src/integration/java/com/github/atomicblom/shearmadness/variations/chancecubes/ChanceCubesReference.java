package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.variations.CommonReference;
import com.github.atomicblom.shearmadness.variations.chancecubes.capability.IChanceCubeParticipationCapability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public final class ChanceCubesReference {
    public static final String CHANCE_CUBES_MODID = "chancecubes";

    @CapabilityInject(IChanceCubeParticipationCapability.class)
    public static final Capability<IChanceCubeParticipationCapability> CHANCE_CUBE_PARTICIPATION;

    public static final ResourceLocation ChanceCubeParticipationCapability = new ResourceLocation(CommonReference.MOD_ID, "chance_cube_participation");
    public static final ResourceLocation ChanceCubeSheepDied = new ResourceLocation(CommonReference.MOD_ID, "chancecube_sheepdied");
    static {
        CHANCE_CUBE_PARTICIPATION = null;
    }

    private ChanceCubesReference() {}
}
