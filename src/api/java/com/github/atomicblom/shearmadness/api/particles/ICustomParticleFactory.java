package com.github.atomicblom.shearmadness.api.particles;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ICustomParticleFactory extends IForgeRegistryEntry<ICustomParticleFactory>
{
    @SideOnly(Side.CLIENT)
    IParticleFactory getParticleFactory();
}
