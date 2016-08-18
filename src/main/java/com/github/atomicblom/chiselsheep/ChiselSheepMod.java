package com.github.atomicblom.chiselsheep;

import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapabilitySerializer;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapabilityProvider;
import com.github.atomicblom.chiselsheep.networking.CheckSheepChiseledRequestMessage;
import com.github.atomicblom.chiselsheep.networking.CheckSheepChiseledRequestMessageHandler;
import com.github.atomicblom.chiselsheep.networking.SheepChiseledMessage;
import com.github.atomicblom.chiselsheep.networking.SheepChiseledMessageHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("MethodMayBeStatic")
@Mod(modid = ChiselSheepMod.MODID, version = ChiselSheepMod.VERSION)
public class ChiselSheepMod
{
    public static final String MODID = "chiselsheep";
    public static final String VERSION = "1.0";
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);


    @SidedProxy(clientSide = "com.github.atomicblom.chiselsheep.ClientProxy", serverSide = "com.github.atomicblom.chiselsheep.CommonProxy")
    public static IProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        CHANNEL.registerMessage(CheckSheepChiseledRequestMessageHandler.class, CheckSheepChiseledRequestMessage.class, 0, Side.SERVER);
        CHANNEL.registerMessage(SheepChiseledMessageHandler.class, SheepChiseledMessage.class, 1, Side.CLIENT);
        CapabilityManager.INSTANCE.register(IChiseledSheepCapability.class, ChiseledSheepCapabilitySerializer.instance, ChiseledSheepCapability::new);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerRenderers();
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        final Entity entity = event.getTarget();
        if (entity == null) return;

        final EntityPlayer serverPlayer = event.getEntityPlayer();
        ItemStack activeStack = serverPlayer.inventory.getCurrentItem();
        boolean attackedWithChisel = false;
        if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem())) {
            attackedWithChisel = true;
        } else {
            activeStack = serverPlayer.inventory.offHandInventory[0];
            if (activeStack != null && ChiselLibrary.isChisel(activeStack.getItem())) {
                attackedWithChisel = true;
            }
        }
        if (!attackedWithChisel) { return; }

        event.setCanceled(true);

        if (entity.hasCapability(ChiseledSheepCapabilityProvider.CHISELED_SHEEP, null))
        {
            final IChiseledSheepCapability capability = entity.getCapability(ChiseledSheepCapabilityProvider.CHISELED_SHEEP, null);

            updateCapability(activeStack, capability);

            CHANNEL.sendToAll(new SheepChiseledMessage(entity));
        }
    }

    private void updateCapability(ItemStack activeStack, IChiseledSheepCapability capability)
    {
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
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
            CHANNEL.sendToServer(new CheckSheepChiseledRequestMessage(event.getEntity()));
        }
    }

    @SubscribeEvent
    public void onCapabilityAttaching(AttachCapabilitiesEvent.Entity event) {
        if (event.getEntity().getClass().equals(EntitySheep.class)) {
            event.addCapability(new ResourceLocation(MODID, "chiseledSheep"), new ChiseledSheepCapabilityProvider());
        }
    }
}
