package com.github.atomicblom.shearmadness.variations.chancecubes.behaviour;

import chanceCubes.config.CCubesSettings;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.networking.SpawnCustomParticleMessage;
import com.github.atomicblom.shearmadness.utility.Logger;
import com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubeType;
import com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubesConfiguration;
import com.github.atomicblom.shearmadness.variations.chancecubes.DelayedTasks;
import com.github.atomicblom.shearmadness.variations.chancecubes.ParticleLibrary;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import static com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubesReference.CHANCE_CUBE_PARTICIPATION;

public class ChanceCubeBehaviour extends BehaviourBase<ChanceCubeBehaviour> {
    private final ChanceCubeType type;

    public ChanceCubeBehaviour(EntitySheep sheep, ChanceCubeType type) {
        super(sheep, () -> ChanceCubesConfiguration.enabled.getBoolean());
        this.type = type;
    }

    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        final EntitySheep entity = getEntity();
        final World worldObj = entity.worldObj;

        final List<EntityPlayer> players = worldObj.getPlayers(
                EntityPlayer.class,
                playerEntity -> playerEntity != null &&
                        playerEntity.getDistanceToEntity(entity) < ChanceCubesConfiguration.distance.getInt() &&
                        playerEntity.hasCapability(CHANCE_CUBE_PARTICIPATION, null) &&
                        playerEntity.getCapability(CHANCE_CUBE_PARTICIPATION, null).isParticipating()
        );

        if (players.isEmpty()) {
            return;
        }
        final EntityPlayer selectedPlayer = players.get(worldObj.rand.nextInt(players.size()));
        if (selectedPlayer instanceof EntityPlayerMP) {
            final BlockPos position = selectedPlayer.getPosition();
            DelayedTasks.addDelayedTask(worldObj.getTotalWorldTime() + 10,
                    () -> {
                        if (selectedPlayer.getEntityWorld() != worldObj) {
                            return;
                        }
                        Logger.info("%s, you lucky devil. You get a chance cube sheep.", selectedPlayer.getGameProfile().getName());

                        ShearMadnessMod.CHANNEL.sendTo(
                                new SpawnCustomParticleMessage(
                                        ParticleLibrary.sheep_head.getRegistryName(), true, position.getX(), position.getY(), position.getZ(), 0, 0, 0, 0, 1),
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
