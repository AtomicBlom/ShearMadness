package com.github.atomicblom.shearmadness.networking;

import com.github.atomicblom.shearmadness.utility.SoundLibrary;
import com.google.common.base.MoreObjects;;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import com.github.atomicblom.shearmadness.api.Capability;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;

import java.util.function.Supplier;

public class SheepChiseledMessage
{
    private int sheepId;
    private boolean isChiseled;
    private ItemStack itemStack = ItemStack.EMPTY;
    private CompoundNBT extraData;
    private int itemVariantIdentifier;

    @SuppressWarnings("unused")
    public SheepChiseledMessage(PacketBuffer buf) {
        sheepId = buf.readInt();
        isChiseled = buf.readBoolean();
        itemStack = buf.readItemStack();
        extraData = buf.readCompoundTag();
        itemVariantIdentifier = buf.readInt();
    }

    public SheepChiseledMessage(Entity sheep)
    {
        sheepId = sheep.getEntityId();

        LazyOptional<IChiseledSheepCapability> optionalCapability = sheep.getCapability(Capability.CHISELED_SHEEP);
        optionalCapability.ifPresent( capability -> {
            isChiseled = capability.isChiseled();
            itemStack = capability.getChiselItemStack();
            extraData = capability.getExtraData();
            itemVariantIdentifier = capability.getItemVariantIdentifier();
        });
    }

    public void toBytes(PacketBuffer buf)
    {
        buf.writeInt(sheepId);
        buf.writeBoolean(isChiseled);
        buf.writeItemStack(itemStack);
        buf.writeCompoundTag(extraData);
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

    public CompoundNBT getExtraData() {
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

    public static long lastSoundPlayed = 0;

    @SuppressWarnings({"ReturnOfNull", "ConstantConditions"})
    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final int sheepId = this.sheepId;
            final Entity entity = Minecraft.getInstance().world.getEntityByID(sheepId);

            if (entity == null)
            {
                return;
            }
            LazyOptional<IChiseledSheepCapability> optionalCapability = entity.getCapability(Capability.CHISELED_SHEEP);
            optionalCapability.ifPresent(capability -> {
                final boolean chiseled = this.isChiseled;
                if (chiseled) {
                    capability.chisel(this.itemStack);
                    long totalWorldTime = entity.getEntityWorld().getGameTime();
                    if (lastSoundPlayed < totalWorldTime) {
                        lastSoundPlayed = totalWorldTime;
                        entity.world.playSound(entity.posX, entity.posY, entity.posZ, SoundLibrary.sheepchiseled, SoundCategory.NEUTRAL, 0.5F, 1.0f, true);
                    }
                } else {
                    capability.unChisel();
                }

                capability.setItemVariantIdentifier(this.itemVariantIdentifier);

                final CompoundNBT extraData = capability.getExtraData();
                for (final String key : extraData.keySet()) {
                    extraData.remove(key);
                }

                final CompoundNBT newData = this.extraData;
                for (final String key : newData.keySet()) {
                    extraData.put(key, newData.get(key).copy());
                }
            });
        });
    }
}
