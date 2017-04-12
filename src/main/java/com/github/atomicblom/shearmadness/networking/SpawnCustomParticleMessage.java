package com.github.atomicblom.shearmadness.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class SpawnCustomParticleMessage implements IMessage
{
    private ResourceLocation particleResourceLocation;
    private float xCoord;
    private float yCoord;
    private float zCoord;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    private float particleSpeed;
    private int particleCount;
    private boolean longDistance;
    /** These are the block/item ids and possibly metaData ids that are used to color or texture the particle. */
    private int[] particleArguments;

    public SpawnCustomParticleMessage()
    {
    }

    public SpawnCustomParticleMessage(ResourceLocation particleResourceLocation, boolean longDistanceIn, float xIn, float yIn, float zIn, float xOffsetIn, float yOffsetIn, float zOffsetIn, float speedIn, int countIn, int... argumentsIn)
    {
        this.particleResourceLocation = particleResourceLocation;
        this.longDistance = longDistanceIn;
        this.xCoord = xIn;
        this.yCoord = yIn;
        this.zCoord = zIn;
        this.xOffset = xOffsetIn;
        this.yOffset = yOffsetIn;
        this.zOffset = zOffsetIn;
        this.particleSpeed = speedIn;
        this.particleCount = countIn;
        this.particleArguments = argumentsIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        PacketBuffer buf = new PacketBuffer(byteBuf);
        this.particleResourceLocation = new ResourceLocation(buf.readStringFromBuffer(255));
        this.longDistance = buf.readBoolean();
        this.xCoord = buf.readFloat();
        this.yCoord = buf.readFloat();
        this.zCoord = buf.readFloat();
        this.xOffset = buf.readFloat();
        this.yOffset = buf.readFloat();
        this.zOffset = buf.readFloat();
        this.particleSpeed = buf.readFloat();
        this.particleCount = buf.readInt();
        int argumentCount = buf.readInt();
        this.particleArguments = new int[argumentCount];

        for (int j = 0; j < argumentCount; ++j)
        {
            this.particleArguments[j] = buf.readVarIntFromBuffer();
        }
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        PacketBuffer buf = new PacketBuffer(byteBuf);
        buf.writeString(this.particleResourceLocation.toString());
        buf.writeBoolean(this.longDistance);
        buf.writeFloat(this.xCoord);
        buf.writeFloat(this.yCoord);
        buf.writeFloat(this.zCoord);
        buf.writeFloat(this.xOffset);
        buf.writeFloat(this.yOffset);
        buf.writeFloat(this.zOffset);
        buf.writeFloat(this.particleSpeed);
        buf.writeInt(this.particleCount);
        buf.writeInt(this.particleArguments.length);

        for (int j = 0; j < this.particleArguments.length; ++j)
        {
            buf.writeVarIntToBuffer(this.particleArguments[j]);
        }
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getParticleResourceLocation()
    {
        return this.particleResourceLocation;
    }


    @SideOnly(Side.CLIENT)
    public boolean isLongDistance()
    {
        return this.longDistance;
    }

    /**
     * Gets the x coordinate to spawn the particle.
     */
    @SideOnly(Side.CLIENT)
    public double getXCoordinate()
    {
        return (double)this.xCoord;
    }

    /**
     * Gets the y coordinate to spawn the particle.
     */
    @SideOnly(Side.CLIENT)
    public double getYCoordinate()
    {
        return (double)this.yCoord;
    }

    /**
     * Gets the z coordinate to spawn the particle.
     */
    @SideOnly(Side.CLIENT)
    public double getZCoordinate()
    {
        return (double)this.zCoord;
    }

    /**
     * Gets the x coordinate offset for the particle. The particle may use the offset for particle spread.
     */
    @SideOnly(Side.CLIENT)
    public float getXOffset()
    {
        return this.xOffset;
    }

    /**
     * Gets the y coordinate offset for the particle. The particle may use the offset for particle spread.
     */
    @SideOnly(Side.CLIENT)
    public float getYOffset()
    {
        return this.yOffset;
    }

    /**
     * Gets the z coordinate offset for the particle. The particle may use the offset for particle spread.
     */
    @SideOnly(Side.CLIENT)
    public float getZOffset()
    {
        return this.zOffset;
    }

    /**
     * Gets the speed of the particle animation (used in client side rendering).
     */
    @SideOnly(Side.CLIENT)
    public float getParticleSpeed()
    {
        return this.particleSpeed;
    }

    /**
     * Gets the amount of particles to spawn
     */
    @SideOnly(Side.CLIENT)
    public int getParticleCount()
    {
        return this.particleCount;
    }

    /**
     * Gets the particle arguments. Some particles rely on block and/or item ids and sometimes metadata ids to color or
     * texture the particle.
     */
    @SideOnly(Side.CLIENT)
    public int[] getParticleArgs()
    {
        return this.particleArguments;
    }
}