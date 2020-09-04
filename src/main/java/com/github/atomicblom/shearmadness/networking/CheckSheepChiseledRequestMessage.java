package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.api.Capability;
import com.google.common.base.MoreObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class CheckSheepChiseledRequestMessage {
    private String sheepUUID;

    public CheckSheepChiseledRequestMessage(PacketBuffer buf) {
        sheepUUID = buf.readString(32767);
    }

    public CheckSheepChiseledRequestMessage(SheepEntity sheep) {
        sheepUUID = sheep.getCachedUniqueIdString();
    }

    public void toBytes(PacketBuffer buf)
    {
        buf.writeString(sheepUUID);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("sheepUUID", sheepUUID)
                .toString();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            ServerWorld worldObj = ctx.get().getSender().getServerWorld();
            final Entity entity = worldObj.getEntityByUuid(UUID.fromString(sheepUUID));
            if (entity == null) return;

            entity.getCapability(Capability.CHISELED_SHEEP).ifPresent(capability -> {
                if (capability.isChiseled())
                {
                    ShearMadnessMod.LOGGER.info("Notifying sheep chiseled - entity {}", entity.toString());
                    ShearMadnessMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SheepChiseledMessage(entity));
                }
            });
        });
    }
}
