package com.github.atomicblom.chiselsheep;

import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.capability.ChiseledSheepCapabilitySerializer;
import com.github.atomicblom.chiselsheep.capability.IChiseledSheepCapability;
import com.github.atomicblom.chiselsheep.configuration.ConfigurationHandler;
import com.github.atomicblom.chiselsheep.networking.CheckSheepChiseledRequestMessage;
import com.github.atomicblom.chiselsheep.networking.CheckSheepChiseledRequestMessageHandler;
import com.github.atomicblom.chiselsheep.networking.SheepChiseledMessage;
import com.github.atomicblom.chiselsheep.networking.SheepChiseledMessageHandler;
import com.github.atomicblom.chiselsheep.proxy.IProxy;
import com.github.atomicblom.chiselsheep.utility.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("MethodMayBeStatic")
@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, guiFactory = Reference.MOD_GUI_FACTORY)
public class ChiselSheepMod
{
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    @SidedProxy(clientSide = "com.github.atomicblom.chiselsheep.proxy.ClientProxy", serverSide = "com.github.atomicblom.chiselsheep.proxy.CommonProxy")
    private static IProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        MinecraftForge.EVENT_BUS.register(proxy);
        CHANNEL.registerMessage(CheckSheepChiseledRequestMessageHandler.class, CheckSheepChiseledRequestMessage.class, 0, Side.SERVER);
        CHANNEL.registerMessage(SheepChiseledMessageHandler.class, SheepChiseledMessage.class, 1, Side.CLIENT);
        CapabilityManager.INSTANCE.register(IChiseledSheepCapability.class, ChiseledSheepCapabilitySerializer.instance, ChiseledSheepCapability::new);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerRenderers();
    }
}
