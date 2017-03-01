package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.api.events.RegisterAdditionalCapabilitiesEvent;
import com.github.atomicblom.shearmadness.api.events.RegisterShearMadnessCommandEvent;
import com.github.atomicblom.shearmadness.api.particles.ICustomParticleFactory;
import com.github.atomicblom.shearmadness.variations.CommonReference;
import com.github.atomicblom.shearmadness.variations.chancecubes.capability.ChanceCubeParticipationCapabilityProvider;
import com.github.atomicblom.shearmadness.variations.chancecubes.capability.ChanceCubeParticipationStorage;
import com.github.atomicblom.shearmadness.variations.chancecubes.capability.IChanceCubeParticipationCapability;
import com.github.atomicblom.shearmadness.variations.chancecubes.client.SheepHeadParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import static com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubesReference.ChanceCubeParticipationCapability;

@SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
@Mod.EventBusSubscriber
public class ChanceCubeForgeEvents {

    @SubscribeEvent
    public static void onCapabilityAttaching(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
            event.addCapability(ChanceCubeParticipationCapability, new ChanceCubeParticipationCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onRegisterAdditionalCapabilities(RegisterAdditionalCapabilitiesEvent event) {
        CapabilityManager.INSTANCE.register(IChanceCubeParticipationCapability.class, ChanceCubeParticipationStorage.instance, com.github.atomicblom.shearmadness.variations.chancecubes.capability.ChanceCubeParticipationCapability::new);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterShearMadnessCommandEvent event) {
        event.addCommand(new ChanceCubeCommand());
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        DelayedTasks.processDelayedTasks(event.world.getTotalWorldTime());
    }

    @SubscribeEvent
    public static void onRegisterCustomParticles(RegistryEvent.Register<ICustomParticleFactory> event) {
        final IForgeRegistry<ICustomParticleFactory> registry = event.getRegistry();
        registry.register(new SheepHeadParticle.Factory()
                .setRegistryName(new ResourceLocation(CommonReference.MOD_ID, "sheep_head"))
        );
    }
}