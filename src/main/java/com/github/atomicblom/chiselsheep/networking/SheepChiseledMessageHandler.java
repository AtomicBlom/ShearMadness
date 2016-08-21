package com.github.atomicblom.chiselsheep.networking;

import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapabilityProvider;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SheepChiseledMessageHandler implements IMessageHandler<SheepChiseledMessage, IMessage>
{
    @SuppressWarnings({"ReturnOfNull", "ConstantConditions"})
    @Override
    public IMessage onMessage(SheepChiseledMessage message, MessageContext ctx)
    {
        final Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.getSheepId());
        if (entity == null)
        {
            return null;
        }
        final IChiseledSheepCapability capability = entity.getCapability(ChiseledSheepCapabilityProvider.CHISELED_SHEEP, null);
        if (capability == null)
        {
            return null;
        }
        capability.setChiseled(message.isChiselled());
        capability.setChiselItemStack(message.getChiselItemStack());

        return null;
    }
}
