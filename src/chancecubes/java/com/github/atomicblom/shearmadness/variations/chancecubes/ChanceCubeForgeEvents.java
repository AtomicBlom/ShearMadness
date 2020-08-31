package com.github.atomicblom.shearmadness.variations.chancecubes;

import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.events.RegisterAdditionalCapabilitiesEvent;
import com.github.atomicblom.shearmadness.api.events.ShearMadnessSheepKilledEvent;
import com.github.atomicblom.shearmadness.variations.chancecubes.capability.ChanceCubeParticipationCapabilityProvider;
import com.github.atomicblom.shearmadness.variations.chancecubes.capability.ChanceCubeParticipationStorage;
import com.github.atomicblom.shearmadness.variations.chancecubes.capability.IChanceCubeParticipationCapability;
import com.github.atomicblom.shearmadness.variations.chancecubes.client.SheepHeadParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import static com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubesReference.*;

@SuppressWarnings({"MethodMayBeStatic", "UnnecessarilyQualifiedInnerClassAccess"})
@Mod.EventBusSubscriber(modid = ChanceCubesReference.CHANCE_CUBES_MODID)
public class ChanceCubeForgeEvents {

    @SubscribeEvent
    public static void onCapabilityAttaching(AttachCapabilitiesEvent<Entity> event)
    {

        if (event.getObject() instanceof PlayerEntity)
        {
            event.addCapability(ChanceCubeParticipationCapability, new ChanceCubeParticipationCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onRegisterAdditionalCapabilities(RegisterAdditionalCapabilitiesEvent event) {
        CapabilityManager.INSTANCE.register(IChanceCubeParticipationCapability.class, ChanceCubeParticipationStorage.instance, com.github.atomicblom.shearmadness.variations.chancecubes.capability.ChanceCubeParticipationCapability::new);
    }

    @SubscribeEvent
    public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
        final IForgeRegistry<SoundEvent> registry = event.getRegistry();
        registry.register(new SoundEvent(ChanceCubeSheepDied).setRegistryName(ChanceCubeSheepDied));
        registry.register(new SoundEvent(ChanceCubeGiantCubeSpawned).setRegistryName(ChanceCubeGiantCubeSpawned));
    }

    //FIXME: Readd command
//    @SubscribeEvent
//    public static void onRegisterCommands(RegisterShearMadnessCommandEvent event) {
//        event.addCommand(new ChanceCubeCommand());
//    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        DelayedTasks.processDelayedTasks(event.world.getGameTime());
    }

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
        BasicParticleType basicParticleType = new BasicParticleType(true);
        basicParticleType.setRegistryName(SheepHeadParticle);
        Minecraft.getInstance().particles.registerFactory(basicParticleType,
                new SheepHeadParticle.Factory()
        );
    }

    @SubscribeEvent
    public static void onSheepKilled(ShearMadnessSheepKilledEvent event) {
        if (ItemStackHelper.isStackForBlock(event.getChiselItemStack(),
                ChanceCubesLibrary.chance_cube,
                ChanceCubesLibrary.chance_cube_giant,
                ChanceCubesLibrary.chance_cube_giant_compact,
                ChanceCubesLibrary.chance_icosadedron)) {
            event.noDrops();
        }
    }

    /*@SubscribeEvent
    public static void onBlockPlacedEvent(BlockEvent.PlaceEvent event) {
        EntityPlayer selectedPlayer = event.getPlayer();
        if (selectedPlayer instanceof EntityPlayerMP) {
            final BlockPos position = selectedPlayer.getPosition();
            ShearMadnessMod.CHANNEL.sendTo(
                    new SpawnCustomParticleMessage(
                            ParticleLibrary.sheep_head.getRegistryName(), true, position.getX(), position.getY(), position.getZ(), 0, 0, 0, 0, 1),
                    (EntityPlayerMP)selectedPlayer);
        } else {
            Logger.info("You dun assumed something.");
        }
    }*/
}