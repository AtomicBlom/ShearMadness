package com.github.atomicblom.shearmadness.api.particles;

import net.minecraft.util.ResourceLocation;

/**
 * Created by codew on 2/03/2017.
 */
public abstract class CustomParticleFactoryBase implements ICustomParticleFactory {
    private ResourceLocation registryName;

    @Override
    public ICustomParticleFactory setRegistryName(ResourceLocation name) {
        registryName = name;
        return this;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public Class<? super ICustomParticleFactory> getRegistryType() {
        return ICustomParticleFactory.class;
    }
}
