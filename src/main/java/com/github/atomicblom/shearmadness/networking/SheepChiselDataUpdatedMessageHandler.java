package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SheepChiselDataUpdatedMessageHandler implements IMessageHandler<SheepChiselDataUpdatedMessage, IMessage>
{
    @SuppressWarnings({"ReturnOfNull", "ConstantConditions"})
    @Override
    public IMessage onMessage(SheepChiselDataUpdatedMessage message, MessageContext ctx)
    {
        final int sheepId = message.getSheepId();
        final Entity entity = Minecraft.getMinecraft().world.getEntityByID(sheepId);

        if (entity == null)
        {
            return null;
        }
        final IChiseledSheepCapability capability = entity.getCapability(Capability.CHISELED_SHEEP, null);
        if (capability == null)
        {
            return null;
        }
        capability.setItemVariantIdentifier(message.getItemVariantIdentifier());

        final NBTTagCompound extraData = capability.getExtraData();
        for (final String key : extraData.getKeySet()) {
            extraData.removeTag(key);
        }

        final NBTTagCompound newData = message.getExtraData();
        for (final String key : newData.getKeySet()) {
            extraData.setTag(key, newData.getTag(key).copy());
        }

        return null;
    }
}
