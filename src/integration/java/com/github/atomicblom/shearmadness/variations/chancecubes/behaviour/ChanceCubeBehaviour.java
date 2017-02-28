package com.github.atomicblom.shearmadness.variations.chancecubes.behaviour;

import chanceCubes.registry.ChanceCubeRegistry;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChanceCubeBehaviour extends BehaviourBase<ChanceCubeBehaviour> {
    public ChanceCubeBehaviour(EntitySheep sheep) {
        super(sheep);
    }

    @Override
    public void onBehaviourStopped(BlockPos previousPos) {
        EntitySheep entity = getEntity();
        World worldObj = entity.worldObj;

        //EntityPlayer nearestPlayer = worldObj.getNearestPlayerNotCreative(entity, Double.MAX_VALUE);
        EntityPlayer nearestPlayer = worldObj.getClosestPlayerToEntity(entity, Double.MAX_VALUE);
        if (nearestPlayer != null) {
            ChanceCubeRegistry.INSTANCE.triggerRandomReward(
                    worldObj,
                    previousPos,
                    nearestPlayer,
                    Math.round((float) (worldObj.rand.nextGaussian() * 40))
                    );
        }
    }
}
