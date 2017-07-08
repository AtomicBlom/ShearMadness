package com.github.atomicblom.shearmadness;

import com.github.atomicblom.shearmadness.api.BehaviourRegistry;
import com.github.atomicblom.shearmadness.api.VariationRegistry;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.api.events.RegisterAdditionalCapabilitiesEvent;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessBehaviourEvent;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessCommandEvent;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapability;
import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapabilityStorage;
import com.github.atomicblom.shearmadness.configuration.ConfigurationHandler;
import com.github.atomicblom.shearmadness.configuration.Settings;
import com.github.atomicblom.shearmadness.networking.*;
import com.github.atomicblom.shearmadness.rendering.RenderChiselSheep;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import com.github.atomicblom.shearmadness.utility.Logger;
import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.utility.ShearMadnessCommand;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import java.util.List;

@SuppressWarnings("MethodMayBeStatic")
@Mod(modid = CommonReference.MOD_ID, version = CommonReference.VERSION, guiFactory = Reference.MOD_GUI_FACTORY, dependencies = "required-after:chisel@[MC1.12-0.0.11.0,)", acceptedMinecraftVersions = "[1.12, 1.13)")
public class ShearMadnessMod
{
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(CommonReference.MOD_ID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        if (!Settings.isReleaseBuild()) {
            Logger.info("You are not running a release build of Shear Madness. This message is purely for informational purposes.");
        }

        //Networking
        CHANNEL.registerMessage(CheckSheepChiseledRequestMessageHandler.class, CheckSheepChiseledRequestMessage.class, 0, Side.SERVER);
        CHANNEL.registerMessage(SheepChiseledMessageHandler.class, SheepChiseledMessage.class, 1, Side.CLIENT);
        CHANNEL.registerMessage(SpawnCustomParticleMessageHandler.class, SpawnCustomParticleMessage.class, 2, Side.CLIENT);
        CHANNEL.registerMessage(PlayCustomSoundMessageHandler.class, PlayCustomSoundMessage.class, 3, Side.CLIENT);
        CHANNEL.registerMessage(SheepChiselDataUpdatedMessageHandler.class, SheepChiselDataUpdatedMessage.class, 4, Side.CLIENT);

        //Capabilities
        CapabilityManager.INSTANCE.register(IChiseledSheepCapability.class, ChiseledSheepCapabilityStorage.instance, ChiseledSheepCapability::new);

        MinecraftForge.EVENT_BUS.post(new RegisterAdditionalCapabilitiesEvent());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.post(new RegisterShearMadnessBehaviourEvent(BehaviourRegistry.INSTANCE));

    }

    @EventHandler
    public void onMissingBlockMapping(RegistryEvent.MissingMappings<Block> event) {
        Logger.info("Repairing missing mappings");
        for (RegistryEvent.MissingMappings.Mapping<Block> missingMapping : event.getMappings()) {
            String resourcePath = missingMapping.key.getResourcePath().toLowerCase();

            if ("invisibleredstone".equals(resourcePath)) {
                missingMapping.remap(BlockLibrary.invisible_redstone);
            } else if ("invisibleglowstone".equals(resourcePath)) {
                missingMapping.remap(BlockLibrary.invisible_glowstone);
            } else if ("invisiblebookshelf".equals(resourcePath)) {
                missingMapping.remap(BlockLibrary.invisible_bookshelf);
            }
        }
    }

    @EventHandler
    public void onMissingItemMapping(RegistryEvent.MissingMappings<Item> event) {
        Logger.info("Repairing missing mappings");
        for (RegistryEvent.MissingMappings.Mapping<Item> missingMapping : event.getMappings()) {
            String resourcePath = missingMapping.key.getResourcePath().toLowerCase();
            if ("invisibleredstone".equals(resourcePath)) {
                missingMapping.remap(Item.getItemFromBlock(BlockLibrary.invisible_redstone));
            } else if ("invisibleglowstone".equals(resourcePath)) {
                missingMapping.remap(Item.getItemFromBlock(BlockLibrary.invisible_glowstone));
            } else if ("invisiblebookshelf".equals(resourcePath)) {
                missingMapping.remap(Item.getItemFromBlock(BlockLibrary.invisible_bookshelf));
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
