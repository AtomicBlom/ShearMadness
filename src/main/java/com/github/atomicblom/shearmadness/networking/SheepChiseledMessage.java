package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.google.common.base.Objects;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SheepChiseledMessage implements IMessage
{
    private int sheepId;
    private boolean isChiseled;
    private ItemStack itemStack = null;

    @SuppressWarnings("unused")
    public SheepChiseledMessage() {}

    public SheepChiseledMessage(Entity sheep)
    {
        sheepId = sheep.getEntityId();
        final IChiseledSheepCapability capability = sheep.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        isChiseled = capability.isChiseled();
        itemStack = capability.getChiselItemStack();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        sheepId = buf.readInt();
        isChiseled = buf.readBoolean();
        itemStack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(sheepId);
        buf.writeBoolean(isChiseled);
        ByteBufUtils.writeItemStack(buf, itemStack);
    }

    int getSheepId()
    {
        return sheepId;
    }

    boolean isChiseled()
    {
        return isChiseled;
    }

    ItemStack getChiselItemStack()
    {
        return itemStack;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("sheepId", sheepId)
                .add("isChiseled", isChiseled)
                .add("itemStack", itemStack)
                .toString();
    }
}