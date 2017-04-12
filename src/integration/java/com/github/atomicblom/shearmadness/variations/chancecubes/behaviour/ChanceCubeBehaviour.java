package com.github.atomicblom.shearmadness.variations.chancecubes.behaviour;

import chanceCubes.config.CCubesSettings;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.networking.PlayCustomSoundMessage;
import com.github.atomicblom.shearmadness.networking.SpawnCustomParticleMessage;
import com.github.atomicblom.shearmadness.utility.Logger;
import com.github.atomicblom.shearmadness.utility.SoundLibrary;
import com.github.atomicblom.shearmadness.variations.chancecubes.*;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

import static com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubesReference.CHANCE_CUBE_PARTICIPATION;

public class ChanceCubeBehaviour extends BehaviourBase<ChanceCubeBehaviour> {
    private final ChanceCubeType type;

    public ChanceCubeBehaviour(EntitySheep sheep, ChanceCubeType type) {
        super(sheep, () -> ChanceCubesConfiguration.enabled.getBoolean());
        this.type = type;
    }

    @Override
    public void onBehaviourStarted(BlockPos currentPos) {
        if (type != ChanceCubeType.GIANT) return;

        EntitySheep entity = getEntity();
        World worldObj = entity.world;
        final int distance = ChanceCubesConfiguration.distance.getInt();
        ShearMadnessMod.CHANNEL.sendToAllAround(
                new PlayCustomSoundMessage(
                        entity.posX, entity.posY, entity.posZ,
                        ChanceCubeSounds.chancecube_giantcubespawned, SoundCategory.NEUTRAL,
                        0.3F, 1.0f,
                        true
                ),
                new NetworkRegistry.TargetPoint(
                        worldObj.provider.getDimension(),
                        entity.posX, entity.posY, entity.posZ,
                        distance
                )
        );
    }

    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        final EntitySheep entity = getEntity();
        final World worldObj = entity.world;

        final int distance = ChanceCubesConfiguration.distance.getInt();
        final List<EntityPlayer> players = worldObj.getPlayers(
                EntityPlayer.class,
                playerEntity -> {

                    return playerEntity != null &&
                            playerEntity.getDistanceToEntity(entity) < distance &&
                            playerEntity.hasCapability(CHANCE_CUBE_PARTICIPATION, null) &&
                            playerEntity.getCapability(CHANCE_CUBE_PARTICIPATION, null).isParticipating();
                }
        );

        if (players.isEmpty()) {
            return;
        }
        final EntityPlayer selectedPlayer = players.get(worldObj.rand.nextInt(players.size()));
        if (selectedPlayer instanceof EntityPlayerMP) {
            final BlockPos position = selectedPlayer.getPosition();
            DelayedTasks.addDelayedTask(
                    worldObj.getTotalWorldTime() + 10,
                    () -> {
                        if (selectedPlayer.getEntityWorld() != worldObj) {
                            return;
                        }
                        Logger.info("%s, you lucky devil. You get a chance cube sheep.", selectedPlayer.getGameProfile().getName());

                        ShearMadnessMod.CHANNEL.sendToAllAround(
                                new PlayCustomSoundMessage(
                                        selectedPlayer.posX, selectedPlayer.posY, selectedPlayer.posZ,
                                        ChanceCubeSounds.chancecube_sheepdied, SoundCategory.NEUTRAL,
                                        0.5F, 1.0f,
                                        true
                                ),
                                new NetworkRegistry.TargetPoint(
                                        worldObj.provider.getDimension(),
                                        entity.posX, entity.posY, entity.posZ,
                                        distance
                                        )
                        );

                        ShearMadnessMod.CHANNEL.sendTo(
                                new SpawnCustomParticleMessage(
                                        ParticleLibrary.sheep_head.getRegistryName(),
                                        true,
                                        position.getX(), position.getY(), position.getZ(),
                                        0, 0, 0,
                                        0, 1
                                ),
                                (EntityPlayerMP)selectedPlayer);
                    });
        }

        DelayedTasks.addDelayedTask(
                worldObj.getTotalWorldTime() + 45,
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
                            if(!CCubesSettings.d20UseNormalChances)
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
