package com.github.atomicblom.chiselsheep.networking;

import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapabilityProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SheepChiseledMessage implements IMessage
{
    private int sheepId;
    private boolean isChiselled;
    private ItemStack itemStack;

    @SuppressWarnings("unused")
    public SheepChiseledMessage(){}

    public SheepChiseledMessage(Entity sheep) {
        sheepId = sheep.getEntityId();
        final IChiseledSheepCapability capability = sheep.getCapability(ChiseledSheepCapabilityProvider.CHISELED_SHEEP, null);
        isChiselled = capability.isChiseled();
        itemStack = capability.getChiselItemStack();
    }

    @Override public void toBytes(ByteBuf buf) {
        buf.writeInt(sheepId);
        buf.writeBoolean(isChiselled);
        ByteBufUtils.writeItemStack(buf, itemStack);
    }

    @Override public void fromBytes(ByteBuf buf) {
        sheepId = buf.readInt();
        isChiselled = buf.readBoolean();
        itemStack = ByteBufUtils.readItemStack(buf);
    }

    int getSheepId()
    {
        return sheepId;
    }

    boolean isChiselled()
    {
        return isChiselled;
    }

    ItemStack getChiselItemStack()
    {
        return itemStack;
    }
}