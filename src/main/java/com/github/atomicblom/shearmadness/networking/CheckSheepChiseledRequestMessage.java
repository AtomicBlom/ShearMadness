package com.github.atomicblom.shearmadness.networking;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CheckSheepChiseledRequestMessage implements IMessage
{
    private String sheepUUID = null;

    @SuppressWarnings("unused")
    public CheckSheepChiseledRequestMessage() {}

    public CheckSheepChiseledRequestMessage(Entity sheep)
    {
        sheepUUID = sheep.getCachedUniqueIdString();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        sheepUUID = ByteBufUtils.readUTF8String(buf);

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, sheepUUID);
    }

    String getSheepUUID()
    {
        return sheepUUID;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("sheepUUID", sheepUUID)
                .toString();
    }
}