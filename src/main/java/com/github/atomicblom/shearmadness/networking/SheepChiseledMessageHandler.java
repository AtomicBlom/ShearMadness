package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.utility.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SheepChiseledMessageHandler implements IMessageHandler<SheepChiseledMessage, IMessage>
{
    @SuppressWarnings({"ReturnOfNull", "ConstantConditions"})
    @Override
    public IMessage onMessage(SheepChiseledMessage message, MessageContext ctx)
    {
        final int sheepId = message.getSheepId();
        final Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(sheepId);
        Logger.info("Attempting to get sheep %d - entity %s", sheepId, entity.toString());
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
        final SoundEvent sheepChiseledSound = ShearMadnessMod.proxy.getSheepChiseledSound();
        entity.worldObj.playSound(entity.posX, entity.posY, entity.posZ, sheepChiseledSound, SoundCategory.NEUTRAL, 0.5F, 1.0f, true);

        return null;
    }
}
