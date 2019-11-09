package com.github.atomicblom.shearmadness;

import com.github.atomicblom.shearmadness.api.BehaviourRegistry;
import com.github.atomicblom.shearmadness.api.VariationRegistry;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessBehaviours;
import com.github.atomicblom.shearmadness.api.events.IRegisterShearMadnessVariations;
import com.github.atomicblom.shearmadness.api.events.RegisterAdditionalCapabilitiesEvent;
import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapability;
import com.github.atomicblom.shearmadness.capability.ChiseledSheepCapabilityStorage;
import com.github.atomicblom.shearmadness.client.gui.NotAChiselScreen;
import com.github.atomicblom.shearmadness.configuration.ConfigurationHandler;
import com.github.atomicblom.shearmadness.networking.*;
import com.github.atomicblom.shearmadness.utility.ContainerTypeLibrary;
import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.api.CommonReference;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CommonReference.MOD_ID)
public class ShearMadnessMod
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(CommonReference.MOD_ID);
    public static SimpleChannel CHANNEL;
    public static IEventBus MOD_EVENT_BUS;

    public ShearMadnessMod() {
        // Register the setup method for modloading
        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_EVENT_BUS.addListener(this::setup);
        // Register the doClientStuff method for modloading
        MOD_EVENT_BUS.addListener(this::doClientStuff);
        MOD_EVENT_BUS.addListener(this::processIMC);

        ConfigurationHandler.build();

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigurationHandler.COMMON_CONFIG);
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigurationHandler.CLIENT_CONFIG);

        ConfigurationHandler.loadConfig(ConfigurationHandler.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("shearmadness-client.toml"));
        ConfigurationHandler.loadConfig(ConfigurationHandler.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("shearmadness-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        CHANNEL = NetworkRegistry.newSimpleChannel(Reference.CHANNEL_NAME, () -> "1.0", s -> true, s -> true);

        int packetId = 0;
        CHANNEL.registerMessage(++packetId, CheckSheepChiseledRequestMessage.class,
                CheckSheepChiseledRequestMessage::toBytes,
                CheckSheepChiseledRequestMessage::new,
                CheckSheepChiseledRequestMessage::handle);

        CHANNEL.registerMessage(++packetId, SheepChiseledMessage.class,
                SheepChiseledMessage::toBytes,
                SheepChiseledMessage::new,
                SheepChiseledMessage::handle);

        CHANNEL.registerMessage(++packetId, SheepChiselDataUpdatedMessage.class,
                SheepChiselDataUpdatedMessage::toBytes,
                SheepChiselDataUpdatedMessage::new,
                SheepChiselDataUpdatedMessage::handle);

        CHANNEL.registerMessage(++packetId, PlayCustomSoundMessage.class,
                PlayCustomSoundMessage::toBytes,
                PlayCustomSoundMessage::new,
                PlayCustomSoundMessage::handle);

        //Capabilities
        CapabilityManager.INSTANCE.register(IChiseledSheepCapability.class, ChiseledSheepCapabilityStorage.instance, ChiseledSheepCapability::new);

        MinecraftForge.EVENT_BUS.post(new RegisterAdditionalCapabilitiesEvent());


    }

    private void processIMC(final InterModProcessEvent event) {
        event.getIMCStream(CommonReference.IMCMethods.REGISTER_VARIATIONS::equals).forEach(imcMessage -> {
            IRegisterShearMadnessVariations messageSupplier = imcMessage.<IRegisterShearMadnessVariations>getMessageSupplier().get();
            messageSupplier.registerVariations(VariationRegistry.INSTANCE);
        });

        event.getIMCStream(CommonReference.IMCMethods.REGISTER_BEHAVIOURS::equals).forEach(imcMessage -> {
            IRegisterShearMadnessBehaviours messageSupplier = imcMessage.<IRegisterShearMadnessBehaviours>getMessageSupplier().get();
            messageSupplier.registerBehaviours(BehaviourRegistry.INSTANCE);
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ContainerTypeLibrary.not_a_chisel_container_type, NotAChiselScreen::new);
    }
}
