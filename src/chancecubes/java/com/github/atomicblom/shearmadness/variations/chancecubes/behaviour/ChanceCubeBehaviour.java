package com.github.atomicblom.shearmadness.variations.chancecubes.behaviour;

import chanceCubes.config.CCubesSettings;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.networking.PlayCustomSoundMessage;
import com.github.atomicblom.shearmadness.variations.IntegrationSettings;
import com.github.atomicblom.shearmadness.variations.chancecubes.*;
import com.github.atomicblom.shearmadness.variations.chancecubes.capability.IChanceCubeParticipationCapability;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubesReference.CHANCE_CUBE_PARTICIPATION;

public class ChanceCubeBehaviour extends BehaviourBase {
    private final ChanceCubeType type;

    private static Logger LOGGER = ChanceCubesReference.LOGGER;

    public ChanceCubeBehaviour(SheepEntity sheep, ChanceCubeType type) {
        super(sheep, IntegrationSettings.ChanceCubes::enabled);
        this.type = type;
    }

    @Override
    public void onBehaviourStarted(BlockPos currentPos) {
        if (type != ChanceCubeType.GIANT) return;

        SheepEntity entity = getEntity();
        World worldObj = entity.world;
        final int distance = IntegrationSettings.ChanceCubes.distance();
        ShearMadnessMod.CHANNEL.send(
                PacketDistributor.NEAR.with(
                        () -> new PacketDistributor.TargetPoint(
                                entity.posX, entity.posY, entity.posZ,
                                distance,
                                worldObj.dimension.getType()
                        )
                ),
                new PlayCustomSoundMessage(
                        entity.posX, entity.posY, entity.posZ,
                        ChanceCubeSounds.chancecube_giantcubespawned, SoundCategory.NEUTRAL,
                        0.3F, 1.0f,
                        true
                )
        );
    }

    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        final SheepEntity entity = getEntity();
        final World worldObj = entity.world;

        final int distance = IntegrationSettings.ChanceCubes.distance();
        final List<PlayerEntity> players = worldObj.getPlayers().stream()
                .filter(Objects::nonNull)
                .filter(player -> player.getDistance(entity) < distance)
                .filter(player -> player.getCapability(CHANCE_CUBE_PARTICIPATION).map(IChanceCubeParticipationCapability::isParticipating).orElse(false))
                .collect(Collectors.toList());

        if (players.isEmpty()) {
            return;
        }
        final PlayerEntity selectedPlayer = players.get(worldObj.rand.nextInt(players.size()));
        if (selectedPlayer instanceof ServerPlayerEntity) {
            final BlockPos position = selectedPlayer.getPosition();
            DelayedTasks.addDelayedTask(
                    worldObj.getGameTime() + 10,
                    () -> {
                        if (selectedPlayer.getEntityWorld() != worldObj) {
                            return;
                        }
                        LOGGER.info("{}, you lucky devil. You get a chance cube sheep.", selectedPlayer.getGameProfile().getName());

                        ShearMadnessMod.CHANNEL.send(
                                PacketDistributor.NEAR.with(
                                        () -> new PacketDistributor.TargetPoint(
                                                entity.posX, entity.posY, entity.posZ,
                                                distance,
                                                worldObj.dimension.getType()
                                        )
                                ),
                                new PlayCustomSoundMessage(
                                        selectedPlayer.posX, selectedPlayer.posY, selectedPlayer.posZ,
                                        ChanceCubeSounds.chancecube_sheepdied, SoundCategory.NEUTRAL,
                                        0.5F, 1.0f,
                                        true
                                )
                        );



                        ServerWorld serverWorld = (ServerWorld)worldObj;
                        serverWorld.spawnParticle(
                                (ServerPlayerEntity)selectedPlayer,
                                ChanceCubeParticleTypes.sheep_head,
                                true,
                                position.getX(), position.getY(), position.getZ(),
                                1,
                                0, 0, 0,
                                1);
                    });
        }

        DelayedTasks.addDelayedTask(
                worldObj.getGameTime() + 45,
                () -> {
                    if (selectedPlayer.getEntityWorld() != worldObj) {
                        return;
                    }

                    int chance = Math.round((float) (worldObj.rand.nextGaussian() * 40));
                    switch(type) {
                        case GIANT:
                            GiantCubeRegistry.INSTANCE.triggerRandomReward(
                                    worldObj,
                                    previousPos,
                                    selectedPlayer,
                                    Math.round((float) (worldObj.rand.nextGaussian() * 40))
                            );
                            break;
                        case ICOSAHEDRON:
                            if(!CCubesSettings.d20UseNormalChances.get())
                            {
                                chance = worldObj.rand.nextBoolean() ? -100 : 100;
                            }
                            else
                            {
                                chance = Math.round((float) (worldObj.rand.nextGaussian() * 40));
                                while(chance > 100 || chance < -100) {
                                    chance = Math.round((float) (worldObj.rand.nextGaussian() * 40));
                                }
                            }
                            //Drop through to normal
                        case NORMAL:
                            ChanceCubeRegistry.INSTANCE.triggerRandomReward(
                                    worldObj,
                                    previousPos,
                                    selectedPlayer,
                                    chance);
                    }
                }
        );
    }
}
