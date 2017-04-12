package com.github.atomicblom.shearmadness;

import com.github.atomicblom.shearmadness.api.BehaviourRegistry;
import com.github.atomicblom.shearmadness.api.events.RegisterAdditionalCapabilitiesEvent;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessBehaviourEvent;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessCommandEvent;
import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapability;
import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapabilityStorage;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.configuration.ConfigurationHandler;
import com.github.atomicblom.shearmadness.networking.*;
import com.github.atomicblom.shearmadness.proxy.Proxies;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import com.github.atomicblom.shearmadness.utility.Logger;
import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.utility.ShearMadnessCommand;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import net.minecraft.item.Item;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("MethodMayBeStatic")
@Mod(modid = CommonReference.MOD_ID, version = CommonReference.VERSION, guiFactory = Reference.MOD_GUI_FACTORY, dependencies = "required-after:chisel@[MC1.11.2-0.0.9.13,)", acceptedMinecraftVersions = "[1.11, 1.12)")
public class ShearMadnessMod
{
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(CommonReference.MOD_ID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        //Networking
        CHANNEL.registerMessage(CheckSheepChiseledRequestMessageHandler.class, CheckSheepChiseledRequestMessage.class, 0, Side.SERVER);
        CHANNEL.registerMessage(SheepChiseledMessageHandler.class, SheepChiseledMessage.class, 1, Side.CLIENT);
        CHANNEL.registerMessage(SpawnCustomParticleMessageHandler.class, SpawnCustomParticleMessage.class, 2, Side.CLIENT);
        CHANNEL.registerMessage(PlayCustomSoundMessageHandler.class, PlayCustomSoundMessage.class, 3, Side.CLIENT);

        //Capabilities
        CapabilityManager.INSTANCE.register(IChiseledSheepCapability.class, ChiseledSheepCapabilityStorage.instance, ChiseledSheepCapability::new);

        //Eventing
        MinecraftForge.EVENT_BUS.register(Proxies.forgeEventProxy);
        MinecraftForge.EVENT_BUS.register(Proxies.renderProxy);

        MinecraftForge.EVENT_BUS.post(new RegisterAdditionalCapabilitiesEvent());

        Proxies.blockProxy.registerBlocks();
        Proxies.audioProxy.registerSounds();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Proxies.renderProxy.registerRenderers();
        Proxies.forgeEventProxy.fireRegistryEvent();
    }

    @EventHandler
    public void onMissingMapping(FMLMissingMappingsEvent event) {
        Logger.info("Repairing missing mappings");
        for (FMLMissingMappingsEvent.MissingMapping missingMapping : event.get()) {
            String resourcePath = missingMapping.resourceLocation.getResourcePath().toLowerCase();

            if (missingMapping.type == GameRegistry.Type.ITEM) {
                if ("invisibleredstone".equals(resourcePath)) {
                    missingMapping.remap(Item.getItemFromBlock(BlockLibrary.invisible_redstone));
                } else if ("invisibleglowstone".equals(resourcePath)) {
                    missingMapping.remap(Item.getItemFromBlock(BlockLibrary.invisible_glowstone));
                } else if ("invisiblebookshelf".equals(resourcePath)) {
                    missingMapping.remap(Item.getItemFromBlock(BlockLibrary.invisible_bookshelf));
                }
            } else if (missingMapping.type == GameRegistry.Type.BLOCK) {
                if ("invisibleredstone".equals(resourcePath)) {
                    missingMapping.remap(BlockLibrary.invisible_redstone);
                } else if ("invisibleglowstone".equals(resourcePath)) {
                    missingMapping.remap(BlockLibrary.invisible_glowstone);
                } else if ("invisiblebookshelf".equals(resourcePath)) {
                    missingMapping.remap(BlockLibrary.invisible_bookshelf);
                }
            }


        }

    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        List<CommandBase> childCommands = Lists.newArrayList();

        MinecraftForge.EVENT_BUS.post(new RegisterShearMadnessCommandEvent(childCommands));

        event.registerServerCommand(new ShearMadnessCommand(childCommands));
    }
}
