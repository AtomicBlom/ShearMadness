package com.github.atomicblom.chiselsheep;

import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapabilityProvider;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.configuration.Settings;
import com.github.atomicblom.chiselsheep.networking.CheckSheepChiseledRequestMessage;
import com.github.atomicblom.chiselsheep.networking.SheepChiseledMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import java.util.Random;

import static com.github.atomicblom.chiselsheep.ChiselSheepMod.CHANNEL;

@SuppressWarnings("MethodMayBeStatic")
public enum EventManagement
{
    INSTANCE;

    @SubscribeEvent
    public void onPlayerInteractionWithEntity(PlayerInteractEvent.EntityInteract event)
    {
        //Process for shearing a sheep
        if (event.getWorld().isRemote) return;
        if (!(event.getTarget() instanceof EntitySheep)) return;
        final ItemStack itemStack = event.getItemStack();
        if (itemStack == null || !(itemStack.getItem() instanceof ItemShears)) return;

        final EntitySheep sheep = (EntitySheep) event.getTarget();
        if (!sheep.isShearable(itemStack, event.getWorld(), event.getPos())) return;

        final IChiseledSheepCapability capability = sheep.getCapability(ChiseledSheepCapabilityProvider.CHISELED_SHEEP, null);
        if (capability == null) return;
        if (!capability.isChiseled()) return;

        //Ok, we have a chiseled sheep, cancel vanilla.
        event.setCanceled(true);

        Shearing.ShearSheep(itemStack, sheep, capability);
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event)
    {
        //Process for chiseling a sheep
        final Entity target = event.getTarget();
        final Entity sheep = target;
        if (sheep == null) return;

        final EntityPlayer entityPlayer = event.getEntityPlayer();
        final EntityPlayer serverPlayer = entityPlayer;
        ItemStack activeStack = serverPlayer.inventory.getCurrentItem();
        boolean attackedWithChisel = false;
        if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem()))
        {
            attackedWithChisel = true;
        } else
        {
            activeStack = serverPlayer.inventory.offHandInventory[0];
            if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem()))
            {
                attackedWithChisel = true;
            }
        }
        if (!attackedWithChisel)
        {
            return;
        }

        event.setCanceled(true);

        Chiseling.ChiselSheep(target, sheep, entityPlayer, activeStack);
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            CHANNEL.sendToServer(new CheckSheepChiseledRequestMessage(event.getEntity()));
        }
    }

    @SubscribeEvent
    public void onCapabilityAttaching(AttachCapabilitiesEvent.Entity event)
    {
        if (event.getEntity().getClass().equals(EntitySheep.class))
        {
            event.addCapability(new ResourceLocation(Reference.MOD_ID, "chiseledSheep"), new ChiseledSheepCapabilityProvider());
        }
    }
}
