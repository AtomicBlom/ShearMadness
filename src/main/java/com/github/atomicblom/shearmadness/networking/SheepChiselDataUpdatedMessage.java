package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SheepChiselDataUpdatedMessage implements IMessage {
    private int sheepId;
    private CompoundNBT extraData;
    private int itemVariantIdentifier;

    public SheepChiselDataUpdatedMessage() {
        extraData = new CompoundNBT();
    }

    public SheepChiselDataUpdatedMessage(SheepEntity sheep) {
        sheepId = sheep.getEntityId();
        final IChiseledSheepCapability capability = sheep.getCapability(Capability.CHISELED_SHEEP, null);
        assert capability != null;
        extraData = capability.getExtraData();
        itemVariantIdentifier = capability.getItemVariantIdentifier();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        sheepId = buf.readInt();
        extraData = ByteBufUtils.readTag(buf);
        itemVariantIdentifier = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(sheepId);
        ByteBufUtils.writeTag(buf, extraData);
        buf.writeInt(itemVariantIdentifier);
    }

    public int getSheepId() {
        return sheepId;
    }

    public int getItemVariantIdentifier() {
        return itemVariantIdentifier;
    }

    public CompoundNBT getExtraData() {
        return extraData;
    }
}
