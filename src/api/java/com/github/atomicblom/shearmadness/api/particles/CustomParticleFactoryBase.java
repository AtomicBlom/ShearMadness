package com.github.atomicblom.shearmadness.api.particles;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by codew on 2/03/2017.
 */
public abstract class CustomParticleFactoryBase implements ICustomParticleFactory {
    private ResourceLocation registryName;

    @OnlyIn(Dist.CLIENT)
    private IParticleFactory factory;

    @Override
    @OnlyIn(Dist.CLIENT)
    public IParticleFactory getParticleFactory() {
        if (factory == null) {
            factory = (particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, parameters) -> createCustomParticle(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, parameters);
        }
        return factory;
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract Particle createCustomParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_);

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
    public Class<ICustomParticleFactory> getRegistryType() {
        return ICustomParticleFactory.class;
    }
}
