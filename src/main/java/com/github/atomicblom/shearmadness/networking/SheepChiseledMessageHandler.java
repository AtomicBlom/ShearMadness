package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.utility.SoundLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SheepChiseledMessageHandler implements IMessageHandler<SheepChiseledMessage, IMessage>
{

    public static long lastSoundPlayed = 0;

    @SuppressWarnings({"ReturnOfNull", "ConstantConditions"})
    @Override
    public IMessage onMessage(SheepChiseledMessage message, MessageContext ctx)
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
        final boolean chiseled = message.isChiseled();
        if (chiseled) {
            capability.chisel(message.getChiselItemStack());
            long totalWorldTime = entity.getEntityWorld().getTotalWorldTime();
            if (lastSoundPlayed < totalWorldTime) {
                lastSoundPlayed = totalWorldTime;
                entity.world.playSound(entity.posX, entity.posY, entity.posZ, SoundLibrary.sheepchiseled, SoundCategory.NEUTRAL, 0.5F, 1.0f, true);
            }
        } else {
            capability.unChisel();
        }

        capability.setItemVariantIdentifier(message.getItemVariantIdentifier());

        final CompoundNBT extraData = capability.getExtraData();
        for (final String key : extraData.getKeySet()) {
            extraData.removeTag(key);
        }

        final CompoundNBT newData = message.getExtraData();
        for (final String key : newData.getKeySet()) {
            extraData.setTag(key, newData.getTag(key).copy());
        }

        return null;
    }
}
