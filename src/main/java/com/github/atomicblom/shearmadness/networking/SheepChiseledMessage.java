package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SheepChiseledMessage implements IMessage
{
    private int sheepId;
    private boolean isChiseled;
    private ItemStack itemStack = ItemStack.EMPTY;
    private NBTTagCompound extraData;
    private int itemVariantIdentifier;

    @SuppressWarnings("unused")
    public SheepChiseledMessage() {
        extraData = new NBTTagCompound();
    }

    public SheepChiseledMessage(Entity sheep)
    {
        sheepId = sheep.getEntityId();
        final IChiseledSheepCapability capability = sheep.getCapability(Capability.CHISELED_SHEEP, null);
        assert capability != null;
        isChiseled = capability.isChiseled();
        itemStack = capability.getChiselItemStack();
        extraData = capability.getExtraData();
        itemVariantIdentifier = capability.getItemVariantIdentifier();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        sheepId = buf.readInt();
        isChiseled = buf.readBoolean();
        itemStack = ByteBufUtils.readItemStack(buf);
        extraData = ByteBufUtils.readTag(buf);
        itemVariantIdentifier = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(sheepId);
        buf.writeBoolean(isChiseled);
        ByteBufUtils.writeItemStack(buf, itemStack);
        ByteBufUtils.writeTag(buf, extraData);
        buf.writeInt(itemVariantIdentifier);
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

    public int getItemVariantIdentifier() {
        return itemVariantIdentifier;
    }

    public NBTTagCompound getExtraData() {
        return extraData;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("sheepId", sheepId)
                .add("isChiseled", isChiseled)
                .add("itemStack", itemStack)
                .toString();
    }
}