package com.github.atomicblom.shearmadness.variations.chancecubes.behaviour;

import chanceCubes.registry.ChanceCubeRegistry;
import com.github.atomicblom.shearmadness.ShearMadnessMod;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.networking.SpawnCustomParticleMessage;
import com.github.atomicblom.shearmadness.utility.Logger;
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
    public ChanceCubeBehaviour(EntitySheep sheep) {
        super(sheep);
    }

    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        final EntitySheep entity = getEntity();
        final World worldObj = entity.worldObj;

        final List<EntityPlayer> players = worldObj.getPlayers(
                EntityPlayer.class,
                playerEntity -> playerEntity != null &&
                        playerEntity.getDistanceToEntity(entity) < 500 &&
                        playerEntity.hasCapability(CHANCE_CUBE_PARTICIPATION, null) &&
                        playerEntity.getCapability(CHANCE_CUBE_PARTICIPATION, null).isParticipating()
        );

        if (players.isEmpty()) {
            Logger.info("No players nearby that qualify as target of a chance cube");
            return;

        }
        final EntityPlayer selectedPlayer = players.get(worldObj.rand.nextInt(players.size()));
        if (selectedPlayer instanceof EntityPlayerMP) {
            final BlockPos position = selectedPlayer.getPosition();
            ShearMadnessMod.CHANNEL.sendTo(
                    new SpawnCustomParticleMessage(
                            ParticleLibrary.sheep_head.getRegistryName(), true, position.getX(), position.getY(), position.getZ(), 0, 0, 0, 0, 1),
                    (EntityPlayerMP)selectedPlayer);
        } else {
            Logger.info("You dun assumed something.");
        }

        Logger.info("Starting timer");
        DelayedTasks.addDelayedTask(
                worldObj.getTotalWorldTime() + 35,
                () -> {
                    if (selectedPlayer.getEntityWorld() != worldObj) {
                        Logger.info("Player has left the world");
                        return;
                    }

                    Logger.info("Times Up. Triggering reward");
                    ChanceCubeRegistry.INSTANCE.triggerRandomReward(
                            worldObj,
                            previousPos,
                            selectedPlayer,
                            Math.round((float) (worldObj.rand.nextGaussian() * 40))
                    );
                }
        );



    }
}
