package com.github.atomicblom.chiselsheep;

import com.github.atomicblom.chiselsheep.capability.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.Callable;

@Mod(modid = ChiselSheepMod.MODID, version = ChiselSheepMod.VERSION)
public class ChiselSheepMod
{
    public static final String MODID = "chiselsheep";
    public static final String VERSION = "1.0";

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
        CapabilityManager.INSTANCE.register(IChiseledSheepCapability.class, ChiseledSheepCapability.ExtentionStorage.instance, new Callable<IChiseledSheepCapability>() {
            @Override
            public IChiseledSheepCapability call() throws Exception {
                return new ChiseledSheepCapability();
            }
        });
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerRenderers();
    }

    @SubscribeEvent
    public void onRightClick(RightClickItem event) {
        final ItemStack itemStack = event.getItemStack();
        boolean chiselOk = false;
        if (itemStack != null) {
            final Item item = itemStack.getItem();
            if (item.getUnlocalizedName().startsWith("item.chisel.chisel")) {
                chiselOk = true;
            }
        }

        final RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver != null && mouseOver.typeOfHit == Type.ENTITY) {
            Entity entity = mouseOver.entityHit;

            if (chiselOk && entity.hasCapability(CHISELED_SHEEP_CAPABILITY, null))
            {
                IChiseledSheepCapability capability = entity.getCapability(CHISELED_SHEEP_CAPABILITY, null);

                event.setCanceled(true);
                final NBTTagCompound tagCompound = itemStack.getTagCompound();
                if (tagCompound != null)
                {
                    final NBTTagCompound chiselTarget = tagCompound.getCompoundTag("chiselTarget");

                    if (chiselTarget.hasKey("id"))
                    {
                        final ItemStack chiselItemStack = ItemStack.loadItemStackFromNBT(chiselTarget);
                        capability.setChiselItemStack(chiselItemStack.copy());
                        final String displayName = chiselItemStack.getDisplayName();
                        entity.setCustomNameTag(displayName);
                    } else {
                        capability.setChiseled(false);
                        entity.setCustomNameTag("Cow Sheep");
                    }
                } else {
                    capability.setChiseled(false);
                    entity.setCustomNameTag("Cow Sheep");
                }
            }
        }
    }

    @SubscribeEvent
    public void onCapabilityAttaching(AttachCapabilitiesEvent.Entity event) {
        if (event.getEntity().getClass().equals(EntitySheep.class)) {
            event.addCapability(new ResourceLocation(ChiselSheepMod.MODID, "chiselledSheep"), new Provider());
        }
    }
}
