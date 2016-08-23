package com.github.atomicblom.shearmadness.capability;

import com.github.atomicblom.shearmadness.utility.ItemStackUtils;
import com.google.common.base.Objects;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import java.util.Arrays;

public class ChiseledSheepCapability implements IChiseledSheepCapability
{
    private ItemStack itemStack = null;
    private boolean isChiseled = false;
    private int itemIdentifier;

    @Override
    public boolean isChiseled()
    {
        return isChiseled;
    }

    @Override
    public void setChiselItemStack(ItemStack itemStack)
    {
        if (itemStack == null)
        {
            setChiseled(false);
        } else
        {
            setChiseled(true);
            this.itemStack = itemStack;
            if (itemStack != null)
            {
                this.itemIdentifier = ItemStackUtils.getHash(itemStack);
            } else {
                this.itemIdentifier = 0;
            }


        }
    }

    @Override
    public void setChiseled(boolean chiseled)
    {
        if (!chiseled)
        {
            itemStack = null;
        }
        isChiseled = chiseled;
    }

    @Override
    public ItemStack getChiselItemStack()
    {
        return itemStack;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("itemStack", itemStack)
                .add("isChiseled", isChiseled)
                .toString();
    }

    @Override
    public int getItemIdentifier()
    {
        return itemIdentifier;
    }
}

