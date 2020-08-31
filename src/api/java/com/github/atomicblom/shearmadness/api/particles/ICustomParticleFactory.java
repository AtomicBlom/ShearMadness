package com.github.atomicblom.shearmadness.api.particles;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ICustomParticleFactory extends IForgeRegistryEntry<ICustomParticleFactory>
{
    @OnlyIn(Dist.CLIENT)
    IParticleFactory getParticleFactory();
}
