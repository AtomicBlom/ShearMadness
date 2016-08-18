package com.github.atomicblom.chiselsheep;

import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.capability.Provider;
import com.github.atomicblom.chiselsheep.networking.CheckSheepChiseledRequestMessage;
import com.github.atomicblom.chiselsheep.networking.CheckSheepChiseledRequestMessageHandler;
import com.github.atomicblom.chiselsheep.networking.SheepChiseledMessage;
import com.github.atomicblom.chiselsheep.networking.SheepChiseledMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
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
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ChiselSheepMod.MODID, version = ChiselSheepMod.VERSION)
public class ChiselSheepMod
{
    public static final String MODID = "chiselsheep";
    public static final String VERSION = "1.0";
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);


    @SidedProxy(clientSide = "com.github.atomicblom.chiselsheep.ClientProxy", serverSide = "com.github.atomicblom.chiselsheep.CommonProxy")
    public static IProxy proxy;

    public static Capability<IChiseledSheepCapability> CHISELED_SHEEP_CAPABILITY;

    @CapabilityInject(IChiseledSheepCapability.class)
    public static void OnCapabilityRegistered(Capability<IChiseledSheepCapability> capability) {
        CHISELED_SHEEP_CAPABILITY = capability;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        CHANNEL.registerMessage(CheckSheepChiseledRequestMessageHandler.class, CheckSheepChiseledRequestMessage.class, 0, Side.SERVER);
        CHANNEL.registerMessage(SheepChiseledMessageHandler.class, SheepChiseledMessage.class, 1, Side.CLIENT);
        CapabilityManager.INSTANCE.register(IChiseledSheepCapability.class, ChiseledSheepCapability.ExtentionStorage.instance, ChiseledSheepCapability::new);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerRenderers();
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        final ItemStack itemStack = event.getEntityPlayer().inventory.getCurrentItem();
        boolean chiselOk = false;
        if (itemStack != null) {
            final Item item = itemStack.getItem();
            if (ChiselLibrary.isChisel(item)) {
                chiselOk = true;
            }
        }

        //final RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        //if (chiselOk && mouseOver != null && mouseOver.typeOfHit == Type.ENTITY) {
        Entity entity = event.getTarget();

            //TryChiselSheepMessage message = new TryChiselSheepMessage(entity.getEntityId());
            //CHANNEL.sendToServer(message);

        //}

        final EntityPlayer serverPlayer = event.getEntityPlayer();
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
        if (!isChisel) { return; }
        event.setCanceled(true);
        //final Entity entity = ctx.getServerHandler().playerEntity.getServerWorld().getEntityByID(message.getSheepUUID());

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
    }

    /*@SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRightClick(RightClickItem event) {
        if (event.getSide() != Side.CLIENT) {
            return;
        }

        final ItemStack itemStack = event.getItemStack();
        boolean chiselOk = false;
        if (itemStack != null) {
            final Item item = itemStack.getItem();
            if (item == ChiselLibrary.chisel_iron || item == ChiselLibrary.chisel_diamond) {
                chiselOk = true;
            }
        }

        final RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (chiselOk && mouseOver != null && mouseOver.typeOfHit == Type.ENTITY) {
            Entity entity = mouseOver.entityHit;

            TryChiselSheepMessage message = new TryChiselSheepMessage(entity.getEntityId());
            CHANNEL.sendToServer(message);
            event.setCanceled(true);
        }
    }*/

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
            CheckSheepChiseledRequestMessage message = new CheckSheepChiseledRequestMessage(event.getEntity());
            CHANNEL.sendToServer(message);
        }
    }

    @SubscribeEvent
    public void onCapabilityAttaching(AttachCapabilitiesEvent.Entity event) {
        if (event.getEntity().getClass().equals(EntitySheep.class)) {
            event.addCapability(new ResourceLocation(ChiselSheepMod.MODID, "chiselledSheep"), new Provider());
            /*if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            {
                Entity entity = event.getEntity();

                Minecraft.getMinecraft().addScheduledTask(() -> {
                    if (entity.getUniqueID() == null) {
                        System.out.println("Attempt to check an entity that hasn't finished initialization");
                        return;
                    }

                });


            }*/
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onPlayerStartTrackingEntity(StartTracking event) {

        /*final boolean hasCapability = event.getTarget().hasCapability(Provider.CAPABILITY, null);
        if (hasCapability) {
            SheepChiseledMessage message = new SheepChiseledMessage(event.getTarget());
            CHANNEL.sendTo(message, (EntityPlayerMP) event.getEntityPlayer());
        }*/
    }

}
