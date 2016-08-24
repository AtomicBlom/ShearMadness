package com.github.atomicblom.shearmadness;

import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapability;
import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapabilityStorage;
import com.github.atomicblom.shearmadness.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.configuration.ConfigurationHandler;
import com.github.atomicblom.shearmadness.networking.CheckSheepChiseledRequestMessage;
import com.github.atomicblom.shearmadness.networking.CheckSheepChiseledRequestMessageHandler;
import com.github.atomicblom.shearmadness.networking.SheepChiseledMessage;
import com.github.atomicblom.shearmadness.networking.SheepChiseledMessageHandler;
import com.github.atomicblom.shearmadness.proxy.CommonBlockProxy;
import com.github.atomicblom.shearmadness.proxy.IRenderProxy;
import com.github.atomicblom.shearmadness.utility.Logger;
import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.utility.ShearMadnessVariations;
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
@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, guiFactory = Reference.MOD_GUI_FACTORY, dependencies = "required-after:chisel", acceptedMinecraftVersions = "[1.9.4, 1.11)")
public class ShearMadnessMod
{
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    @SidedProxy(
            modId = Reference.MOD_ID,
            clientSide = "com.github.atomicblom.shearmadness.proxy.ClientRenderProxy",
            serverSide = "com.github.atomicblom.shearmadness.proxy.CommonRenderProxy")
    public static IRenderProxy proxy = null;

    @SidedProxy(
            modId = Reference.MOD_ID,
            clientSide = "com.github.atomicblom.shearmadness.proxy.ClientBlockProxy",
            serverSide = "com.github.atomicblom.shearmadness.proxy.CommonBlockProxy")
    public static CommonBlockProxy BLOCK_PROXY;
    public static boolean DEBUG = false;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        DEBUG = !System.getProperty("ShearMadnessDebug").isEmpty();
        if (DEBUG) {
            Logger.info("Shear Madness models will be recalculated each frame.");
        }

        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        MinecraftForge.EVENT_BUS.register(proxy);
        CHANNEL.registerMessage(CheckSheepChiseledRequestMessageHandler.class, CheckSheepChiseledRequestMessage.class, 0, Side.SERVER);
        CHANNEL.registerMessage(SheepChiseledMessageHandler.class, SheepChiseledMessage.class, 1, Side.CLIENT);
        CapabilityManager.INSTANCE.register(IChiseledSheepCapability.class, ChiseledSheepCapabilityStorage.instance, ChiseledSheepCapability::new);

        BLOCK_PROXY.registerBlocks();
        if (event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(ShearMadnessVariations.INSTANCE);
        }
        proxy.registerSounds();

    }

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerRenderers();
        proxy.fireRegistryEvent();

    }
}
