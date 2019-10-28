package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.variations.CommonReference;
import com.github.atomicblom.shearmadness.variations.chancecubes.capability.IChanceCubeParticipationCapability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ChanceCubesReference {
    public static final String CHANCE_CUBES_MODID = "chancecubes";

    public static Logger LOGGER = LogManager.getLogger("shearmadness:chance_cubes");

    @CapabilityInject(IChanceCubeParticipationCapability.class)
    public static final Capability<IChanceCubeParticipationCapability> CHANCE_CUBE_PARTICIPATION;

    public static final ResourceLocation ChanceCubeParticipationCapability = new ResourceLocation(CommonReference.MOD_ID, "chance_cube_participation");
    public static final ResourceLocation ChanceCubeSheepDied = new ResourceLocation(CommonReference.MOD_ID, "chancecube_sheepdied");
    public static final ResourceLocation ChanceCubeGiantCubeSpawned = new ResourceLocation(CommonReference.MOD_ID, "chancecube_giantcubespawned");
    static {
        CHANCE_CUBE_PARTICIPATION = null;
    }

    private ChanceCubesReference() {}
}
