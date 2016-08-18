package com.github.atomicblom.chiselsheep.networking;

import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.capability.Provider;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import java.util.UUID;

public class CheckSheepChiseledRequestMessageHandler implements IMessageHandler<CheckSheepChiseledRequestMessage, SheepChiseledMessage>
{
    @Override
    public SheepChiseledMessage onMessage(CheckSheepChiseledRequestMessage message, MessageContext ctx) {

        final WorldServer worldObj = (WorldServer)ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = worldObj.getEntityFromUuid(UUID.fromString(message.getSheepUUID()));
        if (entity == null) { return null; }
        final IChiseledSheepCapability capability = entity.getCapability(Provider.CAPABILITY, null);
        if (capability == null) { return null; }
        if (capability.isChiseled()) {
            return new SheepChiseledMessage(entity);
        }

        return null;
    }
}
