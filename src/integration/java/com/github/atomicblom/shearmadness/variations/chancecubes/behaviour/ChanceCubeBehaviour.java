package com.github.atomicblom.shearmadness.variations.chancecubes.behaviour;

import chanceCubes.registry.ChanceCubeRegistry;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.variations.chancecubes.capability.ChanceCubeParticipationCapabilityProvider;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import java.util.List;

import static com.github.atomicblom.shearmadness.variations.chancecubes.ChanceCubesLibrary.CHANCE_CUBE_PARTICIPATION;

public class ChanceCubeBehaviour extends BehaviourBase<ChanceCubeBehaviour> {
    public ChanceCubeBehaviour(EntitySheep sheep) {
        super(sheep);
    }

    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        EntitySheep entity = getEntity();
        World worldObj = entity.worldObj;

        List<EntityPlayer> players = worldObj.getPlayers(
                EntityPlayer.class,
                (playerEntity) -> playerEntity.getDistanceToEntity(entity) < 500 &&
                        playerEntity.hasCapability(CHANCE_CUBE_PARTICIPATION, null) &&
                        playerEntity.getCapability(CHANCE_CUBE_PARTICIPATION, null).isParticipating()
        );

        if (players.size() <= 0) return;
        EntityPlayer selectedPlayer = players.get(worldObj.rand.nextInt(players.size()))

        ChanceCubeRegistry.INSTANCE.triggerRandomReward(
                worldObj,
                previousPos,
                selectedPlayer,
                Math.round((float) (worldObj.rand.nextGaussian() * 40))
                );

    }
}
