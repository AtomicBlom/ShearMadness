package com.github.atomicblom.chiselsheep.networking;

import com.github.atomicblom.chiselsheep.ChiselLibrary;
import com.github.atomicblom.chiselsheep.ChiselSheepMod;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static com.github.atomicblom.chiselsheep.ChiselSheepMod.CHISELED_SHEEP_CAPABILITY;

/*public class TryChiselSheepMessageHandler implements IMessageHandler<TryChiselSheepMessage, IMessage>
{
    @Override
    public IMessage onMessage(TryChiselSheepMessage message, MessageContext ctx) {
        final EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
        ItemStack activeStack = serverPlayer.inventory.getCurrentItem();
        boolean isChisel = false;
        if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem())) {
            isChisel = true;
        } else {
            activeStack = serverPlayer.inventory.offHandInventory[0];
            if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem())) {
                isChisel = true;
            }
        }
        if (!isChisel) { return null; }

        final Entity entity = ctx.getServerHandler().playerEntity.getServerWorld().getEntityByID(message.getSheepUUID());

        if (entity != null && entity.hasCapability(CHISELED_SHEEP_CAPABILITY, null))
        {
            final IChiseledSheepCapability capability = entity.getCapability(CHISELED_SHEEP_CAPABILITY, null);

            final NBTTagCompound tagCompound = activeStack.getTagCompound();
            if (tagCompound != null)
            {
                final NBTTagCompound chiselTarget = tagCompound.getCompoundTag("chiselTarget");

                if (chiselTarget.hasKey("id"))
                {
                    final ItemStack chiselItemStack = ItemStack.loadItemStackFromNBT(chiselTarget);
                    capability.setChiselItemStack(chiselItemStack.copy());
                } else {
                    capability.setChiseled(false);
                }
            } else {
                capability.setChiseled(false);
            }

            ChiselSheepMod.CHANNEL.sendToAll(new SheepChiseledMessage(entity));
        }

        return null;
    }
}
*/