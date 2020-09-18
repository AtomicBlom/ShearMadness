package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.google.common.base.MoreObjects;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SheepChiselDataUpdatedMessage {
    private int sheepId;
    private CompoundNBT extraData;
    private int itemVariantIdentifier;

    public SheepChiselDataUpdatedMessage(PacketBuffer buf) {
        sheepId = buf.readInt();
        extraData = buf.readCompoundTag();
        itemVariantIdentifier = buf.readInt();
    }

    public SheepChiselDataUpdatedMessage(SheepEntity sheep) {
        sheepId = sheep.getEntityId();

        LazyOptional<IChiseledSheepCapability> optionalCapability = sheep.getCapability(Capability.CHISELED_SHEEP);
        optionalCapability.ifPresent( capability -> {
            extraData = capability.getExtraData();
            itemVariantIdentifier = capability.getItemVariantIdentifier();
        });
    }

    public void toBytes(PacketBuffer buf)
    {
        buf.writeInt(sheepId);
        buf.writeCompoundTag(extraData);
        buf.writeInt(itemVariantIdentifier);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("sheepId", sheepId)
                .add("extraData", extraData)
                .toString();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        final NetworkEvent.Context context = ctx.get();
        context.setPacketHandled(true);
        context.enqueueWork(() -> {
            final int sheepId = this.sheepId;
            final Entity entity = Minecraft.getInstance().world.getEntityByID(sheepId);

            if (entity == null)
            {
                return;
            }
            LazyOptional<IChiseledSheepCapability> optionalCapability = entity.getCapability(Capability.CHISELED_SHEEP);
            optionalCapability.ifPresent(capability -> {
                capability.setItemVariantIdentifier(itemVariantIdentifier);

                final CompoundNBT extraData = capability.getExtraData();
                for (final String key : extraData.keySet()) {
                    extraData.remove(key);
                }

                final CompoundNBT newData = extraData;
                for (final String key : newData.keySet()) {
                    INBT inbt = newData.get(key);
                    assert inbt != null;
                    extraData.put(key, inbt.copy());
                }
            });
        });
    }

}
