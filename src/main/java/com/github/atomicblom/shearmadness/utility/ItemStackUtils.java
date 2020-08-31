package com.github.atomicblom.shearmadness.utility;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.util.Arrays;
import java.util.Random;

@SuppressWarnings("UtilityClass")
public final class ItemStackUtils
{
    private ItemStackUtils() {}

    public static void dropItem(Entity nearEntity, ItemStack itemStack)
    {
        final ItemEntity item = nearEntity.entityDropItem(itemStack, 1.0F);
        if (item == null) {
            return;
        }
        final Random rand = new Random();

        item.addVelocity(
            rand.nextFloat() * 0.05F,
            (rand.nextFloat() - rand.nextFloat()) * 0.1F,
            (rand.nextFloat() - rand.nextFloat()) * 0.1F
        );
    }

    private static final PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);

    public static int getHash(ItemStack itemStack)
    {
        final ByteBuf buffer = allocator.heapBuffer();
        final PacketBuffer packetBuffer = new PacketBuffer(buffer);
        packetBuffer.writeItemStack(itemStack);
        final int i = Arrays.hashCode(packetBuffer.array());
        packetBuffer.release();
        return i;
    }
}
