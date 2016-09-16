package com.github.atomicblom.shearmadness.ai;

import com.github.atomicblom.shearmadness.api.BehaviourRegistry;
import com.github.atomicblom.shearmadness.api.VariationRegistry;
import com.github.atomicblom.shearmadness.api.behaviour.BehaviourBase;
import com.github.atomicblom.shearmadness.capability.CapabilityProvider;
import com.github.atomicblom.shearmadness.api.capability.IChiseledSheepCapability;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class SheepBehaviourAI extends EntityAIBase
{
    private final EntitySheep entity;
    private IChiseledSheepCapability capability = null;
    private int lastCheckedId = 0;
    private boolean hasActiveBehaviour = false;
    private BlockPos previousPos = null;

    private ActiveBehaviour[] activeBehaviours = new ActiveBehaviour[0];

    public SheepBehaviourAI(EntityLiving entity)
    {
        capability = entity.getCapability(CapabilityProvider.CHISELED_SHEEP, null);
        //FIXME: Hack to disable this AI if it's not a sheep.

        if (entity instanceof EntitySheep) {
            this.entity = (EntitySheep)entity;
        } else {
            this.entity = null;
        }
    }

    @Override
    public boolean shouldExecute()
    {
        if (capability == null || entity == null) { return false; }

        if (capability.getItemIdentifier() != lastCheckedId) {
            hasActiveBehaviour = false;
            final ItemStack itemStack = capability.getChiselItemStack();

            ActiveBehaviour[] previousActiveBehaviours = activeBehaviours;
            List<ActiveBehaviour> newActiveBehaviours = Lists.newArrayList();

            for (ActiveBehaviour previousActiveBehaviour : previousActiveBehaviours) {
                previousActiveBehaviour.matched = false;
            }

            if (itemStack != null) {
                for (BehaviourBase newBehaviour : BehaviourRegistry.INSTANCE.getApplicableBehaviours(itemStack, entity)) {
                    boolean matched = false;
                    for (ActiveBehaviour previousActiveBehaviour : previousActiveBehaviours) {
                        if (previousActiveBehaviour.behaviour.getClass() == newBehaviour.getClass() &&
                                previousActiveBehaviour.behaviour.isEquivalentTo(newBehaviour)) {
                            newActiveBehaviours.add(previousActiveBehaviour);
                            previousActiveBehaviour.matched = true;
                            matched = true;
                            break;
                        }
                    }

                    if (!matched) {
                        newActiveBehaviours.add(new ActiveBehaviour(newBehaviour));
                    }
                }
            }

            for (ActiveBehaviour previousActiveBehaviour : previousActiveBehaviours) {
                if (!previousActiveBehaviour.matched) {
                    previousActiveBehaviour.behaviour.onBehaviourStopped(previousPos);
                }
            }

            activeBehaviours = new ActiveBehaviour[newActiveBehaviours.size()];
            activeBehaviours = newActiveBehaviours.toArray(activeBehaviours);
            lastCheckedId = capability.getItemIdentifier();
        }

        if (!capability.isChiseled()) {
            return false;
        }

        hasActiveBehaviour = false;

        for (ActiveBehaviour activeBehaviour : activeBehaviours) {
            boolean ranLastTick = activeBehaviour.runThisTick;
            activeBehaviour.runThisTick = false;
            if (activeBehaviour.behaviour.isBehaviourEnabled()) {
                hasActiveBehaviour = true;
                activeBehaviour.runThisTick = true;
            } else if (ranLastTick) {
                activeBehaviour.behaviour.onBehaviourStopped(previousPos);
                //Reset to call onBehaviourStarted on next time it actually runs.
                activeBehaviour.isFirstTick = true;
            }
        }

        return hasActiveBehaviour;
    }

    @Override
    public void updateTask()
    {
        final BlockPos currentPos = entity.getPosition();
        if (!currentPos.equals(previousPos))
        {
            for (ActiveBehaviour activeBehaviour : activeBehaviours) {
                if (activeBehaviour.runThisTick && !activeBehaviour.isFirstTick) {
                    activeBehaviour.behaviour.onSheepMovedBlock(previousPos, currentPos);
                }
            }
            previousPos = currentPos;
        }

        for (ActiveBehaviour activeBehaviour : activeBehaviours) {
            if (activeBehaviour.runThisTick) {
                if (activeBehaviour.isFirstTick) {
                    activeBehaviour.behaviour.onBehaviourStarted(currentPos);
                    activeBehaviour.isFirstTick = false;
                }

                activeBehaviour.behaviour.updateTask();
            }
        }

    }

    public void onDeath() {
        for (ActiveBehaviour activeBehaviour : activeBehaviours) {
            if (activeBehaviour.runThisTick) {
                activeBehaviour.behaviour.onBehaviourStopped(previousPos);
            }
        }
    }

    private class ActiveBehaviour {
        //The behaviour being tracked
        BehaviourBase behaviour;
        //Determines if this behaviour is active during this tick.
        boolean runThisTick;
        //used for detecting existing behaviours when switching blocks.
        boolean matched;
        //Used to determine if onBehaviourStarted should fire.
        boolean isFirstTick;

        ActiveBehaviour(BehaviourBase behaviour) {
            this.behaviour = behaviour;
            isFirstTick = true;
        }
    }
}

