package com.github.atomicblom.shearmadness.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayCustomSoundMessage implements IMessage
{
    private double posX;
    private double posY;
    private double posZ;
    private ResourceLocation soundIn;
    private SoundCategory category;
    private float volume;
    private float pitch;
    private boolean distanceDelay;

    public PlayCustomSoundMessage()
    {
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

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        PacketBuffer buf = new PacketBuffer(byteBuf);

        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();

        this.category = SoundCategory.values()[buf.readInt()];
        this.soundIn = new ResourceLocation(buf.readString(255));

        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
        this.distanceDelay = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        PacketBuffer buf = new PacketBuffer(byteBuf);

        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);

        buf.writeInt(this.category.ordinal());
        buf.writeString(this.soundIn.toString());

        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
        buf.writeBoolean(this.distanceDelay);
    }

    @SideOnly(Side.CLIENT)
    public double getPosX() {
        return posX;
    }

    @SideOnly(Side.CLIENT)
    public double getPosY() {
        return posY;
    }

    @SideOnly(Side.CLIENT)
    public double getPosZ() {
        return posZ;
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getSoundEvent() {
        return soundIn;
    }

    @SideOnly(Side.CLIENT)
    public SoundCategory getCategory() {
        return category;
    }

    @SideOnly(Side.CLIENT)
    public float getVolume() {
        return volume;
    }

    @SideOnly(Side.CLIENT)
    public float getPitch() {
        return pitch;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldDistanceDelay() {
        return distanceDelay;
    }
}