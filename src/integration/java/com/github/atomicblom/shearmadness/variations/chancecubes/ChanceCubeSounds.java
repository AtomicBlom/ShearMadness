package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(CommonReference.MOD_ID)
public class ChanceCubeSounds {

    public static final SoundEvent chancecube_sheepdied;
    public static final SoundEvent chancecube_giantcubespawned;

    static {
        chancecube_sheepdied = null;
        chancecube_giantcubespawned = null;
    }
}
