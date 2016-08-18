package com.github.atomicblom.chiselsheep.networking;

import com.github.atomicblom.chiselsheep.ChiselLibrary;
import com.github.atomicblom.chiselsheep.ChiselSheepMod;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.capability.Provider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static com.github.atomicblom.chiselsheep.ChiselSheepMod.CHISELED_SHEEP_CAPABILITY;

public class SheepChiseledMessageHandler implements IMessageHandler<SheepChiseledMessage, IMessage>
{
    @Override
    public IMessage onMessage(SheepChiseledMessage message, MessageContext ctx) {
        final Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.getSheepId());
        if (entity == null) { return null; }
        final IChiseledSheepCapability capability = entity.getCapability(Provider.CAPABILITY, null);
        if (capability == null) { return null; }
        capability.setChiseled(message.isChiselled());
        capability.setChiselItemStack(message.getChiselItemStack());

        return null;
    }
}
