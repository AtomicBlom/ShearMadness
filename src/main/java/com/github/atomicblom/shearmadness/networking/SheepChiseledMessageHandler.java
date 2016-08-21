package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
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
        final IChiseledSheepCapability capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        if (capability == null)
        {
            return null;
        }
        capability.setChiseled(message.isChiseled());
        capability.setChiselItemStack(message.getChiselItemStack());

        return null;
    }
}
