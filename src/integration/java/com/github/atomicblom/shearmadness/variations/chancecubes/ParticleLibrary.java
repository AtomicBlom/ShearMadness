package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.api.particles.ICustomParticleFactory;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(CommonReference.MOD_ID)
public class ParticleLibrary {

    public static final ICustomParticleFactory sheep_head;

    static {
        sheep_head = null;
    }
}
