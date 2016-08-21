package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import java.util.UUID;

public class CheckSheepChiseledRequestMessageHandler implements IMessageHandler<CheckSheepChiseledRequestMessage, SheepChiseledMessage>
{
    @SuppressWarnings({"ReturnOfNull", "ConstantConditions"})
    @Override
    public SheepChiseledMessage onMessage(CheckSheepChiseledRequestMessage message, MessageContext ctx)
    {
        final WorldServer worldObj = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = worldObj.getEntityFromUuid(UUID.fromString(message.getSheepUUID()));
        if (entity == null)
        {
            return null;
        }
        final IChiseledSheepCapability capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        if (capability == null)
        {
            return null;
        }
        if (capability.isChiseled())
        {
            return new SheepChiseledMessage(entity);
        }

        return null;
    }
}
