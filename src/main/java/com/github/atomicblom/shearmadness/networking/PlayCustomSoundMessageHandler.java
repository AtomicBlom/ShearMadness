package com.github.atomicblom.shearmadness.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class PlayCustomSoundMessageHandler implements IMessageHandler<PlayCustomSoundMessage, IMessage>
{
    @SuppressWarnings({"ReturnOfNull", "ConstantConditions"})
    @Override
    public IMessage onMessage(PlayCustomSoundMessage message, MessageContext ctx)
    {
        final IForgeRegistry<SoundEvent> registry = GameRegistry.findRegistry(SoundEvent.class);

        Minecraft.getMinecraft().player.world.playSound(
                message.getPosX(), message.getPosY(), message.getPosZ(),
                registry.getValue(message.getSoundEvent()),
                message.getCategory(),
                message.getVolume(), message.getPitch(),
                message.shouldDistanceDelay()
        );

        return null;
    }
}
