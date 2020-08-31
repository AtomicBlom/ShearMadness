package com.github.atomicblom.shearmadness.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class PlayCustomSoundMessage
{
    private double posX;
    private double posY;
    private double posZ;
    private ResourceLocation soundIn;
    private SoundCategory category;
    private float volume;
    private float pitch;
    private boolean distanceDelay;

    public PlayCustomSoundMessage(PacketBuffer buf)
    {
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();

        this.category = SoundCategory.values()[buf.readInt()];
        this.soundIn = new ResourceLocation(buf.readString(255));

        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
        this.distanceDelay = buf.readBoolean();
    }

    public PlayCustomSoundMessage(double posX, double posY, double posZ, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {

        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.soundIn = soundIn.getRegistryName();
        this.category = category;
        this.volume = volume;
        this.pitch = pitch;
        this.distanceDelay = distanceDelay;
    }

    public void toBytes(PacketBuffer buf)
    {
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);

        buf.writeInt(this.category.ordinal());
        buf.writeString(this.soundIn.toString());

        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
        buf.writeBoolean(this.distanceDelay);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            final IForgeRegistry<SoundEvent> registry = GameRegistry.findRegistry(SoundEvent.class);

            Minecraft.getInstance().player.world.playSound(
                    this.posX, this.posY, this.posZ,
                    registry.getValue(this.soundIn),
                    this.category,
                    this.volume,
                    this.pitch,
                    this.distanceDelay
            );
        });
    }
}