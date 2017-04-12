package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.api.particles.ICustomParticleFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class SpawnCustomParticleMessageHandler implements IMessageHandler<SpawnCustomParticleMessage, IMessage>
{
    @SuppressWarnings({"ReturnOfNull", "ConstantConditions"})
    @Override
    public IMessage onMessage(SpawnCustomParticleMessage message, MessageContext ctx)
    {
        final ParticleManager effectRenderer = Minecraft.getMinecraft().effectRenderer;

        final IForgeRegistry<ICustomParticleFactory> registry = GameRegistry.findRegistry(ICustomParticleFactory.class);
        final ICustomParticleFactory factory = registry.getValue(message.getParticleResourceLocation());

        final Particle particle = factory.getParticleFactory().createParticle(-1,
                Minecraft.getMinecraft().thePlayer.worldObj,
                message.getXCoordinate(),
                message.getYCoordinate(),
                message.getZCoordinate(),
                message.getParticleSpeed(),
                message.getParticleSpeed(),
                message.getParticleSpeed(),
                message.getParticleArgs()
        );

        effectRenderer.addEffect(particle);

        return null;
    }
}
